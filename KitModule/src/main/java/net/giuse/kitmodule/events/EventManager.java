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
import java.util.concurrent.ConcurrentMap;


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


    /*
     * Check if player on join has the timer tasks of kits
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (kitModule.getPlayerTime(e.getPlayer().getUniqueId()) == null) {
            PlayerTimerSystem playerTimerSystem = new PlayerTimerSystem();
            kitModule.getKitElements().asMap().forEach(((name, kitBuilder) -> playerTimerSystem.getKitsCooldown().add(new KitCooldown(name.toLowerCase(),kitBuilder))));
            playerTimerSystem.runTaskTimerAsynchronously(mainModule, 20L, 20L);
            kitModule.getCachePlayerKit().put(e.getPlayer().getUniqueId(), playerTimerSystem);
        }
    }
}