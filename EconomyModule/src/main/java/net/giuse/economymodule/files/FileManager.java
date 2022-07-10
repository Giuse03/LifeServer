package net.giuse.economymodule.files;

import lombok.Getter;
import net.giuse.mainmodule.files.abstractfiles.AbstractConfig;
import net.giuse.mainmodule.files.annotations.FileAnnotation;
import net.giuse.mainmodule.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager extends AbstractConfig {
    @FileAnnotation(name = "messages_economy.yml", path = "plugins/LifeServer/messages/messages_economy.yml")
    private File messagesFile;

    @YamlAnnotation(nameFile = "messages_economy.yml")
    @Getter
    private YamlConfiguration messagesYaml;

}
