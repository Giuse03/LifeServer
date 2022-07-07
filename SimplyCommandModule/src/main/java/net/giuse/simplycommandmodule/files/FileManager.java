package net.giuse.simplycommandmodule.files;


import lombok.Getter;
import net.giuse.mainmodule.files.abstractfiles.AbstractConfig;
import net.giuse.mainmodule.files.annotations.FileAnnotation;
import net.giuse.mainmodule.files.annotations.YamlAnnotation;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class FileManager extends AbstractConfig {

    @FileAnnotation(name = "messages_simple_command.yml", path = "plugins/LifeServer/messages/messages_simple_command.yml")
    private File messageSimpleFile;
    @YamlAnnotation(nameFile = "messages_simple_command.yml")
    @Getter
    private YamlConfiguration messageSimpleFileYml;

    public void messagesMsgLoader() {
        setFile(messageSimpleFile);
        setYamlConfiguration(messageSimpleFileYml);
        messageSimpleFileYml.set("messages.simplecommand.fly-enable", "&aFly &2»&7 Fly enabled");
        messageSimpleFileYml.set("messages.simplecommand.fly-disabled", "&aFly &2»&7 Fly disabled");
        messageSimpleFileYml.set("messages.simplecommand.fly-enable-other", "&aFly &2»&7 Fly enabled for %player_name%");
        messageSimpleFileYml.set("messages.simplecommand.fly-disabled-other", "&aFly &2»&7 Fly disabled for %player_name%");
        messageSimpleFileYml.set("messages.simplecommand.repair-usage", "&aRepair &2»&7 Usage: /repair <all|hand>");
        messageSimpleFileYml.set("messages.simplecommand.repair-hand", "&aRepair &2»&7 Repaired item in hand");
        messageSimpleFileYml.set("messages.simplecommand.repair-all", "&aRepair &2»&7 Repaired item in inventory");
        messageSimpleFileYml.set("messages.simplecommand.repair-nothing", "&aRepair &2»&7 You don't have any items in your hand");
        messageSimpleFileYml.set("messages.simplecommand.heal", "&aHeal &2» &7You healed yourself");
        messageSimpleFileYml.set("messages.simplecommand.food", "&aFood &2» &7Gnam Gnam");
        messageSimpleFileYml.set("messages.simplecommand.heal-other", "&aHeal &2» &7You healed %player_name%");
        messageSimpleFileYml.set("messages.simplecommand.food-other", "&aFood &2» &7Gnam Gnam for %player_name%");
        messageSimpleFileYml.set("messages.simplecommand.cleaninv", "&aClenInv &2» &7Your inventory was cleaned");
        messageSimpleFileYml.set("messages.simplecommand.cleaninv-other", "&aCleanInv &2» &7The inventory of %player_name% was cleaned");
        messageSimpleFileYml.set("messages.simplecommand.gamemode", "&aGamemode &2» &7Your gamemode now is %gamemode%");
        messageSimpleFileYml.set("messages.simplecommand.gamemode-other", "&aGamemode &2» &7The gamemode of %player_name% now is %gamemode%");
        messageSimpleFileYml.set("messages.simplecommand.gamemode-available", "&aGamemode &2» &7Available gamemode: <creative> <survival> <spectator> <adventure>");
        messageSimpleFileYml.set("messages.simplecommand.time-usage", "&aTime &2» &7Usage /time <tick>");
        messageSimpleFileYml.set("messages.simplecommand.time", "&aTime &2» &7The time changed");
        messageSimpleFileYml.set("messages.simplecommand.near-nobody", "&aNear &2» &7Nobody in 10 blocks distance");
        messageSimpleFileYml.set("messages.simplecommand.near", "&aNear &2» &7Players: %player_list%");
        messageSimpleFileYml.set("messages.simplecommand.invsee-select-player", "&aInvsee &2» &7Select a player");
        messageSimpleFileYml.set("messages.simplecommand.speed-usage", "&aSpeed &2» &7Usage: /speed <value> [player]");
        messageSimpleFileYml.set("messages.simplecommand.speed-set", "&aWalk &2» &7Speed speed now is %number%");
        messageSimpleFileYml.set("messages.simplecommand.walk-set", "&aSpeed &2» &7Walking speed now is %number%");
        messageSimpleFileYml.set("messages.simplecommand.speed-set-other", "&aWalk &2» &7Flying speed for %player_name% now is %number%");
        messageSimpleFileYml.set("messages.simplecommand.walk-set-other", "&aSpeed &2» &7Walking speed for %player_name% now is %number%");
        messageSimpleFileYml.set("messages.simplecommand.speed-invalid-number-time", "&aSpeed &2»  &7%invalid_number% is invalid number");
        messageSimpleFileYml.set("messages.simplecommand.burn-invalid-number-time", "&aBurn &2» &7%invalid_number% is invalid number");
        messageSimpleFileYml.set("messages.simplecommand.sudo-usage", "&aSudo &2» &7Usage: /sudo <player> <command>");
        messageSimpleFileYml.set("messages.simplecommand.sudo-forced", "&aSudo &2» &7Forced %player_name% to do %command%");
        messageSimpleFileYml.set("messages.simplecommand.broadcast", "&aBroadcast &2» &7%message%");
        messageSimpleFileYml.set("messages.simplecommand.broadcast-usage", "&aBroadcast &2» &7Usage: /broadcast <message>");
        messageSimpleFileYml.set("messages.simplecommand.burning", "&aBurn &2» &7%player_name% is burning for %seconds%");
        messageSimpleFileYml.set("messages.simplecommand.enchant-usage", "&aEnchant &2» &7/enchant <enchantment> <level> EnchantList: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/Enchantment.html");
        messageSimpleFileYml.set("messages.simplecommand.enchant-added", "&aEnchant &2» &7Enchant added!");
        messageSimpleFileYml.set("messages.simplecommand.enchant-insert-level", "&aEnchant &2» &7Insert level for this enchant!");
        messageSimpleFileYml.set("messages.simplecommand.enchant-correct-insert-level", "&aEnchant &2» &7Insert valid number!");
        messageSimpleFileYml.set("messages.simplecommand.enchant-invalid", "&aEnchant &2» %enchant% is invalid! Check list: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/enchantments/Enchantment.html");
        messageSimpleFileYml.set("messages.simplecommand.god-enabled", "&aGod &2» You're now God");
        messageSimpleFileYml.set("messages.simplecommand.god-disabled", "&aGod &2» You aren't now God");
        messageSimpleFileYml.set("messages.simplecommand.god-other-enabled", "&aGod &2» %player_name% is now a god");
        messageSimpleFileYml.set("messages.simplecommand.god-other-disabled", "&aGod &2» %player_name% isn't now a god!");
        messageSimpleFileYml.set("messages.simplecommand.player-not-online", "&7This player is offline");
        messageSimpleFileYml.set("messages.simplecommand.not-player", "&cYou must be player!");
        saveConfig();
    }

}