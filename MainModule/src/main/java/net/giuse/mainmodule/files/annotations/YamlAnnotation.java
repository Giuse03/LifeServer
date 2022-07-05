package net.giuse.mainmodule.files.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for uploading files via reflection, this get YamlConfiguration from the file
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface YamlAnnotation {
    String nameFile() default "";

}