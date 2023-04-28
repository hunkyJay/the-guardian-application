package project.view;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.model.entity.Content;
import project.presenter.TagContentsPresenter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;


/**
 * The tagContents view
 */
public class TagContentsView extends AbstractView {
    private TagContentsPresenter tagContentsPresenter;

    @FXML
    TextField searchingField;
    @FXML
    Button search;
    @FXML
    ProgressIndicator progressInput;

    @FXML
    Label tableDescription;
    @FXML
    TableView<Content> contentsTableView;
    @FXML
    TableColumn<Content,String> webTitleColumn;
    @FXML
    TableColumn<Content,String> webDateColumn;

    @FXML
    Pagination tableViewPagination;
    private final int itemPerPage = 9;
    private int totalItems;//Current item num stored in the table
    List<Content> contentResults;//Current searching results shown in table

    @FXML
    Button back;
    @FXML
    Button output;
    @FXML
    ProgressIndicator progressOutput;
    @FXML
    Button optionalOutput;
    @FXML
    ProgressIndicator progressOutputOptional;
    @FXML
    BorderPane pane;


    /**
     * Initialize the view with table of content list for the searching tag and related view presenter
     * Add help features when pressing CTRL and clicking a component
     */
    public void initialize(){
        tagContentsPresenter = new TagContentsPresenter(this);
        //Content list initialization with multiple pages, receive data from the presenter
        contentResults = tagContentsPresenter.getLongFormResultContents();
        tableDescription.setText("The contents results from Tag " + tagContentsPresenter.getLongFormResultId());
        totalItems = contentResults.size();

        webTitleColumn.setCellValueFactory(new PropertyValueFactory<>("webTitle"));
        webDateColumn.setCellValueFactory(new PropertyValueFactory<>("webPublicationDate"));

        if(totalItems<=itemPerPage){
            tableViewPagination.setPageCount(1);
        }else {
            tableViewPagination.setPageCount((int) Math.ceil(totalItems / itemPerPage));
        }
        tableViewPagination.setPageFactory(this::createPage);

        //Help features
        //Page help
        pane.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("This is the content results and search page for the tag you have selected");
                alert.setContentText("Tips: press CTRL and click a component to get detailed usage.");
                alert.showAndWait();
            }
        });
        //Search field help
        searchingField.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Please enter content key words here to search related contents.");
                alert.setContentText("Enter key words and click search button.");
                alert.showAndWait();
            }
        });
        //Search button help
        search.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Click the button to start searching tag contents related to key words");
                alert.showAndWait();
            }
        });
        //Result table help
        tableViewPagination.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Here are the contents searching results.");
                alert.setContentText("Select a content to view its details by clicking it to jump to the website. ");
                alert.showAndWait();
            }
        });
        //Back button help
        back.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Click the back button to jump back to the tag search page. ");
                alert.showAndWait();
            }
        });
        //Send to Imgur button help
        output.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Select a content from the results and click the back button to send a report to the output api.");
                alert.setContentText("The result would be as a form of QR code sent to Imgur. ");
                alert.showAndWait();
            }
        });
        progressInput.setVisible(false);
    }

    /**
     * A private method to create one page at one time for Pagination
     * @param pageIndex the current page index of tableView Pagination
     * @return A Node that represents the current page contentsTableView
     */
    private Node createPage(int pageIndex){
        int fromIndex = pageIndex * itemPerPage;
        int toIndex = Math.min(fromIndex + itemPerPage, totalItems);
        ObservableList<Content> obList = FXCollections.observableList(contentResults.subList(fromIndex, toIndex));
        contentsTableView.setItems(obList);

        return contentsTableView;
    }

    /**
     * Click the search button to search related contents by keyword
     */
    public void searchContents(){
        String searchInput = searchingField.getText();
        progressInput.setVisible(true);
        //Start searching
        tagContentsPresenter.searchContentsByKeywords(searchInput);
    }

    /**
     * Click the content entry to jump to its website
     */
    public void jumpToContentWeb(){
        Content selectedContent = selectContent();
        if(selectedContent != null) {
            String link = selectedContent.getWebUrl();
            Optional<ButtonType> result = jumpToContentWebAlert(link);
            if (result.isPresent() && result.get() == ButtonType.OK) {
                jumpToWebsite(link);
            }
        }
    }

    /**
     * Click the output button to send short form report to the output api
     */
    public void outputAction() {
        tagContentsPresenter.sendOutput();
    }

    /**
     * A method used to get the item selected by user in the contents table
     * @return The selected content
     */
    public Content selectContent(){
        Content selectedContent = null;
        if(contentsTableView.getSelectionModel() != null && contentsTableView.getSelectionModel().getSelectedItem() != null) {
            selectedContent = contentsTableView.getSelectionModel().getSelectedItem();
        }
        return selectedContent;
    }

    /**
     * Click the back button to jump back to the tag search page
     */
    public void backAction() {
        Stage currentStage = (Stage)back.getScene().getWindow();
        TagSearchView tagSearchView = new TagSearchView();
        URL url = getClass().getResource("../view/tagSearch-view.fxml");
        try {
            tagSearchView.loadView(currentStage, url, "The Guardian Tag Search");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Click the outputOptional button to send short form report to the Reddit
     */
    public void outputActionOptional(){
        tagContentsPresenter.sendOutputOptional();
    }

    @Override
    public void nextView() {
        Stage currentStage = (Stage) optionalOutput.getScene().getWindow();
        OptionalLoginOutputView optionalLoginOutputView = new OptionalLoginOutputView();
        URL url = getClass().getResource("../view/OptionalLoginOutput-view.fxml");
        try {
            optionalLoginOutputView.loadView(currentStage, url, "Login Reddit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The dialog used to ask user whether jump to an external website of a content
     * @param link The target website
     * @return Confirmation result, yes or no
     */
    public Optional<ButtonType> jumpToContentWebAlert(String link){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("View Content Details");
        alert.setHeaderText("Click confirm to jump to the content details website in your browser ");
        alert.setContentText(link);
        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }

    /**
     * The dialog used when user send output without selecting an item
     */
    public void emptyOutputAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Output Error");
        alert.setHeaderText("Please select an item to output! ");
        alert.showAndWait();
    }

    /**
     * The dialog used to ask whether user confirm to output the selected item
     * @param contentInfo The information about the selected item
     * @return Confirmation result, yes or no
     */
    public Optional<ButtonType> outputConfirmAlert(String contentInfo){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Output Confirmation");
        alert.setHeaderText("Send the content as QR code to Imgur? ");
        alert.setContentText(contentInfo);
        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }

    /**
     * The dialog used to alert user that the client id is empty
     */
    public void emptyClientIDAlert(){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Send Report Error");
        alert1.setHeaderText("Please input your client id!");
        alert1.showAndWait();
    }

    /**
     * The dialog used to alert user in offline mode
     */
    public void offlineAlert(){
        Alert alert1 = new Alert(Alert.AlertType.ERROR);
        alert1.setTitle("Send Report Error");
        alert1.setHeaderText("You are using the offline mode");
        alert1.showAndWait();
    }

    /**
     * The dialog used to ask user whether jump to an external website of the output result when sending successfully
     * @param outputReport The output brief information shown in dialog
     * @return Confirmation result, yes or no
     */
    public Optional<ButtonType> jumpToOutputConfirm(String outputReport){
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        alert2.setTitle("Send Report Successfully ");
        alert2.setHeaderText("Click confirm to jump to view the result website in your browser ");
        alert2.setContentText(outputReport);
        Optional<ButtonType> result2 = alert2.showAndWait();
        return result2;
    }

    /**
     * The dialog used to alert user that output failed
     */
    public void outputFailedAlert(){
        Alert alert3 = new Alert(Alert.AlertType.ERROR);
        alert3.setTitle("Send Report Error");
        alert3.setHeaderText("Please check your account status and internet");
        alert3.showAndWait();
    }

    /**
     * The dialog used to ask user to log in the reddit
     * @return The confirmation result, yes or no
     */
    public Optional<ButtonType> loginConfirm(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Login confirmation");
        alert.setHeaderText("Have not logged in, log in Reddit now? ");
        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }

    /**
     * The dialog used to ask whether user confirm to output the selected item to the optional output api
     * @param outputReport The information about the selected item
     * @return Confirmation result, yes or no
     */
    public Optional<ButtonType> optionalOutputConfirm(String outputReport){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Output Confirmation");
        alert.setHeaderText("Send the content as a report to the Reddit? ");
        alert.setContentText(outputReport);
        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }

    /**
     * The dialog used to ask user whether jump to an external website of the optional output result when sending successfully
     * @return Confirmation result, yes or no
     */
    public Optional<ButtonType> jumpToOptionalOutputConfirm(){
        Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
        alert2.setTitle("Send Report successfully");
        alert2.setHeaderText("Confirm to jump to the web page to view the post ");
        alert2.setContentText("Send the short form report to the Reddit successfully. ");
        Optional<ButtonType> result2 = alert2.showAndWait();

        return result2;
    }

    /**
     * Bind the searching progress indicator to the progress of a task
     * @param progress The progress to bind
     */
    public void searchProgressBind(ReadOnlyDoubleProperty progress){
        progressInput.progressProperty().bind(progress);
    }

    /**
     * Update the pages in the content table view
     * @param contents The contents to store in the table
     */
    public void updatePage(List<Content> contents){
            contentResults = contents;
            totalItems = contentResults.size();
            tableViewPagination.setPageCount(totalItems / itemPerPage + 1);
            tableViewPagination.setPageFactory(this::createPage);
    }

    /**
     * The dialog used to log in the Imgur api
     * @return The login result
     */
    public Optional<String> loginImgurAlert(){
        TextInputDialog textInput = new TextInputDialog();
        textInput.setTitle("The Imgur Client ID Required");
        textInput.setHeaderText("Please input your Client ID");
        String idFromEnvironment = System.getenv("IMGUR_API_KEY");
        textInput.getEditor().setText(idFromEnvironment);
        Optional<String> inputResult = textInput.showAndWait();

        return inputResult;
    }

    /**
     * Show the output progress indicator
     * @param visible True shows, false hide
     */
    public void showOutputProgress(boolean visible){
        progressOutput.setVisible(visible);
    }

    /**
     * Bind the output progress indicator to the progress of a task
     * @param progress The progress to bind
     */
    public void outputProgressBind(ReadOnlyDoubleProperty progress){
        progressOutput.progressProperty().bind(progress);
    }

    /**
     * Whether disable the output button
     * @param disable True disable, false enable
     */
    public void disableOutput(boolean disable){
        output.setDisable(disable);
    }

    /**
     * Show the optional output progress indicator
     * @param visible True shows, false hide
     */
    public void showOutputOptionalProgress(boolean visible){
        progressOutputOptional.setVisible(visible);
    }

    /**
     * Bind the optional output progress indicator to the progress of a task
     * @param progress The progress to bind
     */
    public void outputOptionalProgressBind(ReadOnlyDoubleProperty progress){
        progressOutputOptional.progressProperty().bind(progress);
    }

    /**
     * Whether disable the optional output button
     * @param disable True disable, false enable
     */
    public void disableOutputOptional(boolean disable){
        optionalOutput.setDisable(disable);
    }
}
