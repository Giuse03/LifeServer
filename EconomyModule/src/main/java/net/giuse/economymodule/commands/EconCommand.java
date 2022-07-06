package net.giuse.economymodule.commands;

import net.giuse.economymodule.EconPlayer;
import net.giuse.economymodule.EconomyService;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class EconCommand extends AbstractCommand {
    private final EconomyService economyService;

    @Inject
    public EconCommand(final MainModule mainModule) {
        super("eco", "lifeserver.eco", true);
        this.economyService = (EconomyService) mainModule.getService(EconomyService.class);
        this.setNoPerm(this.economyService.getMessage("no-perms"));
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {
        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }
        final Player p = (Player) commandSender;
        if (p.hasPermission("lifeserver.eco")) {
            if (args.length <= 2) {
                p.sendMessage("§a/eco give <player> <money>§7 - Give money to a Player");
                p.sendMessage("§a/eco remove <player> <money>§7 - Remove money to a Player");
                p.sendMessage("§a/eco set <player> <money>§7 - Set balance to a Player");
                return;
            }
        } else {
            p.sendMessage(this.economyService.getMessage("no-perms"));
        }
        if (this.economyService.getEconPlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId()) == null) {
            p.sendMessage(this.economyService.getMessage("economy-neverJoin"));
            return;
        }
        if (!NumberUtils.isNumber(args[2])) {
            p.sendMessage(this.economyService.getMessage("economy-number"));
            return;
        }
        final EconPlayer econPlayer = this.economyService.getEconPlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId());
        if (args[0].equalsIgnoreCase("give")) {
            if (p.hasPermission("lifeserver.eco.give")) {
                this.economyService.getCustomEcoManager().depositPlayer(Bukkit.getOfflinePlayer(args[1]), Double.parseDouble(args[2]));
                Bukkit.getPlayer(econPlayer.getPlayer()).sendMessage(this.economyService.getMessage("economy-addMoney").replace("%money%", String.valueOf(econPlayer.getBalance())).replace("%moneyadd% ", args[2]));
                p.sendMessage(this.economyService.getMessage("economy-addMoney-other").replace("%money%", String.valueOf(econPlayer.getBalance())).replace("%moneyadd% ", args[2]).replace("%player%", Bukkit.getPlayer(econPlayer.getPlayer()).getName()));
            } else {
                p.sendMessage(this.economyService.getMessage("no-perms"));
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            if (p.hasPermission("lifeserver.eco.remove")) {
                this.economyService.getCustomEcoManager().withdrawPlayer(Bukkit.getOfflinePlayer(args[1]), Double.parseDouble(args[2]));
                Bukkit.getPlayer(econPlayer.getPlayer()).sendMessage(this.economyService.getMessage("economy-removeMoney").replace("%money%", String.valueOf(econPlayer.getBalance())).replace("%moneyadd% ", args[2]));
                p.sendMessage(this.economyService.getMessage("economy-removeMoney-other").replace("%money%", String.valueOf(econPlayer.getBalance())).replace("%moneyadd% ", args[2]).replace("%player%", Bukkit.getPlayer(econPlayer.getPlayer()).getName()));
            } else {
                p.sendMessage(this.economyService.getMessage("no-perms"));
            }
        } else if (args[0].equalsIgnoreCase("set")) {
            if (p.hasPermission("lifeserver.eco.set")) {
                this.economyService.getCustomEcoManager().withdrawPlayer(Bukkit.getOfflinePlayer(args[1]), econPlayer.getBalance());
                this.economyService.getCustomEcoManager().depositPlayer(Bukkit.getOfflinePlayer(args[1]), Double.parseDouble(args[2]));
                Bukkit.getPlayer(econPlayer.getPlayer()).sendMessage(this.economyService.getMessage("economy-setMoney").replace("%money%", String.valueOf(econPlayer.getBalance())));
                p.sendMessage(this.economyService.getMessage("economy-setMoney-other").replace("%money%", String.valueOf(econPlayer.getBalance())).replace("%player%", Bukkit.getPlayer(econPlayer.getPlayer()).getName()));
            } else {
                p.sendMessage(this.economyService.getMessage("no-perms"));
            }
        }
    }
}
