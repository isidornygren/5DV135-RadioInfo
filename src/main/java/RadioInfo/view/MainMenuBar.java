package RadioInfo.view;

import RadioInfo.model.Channel;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

/**
 * Builds the main menu bar for the application
 * @version 1.0
 * @author Isidor Nygren
 */
public class MainMenuBar {

    private JMenuBar bar;
    private JMenu channelMenu;
    private JMenu p4Menu;
    private JMenu extraMenu;
    private JMenuItem updateMenu;
    private EventListenerList actionListeners = new EventListenerList();

    /**
     * Creates two menu items and adds them to the menu object without adding
     * button listeners to them
     */
    public MainMenuBar(){
        bar = new JMenuBar();
        channelMenu = new JMenu("Channel");
        JMenu scheduleMenu = new JMenu("Schedule");
        p4Menu = new JMenu("P4");
        extraMenu = new JMenu("Extra");

        channelMenu.add(p4Menu);
        channelMenu.add(extraMenu);

        bar.add(channelMenu);
        bar.add(scheduleMenu);

        updateMenu = new JMenuItem("Update current schedule");
        updateMenu.setMnemonic(KeyEvent.VK_U);

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
     * adds a channel to be rendered in the menu
     * @param channel the channel to be added to the menu
     */
    public void addChannel(Channel channel){
        JMenuItem channelItem = new JMenuItem(channel.getName());
        channelItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireAction(new ChannelSelectEvent(channel, this, 0, "command"));
            }
        });
        if(channel.getName().startsWith("P4")){
            p4Menu.add(channelItem);
        }else if(channel.getName().startsWith("SR Extra")){
            extraMenu.add(channelItem);
        }else{
            channelMenu.add(channelItem);
        }
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
    public void addActionListener(ActionListener listener){
        actionListeners.add(ActionListener.class, listener);
    }
    /**
     * Removes an listener from the listenerlist
     * @param listener the listener to be removed
     */
    public void removeActionListener(ActionListener listener){
        actionListeners.remove(ActionListener.class, listener);
    }
    /**
     * function that is called when an actionlistener returns an event
     * @param event the event that occured
     */
    protected void fireAction(ChannelSelectEvent event)
    {
        Object[] listeners = actionListeners.getListenerList();
        int numListeners = listeners.length;
        for (int i = 0; i<numListeners; i+=2){
            if (listeners[i]==ActionListener.class){
                ((ActionListener)listeners[i+1]).actionPerformed(event);
            }
        }
    }
}
