package net.giuse.teleportmodule.database.spawnquery;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.teleportmodule.subservice.SpawnLoaderService;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.SQLException;

public class SaveQuerySpawn implements Query {
    private final ExecuteQuery executeQuery;

    private final SpawnLoaderService spawnModule;

    @Inject
    public SaveQuerySpawn(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        spawnModule = (SpawnLoaderService) mainModule.getService(SpawnLoaderService.class);
    }


    @Override
    public void query() {
        if (spawnModule.getSpawnBuilderSerializer() == null) return;


        executeQuery.execute("DROP TABLE Spawn;");

        executeQuery.execute("CREATE TABLE IF NOT EXISTS Spawn (Location TEXT);");

        executeQuery.execute(preparedStatement -> {
            try {
                if (spawnModule.getSpawnBuilder() != null) {
                    preparedStatement.setString(1, spawnModule.getSpawnBuilderSerializer().encode(spawnModule.getSpawnBuilder()));
                    preparedStatement.execute();
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }


        }, "INSERT INTO Spawn VALUES(?)");
    }
}
