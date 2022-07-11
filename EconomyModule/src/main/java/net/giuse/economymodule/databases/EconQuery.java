package net.giuse.economymodule.databases;

import net.giuse.economymodule.EconPlayerSerialized;
import net.giuse.economymodule.EconomyService;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EconQuery implements Query {

    private final ExecuteQuery executeQuery;

    private final EconomyService economyService;

    @Inject
    public EconQuery(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        economyService = (EconomyService) mainModule.getService(EconomyService.class);
    }

    @Override
    public void query() {
        executeQuery.execute(preparedStatement -> {
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    EconPlayerSerialized econPlayerSerialized = economyService.getEconPlayerSerializer().decoder(rs.getString(1) + "," + rs.getString(2));
                    economyService.getEconPlayersCache().put(econPlayerSerialized.getUuid(), econPlayerSerialized.getBalance());
                }
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }, "SELECT * FROM Economy");
    }
}
