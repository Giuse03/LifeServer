package net.giuse.kitmodule.events;


import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.cooldownsystem.KitCooldown;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.mainmodule.MainModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;


/**
 * Register a PlayerTimerSystem on Join
 */

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
        if (kitModule.getPlayerTime(e.getPlayer().getUniqueId()) == null) {
            PlayerTimerSystem playerTimerSystem = new PlayerTimerSystem(e.getPlayer().getUniqueId());
            for (KitBuilder kitBuilder : kitModule.getKitElements()) {
                playerTimerSystem.getKitsCooldown().add(new KitCooldown(kitBuilder));
            }

            playerTimerSystem.runTaskTimerAsynchronously(mainModule, 20L, 20L);
            kitModule.getPlayerTimerSystems().add(playerTimerSystem);
        }
    }
}