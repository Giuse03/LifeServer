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
import org.bukkit.entity.Player;

import javax.inject.Inject;


/**
 * Initialize Items in kit gui
 */

public class ItemsGuiInit implements ItemInitializer {
    @Inject
    private MainModule mainModule;

    @Override
    public void initItems(InventoryBuilder inventoryBuilder) {
        KitModule kitModule = (KitModule) mainModule.getService(KitModule.class);
        ConfigurationSection configurationSection = kitModule.getConfigManager().getKitYaml().getConfigurationSection("inventory.items");
        for (String string : configurationSection.getKeys(false)) {
            ConfigurationSection itemsConfig = configurationSection.getConfigurationSection(string);
            if (!string.equalsIgnoreCase("previouspage") && !string.equalsIgnoreCase("nextpage")) {

                //Create ItemStackBuilder
                ItemstackBuilder itemstackBuilder = new ItemstackBuilder(Material.getMaterial(itemsConfig.getString("material").toUpperCase()), itemsConfig.getInt("amount"));
                itemstackBuilder.setData((short) itemsConfig.getInt("data"));
                itemstackBuilder.setName(itemsConfig.getString("display-name"));

                //Check there are enchantments from section
                if (itemsConfig.getString("enchant") != null) {
                    itemstackBuilder.setEnchant(Integer.parseInt(itemsConfig.getString("enchant").split(":")[1]),
                            Enchantment.getByName(itemsConfig.getString("enchant").split(":")[0]));
                }

                //Check there are lores from section
                if (!itemsConfig.getStringList("lore").isEmpty()) {
                    itemstackBuilder.setLores(itemsConfig.getStringList("lore"));
                }

                //Create a button
                ButtonBuilder button = new ButtonBuilder(
                        inventoryBuilder,
                        itemsConfig.getInt("position"),
                        itemsConfig.getInt("page"),
                        itemstackBuilder.toItem(),
                        false, false, true);

                //Set Event of the button
                button.setEvent(inventoryClickEvent -> {
                    if (itemsConfig.getString("givekit") != null) {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        player.performCommand("kit " + itemsConfig.getString("givekit"));
                    }
                });
                inventoryBuilder.addButton(button);
            }
        }
    }
}
