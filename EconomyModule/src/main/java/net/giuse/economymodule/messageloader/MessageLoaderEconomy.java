package net.giuse.economymodule.messageloader;

import net.giuse.economymodule.EconomyService;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.message.SetupMessageLoader;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;


public class MessageLoaderEconomy extends SetupMessageLoader {
    @Inject
    private MainModule mainModule;

    @Override
    public void load() {
        EconomyService economyService = (EconomyService) mainModule.getService(EconomyService.class);
        ConfigurationSection generalMessageSection = economyService.getConfigManager().getMessagesYaml().getConfigurationSection("messages");
        for (String idMessage : generalMessageSection.getKeys(false)) {
            ConfigurationSection messageSection = generalMessageSection.getConfigurationSection(idMessage);

            //setup
            setupMessageChat(idMessage, messageSection);
            setupTitle(idMessage, messageSection);
            setupActionBar(idMessage, messageSection);

        }
    }
}
