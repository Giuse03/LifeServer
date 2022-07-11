package net.giuse.kitmodule.databases.kit.querykit;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.serializer.serializedobject.KitSerialized;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.Callback;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadQueryKit implements Query {

    private final ExecuteQuery executeQuery;

    private final KitModule kitModule;

    @Inject
    public LoadQueryKit(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
    }

    @Override
    public void query() {
        executeQuery.execute(preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    KitSerialized kitSerialized = kitModule.getKitBuilderSerializer().decoder(
                            rs.getString(1) + ":" +
                                    rs.getInt(3) + ":" +
                                    rs.getString(2));
                    kitSerialized.getKitBuilder().build();
                    kitModule.getKitElements().put(kitSerialized.getName(), kitSerialized.getKitBuilder());
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }, "SELECT * FROM Kit");
    }
}
