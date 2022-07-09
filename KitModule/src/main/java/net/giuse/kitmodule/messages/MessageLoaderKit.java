package net.giuse.kitmodule.messages;

import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.messages.MessageActionbar;
import net.giuse.mainmodule.messages.MessageChat;
import net.giuse.mainmodule.messages.MessageTitle;
import net.giuse.mainmodule.messages.interfaces.Message;
import net.giuse.mainmodule.messages.interfaces.MessageLoader;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class MessageLoaderKit implements MessageLoader {
    private final ArrayList<Message> arrayListMessage = new ArrayList<>();
    private final MainModule mainModule;
    private final KitModule kitModule;

    @Inject
    public MessageLoaderKit(MainModule mainModule) {
        this.mainModule = mainModule;
        kitModule = (KitModule) mainModule.getService(KitModule.class);
    }

    @Override
    public void load() {
        MessageChat messageChat = new MessageChat(mainModule.getConfig().getString("no-perms"), "no-perms");
        arrayListMessage.add(messageChat);
        ConfigurationSection generalMessageSection = kitModule.getConfigManager().getMessagesYaml().getConfigurationSection("messages");
        for (String string : generalMessageSection.getKeys(false)) {
            ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(string);
            //Insert Message Chat
            if (messageSection.getBoolean("message-chat.send-chat")) {
                String messageComponent = ChatColor.translateAlternateColorCodes('&', messageSection.getString("message-chat.string-message"));
                MessageChat message = new MessageChat(messageComponent, string);
                arrayListMessage.add(message);
            }

            //Insert Title Message
            if (messageSection.getBoolean("title.send-title")) {
                String subTitle = ChatColor.translateAlternateColorCodes('&', messageSection.getString("title.subtitle"));
                String title = ChatColor.translateAlternateColorCodes('&', messageSection.getString("title.main-title"));
                MessageTitle message = new MessageTitle(
                        title, subTitle,
                        messageSection.getInt("title.fade-in"),
                        messageSection.getInt("title.fade-stay"),
                        messageSection.getInt("title.fade-out"),
                        string);
                arrayListMessage.add(message);
            }

            //Insert ActionBar
            if (messageSection.getBoolean("actionbar.send-actionbar")) {
                String messageComponent = ChatColor.translateAlternateColorCodes('&', messageSection.getString("actionbar.actionbar-message"));
                MessageActionbar message = new MessageActionbar(messageComponent, string);
                arrayListMessage.add(message);
            }
        }
    }

    /*
     * Get Message From Set of Message
     */
    @Override
    public List<Message> getMessagesFromId(String id) {
        List<Message> messagesGet = new ArrayList<>();
        arrayListMessage.stream().filter(message -> message.getID().equalsIgnoreCase(id)).forEach(messagesGet::add);
        return messagesGet;
    }

    /*
     * Send title to Player
     */
    @Override
    public void sendTitle(Player player, Component title,Component subTitle,int fadeIn,int stay,int fadeOut) {
        Title.Times times = Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut));
        Title titleToSend = Title.title(title, subTitle, times);
        mainModule.getAdventure().player(player).showTitle(titleToSend);
    }


    /*
     * Send message to Player
     */
    @Override
    public void sendChat(Player player, Component messageChat) {
        mainModule.getAdventure().player(player).sendMessage(messageChat);
    }

    /*
     * Send message to Player
     */
    @Override
    public void sendActionBar(Player player, Component send) {
         mainModule.getAdventure().player(player).sendActionBar(send);
    }


}
