package net.giuse.teleportmodule.database.homequery;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.teleportmodule.serializer.serializedobject.HomeSerialized;
import net.giuse.teleportmodule.subservice.HomeLoaderService;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeQuery implements Query {

    private final ExecuteQuery executeQuery;

    private final HomeLoaderService homeModule;

    @Inject
    public HomeQuery(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        homeModule = (HomeLoaderService) mainModule.getService(HomeLoaderService.class);
    }

    @Override
    public void query() {
        executeQuery.execute(preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    HomeSerialized homeSerialized = homeModule.getHomeBuilderSerializer().decoder(rs.getString(1) + ":" + rs.getString(2));
                    homeModule.getCacheHome().put(homeSerialized.getOwner(), homeSerialized.getLocations());
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }, "SELECT * FROM Home");
    }
}
