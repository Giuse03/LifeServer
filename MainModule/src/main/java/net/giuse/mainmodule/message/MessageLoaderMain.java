package net.giuse.mainmodule.message;

import net.giuse.ezmessage.MessageLoader;
import net.giuse.ezmessage.messages.MessageActionbar;
import net.giuse.ezmessage.messages.MessageChat;
import net.giuse.ezmessage.messages.MessageTitle;
import net.giuse.mainmodule.MainModule;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;

public class MessageLoaderMain implements Loadable {
    private final MainModule mainModule;
    private final MessageLoader messageLoader;

    @Inject
    public MessageLoaderMain(MainModule mainModule) {
        this.mainModule = mainModule;
        messageLoader = mainModule.getMessageLoader();
    }


    @Override
    public void load() {
        ConfigurationSection generalMessageSection = mainModule.getConfig().getConfigurationSection("messages");
        for (String idMessage : generalMessageSection.getKeys(false)) {
            ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(idMessage);

            //setup
            setupMessageChat(idMessage,messageSection);
            setupTitle(idMessage,messageSection);
            setupActionBar(idMessage, messageSection);

        }
    }


    private void setupActionBar(String idMessage, ConfigurationSection section) {
        if (checkIfOptionIsActive("actionbar.send-actionbar", section)) {
            String messageComponent = ChatColor.translateAlternateColorCodes('&', section.getString("actionbar.actionbar-message"));
            MessageActionbar message = new MessageActionbar(messageComponent);
            messageLoader.addMessageCache(idMessage + "_bossbar", message);
        }
    }

    private void setupMessageChat(String idMessage, ConfigurationSection section) {
        if (checkIfOptionIsActive("message-chat.send-chat", section)) {
            String messageComponent = ChatColor.translateAlternateColorCodes('&', section.getString("message-chat.string-message"));
            MessageChat message = new MessageChat(messageComponent);
            messageLoader.addMessageCache(idMessage + "_chat", message);
        }
    }

    private void setupTitle(String idMessage, ConfigurationSection section) {
        if (checkIfOptionIsActive("title.send-title", section)) {
            String subTitle = ChatColor.translateAlternateColorCodes('&', section.getString("title.subtitle"));
            String title = ChatColor.translateAlternateColorCodes('&', section.getString("title.main-title"));
            MessageTitle message = new MessageTitle(
                    title, subTitle,
                    section.getInt("title.fade-in"),
                    section.getInt("title.fade-stay"),
                    section.getInt("title.fade-out"));
            messageLoader.addMessageCache(idMessage + "_title", message);
        }
    }


    private boolean checkIfOptionIsActive(String typeMessage,ConfigurationSection section){
        return  section.getBoolean(typeMessage);
    }



}
