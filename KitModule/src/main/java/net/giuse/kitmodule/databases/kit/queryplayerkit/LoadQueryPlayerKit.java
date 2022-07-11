package net.giuse.kitmodule.databases.kit.queryplayerkit;

import lombok.SneakyThrows;
import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.serializer.serializedobject.KitSerialized;
import net.giuse.kitmodule.serializer.serializedobject.PlayerKitTimeSerialized;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.Callback;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadQueryPlayerKit implements Query {

    private final ExecuteQuery executeQuery;
    private final MainModule mainModule;
    private final KitModule kitModule;

    @Inject
    public LoadQueryPlayerKit(MainModule mainModule){
        this.mainModule = mainModule;
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
    }

    @Override
    public void query() {
        executeQuery.execute(preparedStatement -> {
            try(ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    PlayerKitTimeSerialized playerTimerSystem = kitModule.getPlayerKitTimeSerializer().decoder(rs.getString(1) + ";" + rs.getString(2));
                    playerTimerSystem.getPlayerTimerSystem().runTaskTimerAsynchronously(mainModule, 20L, 20L);
                    kitModule.getCachePlayerKit().put(playerTimerSystem.getUuid(), playerTimerSystem.getPlayerTimerSystem());
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }, "SELECT * FROM PlayerKit");
    }
}
