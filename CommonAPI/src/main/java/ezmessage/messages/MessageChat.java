package ezmessage.messages;

import ezmessage.interfaces.Message;
import ezmessage.type.MessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MessageChat implements Message {

    @Getter
    private final String messageChat;

    @Override
    public MessageType getMessageType() {
        return MessageType.CHAT;
    }

}
