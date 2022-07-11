package net.giuse.teleportmodule.subservice;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.services.Services;
import net.giuse.teleportmodule.teleporrequest.PendingRequest;

import javax.inject.Inject;
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
