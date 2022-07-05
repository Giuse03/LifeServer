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


    public void messagesWarpLoader() {
        setFile(messagesWarpFile);
        setYamlConfiguration(messagesWarpYaml);
        messagesWarpYaml.set("messages.warp.no-warp-available", "&2Warp &a» &7No warp available");
        messagesWarpYaml.set("messages.warp.warp-list", "&2Warp &a» &7%list%");
        messagesWarpYaml.set("messages.warp.warp-teleport", "&2Warp &a» &7Warping to %name%...");
        messagesWarpYaml.set("messages.warp.warp-insert-name", "&2Warp &a» &7Insert the name of this warp");
        messagesWarpYaml.set("messages.warp.warp-created", "&2Warp &a» &7You created the warp %name%");
        messagesWarpYaml.set("messages.warp.warp-removed", "&2Warp &a» &7You removed the warp %name%");
        messagesWarpYaml.set("messages.warp.warp-already-exists", "&2Warp &a» &7This warp already exists");
        messagesWarpYaml.set("messages.warp.warp-no-exists", "&2Warp &a» &7Warp %name% does not exist");
        saveConfig();
    }


    public void messagesTeleportLoader() {
        setFile(messagesTeleportFile);
        setYamlConfiguration(messagesTeleportYaml);
        messagesTeleportYaml.set("messages.teleport.select-player", "&2TP &a» &7Select a Player");
        messagesTeleportYaml.set("messages.teleport.teleport-player", "&2TP &a» &7Teleporting to %playername%...");
        messagesTeleportYaml.set("messages.teleport.player-not-found", "&2TP &a» &7This player isn't online");
        messagesTeleportYaml.set("messages.teleport.tpa-request-sender", "&2TP &a» &7Tpa sent to %playername%...");
        messagesTeleportYaml.set("messages.teleport.tpa-request-receiver", "&2%playername% wants to come to YOU\n &aType &2/tpaccept &2for accept request \n&2Type &2/tpdeny&a for refuse the request");
        messagesTeleportYaml.set("messages.teleport.tpahere-request-sender", "&2TP &a» &7TpaHere sent to %playername%...");
        messagesTeleportYaml.set("messages.teleport.tpahere-request-receiver", "&2%playername% wants you to come to HIM \n &aType &2/tpaccept &2for accept request\n&2Type &2/tpdeny&a for refuse the request");
        messagesTeleportYaml.set("messages.teleport.no-pending-request", "&2TP &a» &7There aren't requests pending");
        messagesTeleportYaml.set("messages.teleport.request-refused", "&2TP &a» &7%playername% refused your teleport request");
        messagesTeleportYaml.set("messages.teleport.request-accept-receiver", "&2TP &a» &7%playername% is coming to you");
        messagesTeleportYaml.set("messages.teleport.back", "&2Back &a» &7Teleported on previous location");
        messagesTeleportYaml.set("messages.teleport.back-no-location", "&2Back &a» &7No previous location found!");


        saveConfig();
    }


    public void setMessagesHome() {
        setFile(messagesHome);
        setYamlConfiguration(messagesHomeYaml);
        messagesHomeYaml.set("messages.home.teleported", "&2Home &a» &7Teleported to home");
        messagesHomeYaml.set("messages.home.sethome", "&2Home &a» &7Home created!");
        messagesHomeYaml.set("messages.home.deleted_home", "&2Home &a» &7Home deleted!");
        messagesHomeYaml.set("messages.home.home_list", "&7Available home: %list%.");
        messagesHomeYaml.set("messages.home.no_home_found", "&2Home &a» &7No home found!");
        messagesHomeYaml.set("messages.home.max_home_reached", "&2Home &a» &7Max Home Reached!");
        messagesHomeYaml.set("messages.home.select-home", "&2Home &a» &7Select a home to delete!");


        saveConfig();
    }

    public void setMessagesSpawn() {
        setFile(messagesSpawn);
        setYamlConfiguration(messagesSpawnYaml);
        messagesSpawnYaml.set("messages.spawn.setspawn", "&2Spawn &a» &7Spawn Setted");
        messagesSpawnYaml.set("messages.spawn.removespawn", "&2Spawn &a» &7Spawn Removed!");
        messagesSpawnYaml.set("messages.spawn.teleported-spawn", "&2Spawn &a» &7Teleported Spawn!");
        messagesSpawnYaml.set("messages.spawn.no-spawn", "&2Spawn &a» &7Spawn not found!");


        saveConfig();
    }
}