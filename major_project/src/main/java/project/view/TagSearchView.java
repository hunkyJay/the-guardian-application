package project.view;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.presenter.TagSearchPresenter;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

/**
 * The tag search view
 */
public class TagSearchView extends AbstractView {
    private TagSearchPresenter tagSearchPresenter;
    @FXML
    ComboBox<String> tagCombo;
    @FXML
    Button searchButton;
    @FXML
    Button clearCacheButton;
    @FXML
    ProgressIndicator progressIndicate;
    @FXML
    BorderPane pane;
    @FXML
    Label currentCredits;


    /**
     * Initialize the view with related presenter
     * Add help features when pressing CTRL and clicking a component
     */
    public void initialize() {
        tagSearchPresenter = new TagSearchPresenter(this);

        //Page help
        pane.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("This is the tag search page used to search tag with related contents. ");
                alert.setContentText("Tips: press CTRL and click a component to get detailed usage.");
                alert.showAndWait();
            }
        });
        //Combo box help
        tagCombo.getEditor().setOnMouseClicked(e -> {
            if (e.isControlDown()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Please enter tag key words to get autocompleted tags in the drop down box.");
                alert.setContentText("Enter key words and click its drop-down button.");
                alert.showAndWait();
            }
        });
        //Search button help
        searchButton.setOnMouseClicked(e -> {
            if (e.isControlDown()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Click this button to start searching contents related to your selected tag.");
                alert.showAndWait();
            }
        });
        //ClearCache Button help
        clearCacheButton.setOnMouseClicked(e -> {
            if (e.isControlDown()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Click this button to clear your searching cache");
                alert.showAndWait();
            }
        });
    }

    /**
     * Shows autocompleted tags when entering keywords in the searching bar
     */
    public void autoComplete() {
        String searchInput = tagCombo.getEditor().getText();
        tagSearchPresenter.tagSearchAutoComplete(searchInput);
    }

    /**
     * Click the search button to search contents related to teh selected tag
     */
    public void searchContents() {
        progressIndicate.setVisible(true);
        //Start searching
        String searchInput = tagCombo.getEditor().getText();
        tagSearchPresenter.tagSearch(searchInput);
    }


    /**
     * Click the clearCache button to clear tag contents cache
     */
    public void clearCache() {
        tagSearchPresenter.clearCache();
    }


    @Override
    public void nextView() {
        Stage currentStage = (Stage) searchButton.getScene().getWindow();
        TagContentsView tagContentsPresenter = new TagContentsView();
        URL url = getClass().getResource("../view/tagContents-view.fxml");
        try {
            tagContentsPresenter.loadView(currentStage, url, "The Guardian Content Search Results");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The dialog used to alert user when input is empty
     */
    public void emptyTagKeywordsAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Empty Input");
        alert.setHeaderText("Please enter tag keywords and select a matching tag");
        alert.setContentText("Select a tag to search the contents ");
        alert.showAndWait();
    }

    /**
     * The dialog used to alert user when input tag is invalid
     */
    public void invalidTagAlert(){
        Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        alert1.setTitle("Search Contents Result");
        alert1.setHeaderText("No such tag ");
        alert1.setContentText("Please try to select a tag and start to search.");
        alert1.showAndWait();
    }

    /**
     * The dialog used to check whether user confirm to clear caches
     * @return The confirmation result, yes or no
     */
    public Optional<ButtonType> clearCacheConfirmAlert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Clear Cache");
        alert.setHeaderText("Confirm to clear cache? ");
        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }

    /**
     * The dialog used to alert user when clearing cache in offline mode
     */
    public void clearCacheInvalidAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Clear Cache Error");
        alert.setHeaderText("Currently, offline mode does not support cache ");
        alert.showAndWait();
    }

    /**
     * The dialog used to inform whether user choose to use cache date when cache hit occurs
     * @return The confirmation result, yes or no
     */
    public Optional<ButtonType> cacheHitAlert(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Found searching tag contents in cache");
        alert.setHeaderText("Confirm to use data from cache? ");
        alert.setContentText("Or use fresh data by canceling.");
        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }

    /**
     * Update the autocomplete tags in the drop-down box
     * @param tagIds The tags to update previous data
     */
    public void updateAutocompleteTags(List<String> tagIds){
        ObservableList<String> observableList = FXCollections.observableArrayList(tagIds);
        tagCombo.setItems(observableList);
    }


    /**
     * Bind the search progress indicator with the progress of a task
     * @param progress The progress to bind
     */
    public void searchProgressBind(ReadOnlyDoubleProperty progress){
        progressIndicate.progressProperty().bind(progress);
    }

    /**
     * Whether disable the search tag button
     * @param disable True disable, false enable
     */
    public void disableSearchButton(boolean disable){
        searchButton.setDisable(disable);
    }

    /**
     * Whether disable the clear cache button
     * @param disable True disable, false enable
     */
    public void disableClearCache(boolean disable){
        clearCacheButton.setDisable(disable);
    }

    /**
     * Update the remaining credits shown in current page
     * @param credits The value of the credits to update
     */
    public void updateCredits(int credits){
        currentCredits.setText("Current search credits remaining: " + credits);
    }

    /**
     * A dialog used for user having run out of search credits
     */
    public void insufficientCreditsAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Tag Search Error");
        alert.setHeaderText("You have run out of search credits");
        alert.showAndWait();
    }
}
