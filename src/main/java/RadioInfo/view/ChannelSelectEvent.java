package RadioInfo.view;

import RadioInfo.model.Channel;
import java.awt.event.ActionEvent;

public class ChannelSelectEvent extends ActionEvent {
    Channel channel;

    public ChannelSelectEvent(Channel channel, Object source, int id, String command){
        super(source, id, command);
        this.channel = channel;
    }

    public Channel getChannel(){
        return this.channel;
    }
}
