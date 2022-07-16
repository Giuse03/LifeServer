package net.giuse.kitmodule.files;


import lombok.Getter;
import net.giuse.mainmodule.files.abstractfiles.AbstractConfig;
import net.giuse.mainmodule.files.annotations.FileAnnotation;
import net.giuse.mainmodule.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * FileManager of KitModule
 */
public class ConfigKits extends AbstractConfig {
    //Message File Config
    @FileAnnotation(name = "messages_kit.yml", path = "plugins/LifeServer/messages/messages_kit.yml")
    private File messagesFile;
    @YamlAnnotation(nameFile = "messages_kit.yml")
    @Getter
    private YamlConfiguration messagesYaml;


    //Gui Config
    @FileAnnotation(name = "kit_gui_config.yml", path = "plugins/LifeServer/kit_gui_config.yml")
    private File kitFile;
    @YamlAnnotation(nameFile = "kit_gui_config.yml")
    @Getter
    private YamlConfiguration kitYaml;

}