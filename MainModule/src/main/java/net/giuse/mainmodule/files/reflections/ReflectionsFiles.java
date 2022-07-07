package net.giuse.mainmodule.files.reflections;


import net.giuse.mainmodule.files.abstractfiles.AbstractConfig;
import net.giuse.mainmodule.files.annotations.FileAnnotation;
import net.giuse.mainmodule.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Automatic Loader of Files via Reflections
 */
public class ReflectionsFiles {

    public static void loadFiles(AbstractConfig... abstractConfigs) throws IllegalAccessException, IOException {
        for (AbstractConfig config : abstractConfigs) {
            for (Field field : config.getClass().getDeclaredFields()) {
                if (!field.isAnnotationPresent(FileAnnotation.class)) continue;
                FileAnnotation fileAnn = field.getAnnotation(FileAnnotation.class);
                if (!field.getType().equals(File.class)) continue;
                field.setAccessible(true);
                field.set(config, new File(fileAnn.path()));
                File file = (File) field.get(config);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                for (Field field2 : config.getClass().getDeclaredFields()) {
                    if (!field2.isAnnotationPresent(YamlAnnotation.class)) continue;
                    YamlAnnotation yamlAnnotation = field2.getAnnotation(YamlAnnotation.class);
                    if (!yamlAnnotation.nameFile().equalsIgnoreCase(fileAnn.name())) continue;
                    field2.setAccessible(true);
                    field2.set(config, YamlConfiguration.loadConfiguration(file));
                    FileConfiguration fileConfiguration = (FileConfiguration) field2.get(config);
                    config.setFile(file);
                    config.setYamlConfiguration(fileConfiguration);
                }
            }
        }
    }

}