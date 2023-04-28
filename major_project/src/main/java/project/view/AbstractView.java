package project.view;

import javafx.application.HostServices;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;

/**
 * An abstract view class in the MVP pattern
 */
public abstract class AbstractView {
    static HostServices hostServices;

    /**
     * The method to load view
     */
    public void loadView(Stage primaryStage, URL url, String title) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(url));
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
        primaryStage.close();
        stage.setOnCloseRequest((WindowEvent event) -> {
            System.exit(0);
        });
    }

    /**
     * Go to the next view from current view
     */
    public abstract void nextView();

    /**
     * Set host services used for open hyperlinks in browser
     * @param hostServices the host services to set
     */
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }

    /**
     * Jump to the external website in browser
     * @param link The link of target website
     */
    public void jumpToWebsite(String link){
        hostServices.showDocument(link);
    }
}
