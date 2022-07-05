package net.giuse.mainmodule.databases;

/**
 * Connector for databases
 */
public interface Connector {

    void openConnect();

    void closeConnection();
}
