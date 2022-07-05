package net.giuse.kitmodule.databases.kit;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.DBOperations;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PlayerKitOperations implements DBOperations {

    @Inject
    private MainModule mainModule;

    @Override
    public ArrayList<String> getAllString() {
        ArrayList<String> allStrings = new ArrayList<>();
        try {
            PreparedStatement st = mainModule.getConnectorSQLite().getConnection().prepareStatement("SELECT * FROM PlayerKit");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                allStrings.add(rs.getString(1));
            }
            st.close();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Empty Database");
        }
        return allStrings;
    }

    @Override
    public void dropTable() {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("DROP TABLE PlayerKit;")) {
            stmt.execute();
        } catch (SQLException e) {
            mainModule.getLogger().info("Empty Database");
        }
    }

    @Override
    public void insert(final String str) {
        try (PreparedStatement insert = mainModule.getConnectorSQLite().getConnection().prepareStatement("INSERT INTO PlayerKit VALUES(?)")) {
            insert.setString(1, str);
            insert.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS PlayerKit(ptime TEXT);")) {
            stmt.execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public boolean isPresent(String string) {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("SELECT ptime FROM PlayerKit where ptime = '" + string + "';")) {
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(String string) {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("UPDATE PlayerKit SET ptime = '" + string + "' WHERE ptime = '" + string + "'")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(String string) {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("DELETE FROM PlayerKit WHERE ptime = '" + string + "'")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}