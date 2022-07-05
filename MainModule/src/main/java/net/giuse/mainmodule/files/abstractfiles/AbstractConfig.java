package net.giuse.mainmodule.files.abstractfiles;

import lombok.Setter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Utility for Configs
 */
public class AbstractConfig {

    @Setter
    private FileConfiguration yamlConfiguration;
    @Setter
    private File file;

    public void saveConfig() {
        try {
            yamlConfiguration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        try {
            yamlConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {

            e.printStackTrace();
        }
    }


}