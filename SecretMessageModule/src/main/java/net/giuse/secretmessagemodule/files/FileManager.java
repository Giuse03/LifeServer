package net.giuse.secretmessagemodule.files;


import lombok.Getter;
import net.giuse.mainmodule.files.abstractfiles.AbstractConfig;
import net.giuse.mainmodule.files.annotations.FileAnnotation;
import net.giuse.mainmodule.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager extends AbstractConfig {

    @FileAnnotation(name = "messages_secret_chat.yml", path = "plugins/LifeServer/messages/messages_secret_chat.yml")
    private File messagesSecretChat;
    @YamlAnnotation(nameFile = "messages_secret_chat.yml")
    @Getter
    private YamlConfiguration messagesSecretChatYaml;

    /*
     *  Load Messages
     */
    public void messagesMsgLoader() {
        setFile(messagesSecretChat);
        setYamlConfiguration(messagesSecretChatYaml);
        messagesSecretChatYaml.set("messages.msg.sender", "&7(&eSecret Chat&7) &8|  &2[&aYou &7to &a%receiver_name%&2] &8-> &7%text%");
        messagesSecretChatYaml.set("messages.msg.receiver", "&7(&eSecret Chat&7) &8| &2[&a%sender_name%&2 &7to &aYou&2] &8-> &7%text%");
        messagesSecretChatYaml.set("messages.msg.player-offline", "&aSecretChat &2» &7This Player is offline");
        messagesSecretChatYaml.set("messages.msg.insert-text", "&aSecretChat &2» &7Insert the text to send");
        messagesSecretChatYaml.set("messages.msg.usage", "&aSecretChat &2» &7Usage: /msg <player> [text]");
        messagesSecretChatYaml.set("messages.msg.nobody-reply", "&aSecretChat &2» &7Nobody to reply");
        messagesSecretChatYaml.set("messages.msg.msgtoggle", "&aSecretChat &2» &7MSGToggle %status%");
        messagesSecretChatYaml.set("messages.msg.socialspy", "&aSecretChat &2» &7SocialSpy %status%");
        messagesSecretChatYaml.set("messages.msg.socialspy-message", "&7(&eSocialSpy&7) &8| &a%sender% &7to &a%receiver%&7: %text%");
        messagesSecretChatYaml.set("messages.msg.msgtoggle-on", "&aSecretChat &2» &7 One of the two has the msgtoggle active,the message cannot be sent");


        saveConfig();
    }

}