package net.giuse.simplycommandmodule.commands;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class RepairCommand extends AbstractCommand {
    private final SimplyCommandService simplyCommandService;

    @Inject
    public RepairCommand(MainModule mainModule) {
        super("repair", "lifeserver.repair", false);
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        setNoPerm(simplyCommandService.getMex("no-perms"));

    }


    @Override
    public void execute(CommandSender commandSender, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (args.length == 0) {
                player.sendMessage(simplyCommandService.getMex("repair-usage"));
            } else if (args[0].equalsIgnoreCase("hand")) {
                if (player.hasPermission("lifeserver.repair.hand")) {
                    if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                        player.getInventory().getItemInMainHand().setDurability((short) 0);
                        player.sendMessage(simplyCommandService.getMex("repair-hand"));
                    } else {
                        player.sendMessage(simplyCommandService.getMex("repair-nothing"));
                    }
                } else {
                    commandSender.sendMessage(simplyCommandService.getMex("no-perms"));
                }
            } else if (args[0].equalsIgnoreCase("all")) {
                if (player.hasPermission("lifeserver.repair.all")) {
                    for (int i = 0; i < player.getInventory().getContents().length; i++) {
                        if (player.getInventory().getItem(i) != null) {
                            player.getInventory().getItem(i).setDurability((short) 0);
                        }
                    }
                    player.sendMessage(simplyCommandService.getMex("repair-all"));

                } else {
                    commandSender.sendMessage(simplyCommandService.getMex("no-perms"));
                }
            }
        } else {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
        }
    }
}
