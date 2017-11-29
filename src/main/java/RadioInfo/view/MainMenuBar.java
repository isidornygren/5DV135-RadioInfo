package RadioInfo.view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
/**
 * Builds the main menu bar for the application
 * @version 1.0
 * @author Isidor Nygren
 */
public class MainMenuBar {

    private JMenuBar bar;
    private JMenuItem channelsMenu, updateMenu;

    /**
     * Creates two menu items and adds them to the menu object without adding
     * button listeners to them
     */
    public MainMenuBar(){
        bar = new JMenuBar();
        JMenu channelMenu = new JMenu("Channel");
        JMenu scheduleMenu = new JMenu("Schedule");

        bar.add(channelMenu);
        bar.add(scheduleMenu);

        channelsMenu = new JMenuItem("Select channel");
        channelsMenu.setMnemonic(KeyEvent.VK_C);

        updateMenu = new JMenuItem("Update current schedule");
        updateMenu.setMnemonic(KeyEvent.VK_U);

        channelMenu.add(channelsMenu);
        scheduleMenu.add(updateMenu);
    }

    /**
     * Returns the menu bar
     * @return the menu bar
     */
    public JMenuBar getMenuBar(){
        return this.bar;
    }

    /**
     * Adds a listener to the update menu item in the menu bar
     * @param listener the button listener to be applied to the update item
     */
    public void setUpdateButton(ActionListener listener){
        updateMenu.addActionListener(listener);
    }
    /**
     * Adds a listener to the channels menu item in the menu bar
     * @param listener the button listener to be applied to the channels item
     */
    public void setChannelsButton(ActionListener listener){
        channelsMenu.addActionListener(listener);
    }
}
