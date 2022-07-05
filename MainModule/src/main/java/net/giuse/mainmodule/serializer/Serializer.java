package net.giuse.mainmodule.serializer;

/**
 * Serializer interface for save in databases
 *
 * @param <T>
 */
public interface Serializer<T> {
    String encode(T t);

    T decoder(String str);
}
