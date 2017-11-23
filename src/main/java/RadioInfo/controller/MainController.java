package RadioInfo.controller;

import RadioInfo.view.MainView;

public class MainController {
    private MainView view;

    public MainController(){
        javax.swing.SwingUtilities.invokeLater(() -> {
            view = new MainView(this, "RadioInfo");
            view.setVisible(true);
        });
    }
}
