package project.presenter;

import javafx.application.Platform;
import javafx.concurrent.Task;
import project.view.SplashScreenView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The concrete presenter related to the splash image view
 */
public class SplashScreenPresenter extends AbstractPresenter{
    private SplashScreenView splashScreenView;

    //Create thread pool
    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    public SplashScreenPresenter(SplashScreenView splashScreenView) {
        this.splashScreenView = splashScreenView;
        splashScreenView.setSplashScreenPresenter(this);
    }

    /**
     * Preload the splash image and create database
     */
    public void preload(){
        //Task1: Load splash image before jumping to welcome window
        Task<Void> task1 = new Task<>() {
            @Override
            public Void call() {
                try {
                    final int max = 15;
                    for (int i = 1; i <= max; i++) {
                        Thread.sleep(1000);
                        updateProgress(i,max);
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            splashScreenView.nextView();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        //Task2: Create database while preloading
        Task<Void> task2 = new Task<>() {
            @Override
            protected Void call() {
                inputAPI.createDatabase();
                return null;
            }
        };

        splashScreenView.splashProgressBind(task1.progressProperty());
        executorService.submit(task1);
        executorService.submit(task2);
    }
}
