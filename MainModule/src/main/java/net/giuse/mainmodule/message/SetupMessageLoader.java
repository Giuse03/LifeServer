package net.giuse.mainmodule.message;

import ezmessage.messages.MessageActionbar;
import ezmessage.messages.MessageChat;
import ezmessage.messages.MessageTitle;
import net.giuse.mainmodule.MainModule;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;

public abstract class SetupMessageLoader {

    @Inject
    private MainModule mainModule;


    public abstract void load();

    protected void setupActionBar(String idMessage, ConfigurationSection section) {
        if (checkIfOptionIsActive("actionbar.send-actionbar", section)) {
            String messageComponent = ChatColor.translateAlternateColorCodes('&', section.getString("actionbar.actionbar-message"));
            MessageActionbar message = new MessageActionbar(messageComponent);
            mainModule.getMessageLoader().addMessageCache(idMessage + "_bossbar", message);
        }
    }

    protected void setupMessageChat(String idMessage, ConfigurationSection section) {
        if (checkIfOptionIsActive("message-chat.send-chat", section)) {
            String messageComponent = ChatColor.translateAlternateColorCodes('&', section.getString("message-chat.string-message"));
            MessageChat message = new MessageChat(messageComponent);
            mainModule.getMessageLoader().addMessageCache(idMessage + "_chat", message);
        }
    }

    protected void setupTitle(String idMessage, ConfigurationSection section) {
        if (checkIfOptionIsActive("title.send-title", section)) {
            String subTitle = ChatColor.translateAlternateColorCodes('&', section.getString("title.subtitle"));
            String title = ChatColor.translateAlternateColorCodes('&', section.getString("title.main-title"));
            MessageTitle message = new MessageTitle(
                    title, subTitle,
                    section.getInt("title.fade-in"),
                    section.getInt("title.fade-stay"),
                    section.getInt("title.fade-out"));
            mainModule.getMessageLoader().addMessageCache(idMessage + "_title", message);
        }
    }


    private boolean checkIfOptionIsActive(String typeMessage, ConfigurationSection section) {
        return section.getBoolean(typeMessage);
    }
}
