package RadioInfo.view;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class ChannelMenuBar {

    private JMenuBar bar;
    private JMenuItem channelsMenu, updateMenu;

    public ChannelMenuBar(){
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

    public JMenuBar getMenuBar(){
        return this.bar;
    }
    public void setUpdateButton(ActionListener listener){
        updateMenu.addActionListener(listener);
    }
    public void setChannelsButton(ActionListener listener){
        channelsMenu.addActionListener(listener);
    }
}
