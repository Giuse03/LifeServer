package net.giuse.kitmodule.databases.kit.querykit;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import org.bukkit.Bukkit;

import javax.inject.Inject;
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
                    String kitName = rs.getString(1);
                    int kitCooldown = rs.getInt(3);
                    String elementsKitBase64 = rs.getString(2);

                    KitBuilder kitBuilderDecoded = new KitBuilder(kitCooldown);
                    kitBuilderDecoded.setBase(elementsKitBase64);
                    kitBuilderDecoded.build();
                    kitModule.getKitElements().put(kitName, kitBuilderDecoded);
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }, "SELECT * FROM Kit");
    }
}
