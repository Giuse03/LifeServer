package net.giuse.mainmodule.databases;


import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Connector for SQLite
 */
public class ConnectorSQLite implements Connector {

    @Getter
    private Connection connection;

    @Override
    public void openConnect() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:plugins/LifeServer/sql.db");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isOpen() {
        return connection != null;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            this.connection = null;
        }
    }

}

