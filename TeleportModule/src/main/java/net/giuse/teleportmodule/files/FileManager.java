package net.giuse.teleportmodule.files;


import lombok.Getter;
import net.giuse.mainmodule.files.abstractfiles.AbstractConfig;
import net.giuse.mainmodule.files.annotations.FileAnnotation;
import net.giuse.mainmodule.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager extends AbstractConfig {

    @FileAnnotation(name = "messages_warp.yml", path = "plugins/LifeServer/messages/messages_warp.yml")
    private File messagesWarpFile;
    @YamlAnnotation(nameFile = "messages_warp.yml")
    @lombok.Getter
    private YamlConfiguration messagesWarpYaml;
    @FileAnnotation(name = "messages_teleport.yml", path = "plugins/LifeServer/messages/messages_teleport.yml")
    private File messagesTeleportFile;
    @YamlAnnotation(nameFile = "messages_teleport.yml")
    @Getter
    private YamlConfiguration messagesTeleportYaml;
    @FileAnnotation(name = "messages_home.yml", path = "plugins/LifeServer/messages/messages_home.yml")
    private File messagesHome;
    @YamlAnnotation(nameFile = "messages_home.yml")
    @Getter
    private YamlConfiguration messagesHomeYaml;
    @FileAnnotation(name = "messages_spawn.yml", path = "plugins/LifeServer/messages/messages_spawn.yml")
    private File messagesSpawn;
    @YamlAnnotation(nameFile = "messages_spawn.yml")
    @Getter
    private YamlConfiguration messagesSpawnYaml;
    @FileAnnotation(name = "warp_gui_config.yml", path = "plugins/LifeServer/warp_gui_config.yml")
    private File warpFile;
    @YamlAnnotation(nameFile = "warp_gui_config.yml")
    @Getter
    private YamlConfiguration warpYaml;

}