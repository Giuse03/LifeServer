package net.giuse.simplycommandmodule.files;


import lombok.Getter;
import net.giuse.mainmodule.files.abstractfiles.AbstractConfig;
import net.giuse.mainmodule.files.annotations.FileAnnotation;
import net.giuse.mainmodule.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager extends AbstractConfig {

    @FileAnnotation(name = "messages_simple_command.yml", path = "plugins/LifeServer/messages/messages_simple_command.yml")
    private File messageSimpleFile;
    @YamlAnnotation(nameFile = "messages_simple_command.yml")
    @Getter
    private YamlConfiguration messageSimpleFileYml;

}