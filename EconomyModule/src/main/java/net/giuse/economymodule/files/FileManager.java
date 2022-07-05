package net.giuse.economymodule.files;

import net.giuse.mainmodule.files.abstractfiles.AbstractConfig;
import net.giuse.mainmodule.files.annotations.FileAnnotation;
import net.giuse.mainmodule.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager extends AbstractConfig {
    @FileAnnotation(name = "messages_economy.yml", path = "plugins/LifeServer/messages/messages_economy.yml")
    private File messagesFile;
    @YamlAnnotation(nameFile = "messages_economy.yml")
    private YamlConfiguration messagesYaml;

    public void messagesLoader() {
        this.setFile(this.messagesFile);
        this.setYamlConfiguration(this.messagesYaml);
        this.messagesYaml.set("messages.economy-balance", "&2Economy &a» &7Your balance is %money%");
        this.messagesYaml.set("messages.economy-addMoney", "&2Economy &a» &7%moneyadd%  have been added in your account New balance> %money%");
        this.messagesYaml.set("messages.economy-setMoney", "&2Economy &a» &7 Your budget has been set New balance> %money%");
        this.messagesYaml.set("messages.economy-removeMoney", "&2Economy &a» &7%moneyadd%  have been removed in your account. New balance> %money%");
        this.messagesYaml.set("messages.economy-cantAfford", "&2Economy &a» &7You can't afford this purchase");
        this.messagesYaml.set("messages.economy-neverJoin", "&2Economy &a» &7 This player has never joined the server");
        this.messagesYaml.set("messages.economy-balance-other", "&2Economy &a» &7%player% balance> %money%");
        this.messagesYaml.set("messages.economy-addMoney-other", "&2Economy &a» &7%moneyadd%  have been added in %player% account balance> %money%");
        this.messagesYaml.set("messages.economy-setMoney-other", "&2Economy &a» &7 Your budget has been set in %player% account balance> %money%");
        this.messagesYaml.set("messages.economy-removeMoney-other", "&2Economy &a» &7%moneyadd% have been removed in %player% account balance> %money%");
        this.messagesYaml.set("messages.economy-number", "&2Economy &a» &7Invalid number");
        this.messagesYaml.set("messages.economy-pay-send", "&2Economy &a» &7You paid %player% %amount% dollars");
        this.messagesYaml.set("messages.economy-pay-receive", "&2Economy &a» &7You received from %player% %amount% dollars");
        this.messagesYaml.set("messages.economy-pay-args", "&2Economy &a» &a/pay <player> <amount>");
        this.messagesYaml.set("messages.economy-no-money", "&2Economy &a» &7You haven't money for this pay");
        this.saveConfig();
    }

    public YamlConfiguration getMessagesYaml() {
        return this.messagesYaml;
    }
}
