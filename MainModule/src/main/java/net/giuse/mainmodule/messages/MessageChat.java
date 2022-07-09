package net.giuse.mainmodule.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.giuse.mainmodule.messages.interfaces.Message;
import net.giuse.mainmodule.messages.type.MessageType;

@AllArgsConstructor
public class MessageChat implements Message {

    @Getter
    private final String messageChat;
    private final String id;
    @Override
    public String getID() {
        return id;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CHAT;
    }

}
