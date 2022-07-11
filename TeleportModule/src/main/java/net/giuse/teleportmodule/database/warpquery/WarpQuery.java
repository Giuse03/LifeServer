package net.giuse.teleportmodule.database.warpquery;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.teleportmodule.serializer.serializedobject.WarpSerialized;
import net.giuse.teleportmodule.subservice.WarpLoaderService;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WarpQuery implements Query {

    private final ExecuteQuery executeQuery;

    private final WarpLoaderService warpModule;

    @Inject
    public WarpQuery(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        warpModule = (WarpLoaderService) mainModule.getService(WarpLoaderService.class);
    }

    @Override
    public void query() {
        executeQuery.execute(preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    WarpSerialized warpSerialized = warpModule.getWarpBuilderSerializer().decoder(rs.getString(1) + ":" + rs.getString(2));
                    warpModule.getWarps().put(warpSerialized.getName(), warpSerialized.getLocation());
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }, "SELECT * FROM Warp");
    }
}
