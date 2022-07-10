package net.giuse.economymodule.commands;

import net.giuse.economymodule.EconomyService;
import net.giuse.ezmessage.MessageBuilder;
import net.giuse.ezmessage.TextReplacer;
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
    private final MessageBuilder messageBuilder;

    @Inject
    public EconCommand(final MainModule mainModule) {
        super("eco", "lifeserver.eco", true);
        this.economyService = (EconomyService) mainModule.getService(EconomyService.class);
        messageBuilder = mainModule.getMessageBuilder();
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {

        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }
        Player p = (Player) commandSender;

        if (!p.hasPermission("lifeserver.eco")) {
            messageBuilder.setCommandSender(p).setIDMessage("no-perms").sendMessage();
            return;
        }

        if (args.length <= 2) {
            p.sendMessage("§a/eco give <player> <money>§7 - Give money to a Player");
            p.sendMessage("§a/eco remove <player> <money>§7 - Remove money to a Player");
            p.sendMessage("§a/eco set <player> <money>§7 - Set balance to a Player");
            return;
        }

        if (!this.economyService.getEconPlayerIsPresent(Bukkit.getOfflinePlayer(args[1]).getUniqueId())) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-neverJoin").sendMessage();
            return;
        }

        if (!NumberUtils.isNumber(args[2])) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-number").sendMessage();
            return;
        }


        if (args[0].equalsIgnoreCase("give")) {
            if (!p.hasPermission("lifeserver.eco.give")) {
                messageBuilder.setCommandSender(p).setIDMessage("no-perms").sendMessage();
                return;
            }
            this.economyService.getCustomEcoManager().depositPlayer(Bukkit.getOfflinePlayer(args[1]), Double.parseDouble(args[2]));
            messageBuilder.setCommandSender(Bukkit.getPlayer(args[1])).setIDMessage("economy-addMoney").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(this.economyService.getBalancePlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId()))),
                    new TextReplacer().match("%moneyadd%").replaceWith(args[2]));

            messageBuilder.setCommandSender(p).setIDMessage("economy-addMoney-other").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(this.economyService.getBalancePlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId()))),
                    new TextReplacer().match("%moneyadd%").replaceWith(args[2]),
                    new TextReplacer().match("%player%").replaceWith(Bukkit.getPlayer(args[1]).getName()));
            return;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (!p.hasPermission("lifeserver.eco.remove")) {
                messageBuilder.setCommandSender(p).setIDMessage("no-perms").sendMessage();
                return;
            }
            this.economyService.getCustomEcoManager().withdrawPlayer(Bukkit.getOfflinePlayer(args[1]), Double.parseDouble(args[2]));
            messageBuilder.setCommandSender(Bukkit.getPlayer(args[1])).setIDMessage("economy-removeMoney").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(this.economyService.getBalancePlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId()))),
                    new TextReplacer().match("%moneyadd%").replaceWith(args[2]));
            messageBuilder.setCommandSender(p).setIDMessage("economy-removeMoney-other").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(this.economyService.getBalancePlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId()))),
                    new TextReplacer().match("%moneyadd%").replaceWith(args[2]),
                    new TextReplacer().match("%player%").replaceWith(Bukkit.getPlayer(args[1]).getName()));
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (!p.hasPermission("lifeserver.eco.set")) {
                messageBuilder.setCommandSender(p).setIDMessage("no-perms").sendMessage();
                return;
            }
            this.economyService.getCustomEcoManager().withdrawPlayer(Bukkit.getOfflinePlayer(args[1]), this.economyService.getBalancePlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId()));
            this.economyService.getCustomEcoManager().depositPlayer(Bukkit.getOfflinePlayer(args[1]), Double.parseDouble(args[2]));
            messageBuilder.setCommandSender(Bukkit.getPlayer(args[1])).setIDMessage("economy-setMoney").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(this.economyService.getBalancePlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId()))));

            messageBuilder.setCommandSender(p).setIDMessage("economy-setMoney-other").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(this.economyService.getBalancePlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId()))),
                    new TextReplacer().match("%player%").replaceWith(Bukkit.getPlayer(args[1]).getName()));
        }
    }
}
