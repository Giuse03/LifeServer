package net.giuse.mainmodule.files.reflections;


import lombok.SneakyThrows;
import net.giuse.mainmodule.files.abstractfiles.AbstractConfig;
import net.giuse.mainmodule.files.annotations.FileAnnotation;
import net.giuse.mainmodule.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.lang.reflect.Field;

/**
 * Automatic Loader of Files via Reflections
 */
public class ReflectionsFiles {

    public static void loadFiles(AbstractConfig instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (!checkIfFieldIsAnnotated(field)) continue;

            FileAnnotation fileAnn = field.getAnnotation(FileAnnotation.class);
            File file = getFileFromField(fileAnn.path(), field, instance);
            setupFile(file);
            setupYamlFiles(fileAnn.name(), file, instance);
        }
    }


    @SneakyThrows
    private static void setupYamlFiles(String annotationName, File file, AbstractConfig instance) {
        for (Field field : instance.getClass().getDeclaredFields()) {
            if (!field.isAnnotationPresent(YamlAnnotation.class)) continue;

            //declarations
            YamlAnnotation yamlAnnotation = field.getAnnotation(YamlAnnotation.class);

            if (!yamlAnnotation.nameFile().equalsIgnoreCase(annotationName)) continue;

            loadYaml(field, file, instance);
        }
    }

    @SneakyThrows
    private static void loadYaml(Field field, File file, AbstractConfig instance) {
        field.setAccessible(true);
        System.out.println("Loaded " + file.getAbsoluteFile());
        YamlConfiguration yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        field.set(instance, yamlConfiguration);
    }

    @SneakyThrows
    private static void setupFile(File file) {
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    @SneakyThrows
    private static File getFileFromField(String filePath, Field field, AbstractConfig instance) {
        field.setAccessible(true);
        field.set(instance, new File(filePath));
        return (File) field.get(instance);
    }

    private static boolean checkIfFieldIsAnnotated(Field field) {
        return field.isAnnotationPresent(FileAnnotation.class) && field.getType().equals(File.class);
    }

}