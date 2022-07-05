package net.giuse.kitmodule.events;


import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.mainmodule.MainModule;
import org.bukkit.entity.Player;
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
        kitModule.getPlayerTimerSystems().forEach(playerTimerSystemGeneral -> {
            if (!playerTimerSystemGeneral.getUuid().equals(e.getPlayer().getUniqueId())) {
                for (String kitBuilder : kitModule.getKitElements()) {
                    KitBuilder kb = kitModule.getKitBuilderSerializer().decoder(kitBuilder);
                    PlayerTimerSystem playerTimerSystem = new PlayerTimerSystem(kb.getName(), e.getPlayer().getUniqueId(), kb.getCoolDown(), 0);
                    playerTimerSystem.runTaskTimerAsynchronously(mainModule, 20L, 20L);
                    kitModule.getPlayerTimerSystems().add(playerTimerSystem);
                }
            }
        });
    }
}