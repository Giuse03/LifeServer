package net.giuse.mainmodule.files.abstractfiles;

import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * Utility for Configs
 */
@Setter
public class AbstractConfig {

    private FileConfiguration yamlConfiguration;
    private File file;

    @SneakyThrows
    public void saveConfig() {
        yamlConfiguration.save(file);
    }

    @SneakyThrows
    public void reload() {
        yamlConfiguration.load(file);
    }


}