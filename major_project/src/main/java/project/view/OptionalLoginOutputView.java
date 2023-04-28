package project.view;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import project.presenter.OptionalLoginOutputPresenter;

import java.io.IOException;
import java.net.URL;

/**
 * The Optional Login Output api view
 */
public class OptionalLoginOutputView extends AbstractView {
    private OptionalLoginOutputPresenter optionalLoginOutputPresenter;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;
    @FXML
    Button back;
    @FXML
    Button login;
    @FXML
    ProgressIndicator progressIndicate;
    @FXML
    AnchorPane pane;

    /**
     * Initialize the view with related presenter
     * Add help features when pressing CTRL and clicking a component
     */
    public void initialize(){
        optionalLoginOutputPresenter = new OptionalLoginOutputPresenter(this);
        //Page help
        pane.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("This is the the login window for the Reddit api, try to login with username and password.");
                alert.setContentText("Your client and secret should be set as environment variables in advance");
                alert.showAndWait();
            }
        });
        //Login field help
        usernameField.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Please enter your reddit username here.");
                alert.showAndWait();
            }
        });
        passwordField.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Please enter your reddit password here.");
                alert.showAndWait();
            }
        });
        //Login button help
        login.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("After entering the username and password, click the button to login");
                alert.showAndWait();
            }
        });
        //Back button help
        back.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Click this button to jump back to the previous contents result page");
                alert.showAndWait();
            }
        });
    }

    /**
     * After entering optional output api username and id, click the login button to start using
     */
    public void loginAction(){
        String usernameInput = usernameField.getText();
        String passwordInput = passwordField.getText();
        optionalLoginOutputPresenter.login(usernameInput, passwordInput);
    }

    /**
     * Click back button to go back to the contents result page
     */
    public void backView() {
        Stage currentStage = (Stage)back.getScene().getWindow();
        TagContentsView tagContentsView = new TagContentsView();
        URL url = getClass().getResource("../view/tagContents-view.fxml");
        try {
            tagContentsView.loadView(currentStage, url, "The Guardian Content Search Results");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nextView() {
        backView();
    }

    /**
     * The dialog used for when login with empty input
     */
    public void emptyLoginAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText("Invalid account");
        alert.setContentText("Input should not be empty, please check and try again!");
        alert.showAndWait();
    }

    /**
     * The dialog used for when login successfully
     */
    public void successfulLoginAlert(String accessToken){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Successfully");
        alert.setHeaderText("You have successfully logged in your Reddit account");
        alert.setContentText("Got the access token: " + accessToken);
        alert.showAndWait();
    }

    /**
     * The dialog used for when login failed
     */
    public void loginFailedAlert(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText("Login failed");
        alert.setContentText("Please check your account and environment variables");
        alert.showAndWait();
    }

    /**
     * Bind the login progress indicator with a task progress
     * @param progress The progress to bind
     */
    public void loginProgressBind(ReadOnlyDoubleProperty progress){
        progressIndicate.progressProperty().bind(progress);
    }

    /**
     * Disable or enable login button
     * @param disable Ture disable, false enable
     */
    public void disableLogin(boolean disable){
        login.setDisable(disable);
    }

    /**
     * Show the login progress indicator
     * @param visible True show, false hide
     */
    public void showLoginProgress(boolean visible){
        progressIndicate.setVisible(visible);
    }
}
