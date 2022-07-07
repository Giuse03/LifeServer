package net.giuse.teleportmodule.events;


import net.giuse.mainmodule.MainModule;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import javax.inject.Inject;

public class EntityBackOnDeath implements Listener {
    private final TeleportModule teleportService;

    @Inject
    public EntityBackOnDeath(MainModule mainModule) {
        this.teleportService = (TeleportModule) mainModule.getService(TeleportModule.class);
    }

    /*
     * Event for back on death
     */
    @EventHandler
    public void onTeleport(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            teleportService.getBackLocations().put((Player) e.getEntity(), e.getEntity().getLocation());
        }
    }


}
