package project;

import javafx.application.Application;
import javafx.stage.Stage;
import project.presenter.SplashScreenPresenter;
import project.view.SplashScreenView;
import project.model.inputAPI.facade.OfflineGuardian;
import project.model.inputAPI.facade.OnlineGuardian;
import project.model.outputAPI.facade.OfflineOutputAPI;
import project.model.outputAPI.facade.OnlineOutputAPI;

import java.io.IOException;
import java.net.URL;

import static java.lang.System.exit;

/**
 * The starting point of the project
 */
public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        SplashScreenView splashScreenView = new SplashScreenView();
        URL url = getClass().getResource("view/splashScreen-view.fxml");
        splashScreenView.setHostServices(getHostServices());
        SplashScreenPresenter splashScreenPresenter = new SplashScreenPresenter(splashScreenView);

        //Default mode, online input api and online output api
        if(getParameters().getRaw().size() == 0){
            splashScreenView.loadView(stage, url, "Welcome to The Guardian!");
        }

        //Gradle run with two arguments
        else if(getParameters().getRaw().size() == 2) {
            if(getParameters().getRaw().get(0).equals("online")) {
                splashScreenPresenter.setInputAPI(new OnlineGuardian());
            }else if(getParameters().getRaw().get(0).equals("offline")){
                splashScreenPresenter.setInputAPI(new OfflineGuardian());
            }else {
                System.out.println("Incorrect command line argument");
                exit(-1);
            }

            if(getParameters().getRaw().get(1).equals("online")) {
                splashScreenPresenter.setOutputAPI(new OnlineOutputAPI());
            }else if(getParameters().getRaw().get(1).equals("offline")){
                splashScreenPresenter.setOutputAPI(new OfflineOutputAPI());
            }else {
                System.out.println("Incorrect command line argument");
                exit(-1);
            }
            splashScreenView.loadView(stage, url, "Welcome to The Guardian!");
        }

        else {
            System.out.println("Incorrect command line argument");
            exit(-1);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}