package net.giuse.mainmodule.messages.interfaces;

import net.giuse.mainmodule.MainModule;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.time.Duration;
import java.util.List;

public abstract class MessageLoader {

    protected final MainModule mainModule;

    @Inject
    public MessageLoader(MainModule mainModule) {
        this.mainModule = mainModule;
    }

    public abstract void load();

    public abstract List<Message> getMessagesFromId(String id);


    /*
     * Send title to Player
     */
    public void sendTitle(Player player, Component title,Component subTitle,int fadeIn,int stay,int fadeOut) {
        Title.Times times = Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));
        Title titleToSend = Title.title(title, subTitle, times);
        mainModule.getAdventure().player(player).showTitle(titleToSend);
    }


    /*
     * Send message to Player
     */
    public void sendChat(Player player, Component messageChat) {
        mainModule.getAdventure().player(player).sendMessage(messageChat);
    }

    /*
     * Send message to Player
     */
    public void sendActionBar(Player player, Component send) {
        mainModule.getAdventure().player(player).sendActionBar(send);
    }
}
