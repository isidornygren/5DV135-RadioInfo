package RadioInfo.view;

import RadioInfo.model.Channel;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
/**
 * Builds the Channel window
 * @version 1.0
 * @author Isidor Nygren
 */
public class ChannelView {

    private JFrame frame;
    private JPanel channelsPanel;
    private EventListenerList actionListeners = new EventListenerList();

    /**
     * Creates a new frame in which to render the channel window
     */
    public ChannelView(){
        frame = new JFrame("Select channel");
        channelsPanel = new JPanel(new FlowLayout());
        channelsPanel.setPreferredSize(new Dimension(400,400));

        JScrollPane scroll = new JScrollPane(channelsPanel);
        frame.add(scroll);
        frame.pack();
    }

    /**
     * Sets the channelwindow to visible or not
     * @param visible true = visible, false = not visible
     */
    public void setVisible(boolean visible){
        frame.setVisible(visible);
    }

    /**
     * adds a channel to be rendered in the channel view
     * @param channel the channel to be rendered
     */
    public void addChannel(Channel channel){
        JPanel channelPanel = new JPanel();
        JLabel channelIcon = new JLabel();

        channelPanel.setLayout(new BoxLayout(channelPanel, BoxLayout.Y_AXIS));
        channelIcon.setIcon(new ImageIcon(channel.getImage().getScaledInstance(-1,32,Image.SCALE_SMOOTH)));
        JLabel label = new JLabel(channel.getName());
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        channelIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        channelPanel.add(channelIcon);
        channelPanel.add(label);
        channelPanel.setPreferredSize(new Dimension(64, 64));

        // Make every channel clickable
        channelPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                setVisible(false);
                fireAction(new ChannelSelectEvent(channel, this, 0, "command"));
            }
        });
        channelsPanel.add(channelPanel);
        // Update panel contents
        channelsPanel.revalidate();
    }

    /**
     * Sets all the channels in the channel window
     * @param channels an arraylist of channels
     */
    public void setChannels(ArrayList<Channel> channels){
        for(Channel channel: channels){
            addChannel(channel);
        }
    }

    /**
     * Adds an actionlistener to each channel that is rendered allowing
     * them to be pressable
     * @param listener the listener that should be added to the buttons
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
