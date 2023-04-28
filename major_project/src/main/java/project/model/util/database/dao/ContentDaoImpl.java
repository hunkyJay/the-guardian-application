package project.model.util.database.dao;

import project.model.entity.Content;
import project.model.entity.Tag;
import project.model.util.database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The concrete Dao class to perform CRUD operations for contents table in the database
 */
public class ContentDaoImpl implements ContentDao{
    @Override
    public void createTable() {
        String createContentTableSQL =
                """
                CREATE TABLE IF NOT EXISTS contents (
                    id TEXT,
                    webTitle TEXT,
                    webPublicationDate VARCHAR (25),
                    webUrl TEXT,
                    tagId TEXT,
                    tagWebUrl TEXT,
                    PRIMARY KEY (id, tagId)
                );
                """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement statement = conn.createStatement()) {
            statement.execute(createContentTableSQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addTagContents(Tag tag) {
        String addContentSQL =
                """
                INSERT INTO contents(id, webTitle, webPublicationDate, webUrl, tagId, tagWebUrl) VALUES (?,?,?,?,?,?);
                """;

        try(Connection conn = DBConnection.getInstance().getConnection();
            PreparedStatement statement = conn.prepareStatement(addContentSQL)){
            for(Content content: tag.getMatchingContents()) {
                statement.setString(1, content.getId());
                statement.setString(2, content.getWebTitle());
                statement.setString(3, content.getWebPublicationDate());
                statement.setString(4, content.getWebUrl());
                statement.setString(5, tag.getId());
                statement.setString(6, tag.getWebUrl());
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Tag queryContentsByTagId(String tagId) {
        createTable();
        List<Content> contents = new ArrayList<>();
        String tagWebUrl = null;
        Tag tag = null;
        String queryContentSQL =
                """
                SELECT id, webTitle, webPublicationDate, webUrl, tagWebUrl
                FROM contents
                WHERE tagId = ?;
                """;

        try(Connection conn = DBConnection.getInstance().getConnection();
        PreparedStatement statement = conn.prepareStatement(queryContentSQL)) {
            statement.setString(1, tagId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                String id = resultSet.getString("id");
                String webTitle = resultSet.getString("webTitle");
                String webPublicationDate = resultSet.getString("webPublicationDate");
                String webUrl = resultSet.getString("webUrl");
                tagWebUrl = resultSet.getString("tagWebUrl");
                contents.add(new Content(id, webTitle, webPublicationDate, webUrl));
            }
            tag = new Tag(tagId, tagWebUrl);
            tag.setMatchingContents(contents);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tag;
    }

    @Override
    public void updateTagContents(Tag tag) {
        //Delete previous matching cache
        String deleteContentsSQL =
                """
                DELETE FROM contents
                WHERE tagId = ?
                """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement statement = conn.prepareStatement(deleteContentsSQL)) {
            statement.setString(1, tag.getId());
            statement.execute();
//            System.out.println("Clear previous matching cache.");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //Add new tags to update the table
        addTagContents(tag);
    }

    @Override
    public void deleteTable() {
        String dropContentTableSQL =
                """
                DROP TABLE IF EXISTS contents;
                """;

        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement statement = conn.createStatement()) {
            statement.execute(dropContentTableSQL);
//            System.out.println("Clear cache");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
