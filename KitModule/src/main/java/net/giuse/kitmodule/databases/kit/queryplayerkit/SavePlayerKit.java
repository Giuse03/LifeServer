package net.giuse.kitmodule.databases.kit.queryplayerkit;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.messages.serializer.serializedobject.PlayerKitCooldownSerialized;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.SQLException;

public class SavePlayerKit implements Query {

    private final ExecuteQuery executeQuery;

    private final KitModule kitModule;

    @Inject
    public SavePlayerKit(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
    }


    @Override
    public void query() {
        if (kitModule.getCachePlayerKit().isEmpty()) return;

        executeQuery.execute("DROP TABLE PlayerKit;");

        executeQuery.execute("CREATE TABLE IF NOT EXISTS PlayerKit(PlayerUUID TEXT,KitCooldown TEXT);");

        executeQuery.execute(preparedStatement -> kitModule.getCachePlayerKit().forEach(((uuid, playerTimerSystem) -> {
            String[] playerCooldownEncoded = kitModule.getPlayerCooldownSerializer().encode(new PlayerKitCooldownSerialized(uuid, playerTimerSystem)).split(";");
            boolean notEnoughArgument = playerCooldownEncoded.length == 1;

            if (notEnoughArgument) return;
            try {
                preparedStatement.setString(1, playerCooldownEncoded[0]);
                preparedStatement.setString(2, playerCooldownEncoded[1]);
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }
        })), "INSERT INTO PlayerKit VALUES(?,?)");


    }
}
