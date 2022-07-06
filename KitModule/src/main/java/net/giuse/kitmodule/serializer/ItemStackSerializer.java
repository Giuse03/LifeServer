package net.giuse.kitmodule.serializer;

import net.giuse.mainmodule.builder.ItemstackBuilder;
import net.giuse.mainmodule.serializer.Serializer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * ItemStack serializer for save in database
 */

public class ItemStackSerializer implements Serializer<ItemStack> {


    /**
     * Convert String to ItemStack
     */
    @Override
    public ItemStack decoder(String str) {
        //Split ItemStack serialized
        String[] valueItemStack = str.split(";");

        //Create an ItemStackBuilder
        ItemstackBuilder itemStackBuild = new ItemstackBuilder(
                Material.getMaterial(valueItemStack[0].replace("material_", "")),
                Integer.parseInt(valueItemStack[2].replace("amount_", "")));
        itemStackBuild.setData(Short.parseShort(valueItemStack[1].replace("data_", "")));

        //Iterate all Strings of ItemStack Serialized
        for (String strings : valueItemStack) {
            //Set Name to ItemStackBuilder
            if (strings.contains("name_")) {
                itemStackBuild.setName(strings.replace("name_", ""));
            }

            //Set Enchants to ItemStackBuilder
            if (strings.contains("enchant_")) {
                String[] valueEnchant = strings.replace("enchant_", "").split(",");
                int i;
                for (i = 0; i < valueEnchant.length; i++) {
                    String[] valueEnchantAndLevel = valueEnchant[i].split(":");
                    int lvl = Integer.parseInt(valueEnchantAndLevel[1]);
                    itemStackBuild.setEnchant(lvl, Enchantment.getByName(valueEnchantAndLevel[0]));
                }
            }

            //Set Lore to ItemStackBuilder
            if (strings.contains("lore_")) {
                itemStackBuild.setLores(strings.split(":"));
            }
        }
        return itemStackBuild.toItem();
    }


    /**
     * Convert ItemStack to String
     */
    @Override
    public String encode(ItemStack itemStack) {
        StringBuilder sb = new StringBuilder();
        //Save Material
        sb.append("material_").append(itemStack.getType().toString()).append(";");

        //Save Data
        sb.append("data_").append(itemStack.getDurability()).append(";");

        //Save Amount
        sb.append("amount_").append(itemStack.getAmount()).append(";");

        //Save Name
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()){
            sb.append("name_").append(itemStack.getItemMeta().getDisplayName()).append(";");
        }

        //Save Enchant
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasEnchants()) {
            sb.append("enchant_");
            if (itemStack.getEnchantments() != null) {
                int i = 0;
                for (Enchantment enchantment : itemStack.getEnchantments().keySet()) {
                    i++;
                    if ((i) == itemStack.getEnchantments().size()) {
                        sb.append(enchantment.getName()).append(":").append(itemStack.getEnchantmentLevel(enchantment));
                    } else {
                        sb.append(enchantment.getName()).append(":").append(itemStack.getEnchantmentLevel(enchantment)).append(",");
                    }
                }
                sb.append(";");
            }
        }


        //Save lores
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasLore()) {
            sb.append("lore_");
            int size = itemStack.getItemMeta().getLore().size();
            for (int i = 0; i < size; i++) {
                if (i == (size-1)) {
                    sb.append(itemStack.getItemMeta().getLore().get(i));
                } else{
                    sb.append(itemStack.getItemMeta().getLore().get(i)).append(":");
                }
            }
            sb.append(";");
        }
        return sb.toString();
    }

}

