package net.giuse.teleportmodule.subservice;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.databases.Savable;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.builder.HomeBuilder;
import net.giuse.teleportmodule.database.HomeOperations;
import net.giuse.teleportmodule.serializer.HomeBuilderSerializer;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class HomeLoaderService extends Services implements Savable {

    @Getter
    private final Set<HomeBuilder> homeBuilders = new HashSet<>();
    private final Serializer<HomeBuilder> homeBuilderSerializer = new HomeBuilderSerializer();
    @Inject
    private MainModule mainModule;
    private HomeOperations homeOperations;

    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Home...");
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        homeOperations = mainModule.getInjector().getSingleton(HomeOperations.class);
        homeOperations.createTable();
        homeOperations.getAllString().forEach(homeBuilder -> {
            homeBuilders.add(homeBuilderSerializer.decoder(homeBuilder));
        });
        if (Files.size(Paths.get("plugins/LifeServer/messages/messages_home.yml")) == 0) {
            teleportModule.getFileManager().setMessagesHome();
        }
        for (String messageConfig : teleportModule.getFileManager().getMessagesHomeYaml().getConfigurationSection("messages.home").getKeys(true)) {
            teleportModule.getMessage().put(messageConfig, teleportModule.getFileManager().getMessagesHomeYaml().getString("messages.home." + messageConfig));
        }
    }

    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Home...");
        save();
    }

    @Override
    public int priority() {
        return 1;
    }


    public HomeBuilder getHome(UUID owner) {
        return homeBuilders.stream().filter(homeBuilder -> homeBuilder.getOwner().equals(owner)).findFirst().orElse(null);
    }

    @Override
    public void save() {
        homeOperations.dropTable();
        homeOperations.createTable();
        homeBuilders.stream().filter(homeBuilders -> !homeBuilders.toString().endsWith("_")).forEach(homeBuilder -> homeOperations.insert(homeBuilderSerializer.encode(homeBuilder)));
    }


}
