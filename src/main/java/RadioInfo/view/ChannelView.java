package RadioInfo.view;

import RadioInfo.model.Channel;

import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ChannelView {

    private JFrame frame;
    private JPanel channelsPanel;
    private EventListenerList actionListeners = new EventListenerList();

    public ChannelView(){
        frame = new JFrame("Select channel");
        channelsPanel = new JPanel(new FlowLayout());
        channelsPanel.setPreferredSize(new Dimension(400,400));

        JScrollPane scroll = new JScrollPane(channelsPanel);
        frame.add(scroll);
        //frame.setSize(400, 400);
        frame.pack();
    }

    public void setVisible(boolean visible){
        frame.setVisible(visible);
    }

    public void addChannel(Channel channel){
        System.out.println("Adding channel.");
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

    public void setChannels(ArrayList<Channel> channels){
        for(Channel channel: channels){
            addChannel(channel);
        }
    }

    public void addActionListener(ActionListener listener){
        actionListeners.add(ActionListener.class, listener);
    }

    public void removeActionListener(ActionListener listener){
        actionListeners.remove(ActionListener.class, listener);
    }

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
