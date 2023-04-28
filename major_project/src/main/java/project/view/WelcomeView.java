package project.view;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import project.presenter.WelcomePresenter;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;


/**
 * The welcome view
 */
public class WelcomeView extends AbstractView {
    private WelcomePresenter welcomePresenter;

    @FXML
    Label tokenLabel;
    @FXML
    TextField inputToken;

    @FXML
    Button login;
    @FXML
    Button register;
    @FXML
    ProgressIndicator progressIndicate;

    @FXML
    BorderPane pane;


    /**
     *Initialize the view with related presenter
     * Add help features when pressing CTRL and clicking a component and initialize
     */
    public void initialize(){
        welcomePresenter = new WelcomePresenter(this);
        //Api token prompt text
        String promptKey = System.getenv("INPUT_API_KEY");
        inputToken.setText(promptKey);

        //Page help
        pane.setOnMouseClicked(e->{
            if(e.isControlDown()){
                helpView();
            }
        });
        //Token Label help
        tokenLabel.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Please enter your token to get started.");
                alert.setContentText("The token is your user api key registered from the official website.");
                alert.showAndWait();
            }
        });
        //Input field help
        inputToken.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Please enter your user api key here and then click login.");
                alert.showAndWait();
            }
        });
        //Login button help
        login.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Click this button to confirm token to login");
                alert.setContentText("The token is your user api key registered from the official website.");
                alert.showAndWait();
            }
        });
        //Register button help
        register.setOnMouseClicked(e->{
            if(e.isControlDown()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Help");
                alert.setHeaderText("Click this button to access registering api token");
                alert.showAndWait();
            }
        });
    }

    /**
     * Enter input api token and click the login button to start using
     */
    public void loginAction() {
        showLoginProgress(true);
        String token = inputToken.getText();
        welcomePresenter.login(token);
    }

    /**
     * Click the register button to register a token
     */
    public void registerAction(){
        welcomePresenter.register();
    }

    /**
     * Jump to the api website through the hyperlink
     */
    public void jumpToAPIWebsite(){
        jumpToWebsite("https://open-platform.theguardian.com/documentation/");
    }

    /**
     * Click the help menu to get help information and tips
     */
    public void helpView(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Help");
        alert.setHeaderText("This is a desktop application to search tags and contents, get started via your client token. ");
        alert.setContentText("Tips: press CTRL and click a component to get detailed usage for each page.");
        alert.showAndWait();
    }

    /**
     * Click the about menu to get help information and tips
     */
    public void aboutView(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("This is the about information of the app ");
        alert.setContentText("Application: The Guardian and Imgur desktop application\n\n" +
                "Author: Renjie \n\n" +
                "Reference: \n" +
                "Use Zxing to generate qr code picture and decode - CSDN. (2020) https://blog.csdn.net/weixin_43944305/article/details/106701133\n\n" +
                "JavaFx concurrent and ThreadPoolExecutor - CSDN. (2021) https://blog.csdn.net/baidu_25117757/article/details/117375913?spm=1001.2014.3001.5502\n\n" +
                "JavaFX Tutorial Pagination - Genius Coders, YouTube (2017) https://www.youtube.com/watch?v=bB--iW3AIvI&t=458s\n\n" +
                "Splash Image, Global Search - VectorStock (2022) https://www.vectorstock.com/royalty-free-vector/global-search-concept-isolated-on-white-background-vector-974819");
        alert.showAndWait();
    }

    @Override
    public void nextView() {
        Stage currentStage = (Stage) login.getScene().getWindow();
        InputCreditsView inputCreditsView = new InputCreditsView();
        URL url = getClass().getResource("../view/inputCredits-view.fxml");
        try {
            inputCreditsView.loadView(currentStage, url, "The Guardian Tag Search");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The dialog used to alert user when the input token is invalid
     */
    public void invalidTokenDialog(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Login Error");
        alert.setHeaderText("Invalid Token");
        alert.setContentText("Input is invalid, please check and try again!");
        alert.showAndWait();
    }

    /**
     * The dialog used when successfully login
     * @param userInfo
     */
    public void successfulLoginDialog(String userInfo){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Login Information");
        alert.setHeaderText("User has logged in successfully.");
        alert.setContentText(userInfo);
        alert.showAndWait();
    }

    /**
     * The dialog used to check whether user confirm to jump to the external website to register
     * @param registerInfo The information about the register
     * @return The confirmation result, yes or no
     */
    public Optional<ButtonType> registerDialog(String registerInfo){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Register API Key");
        alert.setHeaderText(registerInfo);
        alert.setContentText("Click confirm to jump to the register website.");
        Optional<ButtonType> result = alert.showAndWait();

        return result;
    }

    /**
     * Bind the login progress indicator with the progress of a task
     * @param progress The progress to bind
     */
    public void loginProgressBind(ReadOnlyDoubleProperty progress){
        progressIndicate.progressProperty().bind(progress);
    }

    /**
     * Whether disable the login button
     * @param disable True disable, false enable
     */
    public void disableLogin(boolean disable){
        login.setDisable(disable);
    }

    /**
     * Whether show the login progress indicator
     * @param visible True show, False hide
     */
    public void showLoginProgress(boolean visible){
        progressIndicate.setVisible(visible);
    }
}
