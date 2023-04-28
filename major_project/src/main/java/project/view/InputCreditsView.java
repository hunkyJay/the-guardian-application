package project.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.presenter.InputCreditPresenter;

import java.io.IOException;
import java.net.URL;
/**
 * The Input search credits view
 */
public class InputCreditsView extends AbstractView {
    private InputCreditPresenter inputCreditPresenter;
    @FXML
    TextField creditField;
    @FXML
    Button setCreditsButton;
    @FXML
    AnchorPane pane;

    /**
     * Initialize the view with related presenter
     * Add help features when pressing CTRL and clicking a component
     */
    public void initialize() {
        inputCreditPresenter = new InputCreditPresenter(this);
        //Page help
        pane.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("This is the set credits window for the guardian api, enter a number to set as your credits");
                alert.setContentText("The credit represents the number of tag searches you are allowed to make that run");
                alert.showAndWait();
            }
        });
        //Credit field help
        creditField.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Please the credit number you would like to set here.");
                alert.showAndWait();
            }
        });
        //Set button help
        setCreditsButton.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("After entering the credit, click the button to complete setting.");
                alert.showAndWait();
            }
        });
    }

    /**
     * After entering the credit number, click the set button to initialize credits with that value
     */
    public void setCreditsAction(){
        String creditsInput = creditField.getText();
        inputCreditPresenter.setCredit(creditsInput);
    }

    @Override
    public void nextView() {
        Stage currentStage = (Stage) creditField.getScene().getWindow();
        TagSearchView tagSearchView = new TagSearchView();
        URL url = getClass().getResource("../view/tagSearch-view.fxml");
        try {
            tagSearchView.loadView(currentStage, url, "The Guardian Tag Search");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The dialog used for when input is empty
     */
    public void emptyInputAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Set Credits Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText("Input should not be empty.");
        alert.showAndWait();
    }

    /**
     * The dialog used for when input is invalid (not a number within the required scope)
     * @param min The minimum value that the initializing credit must be grater than
     * @param max The maximum value that the initializing credit must be less than
     */
    public void invalidInputAlert(int min, int max){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Set Credits Error");
        alert.setHeaderText("Invalid Input");
        alert.setContentText("Input should be a number between " + min + " and " + max);
        alert.showAndWait();
    }


}
