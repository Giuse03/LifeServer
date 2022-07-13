package net.giuse.mainmodule;

import ch.jalu.injector.Injector;
import ch.jalu.injector.InjectorBuilder;
import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.engine.ProcessEngine;
import net.giuse.engine.Worker;
import net.giuse.ezmessage.MessageBuilder;
import net.giuse.ezmessage.MessageLoader;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.mainmodule.databases.ConnectorSQLite;
import net.giuse.mainmodule.files.SQLFile;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.gui.GuiInitializer;
import net.giuse.mainmodule.message.MessageLoaderMain;
import net.giuse.mainmodule.services.Services;
import net.giuse.mainmodule.utils.Utils;
import net.lib.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.util.HashMap;

public class MainModule extends JavaPlugin {
    @Getter
    private final Injector injector = new InjectorBuilder().addDefaultHandlers("net.giuse").create();
    private final Reflections reflections = new Reflections("net.giuse");
    @Getter
    private final ConnectorSQLite connectorSQLite = new ConnectorSQLite();
    private HashMap<Services, Integer> servicesByPriority = new HashMap<>();
    @Getter
    private MessageBuilder messageBuilder;
    @Getter
    private MessageLoader messageLoader;

    private MessageLoaderMain messageLoaderMain;
    @Getter
    private ProcessEngine engine;

    /*
     * Enable MainModule
     */
    @Override
    @SneakyThrows
    public void onEnable() {
        //Get current millis for check startup time
        long millis = System.currentTimeMillis();

        //Enable workloads
        engine = new ProcessEngine(this);

        //Set Injector
        injector.register(MainModule.class, this);
        injector.register(Worker.class, new Worker(engine));

        //Loading Message
        messageLoader = new MessageLoader(this, engine);
        messageBuilder = new MessageBuilder(messageLoader);
        injector.getSingleton(MessageLoaderMain.class).load();

        getLogger().info("§aLifeserver starting...");


        //Save default configs
        saveResource("config.yml", false);
        //Load Guis
        saveResource("kit_gui_config.yml", false);
        saveResource("warp_gui_config.yml", false);

        //Load Messages
        saveResource("messages/messages_kit.yml", false);
        saveResource("messages/messages_simple_command.yml", false);
        saveResource("messages/messages_warp.yml", false);
        saveResource("messages/messages_spawn.yml", false);
        saveResource("messages/messages_home.yml", false);
        saveResource("messages/messages_economy.yml", false);
        saveResource("messages/messages_teleport.yml", false);
        saveResource("messages/messages_secret_chat.yml", false);

        //Load SQL
        ReflectionsFiles.loadFiles(new SQLFile());
        connectorSQLite.openConnect();

        //Load Services
        reflections.getSubTypesOf(Services.class).forEach(serviceKlass -> {
            Services services = injector.newInstance(serviceKlass);
            servicesByPriority.put(services, services.priority());
        });
        servicesByPriority = (HashMap<Services, Integer>) Utils.sortByValue(servicesByPriority);
        servicesByPriority.keySet().forEach(Services::load);

        //Load Commands
        reflections.getSubTypesOf(AbstractCommand.class).forEach(command -> Utils.registerCommand(command.getName(), injector.getSingleton(command)));

        //Load Guis
        reflections.getSubTypesOf(GuiInitializer.class).forEach(guiInitializer -> injector.getSingleton(guiInitializer).initInv());

        //Load Events
        reflections.getSubTypesOf(Listener.class).stream().filter(aClass -> !aClass.getSimpleName().equalsIgnoreCase("FoodEvent")
                        && !aClass.getSimpleName().equalsIgnoreCase("EntityBackOnDeath"))
                .forEach(listener -> Bukkit.getPluginManager().registerEvents(injector.getSingleton(listener), this));


        getLogger().info("§aLifeserver started in " + (System.currentTimeMillis() - millis) + "ms...");
        connectorSQLite.closeConnection();


    }

    /*
     * Disable MainModule
     */
    @Override
    public void onDisable() {
        //Unload services
        connectorSQLite.openConnect();
        servicesByPriority.keySet().forEach(Services::unload);
        connectorSQLite.closeConnection();
    }

    /*
     * Get a Service by Class
     */
    public Services getService(Class<?> name) {
        return servicesByPriority.keySet()
                .stream()
                .filter(services -> services.getClass().equals(name))
                .findFirst().orElseThrow(() -> new NullPointerException("No Service Found"));
    }
}
