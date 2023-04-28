package project.presenter;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import project.model.entity.RegisterInfo;
import project.model.entity.User;
import project.view.WelcomeView;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The concrete presenter related to the welcome view
 */
public class WelcomePresenter extends AbstractPresenter {
    private WelcomeView welcomeView;

    public WelcomePresenter(WelcomeView welcomeView) {
        this.welcomeView = welcomeView;
    }

    //Create the thread pool
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    /**
     * Login the api with an input token
     * @param token The token input
     */
    public void login(String token){
        Task<Void> task1 = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                welcomeView.disableLogin(true);
                //Enter empty token
                if (token == null || token.equals("")) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                           welcomeView.invalidTokenDialog();
                        }
                    });
                } else {
                    //Start login
                    User currentUser = (User) inputAPI.login(token);
                    updateProgress(1,1);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (currentUser != null) {
                                //currentUser.setToken(token);
                                welcomeView.successfulLoginDialog(currentUser.getEntityInfo());
                                //Jump to the Tag Search page
                                welcomeView.nextView();
                                inputAPI.setCurrentToken(token);
                            } else {
                                welcomeView.invalidTokenDialog();
                            }
                        }
                    });
                }
                welcomeView.showLoginProgress(false);
                welcomeView.disableLogin(false);
                return null;
            }
        };
        welcomeView.loginProgressBind(task1.progressProperty());
        executorService.submit(task1);
    }

    /**
     * Register for the input api
     */
    public void register(){
        RegisterInfo registerInfo = (RegisterInfo) inputAPI.register();
        Optional<ButtonType> result = welcomeView.registerDialog(registerInfo.getEntityInfo());
        //Check whether user confirm to jump to the website
        if (result.isPresent() && result.get() == ButtonType.OK) {
            welcomeView.jumpToWebsite(registerInfo.getLink());
        }
    }
}
