package net.giuse.teleportmodule.gui;

import com.cryptomorin.xseries.XEnchantment;
import com.cryptomorin.xseries.XMaterial;
import inventorylib.ButtonBuilder;
import inventorylib.InventoryBuilder;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.builder.ItemstackBuilder;
import net.giuse.mainmodule.gui.ItemInitializer;
import net.giuse.teleportmodule.TeleportModule;
import org.bukkit.configuration.ConfigurationSection;

import javax.inject.Inject;

public class NextItemGuiWarpInit implements ItemInitializer {
    @Inject
    private MainModule mainModule;

    @Override
    public void initItems(InventoryBuilder inventoryBuilder) {
        TeleportModule teleportModule = (TeleportModule) mainModule.getService(TeleportModule.class);
        ConfigurationSection configurationSection = teleportModule.getFileManager().getWarpYaml().getConfigurationSection("inventory.items");
        configurationSection.getKeys(false).forEach(string -> {
            ConfigurationSection itemsConfig = configurationSection.getConfigurationSection(string);
            if (teleportModule.getFileManager().getWarpYaml().getInt("inventory.page") != 1 && string.equalsIgnoreCase("nextpage")) {
                for (int i = 1; i < inventoryBuilder.getInventoryHash().values().size() + 1; i++) {

                    //Create a ItemBuilderStack
                    ItemstackBuilder itemstackBuilder = new ItemstackBuilder(XMaterial.matchXMaterial(itemsConfig.getString("material").toUpperCase()).get().parseMaterial(),
                            itemsConfig.getInt("amount"))
                            .setName(itemsConfig.getString("display-name"))
                            .setData((short) itemsConfig.getInt("data"));

                    //Check there are enchantments from section
                    if (itemsConfig.getString("enchant") != null) {
                        itemstackBuilder.setEnchant(Integer.parseInt(itemsConfig.getString("enchant").split(":")[1]),
                                XEnchantment.matchXEnchantment(itemsConfig.getString("enchant").split(":")[0]).get().getEnchant()).toItem();
                    }

                    //Add Item in Gui
                    inventoryBuilder.addButton(new ButtonBuilder(
                            inventoryBuilder, itemsConfig.getInt("position"),
                            i, itemstackBuilder.toItem()
                            , true, false, true)
                    );
                }
            }
        });
    }
}
