package net.giuse.teleportmodule.database.warpquery;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import net.giuse.teleportmodule.serializer.serializedobject.WarpSerialized;
import net.giuse.teleportmodule.subservice.WarpLoaderService;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.SQLException;

public class SaveQueryWarp implements Query {
    private final ExecuteQuery executeQuery;
    private final WarpLoaderService warpModule;

    @Inject
    public SaveQueryWarp(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        warpModule = (WarpLoaderService) mainModule.getService(WarpLoaderService.class);
    }


    @Override
    public void query() {
        if (warpModule.getWarps().isEmpty()) return;

        executeQuery.execute("DROP TABLE Warp;");

        executeQuery.execute("CREATE TABLE IF NOT EXISTS Warp (Name TEXT,Location TEXT)");

        executeQuery.execute(preparedStatement -> warpModule.getWarps().forEach((uuid, hashMap) -> {
            try {
                String[] args = warpModule.getWarpBuilderSerializer().encode(new WarpSerialized(uuid, hashMap)).split(":");
                preparedStatement.setString(1, args[0]);
                preparedStatement.setString(2, args[1]);
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }), "INSERT INTO Warp VALUES(?,?)");


    }
}
