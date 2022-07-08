package net.giuse.simplycommandmodule.commands;


import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Objects;

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
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage(simplyCommandService.getMex("not-player"));
            return;
        }
        Player player = (Player) commandSender;

        if (args.length == 0) {
            player.sendMessage(simplyCommandService.getMex("repair-usage"));
            return;
        }

        if (args[0].equalsIgnoreCase("hand")) {
            if (!player.hasPermission("lifeserver.repair.hand")) {
                commandSender.sendMessage(simplyCommandService.getMex("no-perms"));
                return;
            }
            if (player.getInventory().getItemInMainHand().getType() == Material.AIR) {
                player.sendMessage(simplyCommandService.getMex("repair-nothing"));
                return;
            }
            player.getInventory().getItemInMainHand().setDurability((short) 0);
            player.sendMessage(simplyCommandService.getMex("repair-hand"));
        }

        if (args[0].equalsIgnoreCase("all")) {
            if (!player.hasPermission("lifeserver.repair.all")) {
                commandSender.sendMessage(simplyCommandService.getMex("no-perms"));
                return;
            }

            Arrays.stream(player.getInventory().getContents()).filter(Objects::nonNull).forEach(itemStack -> itemStack.setDurability((short) 0));
            player.sendMessage(simplyCommandService.getMex("repair-all"));
        }
    }
}
