package project.model.util.database.dao;

/**
 * The factory to create contentDao instance
 */
public class ContentDaoFactory {
    public static ContentDao create(){
        return new ContentDaoImpl();
    }
}
