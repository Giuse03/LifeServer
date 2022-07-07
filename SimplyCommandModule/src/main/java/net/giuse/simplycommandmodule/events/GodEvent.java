package net.giuse.simplycommandmodule.events;


import net.giuse.mainmodule.MainModule;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.inject.Inject;

public class GodEvent implements Listener {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public GodEvent(MainModule mainModule) {
        this.simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
    }

    @EventHandler
    public void onGod(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (simplyCommandService.getStringsNameGods().contains(e.getEntity().getName())) {
                e.setCancelled(true);
            }
        }

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            if (simplyCommandService.getStringsNameGods().contains(e.getDamager().getName())) {
                e.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void onGodEntity(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            if (simplyCommandService.getStringsNameGods().contains(e.getEntity().getName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onGodBlock(EntityDamageByBlockEvent e) {
        if (e.getEntity() instanceof Player) {
            if (simplyCommandService.getStringsNameGods().contains(e.getEntity().getName())) {
                e.setCancelled(true);
            }
        }
    }
}
