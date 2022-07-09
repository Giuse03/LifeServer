package net.giuse.mainmodule.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.giuse.mainmodule.messages.interfaces.Message;
import net.giuse.mainmodule.messages.type.MessageType;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public class MessageActionbar implements Message {

    @Getter
    private final String messageBar;
    private final String id;

    @Override
    public String getID() {
        return id;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.ACTION_BAR;
    }

}
