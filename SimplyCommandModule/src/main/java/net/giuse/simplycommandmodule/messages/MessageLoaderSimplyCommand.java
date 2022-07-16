package net.giuse.simplycommandmodule.messages;

import net.giuse.ezmessage.MessageLoader;
import net.giuse.ezmessage.messages.MessageActionbar;
import net.giuse.ezmessage.messages.MessageChat;
import net.giuse.ezmessage.messages.MessageTitle;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.message.Loadable;
import net.giuse.simplycommandmodule.SimplyCommandService;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;

public class MessageLoaderSimplyCommand implements Loadable {
    private final SimplyCommandService simplyCommandService;
    private final MainModule mainModule;
    private final MessageLoader messageLoader;

    @Inject
    public MessageLoaderSimplyCommand(MainModule mainModule) {
        this.mainModule = mainModule;
        simplyCommandService = (SimplyCommandService) mainModule.getService(SimplyCommandService.class);
        messageLoader = mainModule.getMessageLoader();
    }


    @Override
    public void load() {

        ConfigurationSection generalMessageSection = simplyCommandService.getFileManager().getMessageSimpleFileYml().getConfigurationSection("messages");
        for (String string : generalMessageSection.getKeys(false)) {
            ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(string);
            //Insert Message Chat
            if (messageSection.getBoolean("message-chat.send-chat")) {
                String messageComponent = ChatColor.translateAlternateColorCodes('&', messageSection.getString("message-chat.string-message"));
                MessageChat message = new MessageChat(messageComponent);
                messageLoader.addMessageCache(string + "_chat", message);
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
                messageLoader.addMessageCache(string + "_title", message);
            }

            //Insert ActionBar
            if (messageSection.getBoolean("actionbar.send-actionbar")) {
                String messageComponent = ChatColor.translateAlternateColorCodes('&', messageSection.getString("actionbar.actionbar-message"));
                MessageActionbar message = new MessageActionbar(messageComponent);
                messageLoader.addMessageCache(string + "_bossbar", message);
            }
        }
    }


}
