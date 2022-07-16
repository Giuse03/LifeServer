package net.giuse.economymodule.databases;


import net.giuse.economymodule.EconPlayerSerialized;
import net.giuse.economymodule.EconomyService;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.SQLException;

public class SaveQueryEcon implements Query {
    private final ExecuteQuery executeQuery;
    private final EconomyService economyService;

    @Inject
    public SaveQueryEcon(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        economyService = (EconomyService) mainModule.getService(EconomyService.class);
    }


    @Override
    public void query() {
        if (economyService.getEconPlayersCache().isEmpty()) return;

        executeQuery.execute("DROP TABLE Economy;");

        executeQuery.execute("CREATE TABLE IF NOT EXISTS Economy (name TEXT, balance DOUBLE);");

        executeQuery.execute(preparedStatement -> economyService.getEconPlayersCache().forEach((uuid, balance) -> {
            try {
                String[] args = economyService.getEconPlayerSerializer().encode(new EconPlayerSerialized(uuid, balance)).split(",");
                preparedStatement.setString(1, args[0]);
                preparedStatement.setString(2, args[1]);
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }), "INSERT INTO Economy VALUES(?,?);");


    }
}
