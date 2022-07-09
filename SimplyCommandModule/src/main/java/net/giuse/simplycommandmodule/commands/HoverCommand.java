package net.giuse.simplycommandmodule.commands;

import com.cryptomorin.xseries.messages.Titles;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.commands.AbstractCommand;
import net.giuse.mainmodule.utils.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.time.Duration;

public class HoverCommand extends AbstractCommand {
    @Inject
    private MainModule mainModule;

    public HoverCommand() {
        super("test", "permission", false);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        player.sendMessage(MainModule.version);

        //Send Text Component
        final TextComponent textComponent = Component.text("il codice è scritto in paper", NamedTextColor.LIGHT_PURPLE).append(Component.text("  1.19!"));
        mainModule.getAdventure().player(player).sendActionBar(textComponent);

        //SEnd hover message
        mainModule.getAdventure().player(player).sendMessage(
                Component.text("Tu sei").clickEvent(ClickEvent.runCommand("/kit")).hoverEvent(HoverEvent.showText(Component.text("gay")
                        .append(Component.text(" Clickami per accettare tpa"))).asHoverEvent().value()));


        //Sent Title
        final Component mainTitle = Component.text("Il server è " , NamedTextColor.WHITE);
        final Component subtitle = Component.text("§61.8.8");
        final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3000), Duration.ofMillis(1000));
        final Title title = Title.title(mainTitle, subtitle, times);
        mainModule.getAdventure().player(player).showTitle(title);

        //Footer and HEader
        mainModule.getAdventure().players().sendPlayerListHeaderAndFooter(Component.text("gay"),Component.text("gay supremo"));

    }

}
