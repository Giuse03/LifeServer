package net.giuse.mainmodule.databases;


import lombok.Getter;
import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Connector for SQLite
 */
public class ConnectorSQLite implements Connector {

    @Getter
    private Connection connection;

    @Override
    @SneakyThrows
    public void openConnect() {
        Class.forName("org.sqlite.JDBC");
        this.connection = DriverManager.getConnection("jdbc:sqlite:plugins/LifeServer/sql.db");
    }

    @SneakyThrows
    public void closeConnection() {
        if (connection == null) {
            return;
        }
        this.connection.close();
    }

}

