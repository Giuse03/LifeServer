package net.giuse.teleportmodule.events;

import net.giuse.mainmodule.MainModule;
import net.giuse.teleportmodule.builder.HomeBuilder;
import net.giuse.teleportmodule.subservice.HomeLoaderService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

public class JoinEvent implements Listener {
    private final HomeLoaderService homeLoaderService;

    @Inject
    public JoinEvent(MainModule mainModule) {
        homeLoaderService = (HomeLoaderService) mainModule.getService(HomeLoaderService.class);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (homeLoaderService.getHome(e.getPlayer().getName()) == null)
            homeLoaderService.getHomeBuilders().add(new HomeBuilder(e.getPlayer().getName()));
    }

}
