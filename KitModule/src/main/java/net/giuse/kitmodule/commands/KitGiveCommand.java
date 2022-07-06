package net.giuse.kitmodule.commands;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.builder.KitBuilder;
import net.giuse.kitmodule.cooldownsystem.KitCooldown;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.kitmodule.gui.KitGui;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.Utils;
import net.giuse.mainmodule.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

/**
 * Command /kit for view a list of kit and give a kit
 */
public class KitGiveCommand extends AbstractCommand {
    private final MainModule mainModule;
    private final KitModule kitModule;

    @Inject
    public KitGiveCommand(MainModule mainModule) {
        super("kit", "lifeserver.kit", false);
        this.mainModule = mainModule;
        kitModule = (KitModule) mainModule.getService(KitModule.class);
        setNoPerm(kitModule.getMessage("no-perms"));

    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage("Not Supported From Console");
            return;
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            if (p.hasPermission("lifeserver.kit.list")) {
                if (mainModule.getConfig().getBoolean("use-kit-gui")) {
                    KitGui guiManager = mainModule.getInjector().getSingleton(KitGui.class);
                    guiManager.openInv(p);
                    return;
                }
                if (kitModule.getKitElements().isEmpty()) {
                    p.sendMessage(kitModule.getMessage("kit-list-empty"));
                } else {
                    StringBuilder sb = new StringBuilder();
                    kitModule.getKitElements().forEach(kitBuilder -> sb.append(kitBuilder.getName()).append(","));
                    p.sendMessage(kitModule.getMessage("kit-list").replace("%listkit%", sb.deleteCharAt(sb.length() - 1).toString()));
                }
            } else {
                p.sendMessage(kitModule.getMessage("no-perms"));

            }
        } else {
            if (p.hasPermission("lifeserver.kit." + args[0])) {
                if (kitModule.getKit(args[0]) != null) {
                    KitBuilder kitBuilder = kitModule.getKit(args[0]);
                    if(kitModule.getPlayerTime(p.getUniqueId()).getKitsCooldown().stream().anyMatch(kitCooldown -> kitCooldown.getKitBuilder().equals(kitBuilder))){
                        KitCooldown kitCooldown = kitModule.getPlayerTime(p.getUniqueId()).getKitsCooldown().stream().filter(kitCooldowns -> kitCooldowns.getKitBuilder().equals(kitBuilder)).findFirst().get();
                        if(kitCooldown.getVariableCoolDown() == 0){
                            kitModule.getPlayerTime(p.getUniqueId()).start(kitCooldown);
                            kitBuilder.getItems().forEach(itemStack -> p.getInventory().addItem(kitModule.getItemStackSerializer().decoder(itemStack)));
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', kitModule.getMessage("kit-receive").replace("%kit%", kitBuilder.getName())));
                        }else{
                            p.sendMessage(kitModule.getMessage("kit-wait").replace("%time%", Utils.formatTime(kitCooldown.getVariableCoolDown())));
                        }
                    }
                } else {
                    p.sendMessage(kitModule.getMessage("kit-doesnt-exists"));
                }
            } else {
                p.sendMessage(kitModule.getMessage("no-perms"));

            }
        }
    }

}
