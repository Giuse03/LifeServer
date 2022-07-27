package net.giuse.kitmodule.messages;

import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.message.SetupMessageLoader;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;


public class MessageLoaderKit extends SetupMessageLoader {
    @Inject
    private MainModule mainModule;

    @Override
    public void load() {


        KitModule kitModule = (KitModule) mainModule.getService(KitModule.class);
        ConfigurationSection generalMessageSection = kitModule.getFileKits().getMessagesYaml().getConfigurationSection("messages");
        for (String idMessage : generalMessageSection.getKeys(false)) {
            ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(idMessage);

            //setup
            setupMessageChat(idMessage, messageSection);
            setupTitle(idMessage, messageSection);
            setupActionBar(idMessage, messageSection);

        }
    }
}
