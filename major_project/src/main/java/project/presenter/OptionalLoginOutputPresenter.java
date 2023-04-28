package project.presenter;

import javafx.application.Platform;
import javafx.concurrent.Task;
import project.model.entity.User;
import project.view.OptionalLoginOutputView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The concrete presenter related to the  Optional Login Output api view
 */
public class OptionalLoginOutputPresenter extends AbstractPresenter{
    private OptionalLoginOutputView optionalLoginOutputView;

    public OptionalLoginOutputPresenter(OptionalLoginOutputView optionalLoginOutputView) {
        this.optionalLoginOutputView = optionalLoginOutputView;
    }

    //Create thread pool
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    /**
     * Login the optional output api
     * @param usernameInput The username input
     * @param passwordInput The password input
     */
    public void login(String usernameInput, String passwordInput){
        //Empty input
        if(usernameInput == null || usernameInput.equals("") ||passwordInput ==null || passwordInput.equals("")){
            optionalLoginOutputView.emptyLoginAlert();
            return;
        }
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                optionalLoginOutputView.showLoginProgress(true);
                optionalLoginOutputView.disableLogin(true);
                User currentUser = (User) outputAPI.login(usernameInput, passwordInput);
                //Update UI
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (currentUser != null) {
                            optionalLoginOutputView.successfulLoginAlert(currentUser.getToken());
                            //Successful login, jump to main contents page
                            optionalLoginOutputView.nextView();
                        } else {
                            optionalLoginOutputView.loginFailedAlert();
                        }
                    }
                });
                updateProgress(1, 1);
                optionalLoginOutputView.disableLogin(false);
                return null;
            }
        };
        optionalLoginOutputView.loginProgressBind(task.progressProperty());
        executorService.submit(task);

    }
}
