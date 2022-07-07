package net.giuse.secretmessagemodule.process;

import lombok.Getter;
import lombok.Setter;
import net.giuse.mainmodule.MainModule;
import net.giuse.secretmessagemodule.SecretChatBuilder;
import net.giuse.secretmessagemodule.SecretMessageModule;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.inject.Inject;


public class SecretChatProcess {
    private final SecretChatBuilder secretChatBuilder = new SecretChatBuilder();
    private final SecretMessageModule secretMessageModule;
    @Setter
    @Getter
    private Player sender, receiver;
    @Setter
    @Getter
    private String text;

    @Inject
    public SecretChatProcess(MainModule mainModule) {
        this.secretMessageModule = (SecretMessageModule) mainModule.getService(SecretMessageModule.class);
    }

    /*
     * Send a message from Sender to Receiver
     */
    public void send() {
        //Check if msg-toggle is ON
        if (secretMessageModule.getPlayerMsgToggle().contains(receiver) || secretMessageModule.getPlayerMsgToggle().contains(sender)) {
            sender.sendMessage(secretMessageModule.getMessages("msgtoggle-on"));
            return;
        }
        //Send Message
        receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', secretMessageModule.getMessage().get("receiver")).replace("%sender_name%", sender.getName()).replace("%text%", text));
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', secretMessageModule.getMessage().get("sender")).replace("%receiver_name%", receiver.getName()).replace("%text%", text));

        //Update sender and Receiver
        secretChatBuilder.setSender(sender);
        secretChatBuilder.setReceiver(receiver);
        secretChatBuilder.setText(text);
        secretMessageModule.getSecretsChats().add(secretChatBuilder);
        secretMessageModule.getPlayerSocialSpy().forEach(player -> player.sendMessage(secretMessageModule.getMessages("socialspy-message").replace("%receiver%", receiver.getName()).replace("%sender%", sender.getName()).replace("%text%", text)));
    }


}