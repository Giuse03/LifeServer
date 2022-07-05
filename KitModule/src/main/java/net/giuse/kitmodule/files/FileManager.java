package net.giuse.kitmodule.files;


import lombok.Getter;
import net.giuse.mainmodule.files.abstractfiles.AbstractConfig;
import net.giuse.mainmodule.files.annotations.FileAnnotation;
import net.giuse.mainmodule.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager extends AbstractConfig {
    @FileAnnotation(name = "messages_kit.yml", path = "plugins/LifeServer/messages/messages_kit.yml")
    private File messagesFile;
    @YamlAnnotation(nameFile = "messages_kit.yml")
    @Getter
    private YamlConfiguration messagesYaml;

    @FileAnnotation(name = "kit_gui_config.yml", path = "plugins/LifeServer/kit_gui_config.yml")
    private File kitFile;
    @YamlAnnotation(nameFile = "kit_gui_config.yml")
    @Getter
    private YamlConfiguration kitYaml;

    public void messagesLoader() {
        setFile(messagesFile);
        setYamlConfiguration(messagesYaml);
        messagesYaml.set("messages.kit-cooldown", "&2Kit &a» &7Insert cooldown of kit");
        messagesYaml.set("messages.kit-doesnt-exists", "&2Kit &a» &7This kit doesn't exists");
        messagesYaml.set("messages.kit-insert-name-kit", "&2Kit &a» &7Insert name of kit");
        messagesYaml.set("messages.kit-already-exists", "&2Kit &a» &7This kit already exists");
        messagesYaml.set("messages.kit-created", "&2Kit &a» &7You created the kit %kit%");
        messagesYaml.set("messages.kit-removed", "&2Kit &a» &7You removed the kit %kit%");
        messagesYaml.set("messages.kit-list", "&2Kit &a» §7%listkit%");
        messagesYaml.set("messages.kit-list-empty", "&2Kit &a» &7No kit available.");
        messagesYaml.set("messages.kit-wait", "&2Kit &a» &7You must wait %time%.");
        messagesYaml.set("messages.kit-receive", "&2Kit &a» &7You received the kit %kit%");
        messagesYaml.set("messages.must-have-item", "&2Kit &a» &7Your inventory is empty");

        saveConfig();
    }

}