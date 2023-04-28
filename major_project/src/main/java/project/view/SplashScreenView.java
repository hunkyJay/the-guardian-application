package project.view;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import project.presenter.SplashScreenPresenter;


import java.io.IOException;
import java.net.URL;

/**
 * The splash image view
 */
public class SplashScreenView extends AbstractView {
    private SplashScreenPresenter splashScreenPresenter;

    @FXML
    ProgressBar progressBar;

    public void setSplashScreenPresenter(SplashScreenPresenter splashScreenPresenter) {
        this.splashScreenPresenter = splashScreenPresenter;
    }

    /**
     * Initialize the view with related presenter
     * Preload
     */
    public void initialize() {
        splashScreenPresenter = new SplashScreenPresenter(this);
        splashScreenPresenter.preload();
    }

    @Override
    public void loadView(Stage primaryStage, URL url, String title) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(url));
        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    @Override
    public void nextView() {
        Stage currentStage = (Stage)progressBar.getScene().getWindow();
        AbstractView welcomePresenter = new WelcomeView();
        URL url = getClass().getResource("../view/welcome-view.fxml");
        try {
            welcomePresenter.loadView(currentStage, url, "Welcome to The Guardian!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Bind the progress bar with a task progress
     * @param progress The progress to bind
     */
    public void splashProgressBind(ReadOnlyDoubleProperty progress){
        progressBar.progressProperty().bind(progress);
    }
}
