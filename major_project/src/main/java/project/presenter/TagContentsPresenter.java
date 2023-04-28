package project.presenter;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import project.model.entity.*;
import project.model.util.QRCodeHelper;
import project.view.TagContentsView;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The concrete presenter related to the tagContents view
 */
public class TagContentsPresenter extends AbstractPresenter{
    private TagContentsView tagContentsView;

    //Create thread pool
    ExecutorService executorService = Executors.newFixedThreadPool(3);

    public TagContentsPresenter(TagContentsView tagContentsView) {
        this.tagContentsView = tagContentsView;
    }

    /**
     * Search related contents by keywords
     * @param searchInput The keywords input
     */
    public void searchContentsByKeywords(String searchInput){
        if(searchInput != null && !searchInput.equals("")){
            Task<Void> task1 = new Task<Void>() {
                ContentSearch currentSearch;//Content key word with searching results
                LongFormResult longFormResult;
                @Override
                protected Void call() throws Exception {
                    longFormResult = (LongFormResult) inputAPI.getLongFormResult();
                    String keywords = "q=" + searchInput + "&tag=" + longFormResult.getSelectedTag().getId();
                    currentSearch = (ContentSearch) inputAPI.searchContents(inputAPI.getCurrentToken(), keywords);
                    if (!inputAPI.isOnline()) Thread.sleep(1000);//Dummy mode for progress indicator
                    updateProgress(1, 1);
                    //Update results in UI
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (currentSearch != null) {
                                //Get related contents for the input search and update pages
                                tagContentsView.updatePage(currentSearch.getRelatedContents());
                            }
                        }
                    });
                    return null;
                }
            };
            tagContentsView.searchProgressBind(task1.progressProperty());
            executorService.submit(task1);
        }else {
            //Search input is empty
            tagContentsView.initialize();
        }
    }

    /**
     * Send output to the output api
     */
    public void sendOutput(){
        Content selectedContent = tagContentsView.selectContent();
        //Select no content as a report to send
        if (selectedContent == null) {
            tagContentsView.emptyOutputAlert();
            return;
        }

        //Confirm the selected item
        Optional<ButtonType> result = tagContentsView.outputConfirmAlert(selectedContent.getEntityInfo());

        if (result.isPresent() && result.get() == ButtonType.OK) {
            LongFormResult longFormResult = (LongFormResult) inputAPI.getLongFormResult();
            inputAPI.setShortFormResult(new ShortFormResult(longFormResult.getSelectedTag(), selectedContent));
            String shortFormReport = inputAPI.getShortFormResult().getEntityInfo();

            //Enter the output api client id to use the output api
            Optional<String> inputResult = tagContentsView.loginImgurAlert();

            //Empty client id
            if (inputResult.isPresent()) {
                String inputId = inputResult.get();
                if (inputId.length() == 0) {
                    tagContentsView.emptyClientIDAlert();
                    return;
                }

                //Offline mode is not required to upload report
                if(!outputAPI.isOnline()){
                    tagContentsView.offlineAlert();
                    return;
                }

                tagContentsView.showOutputProgress(true);
                //Send report to imgur and jump to the website,using concurrency to avoid slow network
                Task<Void> task2 = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        tagContentsView.disableOutput(true);
                        //Create short form report QR code
                        Path QRCodePath = Paths.get("src/main/resources/project/image/QRCode.jpg");
                        String encodedQRCode = QRCodeHelper.generateQRCodeImage(shortFormReport, 300, QRCodePath);
                        //Send the QR code to output api
                        Output outputReport = (Output) outputAPI.outputReport(inputId, encodedQRCode);
                        updateProgress(1,1);
                        //Update UI
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                if (outputReport != null) {

                                    Optional<ButtonType> result2 = tagContentsView.jumpToOutputConfirm(outputReport.getEntityInfo());
                                    if (result2.isPresent() && result2.get() == ButtonType.OK) {
                                        tagContentsView.jumpToWebsite(outputReport.getLink());
                                    }
                                } else {
                                    tagContentsView.outputFailedAlert();
                                }
                            }
                        });
                        tagContentsView.disableOutput(false);
                        tagContentsView.showOutputProgress(false);
                        return null;
                    }
                };
                tagContentsView.outputProgressBind(task2.progressProperty());
                executorService.submit(task2);
            }
        }
    }

    /**
     * Send output to the optional output api
     */
    public void sendOutputOptional(){
        //This feature is not required for offline mode
        if(!outputAPI.isOnline()){
            tagContentsView.offlineAlert();
            return;
        }
        //Have not logged in the Reddit api
        if(outputAPI.getAccessToken() == null){
            //Confirm to login api
            Optional<ButtonType> result = tagContentsView.loginConfirm();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //Show login page
                tagContentsView.nextView();
            }
            return;
        }

        //Get selected to content to output
        Content selectedContent = tagContentsView.selectContent();
        //Select no content as a report to send
        if (selectedContent == null) {
            tagContentsView.emptyOutputAlert();
            return;
        }

        //Confirm the selected item
        Optional<ButtonType> result = tagContentsView.optionalOutputConfirm(selectedContent.getEntityInfo());
        if (result.isPresent() && result.get() == ButtonType.OK) {
            tagContentsView.showOutputOptionalProgress(true);
            Task<Void> task3 = new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    tagContentsView.disableOutputOptional(true);
                    //Get the short form result to send
                    LongFormResult longFormResult = (LongFormResult) inputAPI.getLongFormResult();
                    inputAPI.setShortFormResult(new ShortFormResult(longFormResult.getSelectedTag(), selectedContent));
                    String shortFormReport = inputAPI.getShortFormResult().getEntityInfo();

                    String user = outputAPI.getUsername();
                    String accessToken = outputAPI.getAccessToken();
                    Output output = (Output) outputAPI.optionalOutputReport(user, accessToken, shortFormReport);
                    updateProgress(1, 1);
                    //Update UI
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            if (output == null) {
                                tagContentsView.outputFailedAlert();
                            } else {
                                Optional<ButtonType> result2 = tagContentsView.jumpToOptionalOutputConfirm();
                                if (result2.isPresent() && result2.get() == ButtonType.OK) {
                                    tagContentsView.jumpToWebsite(output.getLink());
                                }
                            }
                        }
                    });
                    tagContentsView.disableOutputOptional(false);
                    tagContentsView.showOutputOptionalProgress(false);
                    return null;
                }
            };
            tagContentsView.outputOptionalProgressBind(task3.progressProperty());
            executorService.submit(task3);
        }
    }

    /**
     * Get the contents from the long form result report
     * @return The list of contents
     */
    public List<Content> getLongFormResultContents(){
        LongFormResult longFormResult = (LongFormResult) inputAPI.getLongFormResult();
        return longFormResult.getContentList();
    }

    /**
     * Get the Tag id of long form result report
     * @return The tah id
     */
    public String getLongFormResultId(){
        LongFormResult longFormResult = (LongFormResult) inputAPI.getLongFormResult();
        return longFormResult.getSelectedTag().getId();
    }
}
