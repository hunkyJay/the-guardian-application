package project.presenter;

import project.model.entity.SearchCreditRule;
import project.view.InputCreditsView;
/**
 * The concrete presenter related to the InputCredit view
 */
public class InputCreditPresenter extends AbstractPresenter {
    private InputCreditsView inputCreditsView;

    public InputCreditPresenter(InputCreditsView inputCreditsView) {
        this.inputCreditsView = inputCreditsView;
    }

    /**
     * Initialize search credits at the start of the application
     * @param input The input credits to set
     */
    public void setCredit(String input){
        //Empty input
        if(input == null || input.length() == 0){
            inputCreditsView.emptyInputAlert();
            return;
        }

        //Invalid input
        SearchCreditRule searchCreditRule = (SearchCreditRule) inputAPI.currentCreditRule();
        int min = searchCreditRule.getMin();
        int max = searchCreditRule.getMax();
        if(!checkInputValid(input, min, max)){
            inputCreditsView.invalidInputAlert(min, max);
            return;
        }

        //Set credits successfully
        int credit = Integer.parseInt(input);
        inputAPI.setCurrentTagCredits(credit);
        inputCreditsView.nextView();
    }

    /**
     * A private method used to check the validity of entering input for setting credits
     * @param input The entering credit input
     * @param min The minimum value that the initializing credit must be grater than
     * @param max The maximum value that the initializing credit must be less than
     * @return true valid, false invalid
     */
    private boolean checkInputValid(String input, int min, int max){
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                return false;
            }
        }
        int result = Integer.parseInt(input);

        if(result <= min || result >= max){
            return false;
        }
        return true;
    }
}
