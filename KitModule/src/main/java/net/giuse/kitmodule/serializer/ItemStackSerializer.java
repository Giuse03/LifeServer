package net.giuse.kitmodule.serializer;

import net.giuse.mainmodule.builder.ItemstackBuilder;
import net.giuse.mainmodule.serializer.Serializer;
import net.giuse.mainmodule.utils.BukkitObjectUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * ItemStack serializer for save in database
 */

public class ItemStackSerializer implements Serializer<ItemStack> {


    /**
     * Convert String to ItemStack
     */
    @Override
    public ItemStack decoder(String str) {
        try {
            byte[] b = BukkitObjectUtil.hexStringToByteArray(str);
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            BukkitObjectInputStream bois = new BukkitObjectInputStream(bais);
            ItemStack items = (ItemStack) bois.readObject();
            bois.close();
            bais.close();
            return items;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;

    }


    /**
     * Convert ItemStack to String
     */
    @Override
    public String encode(ItemStack itemStack) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BukkitObjectOutputStream bos = new BukkitObjectOutputStream(os);
            bos.writeObject(itemStack);
            String hex = BukkitObjectUtil.byteArrayToHexString(os.toByteArray());
            bos.close();
            os.close();
            return hex;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}