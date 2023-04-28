package project.presenter;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.ButtonType;
import project.model.entity.Content;
import project.model.entity.LongFormResult;
import project.model.entity.Tag;
import project.model.entity.TagSearch;
import project.view.TagSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The concrete presenter related to the tag search view
 */
public class TagSearchPresenter extends AbstractPresenter{
    private TagSearchView tagSearchView;

    public TagSearchPresenter(TagSearchView tagSearchView) {
        this.tagSearchView = tagSearchView;
        //Initialize the credits
        tagSearchView.updateCredits(inputAPI.getCurrentTagCredits());
    }

    //Create thread pool
    ExecutorService executorService = Executors.newFixedThreadPool(3);

    /**
     * Autocomplete the tags when input searching keywords
     * @param searchInput The keywords input
     */
    public void tagSearchAutoComplete(String searchInput){
        Task<Void> task1 = new Task<Void>() {
            @Override
            protected Void call() {
                if (searchInput != null && !searchInput.equals("")) {
                    //Receive result from http request
                    TagSearch currentSearch = (TagSearch) inputAPI.searchRelatedTags(inputAPI.getCurrentToken(), searchInput);
                    if (currentSearch != null) {
                        //Get related tags for the input search
                        List<String> tagIds = new ArrayList<>();
                        for (Tag t : currentSearch.getRelatedTags()) {
                            tagIds.add(t.getId());
                        }

                        //Update UI
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                tagSearchView.updateAutocompleteTags(tagIds);
                            }
                        });
                    }
                }
                return null;
            }
        };
        executorService.submit(task1);
    }

    /**
     * Search a tag
     * @param searchInput The keywords input
     */
    public void tagSearch(String searchInput){
        Task<Void> task2 = new Task<Void>() {
            Tag selectedTag;//The tag with contents used as the searching result
            @Override
            protected Void call() throws Exception {
                //Disable clear cache function while processing searching
                tagSearchView.disableClearCache(true);
                tagSearchView.disableSearchButton(true);
                //Search input is empty
                if (searchInput == null || searchInput.equals("")) {
                    updateProgress(1,1);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                           tagSearchView.emptyTagKeywordsAlert();
                        }
                    });
                }
                else if(!inputAPI.consumeCredit()){
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            tagSearchView.insufficientCreditsAlert();
                        }
                    });
                }
                else {
                    //Check tag from cache when using online mode input api
                    if (inputAPI.isOnline()) {
                        Tag cachedTag = (Tag) inputAPI.getTagContentsCache(searchInput);//Search from cache
                        Tag onlineTag = (Tag) inputAPI.getTagContents(inputAPI.getCurrentToken(), searchInput);//Search from Http request
                        //No cache hit occurs, updated by the searching result from http request
                        if (cachedTag == null) {
                            if (onlineTag != null) {
                                selectedTag = onlineTag;
                                inputAPI.updateTagContentsCache(selectedTag);
                            }
                            updateProgress(1,1);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    getSearchingResult(selectedTag);
                                }
                            });
                        } else {
                            //Cache hit occurs
                            updateProgress(1,1);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Optional<ButtonType> result = tagSearchView.cacheHitAlert();
                                    if (result.isPresent() && result.get() == ButtonType.OK) {
                                        selectedTag = cachedTag;
                                    } else {
                                        selectedTag = onlineTag;
                                    }
                                    getSearchingResult(selectedTag);
                                }
                            });
                        }
                    } else {
                        //No cache for offline mode input api
                        selectedTag = (Tag) inputAPI.getTagContents(inputAPI.getCurrentToken(), searchInput);
                        Thread.sleep(1500);//Dummy mode for progress indicator
                        updateProgress(1,1);
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                getSearchingResult(selectedTag);
                            }
                        });
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            tagSearchView.updateCredits(inputAPI.getCurrentTagCredits());
                        }
                    });
                }
                tagSearchView.disableClearCache(false);//Enable clear cache after searching
                tagSearchView.disableSearchButton(false);//Enable search after
                return null;
            }
        };
        tagSearchView.searchProgressBind(task2.progressProperty());
        executorService.submit(task2);
    }

    /**
     * Get the contents searching results of a tag
     * @param selectedTag The tag selected for search
     */
    public void getSearchingResult(Tag selectedTag){
        if (selectedTag != null) {
            //Get related contents for the input tag
            List<Content> contents = new ArrayList<>();
            for (Content t : selectedTag.getMatchingContents()) {
                contents.add(t);
            }

            //Get the long from result for input api (The currently selected tag along with the list of content)
            inputAPI.setLongFormResult(new LongFormResult(selectedTag, contents));
            tagSearchView.nextView();
        } else {
            tagSearchView.invalidTagAlert();
        }
    }

    /**
     * Clear searching caches
     */
    public void clearCache(){
        if (inputAPI.isOnline()) {
            Optional<ButtonType> result = tagSearchView.clearCacheConfirmAlert();
            //Confirm to clear data in cache
            if (result.isPresent() && result.get() == ButtonType.OK) {
                inputAPI.clearCache();
            }
        } else {
            tagSearchView.clearCacheInvalidAlert();
        }
    }

}
