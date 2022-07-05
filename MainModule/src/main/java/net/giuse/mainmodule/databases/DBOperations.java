package net.giuse.mainmodule.databases;

import java.util.ArrayList;

/**
 * Operation in the databases
 */
public interface DBOperations {
    ArrayList<String> getAllString();

    void dropTable();

    void insert(String str);

    void createTable();

    boolean isPresent(String string);

    void update(String string);

    void remove(String string);

}
