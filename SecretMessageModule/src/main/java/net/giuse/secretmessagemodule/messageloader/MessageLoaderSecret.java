package net.giuse.secretmessagemodule.messageloader;

import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.message.SetupMessageLoader;
import net.giuse.secretmessagemodule.SecretMessageModule;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;


public class MessageLoaderSecret extends SetupMessageLoader {
    @Inject
    private MainModule mainModule;

    @Override
    public void load() {
        SecretMessageModule secretMessageModule = (SecretMessageModule) mainModule.getService(SecretMessageModule.class);
        ConfigurationSection generalMessageSection = secretMessageModule.getFileManager().getMessagesSecretChatYaml().getConfigurationSection("messages");
        for (String idMessage : generalMessageSection.getKeys(false)) {
            ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(idMessage);

            //setup
            setupMessageChat(idMessage, messageSection);
            setupTitle(idMessage, messageSection);
            setupActionBar(idMessage, messageSection);

        }
    }
}
