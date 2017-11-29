package RadioInfo.view;

import RadioInfo.model.ChannelObject;
import java.awt.event.ActionEvent;

public class ChannelSelectEvent extends ActionEvent {
    ChannelObject channel;

    public ChannelSelectEvent(ChannelObject channel,Object source, int id, String command){
        super(source, id, command);
        this.channel = channel;
    }

    public ChannelObject getChannel(){
        return this.channel;
    }
}
