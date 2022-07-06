package net.giuse.stress;

import io.papermc.lib.PaperLib;
import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.secretmessagemodule.process.SecretChatProcess;
import net.giuse.simplycommandmodule.SimplyCommandService;
import net.giuse.teleportmodule.subservice.HomeLoaderService;
import net.giuse.teleportmodule.subservice.WarpLoaderService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class StresserCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;
    private final WarpLoaderService warpLoaderService;
    private final HomeLoaderService homeLoaderService;
    private final KitModule kitModule;

    private final SecretChatProcess secretChatProcess;



    @Inject
    public StresserCommand(MainModule mainModule) {
        super("stress", "lifeserver.op", true);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        homeLoaderService = (HomeLoaderService) mainModule.getService(HomeLoaderService.class);
        warpLoaderService = (WarpLoaderService) mainModule.getService(WarpLoaderService.class);
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        secretChatProcess = mainModule.getInjector().getSingleton(SecretChatProcess.class);


    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        System.out.println(simplyCommandService.priority());
        player.sendMessage("§c§oStress Test started...");
        long mills = System.currentTimeMillis();
        for (int i = 1; i < 21; i++) {
            PaperLib.teleportAsync(player,warpLoaderService.getWarp("test"+1).getLocation());
            homeLoaderService.getHome(player.getUniqueId()).getLocations().forEach((s, location) -> {
                PaperLib.teleportAsync(player,location);
            });
        }
        for(int j = 0; j<25;j++) {

            for (int i = 1; i < 7; i++) {
                kitModule.getKit("kit test"+i).getItems().forEach(items ->{
                   player.getInventory().addItem(kitModule.getItemStackSerializer().decoder(items));
                });
            }

            for (int i = 0; i < 100; i++) {
                secretChatProcess.setSender(player);
                secretChatProcess.setReceiver(player);
                secretChatProcess.setText("§7Big Test Stress");
                secretChatProcess.send();

            }
        }
        player.sendMessage("§aText executed in §2" + (System.currentTimeMillis() - mills) + "§a ms");
    }
}
