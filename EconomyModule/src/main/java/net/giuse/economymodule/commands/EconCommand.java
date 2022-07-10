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

        this.setNoPerm("No Perms");
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] args) {

        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage("Not supported from console");
            return;
        }
        Player p = (Player) commandSender;

        if (!p.hasPermission("lifeserver.eco")) {
            p.sendMessage("No Perms");
            return;
        }

        if (args.length <= 2) {
            p.sendMessage("§a/eco give <player> <money>§7 - Give money to a Player");
            p.sendMessage("§a/eco remove <player> <money>§7 - Remove money to a Player");
            p.sendMessage("§a/eco set <player> <money>§7 - Set balance to a Player");
            return;
        }

        if (this.economyService.getEconPlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId()) == null) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-neverJoin").sendMessage();
            return;
        }

        if (!NumberUtils.isNumber(args[2])) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-number").sendMessage();
            return;
        }

        final EconPlayer econPlayer = this.economyService.getEconPlayer(Bukkit.getOfflinePlayer(args[1]).getUniqueId());

        if (args[0].equalsIgnoreCase("give")) {
            if (!p.hasPermission("lifeserver.eco.give")) {
                p.sendMessage("No Perms");
                return;
            }
            this.economyService.getCustomEcoManager().depositPlayer(Bukkit.getOfflinePlayer(args[1]), Double.parseDouble(args[2]));
            messageBuilder.setCommandSender(Bukkit.getPlayer(econPlayer.getPlayer())).setIDMessage("economy-addMoney").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(econPlayer.getBalance())),
                    new TextReplacer().match("%moneyadd%").replaceWith(args[2]));

            messageBuilder.setCommandSender(p).setIDMessage("economy-addMoney-other").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(econPlayer.getBalance())),
                    new TextReplacer().match("%moneyadd%").replaceWith(args[2]),
                    new TextReplacer().match("%player%").replaceWith(Bukkit.getPlayer(econPlayer.getPlayer()).getName()));
            return;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            if (!p.hasPermission("lifeserver.eco.remove")) {
                p.sendMessage("No Perms");
                return;
            }
            this.economyService.getCustomEcoManager().withdrawPlayer(Bukkit.getOfflinePlayer(args[1]), Double.parseDouble(args[2]));
            messageBuilder.setCommandSender(Bukkit.getPlayer(econPlayer.getPlayer())).setIDMessage("economy-removeMoney").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(econPlayer.getBalance())),
                    new TextReplacer().match("%moneyadd%").replaceWith(args[2]));
            messageBuilder.setCommandSender(p).setIDMessage("economy-removeMoney-other").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(econPlayer.getBalance())),
                    new TextReplacer().match("%moneyadd%").replaceWith(args[2]),
                    new TextReplacer().match("%player%").replaceWith(Bukkit.getPlayer(econPlayer.getPlayer()).getName()));
        }

        if (args[0].equalsIgnoreCase("set")) {
            if (!p.hasPermission("lifeserver.eco.set")) {
                p.sendMessage("No Perms");
                return;
            }
            this.economyService.getCustomEcoManager().withdrawPlayer(Bukkit.getOfflinePlayer(args[1]), econPlayer.getBalance());
            this.economyService.getCustomEcoManager().depositPlayer(Bukkit.getOfflinePlayer(args[1]), Double.parseDouble(args[2]));
            messageBuilder.setCommandSender(Bukkit.getPlayer(econPlayer.getPlayer())).setIDMessage("economy-setMoney").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(econPlayer.getBalance())));

            messageBuilder.setCommandSender(p).setIDMessage("economy-setMoney-other").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(econPlayer.getBalance())),
                    new TextReplacer().match("%player%").replaceWith(Bukkit.getPlayer(econPlayer.getPlayer()).getName()));
        }
    }
}
