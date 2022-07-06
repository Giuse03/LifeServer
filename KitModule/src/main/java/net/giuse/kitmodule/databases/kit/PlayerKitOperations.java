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
        StringBuilder sb = new StringBuilder();
        try {
            PreparedStatement st = mainModule.getConnectorSQLite().getConnection().prepareStatement("SELECT * FROM PlayerKit");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                sb.append(rs.getString(1)).append(";");
                sb.append(rs.getString(2));
                allStrings.add(sb.toString());
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
        String[] args = str.split(";");
        if (args.length == 2) {
            try (PreparedStatement insert = mainModule.getConnectorSQLite().getConnection().prepareStatement("INSERT INTO PlayerKit VALUES(?,?)")) {
                insert.setString(1, args[0]);
                insert.setString(2, args[1]);
                insert.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void createTable() {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS PlayerKit(PlayerUUID TEXT,KitCooldown TEXT);")) {
            stmt.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public boolean isPresent(String string) {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("SELECT PlayerUUID FROM PlayerKit where PlayerUUID = '" + string + "';")) {
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(String string) {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("UPDATE PlayerKit SET PlayerUUID = '" + string + "' WHERE PlayerUUID = '" + string + "'")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(String string) {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("DELETE FROM PlayerKit WHERE PlayerUUID = '" + string + "'")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}