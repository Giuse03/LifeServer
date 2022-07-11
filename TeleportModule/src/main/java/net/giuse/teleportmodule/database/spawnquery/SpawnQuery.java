package net.giuse.teleportmodule.database.spawnquery;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.teleportmodule.subservice.SpawnLoaderService;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SpawnQuery implements Query {

    private final ExecuteQuery executeQuery;

    private final SpawnLoaderService spawnModule;

    @Inject
    public SpawnQuery(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        spawnModule = (SpawnLoaderService) mainModule.getService(SpawnLoaderService.class);
    }

    @Override
    public void query() {
        executeQuery.execute(preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                spawnModule.setSpawnBuilder(spawnModule.getSpawnBuilderSerializer().decoder(rs.getString(1)));
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }, "SELECT * FROM Spawn");
    }
}
