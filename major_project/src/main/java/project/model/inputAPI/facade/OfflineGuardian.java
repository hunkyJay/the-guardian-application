package project.model.inputAPI.facade;

import project.model.entity.*;
import project.model.inputAPI.inputService.*;
import project.model.inputAPI.inputService.inputStrategy.*;


/**
 * The offline mode input api Guardian facade
 */
public class OfflineGuardian implements InputAPI {
    private LongFormResult longFormResult;
    private ShortFormResult shortFormResult;
    private String currentToken;
    private int currentTagCredits;

    @Override
    public Entity login(String token) {
        InputAPIService loginGuardianService = new LoginGuardianService(new OfflineLoginGuardianStrategy(), token);
        return loginGuardianService.operate();
    }

    @Override
    public Entity register() {
        InputAPIService registerGuardianService = new RegisterGuardianService(new OfflineRegisterGuardianStrategy());
        return registerGuardianService.operate();
    }

    @Override
    public Entity searchRelatedTags(String token, String tagKeyword) {
        InputAPIService tagSearchService = new TagSearchService(new OfflineTagSearchStrategy(), token, tagKeyword);
        return tagSearchService.operate();
    }

    @Override
    public Entity getTagContents(String token, String tag) {
        InputAPIService tagContentsService = new TagContentsService(new OfflineTagContentsStrategy(), token, tag);
        return tagContentsService.operate();
    }

    @Override
    public Entity getTagContentsCache(String tag) {
        //Currently, cache is not required for Offline mode
        return null;
    }

    @Override
    public Entity searchContents(String token, String keyword) {
        InputAPIService contentSearchService = new ContentSearchService(new OfflineContentSearchStrategy(), token, keyword);
        return contentSearchService.operate();
    }

    @Override
    public Entity updateTagContentsCache(Tag tag) {
        //Currently, cache is not required for Offline mode
        return null;
    }

    @Override
    public Entity clearCache() {
        //Do nothing, since cache is not required for Offline mode now
        return null;
    }


    @Override
    public Entity getLongFormResult() {
        return longFormResult;
    }

    @Override
    public Entity getShortFormResult() {
        return shortFormResult;
    }

    @Override
    public void setLongFormResult(LongFormResult result) {
        this.longFormResult = result;
    }

    @Override
    public void setShortFormResult(ShortFormResult result) {
        this.shortFormResult = result;
    }

    @Override
    public String getCurrentToken() {
        return currentToken;
    }

    @Override
    public void setCurrentToken(String token) {
        this.currentToken = token;
    }

    @Override
    public boolean isOnline() {
        return false;
    }

    @Override
    public void createDatabase() {
        //Do nothing, since database is not required for Offline mode now
    }

    @Override
    public void setCurrentTagCredits(int credits) {
        this.currentTagCredits = credits;
    }

    @Override
    public int getCurrentTagCredits() {
        return currentTagCredits;
    }

    @Override
    public Entity currentCreditRule() {
        InputAPIService creditService = new CreditService(new CreditStrategy(), currentToken);
        return creditService.operate();
    }

    @Override
    public boolean consumeCredit() {
        SearchCreditRule searchCreditRule = (SearchCreditRule) currentCreditRule();
        int consumptionPerTime = searchCreditRule.getConsumptionPerTime();

        if(currentTagCredits < consumptionPerTime){
            return false;
        }
        setCurrentTagCredits(currentTagCredits - consumptionPerTime);
        return true;
    }


}
