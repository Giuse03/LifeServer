package net.giuse.mainmodule.messages.interfaces;

import net.giuse.mainmodule.messages.type.MessageType;

public interface Message {


    String getID();

    MessageType getMessageType();
}
