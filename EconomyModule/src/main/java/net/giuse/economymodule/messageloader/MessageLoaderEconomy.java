package net.giuse.economymodule.messageloader;

import net.giuse.economymodule.EconomyService;
import net.giuse.ezmessage.MessageLoader;
import net.giuse.ezmessage.messages.MessageActionbar;
import net.giuse.ezmessage.messages.MessageChat;
import net.giuse.ezmessage.messages.MessageTitle;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.message.Loadable;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class MessageLoaderEconomy implements Loadable {
    private final EconomyService economyService;
    private final MainModule mainModule;
    private final MessageLoader messageLoader;

    @Inject
    public MessageLoaderEconomy(MainModule mainModule) {
        this.mainModule = mainModule;
        economyService = (EconomyService) mainModule.getService(EconomyService.class);
        messageLoader = mainModule.getMessageLoader();
    }


    @Override
    public void load() {

        ConfigurationSection generalMessageSection = economyService.getConfigManager().getMessagesYaml().getConfigurationSection("messages");
        for (String string : generalMessageSection.getKeys(false)) {
            ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(string);
            //Insert Message Chat
            if (messageSection.getBoolean("message-chat.send-chat")) {
                String messageComponent = ChatColor.translateAlternateColorCodes('&', messageSection.getString("message-chat.string-message"));
                MessageChat message = new MessageChat(messageComponent);
                messageLoader.addMessageCache(string + "_chat", CompletableFuture.supplyAsync(() -> message));
            }

            //Insert Title Message
            if (messageSection.getBoolean("title.send-title")) {
                String subTitle = ChatColor.translateAlternateColorCodes('&', messageSection.getString("title.subtitle"));
                String title = ChatColor.translateAlternateColorCodes('&', messageSection.getString("title.main-title"));
                MessageTitle message = new MessageTitle(
                        title, subTitle,
                        messageSection.getInt("title.fade-in"),
                        messageSection.getInt("title.fade-stay"),
                        messageSection.getInt("title.fade-out"));
                messageLoader.addMessageCache(string + "_title", CompletableFuture.supplyAsync(() -> message));
            }

            //Insert ActionBar
            if (messageSection.getBoolean("actionbar.send-actionbar")) {
                String messageComponent = ChatColor.translateAlternateColorCodes('&', messageSection.getString("actionbar.actionbar-message"));
                MessageActionbar message = new MessageActionbar(messageComponent);
                messageLoader.addMessageCache(string + "_bossbar", CompletableFuture.supplyAsync(() -> message));
            }
        }
    }


}
