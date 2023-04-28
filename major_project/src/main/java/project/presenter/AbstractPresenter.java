package project.presenter;

import project.model.inputAPI.facade.InputAPI;
import project.model.inputAPI.facade.OnlineGuardian;
import project.model.outputAPI.facade.OnlineOutputAPI;
import project.model.outputAPI.facade.OutputAPI;
import project.view.AbstractView;

/**
 * An abstract presenter class in the MVP pattern
 */
public class AbstractPresenter {
    static InputAPI inputAPI = new OnlineGuardian();//By default, online mode
    static OutputAPI outputAPI = new OnlineOutputAPI();//By default, online mode
    protected AbstractView view;

    /**
     * Set input api facade for the whole application
     * @param inputAPI the input api to set
     */
    public void setInputAPI(InputAPI inputAPI){
        this.inputAPI = inputAPI;
    }

    /**
     * Set output api facade for the whole application
     * @param outputAPI the output api to set
     */
    public void setOutputAPI(OutputAPI outputAPI){
        this.outputAPI = outputAPI;
    }

}
