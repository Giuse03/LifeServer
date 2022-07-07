package net.giuse.teleportmodule.subservice;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.TeleportModule;
import net.giuse.teleportmodule.teleporrequest.PendingRequest;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TeleportRequestService extends Services {
    @Getter
    private final Set<PendingRequest> pendingRequests = new HashSet<>();
    @Inject
    private MainModule mainModule;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Loading Teleport Requests...");
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);

        //Load File
        if (Files.size(Paths.get("plugins/LifeServer/messages/messages_teleport.yml")) == 0) {
            teleportModule.getFileManager().messagesTeleportLoader();
        }

        //Load Message
        for (String messageConfig : teleportModule.getFileManager().getMessagesTeleportYaml().getConfigurationSection("messages.teleport").getKeys(true)) {
            teleportModule.getMessage().put(messageConfig, teleportModule.getFileManager().getMessagesTeleportYaml().getString("messages.teleport." + messageConfig));
        }
    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eTeleportModule§9] §7Unloading Teleport Requests...");
    }

    /*
     * Get Service Priority
     */
    @Override
    public int priority() {
        return 1;
    }


    /*
     * Get a PendingRequest from a player's UUID
     */
    public PendingRequest getPending(UUID uuid) {
        return pendingRequests.stream()
                .filter(pendingRequests -> pendingRequests.getReceiver().getUniqueId().equals(uuid))
                .findFirst().orElse(null);
    }

}
