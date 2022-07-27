package net.giuse.teleportmodule.events;


import net.giuse.mainmodule.MainModule;
import net.giuse.teleportmodule.subservice.HomeLoaderService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;
import java.util.HashMap;

public class JoinEvent implements Listener {
    private final HomeLoaderService homeLoaderService;

    private final MainModule mainModule;

    @Inject
    public JoinEvent(MainModule mainModule) {
        this.mainModule = mainModule;
        homeLoaderService = (HomeLoaderService) mainModule.getService(HomeLoaderService.class);
    }

    /*
     * Check if player has a HomeBuilder instance
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (homeLoaderService.getHome(e.getPlayer().getUniqueId()) == null) {
            homeLoaderService.getCacheHome().put(e.getPlayer().getUniqueId(), new HashMap<>());
        }
    }

}
