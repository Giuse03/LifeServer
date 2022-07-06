package net.giuse.kitmodule.gui;

import eu.giuse.inventorylib.ButtonBuilder;
import eu.giuse.inventorylib.InventoryBuilder;
import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.builder.ItemstackBuilder;
import net.giuse.mainmodule.gui.ItemInitializer;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

import javax.inject.Inject;

/**
 * Initialize Previous Arrow in Kit gui
 */

public class PreviousItemGuiInit implements ItemInitializer {

    @Inject
    private MainModule mainModule;

    @Override
    public void initItems(InventoryBuilder inventoryBuilder) {
        KitModule kitModule = (KitModule) mainModule.getService(KitModule.class);
        ConfigurationSection configurationSection = kitModule.getConfigManager().getKitYaml().getConfigurationSection("inventory.items");
        for (String string : configurationSection.getKeys(false)) {
            ConfigurationSection itemsConfig = configurationSection.getConfigurationSection(string);
            if (kitModule.getConfigManager().getKitYaml().getInt("inventory.page") != 1) {
                if (string.equalsIgnoreCase("previouspage")) {
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
                                , false, true, true)
                        );
                    }
                }
            }
        }
    }
}
