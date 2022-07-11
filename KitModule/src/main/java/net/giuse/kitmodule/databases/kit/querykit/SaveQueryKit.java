package net.giuse.kitmodule.databases.kit.querykit;

import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.Savable;
import net.giuse.mainmodule.databases.execute.Callback;
import net.giuse.mainmodule.databases.execute.ExecuteQuery;
import org.bukkit.Bukkit;

import javax.inject.Inject;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SaveQueryKit implements Savable {

    private final ExecuteQuery executeQuery;

    private final KitModule kitModule;

    @Inject
    public SaveQueryKit(MainModule mainModule) {
        executeQuery = mainModule.getInjector().getSingleton(ExecuteQuery.class);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
    }


    @Override
    public void save() {
        executeQuery.execute("DROP TABLE Kit;");

        executeQuery.execute("CREATE TABLE IF NOT EXISTS Kit (KitName TEXT, KitItems TEXT, coolDown INT);");

        kitModule.getKitElements().asMap().forEach((name, kitBuilder) -> executeQuery.execute(new Callback() {
            @Override
            public void setQuery(PreparedStatement preparedStatement) {

                try {
                    preparedStatement.setString(1, name);
                    preparedStatement.setString(2, kitBuilder.getBase64());
                    preparedStatement.setInt(3, kitBuilder.getCoolDown());
                } catch (SQLException e) {
                    Bukkit.getLogger().info("Empty Database");
                }

            }
        }, "INSERT INTO Kit VALUES(?,?,?)"));
    }
}
