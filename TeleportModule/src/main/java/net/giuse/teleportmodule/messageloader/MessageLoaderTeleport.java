package net.giuse.teleportmodule.messageloader;

import net.giuse.ezmessage.MessageLoader;
import net.giuse.ezmessage.messages.MessageActionbar;
import net.giuse.ezmessage.messages.MessageChat;
import net.giuse.ezmessage.messages.MessageTitle;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.message.Loadable;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import sun.security.krb5.Config;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class MessageLoaderTeleport implements Loadable {
    private final TeleportModule kitModule;
    private final MainModule mainModule;
    private final MessageLoader messageLoader;

    @Inject
    public MessageLoaderTeleport(MainModule mainModule) {
        this.mainModule = mainModule;
        kitModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        messageLoader = mainModule.getMessageLoader();
    }


    @Override
    public void load() {
        ConfigurationSection[] generalMessageSectionArray = new ConfigurationSection[]{
                kitModule.getFileManager().getMessagesTeleportYaml().getConfigurationSection("messages"),
                kitModule.getFileManager().getMessagesWarpYaml().getConfigurationSection("messages"),
                kitModule.getFileManager().getMessagesHomeYaml().getConfigurationSection("messages"),
                kitModule.getFileManager().getMessagesSpawnYaml().getConfigurationSection("messages")};

        for(ConfigurationSection generalMessageSection : generalMessageSectionArray) {
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


}
