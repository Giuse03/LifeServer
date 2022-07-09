package net.giuse.mainmodule.messages.interfaces;

import net.giuse.mainmodule.messages.MessageActionbar;
import net.giuse.mainmodule.messages.MessageChat;
import net.giuse.mainmodule.messages.MessageTitle;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface MessageLoader {
    void load();

    List<Message> getMessagesFromId(String id);

    void sendTitle(Player player, Component title,Component subTitle,int fadeIn,int stay,int fadeOut);

    void sendChat(Player player, Component sendChat);

    void sendActionBar(Player player, Component sendActionbar);
}
