package net.giuse.mainmodule;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.engine.ProcessEngine;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.mainmodule.databases.ConnectorSQLite;
import net.giuse.mainmodule.files.SQLFile;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.gui.GuiInitializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.mainmodule.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.HashMap;

public class MainModule extends JavaPlugin {
    private final Reflections reflections = new Reflections("net.giuse");
    @Getter
    private final ConnectorSQLite connectorSQLite = new ConnectorSQLite();
    private HashMap<Services, Integer> servicesByPriority = new HashMap<>();
    @Getter
    private Injector injector;

    @Override
    @SneakyThrows
    public void onEnable() {
        long millis = System.currentTimeMillis();
        getLogger().info("§aLifeserver starting...");
        new ProcessEngine(this);
        saveResource("config.yml", false);
        saveResource("kit_gui_config.yml", false);
        saveResource("warp_gui_config.yml", false);
        ReflectionsFiles.loadFiles(new SQLFile());
        connectorSQLite.openConnect();
        injector = new InjectorBuilder().addDefaultHandlers("net.giuse").create();
        injector.register(MainModule.class, this);
        reflections.getSubTypesOf(Services.class).forEach(serviceKlass -> {
            Services services = injector.newInstance(serviceKlass);
            servicesByPriority.put(services, services.priority());
        });
        servicesByPriority = (HashMap<Services, Integer>) Utils.sortByValue(servicesByPriority);
        servicesByPriority.keySet().forEach(Services::load);
        reflections.getSubTypesOf(AbstractCommand.class).forEach(command -> Utils.registerCommand(command.getName(), injector.getSingleton(command)));
        reflections.getSubTypesOf(GuiInitializer.class).forEach(guiInitializer -> injector.getSingleton(guiInitializer).initInv());
        reflections.getSubTypesOf(Listener.class).stream().filter(aClass -> !aClass.getSimpleName().equalsIgnoreCase("FoodEvent")
                        && !aClass.getSimpleName().equalsIgnoreCase("EntityBackOnDeath"))
                .forEach(listener -> Bukkit.getPluginManager().registerEvents(injector.getSingleton(listener), this));
        getLogger().info("§aLifeserver started in " + (System.currentTimeMillis() - millis) + "ms...");
        connectorSQLite.closeConnection();
    }

    @Override
    public void onDisable() {
        servicesByPriority.keySet().forEach(Services::unload);
        connectorSQLite.closeConnection();
    }

    public Services getService(Class<?> name) {
        return servicesByPriority.keySet()
                .stream()
                .filter(services -> services.getClass().equals(name))
                .findFirst().orElseThrow(() -> new NullPointerException("No Service Found"));
    }
}
