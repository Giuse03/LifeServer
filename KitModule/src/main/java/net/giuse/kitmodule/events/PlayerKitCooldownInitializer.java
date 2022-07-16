package net.giuse.kitmodule.events;


import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.cooldownsystem.PlayerKitCooldown;
import net.giuse.mainmodule.MainModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;


/**
 * Register a PlayerTimerSystem on Join
 */

public class PlayerKitCooldownInitializer implements Listener {
    private final MainModule mainModule;

    private final KitModule kitModule;

    @Inject
    public PlayerKitCooldownInitializer(MainModule mainModule) {
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        this.mainModule = mainModule;
    }


    /*
     * Check if player on join has the timer tasks of kits
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (kitModule.getPlayerCooldown(e.getPlayer().getUniqueId()) == null) {
            PlayerKitCooldown joinedPlayer = new PlayerKitCooldown();
            kitModule.getKitElements().forEach(((name, kitBuilder) -> joinedPlayer.addKit(name, kitBuilder.getCoolDown())));
            joinedPlayer.runTaskTimerAsynchronously(mainModule, 20L, 20L);
            kitModule.getCachePlayerKit().put(e.getPlayer().getUniqueId(), joinedPlayer);
        }
    }
}