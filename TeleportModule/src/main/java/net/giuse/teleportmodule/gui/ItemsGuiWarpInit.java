package net.giuse.teleportmodule.gui;

import eu.giuse.inventorylib.ButtonBuilder;
import eu.giuse.inventorylib.InventoryBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.builder.ItemstackBuilder;
import net.giuse.mainmodule.gui.ItemInitializer;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class ItemsGuiWarpInit implements ItemInitializer {
    @Inject
    private MainModule mainModule;


    @Override
    public void initItems(InventoryBuilder inventoryBuilder) {
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        ConfigurationSection configurationSection = teleportModule.getFileManager().getWarpYaml().getConfigurationSection("inventory.items");
        for (String string : configurationSection.getKeys(false)) {
            ConfigurationSection itemsConfig = configurationSection.getConfigurationSection(string);
            if (!string.equalsIgnoreCase("previouspage") && !string.equalsIgnoreCase("nextpage")) {
                ItemstackBuilder itemstackBuilder = new ItemstackBuilder(Material.getMaterial(itemsConfig.getString("material").toUpperCase()), itemsConfig.getInt("amount"));
                itemstackBuilder.setData((short) itemsConfig.getInt("data"));
                itemstackBuilder.setName(itemsConfig.getString("display-name"));
                if (itemsConfig.getString("enchant") != null) itemstackBuilder
                        .setEnchant(Integer.parseInt(itemsConfig.getString("enchant").split(":")[1]),
                                Enchantment.getByName(itemsConfig.getString("enchant").split(":")[0]));
                if (!itemsConfig.getStringList("lore").isEmpty()) {
                    itemstackBuilder.setLores(itemsConfig.getStringList("lore"));
                }

                ButtonBuilder button = new ButtonBuilder(
                        inventoryBuilder,
                        itemsConfig.getInt("position"),
                        itemsConfig.getInt("page"),
                        itemstackBuilder.toItem(),
                        false, false, true);
                button.setEvent(inventoryClickEvent -> {

                    if (itemsConfig.getString("warp") != null) {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        player.performCommand("warp " + itemsConfig.getString("warp"));
                    }
                });
                inventoryBuilder.addButton(button);
            }
        }
    }
}
