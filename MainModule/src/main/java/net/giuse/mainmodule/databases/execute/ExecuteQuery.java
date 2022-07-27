package net.giuse.mainmodule.databases.execute;


import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ExecuteQuery {
    private final MainModule mainModule;


    @Inject
    @SneakyThrows
    public ExecuteQuery(MainModule mainModule) {
        this.mainModule = mainModule;
    }

    /*
        @SneakyThrows
        public void executeDistribuited(Callback callback, String query, boolean async) {
            try (PreparedStatement preparedStatement = mainModule.getConnectorSQLite().getConnection().prepareStatement(query)) {
                worker.executeProcess(() -> new Workload() {
                    @Override
                    public void compute() {
                        callback.setQuery(preparedStatement);
                    }
                }, async);
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }
        }
    */
    @SneakyThrows
    public void execute(Callback callback, String query) {
        try (PreparedStatement preparedStatement = mainModule.getConnectorSQLite().getConnection().prepareStatement(query)) {
            callback.setQuery(preparedStatement);
        } catch (SQLException e) {
            Bukkit.getLogger().info("Empty Database");
        }
    }


    @SneakyThrows
    public void execute(String query) {
        try (PreparedStatement preparedStatement = mainModule.getConnectorSQLite().getConnection().prepareStatement(query)) {
            preparedStatement.execute();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Empty Database");
        }
    }

}
