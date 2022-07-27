package net.giuse.mainmodule.message;

import net.giuse.mainmodule.MainModule;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;

public class MessageLoaderMain extends SetupMessageLoader {
    @Inject
    private MainModule mainModule;


    @Override
    public void load() {
        ConfigurationSection generalMessageSection = mainModule.getConfig().getConfigurationSection("messages");
        for (String idMessage : generalMessageSection.getKeys(false)) {
            ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(idMessage);

            //setup
            setupMessageChat(idMessage, messageSection);
            setupTitle(idMessage, messageSection);
            setupActionBar(idMessage, messageSection);

        }
    }


}
