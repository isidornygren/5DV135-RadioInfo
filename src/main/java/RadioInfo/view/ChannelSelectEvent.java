package RadioInfo.view;

import RadioInfo.model.Channel;
import java.awt.event.ActionEvent;

/**
 * Extends ActionEvent and is used by the channelview window to check for
 * if the user pressed a channel.
 * @version 1.0
 * @author Isidor Nygren
 */
public class ChannelSelectEvent extends ActionEvent {
    Channel channel;

    /**
     * Builds the channel select event
     * @param channel the channel to be returned when interacted with
     * @param source the source of the event
     * @param id the id of the event
     * @param command the command sent in with the event
     */
    public ChannelSelectEvent(Channel channel, Object source, int id, String command){
        super(source, id, command);
        this.channel = channel;
    }

    /**
     * @return the channel connected with the interactable object
     */
    public Channel getChannel(){
        return this.channel;
    }
}
