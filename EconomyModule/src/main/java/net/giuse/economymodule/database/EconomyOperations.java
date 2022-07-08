package net.giuse.economymodule.database;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.DBOperations;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EconomyOperations implements DBOperations {
    @Inject
    private MainModule mainModule;

    @Override
    public ArrayList<String> getAllString() {
        final ArrayList<String> allStrings = new ArrayList<>();
        try (final PreparedStatement st = this.mainModule.getConnectorSQLite().getConnection().prepareStatement("SELECT * FROM Economy")) {
            final ResultSet rs = st.executeQuery();
            while (rs.next()) {
                allStrings.add(rs.getString(1) + "," + rs.getDouble(2));
            }
        } catch (SQLException ignored) {
            this.mainModule.getLogger().info("Empty Database");
        }
        return allStrings;
    }

    @Override
    public void dropTable() {
        try (final PreparedStatement stmt = this.mainModule.getConnectorSQLite().getConnection().prepareStatement("DROP TABLE Economy;")) {
            stmt.execute();
        } catch (SQLException ignored) {
            this.mainModule.getLogger().info("Empty Database");
        }
    }

    @Override
    public void insert(final String str) {
        final String[] args = str.split(",");
        try (final PreparedStatement insert = this.mainModule.getConnectorSQLite().getConnection().prepareStatement("INSERT INTO Economy VALUES(?,?);")) {
            insert.setString(1, args[0]);
            insert.setDouble(2, Double.parseDouble(args[1]));
            insert.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        try (final PreparedStatement stmt = this.mainModule.getConnectorSQLite().getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS Economy (name TEXT, balance DOUBLE);")) {
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPresent(final String string) {
        try (final PreparedStatement stmt = this.mainModule.getConnectorSQLite().getConnection().prepareStatement("SELECT name FROM Economy where name = '" + string + "';")) {
            final ResultSet resultSet = stmt.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void update(final String string) {
        final String[] args = string.split(",");
        try (final PreparedStatement stmt = this.mainModule.getConnectorSQLite().getConnection().prepareStatement("UPDATE Economy SET balance = " + Double.parseDouble(args[1]) + " WHERE name = '" + args[0] + "'")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(final String string) {
    }
}
