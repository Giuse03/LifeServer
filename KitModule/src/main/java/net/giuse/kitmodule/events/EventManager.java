package net.giuse.kitmodule.events;


import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.mainmodule.MainModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;


public class EventManager implements Listener {
    private final MainModule mainModule;

    private final KitModule kitModule;

    @Inject
    public EventManager(MainModule mainModule) {
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        this.mainModule = mainModule;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        for (KitBuilder kitBuilder : kitModule.getKitElements()) {
            if (kitModule.getPlayerTime(e.getPlayer().getUniqueId(), kitBuilder) == null) {
                PlayerTimerSystem playerTimerSystem = new PlayerTimerSystem(kitBuilder.getName(), e.getPlayer().getUniqueId(), kitBuilder.getCoolDown(), 0);
                playerTimerSystem.runTaskTimerAsynchronously(mainModule, 20L, 20L);
                kitModule.getPlayerTimerSystems().add(playerTimerSystem);
            }
        }
    }
}