package net.giuse.teleportmodule.database;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.DBOperations;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class WarpOperations implements DBOperations {
    @Inject
    private MainModule mainModule;

    @Override
    public ArrayList<String> getAllString() {
        ArrayList<String> allStrings = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();
        try (PreparedStatement st = mainModule.getConnectorSQLite().getConnection().prepareStatement("SELECT * FROM Warp")) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                stringBuilder.append(rs.getString(1)).append("-");
                stringBuilder.append(rs.getString(2));
                allStrings.add(stringBuilder.toString());
            }
        } catch (SQLException ignored) {
            mainModule.getLogger().info("Empty Database");
        }
        return allStrings;
    }

    @Override
    public void dropTable() {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("DROP TABLE Warp;");) {
            stmt.execute();
        } catch (SQLException ignored) {
            mainModule.getLogger().info("Empty Database");
        }
    }

    @Override
    public void insert(final String str) {
        String[] args = str.split("-");
        try (PreparedStatement insert = mainModule.getConnectorSQLite().getConnection().prepareStatement("INSERT INTO Warp VALUES(?,?)");) {
            insert.setString(1, args[0]);
            insert.setString(2, args[1]);
            insert.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Warp (Name TEXT,Location TEXT);");) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPresent(String string) {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("SELECT name FROM Warp where name = '" + string + "';")) {
            ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void update(String string) {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("UPDATE Warp SET name = '" + string + "' WHERE name = '" + string + "'")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(String string) {
        try (PreparedStatement stmt = mainModule.getConnectorSQLite().getConnection().prepareStatement("DELETE FROM Warp WHERE name = '" + string + "'")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}