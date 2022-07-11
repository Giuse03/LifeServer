package net.giuse.kitmodule.databases.kit.queryplayerkit;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.serializer.serializedobject.PlayerKitTimeSerialized;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.Savable;
import net.giuse.mainmodule.databases.execute.Callback;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveQueryPlayerKit implements Savable {

    private final ExecuteQuery executeQuery;

    private final KitModule kitModule;

    @Inject
    public SaveQueryPlayerKit(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
    }


    @Override
    public void save() {
        executeQuery.execute("DROP TABLE PlayerKit;");

        executeQuery.execute("CREATE TABLE IF NOT EXISTS PlayerKit(PlayerUUID TEXT,KitCooldown TEXT);");

        executeQuery.execute(preparedStatement -> kitModule.getCachePlayerKit().asMap().forEach(((uuid, playerTimerSystem) -> {
            String[] args = kitModule.getPlayerKitTimeSerializer().encode(new PlayerKitTimeSerialized(uuid, playerTimerSystem)).split(";");
            try {
                preparedStatement.setString(1, args[0]);
                preparedStatement.setString(2, args[1]);
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }
        })), "INSERT INTO PlayerKit VALUES(?,?)");


    }
}
