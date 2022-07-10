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

public class PayCommand extends AbstractCommand {
    private final EconomyService economyService;

    private final MessageBuilder messageBuilder;
    @Inject
    public PayCommand(final MainModule mainModule) {
        super("pay", "lifeserver.pay", true);
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
        if (args.length <= 1) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-pay-args").sendMessage();
            return;
        }
        if (!NumberUtils.isNumber(args[1])) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-number").sendMessage();
            return;
        }

        if (Double.parseDouble(args[1]) < 0.0) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-number").sendMessage();
            return;
        }

        if (!this.economyService.getEconPlayerIsPresent(Bukkit.getPlayer(args[0]).getUniqueId())) {
            messageBuilder.setCommandSender(p).setIDMessage("economy-neverJoin").sendMessage();
            return;
        }

        if (this.economyService.getCustomEcoManager().getBalance(p) >= Double.parseDouble(args[1])) {
            this.economyService.getCustomEcoManager().depositPlayer(Bukkit.getOfflinePlayer(args[0]), Double.parseDouble(args[1]));
            this.economyService.getCustomEcoManager().withdrawPlayer(p, Double.parseDouble(args[1]));

            messageBuilder.setCommandSender(p).setIDMessage("economy-pay-send").sendMessage(
                    new TextReplacer().match("%player%").replaceWith(args[0]),
                    new TextReplacer().match("%amount%").replaceWith(args[1]));

            messageBuilder.setCommandSender(Bukkit.getOfflinePlayer(args[0]).getPlayer()).setIDMessage("economy-pay-receive").sendMessage(
                    new TextReplacer().match("%player%").replaceWith(p.getName()),
                    new TextReplacer().match("%amount%").replaceWith(args[1]));

            return;
        }
        messageBuilder.setCommandSender(p).setIDMessage("economy-no-money").sendMessage();

    }
}
