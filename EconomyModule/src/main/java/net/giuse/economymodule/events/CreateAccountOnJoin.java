package net.giuse.economymodule.events;

import net.giuse.economymodule.EconomyService;
import net.giuse.mainmodule.MainModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;

public class CreateAccountOnJoin implements Listener {
    private final EconomyService economyService;
    private final MainModule mainModule;

    @Inject
    public CreateAccountOnJoin(final MainModule mainModule) {
        this.mainModule = mainModule;
        this.economyService = (EconomyService) mainModule.getService(EconomyService.class);
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        if (!this.economyService.getCustomEcoManager().hasAccount(e.getPlayer().getName())) {
            this.economyService.getCustomEcoManager().createPlayerAccount(e.getPlayer().getName());
            this.economyService.getCustomEcoManager().depositPlayer(e.getPlayer().getName(), this.mainModule.getConfig().getDouble("money-first-time-join"));
        }
    }
}
