package net.giuse.teleportmodule.database.homequery;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.teleportmodule.serializer.serializedobject.HomeSerialized;
import net.giuse.teleportmodule.subservice.HomeLoaderService;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.SQLException;

public class SaveQueryHome implements Query {
    private final ExecuteQuery executeQuery;
    private final HomeLoaderService homeModule;

    @Inject
    public SaveQueryHome(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        homeModule = (HomeLoaderService) mainModule.getService(HomeLoaderService.class);
    }


    @Override
    public void query() {
        if (homeModule.getCacheHome().isEmpty()) return;


        executeQuery.execute("DROP TABLE Home;");

        executeQuery.execute("CREATE TABLE IF NOT EXISTS Home (UUID TEXT,Location TEXT);");

        executeQuery.execute(preparedStatement -> homeModule.getCacheHome().forEach((uuid, hashMap) -> {
            String[] args = homeModule.getHomeBuilderSerializer().encode(new HomeSerialized(uuid, hashMap)).split(":");
            if (args.length == 1) return;
            try {
                preparedStatement.setString(1, args[0]);
                preparedStatement.setString(2, args[1]);
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }), "INSERT INTO Home VALUES(?,?)");


    }
}
