package net.giuse.kitmodule.databases.kit.querykit;

import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import net.giuse.mainmodule.databases.execute.Query;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.SQLException;

public class SaveKit implements Query {
    private final ExecuteQuery executeQuery;
    private final KitModule kitModule;

    @Inject
    public SaveKit(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
    }


    @Override
    public void query() {
        if (kitModule.getKitElements().isEmpty()) return;
        executeQuery.execute("DROP TABLE Kit;");

        executeQuery.execute("CREATE TABLE IF NOT EXISTS Kit (KitName TEXT, KitItems TEXT, coolDown INT);");

        executeQuery.execute(preparedStatement -> kitModule.getKitElements().forEach((name, kitBuilder) -> {
            try {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, kitBuilder.getElementsKitBase64());
                preparedStatement.setInt(3, kitBuilder.getCoolDown());
                preparedStatement.execute();
            } catch (SQLException e) {
                Bukkit.getLogger().info("Empty Database");
            }

        }), "INSERT INTO Kit VALUES(?,?,?)");


    }
}
