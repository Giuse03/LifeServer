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

}