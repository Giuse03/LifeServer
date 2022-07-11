package net.giuse.kitmodule.gui;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import eu.giuse.inventorylib.ButtonBuilder;
import eu.giuse.inventorylib.InventoryBuilder;
import net.giuse.engine.Worker;
import net.giuse.kitmodule.KitModule;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.builder.ItemstackBuilder;
import net.giuse.mainmodule.gui.ItemInitializer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.inject.Inject;


/**
 * Initialize Items in kit gui
 */

public class ItemsGuiInit implements ItemInitializer {
    @Inject
    private MainModule mainModule;
    @Inject
    private Worker worker;

    @Override
    public void initItems(InventoryBuilder inventoryBuilder) {
        KitModule kitModule = (KitModule) mainModule.getService(KitModule.class);
        ConfigurationSection configurationSection = kitModule.getConfigManager().getKitYaml().getConfigurationSection("inventory.items");
        for (String string : configurationSection.getKeys(false)) {
            ConfigurationSection itemsConfig = configurationSection.getConfigurationSection(string);
            if (!string.equalsIgnoreCase("previouspage") && !string.equalsIgnoreCase("nextpage")) {

                //Create ItemStackBuilder
                ItemstackBuilder itemstackBuilder = new ItemstackBuilder(XMaterial.matchXMaterial(itemsConfig.getString("material").toUpperCase()).get().parseMaterial(), itemsConfig.getInt("amount"));
                itemstackBuilder.setData((short) itemsConfig.getInt("data"));
                itemstackBuilder.setName(itemsConfig.getString("display-name"));

                //Check there are enchantments from section
                if (itemsConfig.getString("enchant") != null) {
                    itemstackBuilder.setEnchant(Integer.parseInt(itemsConfig.getString("enchant").split(":")[1]),
                            XEnchantment.matchXEnchantment(itemsConfig.getString("enchant").split(":")[0]).get().getEnchant());
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
                        false, false, true, worker);

                //Set Event of the button
                button.setEvent(inventoryClickEvent -> {
                    if (itemsConfig.getString("givekit") != null) {
                        Player player = (Player) inventoryClickEvent.getWhoClicked();
                        player.performCommand("kit " + itemsConfig.getString("givekit"));
                        inventoryClickEvent.getWhoClicked().closeInventory();
                    }
                });
                inventoryBuilder.addButton(button);
            }
        }
    }
}
