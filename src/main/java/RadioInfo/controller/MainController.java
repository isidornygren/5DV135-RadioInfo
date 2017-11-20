package RadioInfo.controller;

import RadioInfo.view.MainView;

public class MainController {
    private final MainView view;

    public MainController(){
        view = new MainView(this, "RadioInfo");
        view.setVisible(true);
    }
}
