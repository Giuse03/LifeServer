package net.giuse.economymodule.commands;

import net.giuse.economymodule.EconomyService;
import net.giuse.ezmessage.MessageBuilder;
import net.giuse.ezmessage.TextReplacer;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class MoneyCommand extends AbstractCommand {
    private final EconomyService economyService;
    private final MessageBuilder messageBuilder;

    @Inject
    public MoneyCommand(final MainModule mainModule) {
        super("money", "lifeserver.money", true);
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
        final Player p = (Player) commandSender;
        if (args.length == 0) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-balance").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(economyService.getBalancePlayer(p.getUniqueId()))));
            return;
        }

        if (!p.hasPermission("lifeserver.balance.other")) {
            p.sendMessage("No Perms");
        }

        if (economyService.getEconPlayerIsPresent(Bukkit.getOfflinePlayer(args[0]).getUniqueId())) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-balance-other").sendMessage(
                    new TextReplacer().match("%money%").replaceWith(String.valueOf(economyService.getBalancePlayer(p.getUniqueId()))),
                    new TextReplacer().match("%player%").replaceWith(args[0]));
            return;
        }
        messageBuilder.setCommandSender(p).setIDMessage("economy-neverJoin").sendMessage();
    }
}
