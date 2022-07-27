package net.giuse.secretmessagemodule.process;

import ezmessage.MessageBuilder;
import ezmessage.TextReplacer;
import lombok.Getter;
import lombok.Setter;
import net.giuse.mainmodule.MainModule;
import net.giuse.secretmessagemodule.SecretChatBuilder;
import net.giuse.secretmessagemodule.SecretMessageModule;
import org.bukkit.entity.Player;

import javax.inject.Inject;


public class SecretChatProcess {
    private final SecretChatBuilder secretChatBuilder = new SecretChatBuilder();
    private final SecretMessageModule secretMessageModule;
    private final MessageBuilder messageBuilder;
    @Setter
    @Getter
    private Player sender, receiver;
    @Setter
    @Getter
    private String text;

    @Inject
    public SecretChatProcess(MainModule mainModule) {
        this.secretMessageModule = (SecretMessageModule) mainModule.getService(SecretMessageModule.class);
        messageBuilder = mainModule.getMessageBuilder();
    }

    /*
     * Send a message from Sender to Receiver
     */
    public void send() {
        //Check if msg-toggle is ON
        if (secretMessageModule.getPlayerMsgToggle().contains(receiver) || secretMessageModule.getPlayerMsgToggle().contains(sender)) {
            messageBuilder.setCommandSender(sender).setIDMessage("msgtoggle-on").sendMessage();
            return;
        }
        //Send Message

        messageBuilder.setIDMessage("receiver").setCommandSender(receiver).sendMessage(
                new TextReplacer().match("%sender_name%").replaceWith(sender.getName()),
                new TextReplacer().match("%text%").replaceWith(text));

        messageBuilder.setIDMessage("sender").setCommandSender(sender).sendMessage(
                new TextReplacer().match("%receiver_name%").replaceWith(receiver.getName()),
                new TextReplacer().match("%text%").replaceWith(text));

        //Update sender and Receiver
        secretChatBuilder.setSender(sender);
        secretChatBuilder.setReceiver(receiver);
        secretChatBuilder.setText(text);
        secretMessageModule.getSecretsChats().add(secretChatBuilder);
        secretMessageModule.getPlayerSocialSpy().forEach(player ->
                messageBuilder.setCommandSender(player).setIDMessage("socialspy-message").sendMessage(
                        new TextReplacer().match("%receiver%").replaceWith(receiver.getName()),
                        new TextReplacer().match("%sender%").replaceWith(sender.getName()),
                        new TextReplacer().match("%text%").replaceWith(text)));
    }


}