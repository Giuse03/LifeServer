package net.giuse.kitmodule.databases.kit.queryplayerkit;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.messages.serializer.serializedobject.PlayerKitCooldownSerialized;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadPlayerKit implements Query {

    private final ExecuteQuery executeQuery;
    private final MainModule mainModule;
    private final KitModule kitModule;

    @Inject
    public LoadPlayerKit(MainModule mainModule) {
        this.mainModule = mainModule;
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
    }

    @Override
    public void query() {
        executeQuery.execute(preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    String playerCooldownSerialized = rs.getString(1) + ";" + rs.getString(2);
                    PlayerKitCooldownSerialized PlayerCooldownDecoded = kitModule.getPlayerCooldownSerializer().decoder(playerCooldownSerialized);
                    PlayerCooldownDecoded.getPlayerKitCooldown().runTaskTimerAsynchronously(mainModule, 20L, 20L);
                    kitModule.getCachePlayerKit().put(PlayerCooldownDecoded.getUuidPlayer(), PlayerCooldownDecoded.getPlayerKitCooldown());
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }, "SELECT * FROM PlayerKit");
    }
}
