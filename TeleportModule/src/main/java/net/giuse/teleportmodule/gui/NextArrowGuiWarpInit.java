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

import javax.inject.Inject;

public class NextArrowGuiWarpInit implements ItemInitializer {

    @Inject
    private MainModule mainModule;


    @Override
    public void initItems(InventoryBuilder inventoryBuilder) {
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        ConfigurationSection configurationSection = teleportModule.getFileManager().getWarpYaml().getConfigurationSection("inventory.items");
        for (String string : configurationSection.getKeys(false)) {
            ConfigurationSection itemsConfig = configurationSection.getConfigurationSection(string);
            if (teleportModule.getFileManager().getWarpYaml().getInt("inventory.page") != 1) {
                if (string.equalsIgnoreCase("nextpage")) {
                    for (int i = 1; i < inventoryBuilder.getInventoryHash().values().size() + 1; i++) {
                        ItemstackBuilder itemstackBuilder = new ItemstackBuilder(Material.getMaterial(itemsConfig.getString("material").toUpperCase()),
                                itemsConfig.getInt("amount"))
                                .setName(itemsConfig.getString("display-name"))
                                .setData((short) itemsConfig.getInt("data"));
                        if (itemsConfig.getString("enchant") != null) {
                            itemstackBuilder.setEnchant(Integer.parseInt(itemsConfig.getString("enchant").split(":")[1]),
                                    Enchantment.getByName(itemsConfig.getString("enchant").split(":")[0])).toItem();
                        }
                        inventoryBuilder.addButton(new ButtonBuilder(
                                inventoryBuilder, itemsConfig.getInt("position"),
                                i, itemstackBuilder.toItem()
                                , true, false, true)
                        );
                    }
                }
            }
        }
    }
}
