package net.giuse.mainmodule.utils;

import lombok.SneakyThrows;
import net.giuse.mainmodule.messages.MessageActionbar;
import net.giuse.mainmodule.messages.MessageChat;
import net.giuse.mainmodule.messages.MessageTitle;
import net.giuse.mainmodule.messages.interfaces.Message;
import net.giuse.mainmodule.messages.interfaces.MessageLoader;
import net.giuse.mainmodule.messages.type.MessageType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*
 *   Utility Class
 */
public class Utils {

    /*
     * Create a sorted hashmap
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    /*
     * Register Commands
     */
    @SneakyThrows
    public static void registerCommand(String name, Command command) {
        Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
        commandMap.register(name, command);
    }

    /*
     * Convert Seconds in Time Format
     */
    public static String formatTime(int secs) {
        int remainder = secs % 86400;
        int days = secs / 86400;
        int hours = remainder / 3600;
        int minutes = remainder / 60 - hours * 60;
        int seconds = remainder % 3600 - minutes * 60;
        String fDays = (days > 0) ? (" " + days + " day" + ((days > 1) ? "s" : "")) : "";
        String fHours = (hours > 0) ? (" " + hours + " hour" + ((hours > 1) ? "s" : "")) : "";
        String fMinutes = (minutes > 0) ? (" " + minutes + " minute" + ((minutes > 1) ? "s" : "")) : "";
        String fSeconds = (seconds > 0) ? (" " + seconds + " second" + ((seconds > 1) ? "s" : "")) : "";
        return fDays.concat(fHours).concat(fMinutes).concat(fSeconds);
    }


    /**
     * A method to serialize an ItemStack array to Base64 String.
     * <p>
     * <p/>
     * <p>
     * src: https://gist.github.com/graywolf336/8153678
     *
     * @param items to turn into a Base64 String.
     * @return Base64 string of the items.
     */
    public static String listItemStackToBase64(List<ItemStack> items) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            // Write the size of the inventory
            dataOutput.writeInt(items.size());

            // Save every element in the list
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }


    /**
     * Gets an array of ItemStacks from Base64 string.
     * <p>
     * <p/>
     * <p>
     * src: https://gist.github.com/graywolf336/8153678
     *
     * @param data Base64 string to convert to ItemStack array.
     * @return ItemStack array created from the Base64 string.
     * @throws IOException
     */
    @SneakyThrows
    public static ItemStack[] itemStackArrayFromBase64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack[] items = new ItemStack[dataInput.readInt()];

            // Read the serialized inventory
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }


    /**
     * Send Message to Player with Placeholders
     */

    public static void sendMessage(MessageLoader messageLoader, Player player, String idMessage, String... replace) {
        List<Message> messages = messageLoader.getMessagesFromId(idMessage);
        //Send Chat
        for (Message message : messages) {
            if (message.getMessageType().equals(MessageType.CHAT)) {
                MessageChat messageChat = (MessageChat) message;
                String messageToReplace = messageChat.getMessageChat();
                if (replace.length == 0) {
                    messageLoader.sendChat(player, Component.text(messageToReplace));
                } else {
                    String newComponent = null;
                    for (String args : replace) {
                        String[] arg = args.split("=");
                        newComponent = messageToReplace.replace(arg[0], arg[1]);
                    }
                    messageLoader.sendChat(player, Component.text(newComponent));
                }

            }

            //Send Title
            if (message.getMessageType().equals(MessageType.TITLE)) {
                MessageTitle messageTitle = (MessageTitle) message;
                String title = messageTitle.getTitle();
                String subTitle = messageTitle.getSubTitle();
                if (replace.length == 0) {
                    messageLoader.sendTitle(player, Component.text(title), Component.text(subTitle), messageTitle.getFadeIn(), messageTitle.getStay(), messageTitle.getFadeOut());
                } else {
                    String newTitle = null, newSubTitle = null;
                    for (String args : replace) {
                        String[] arg = args.split("=");
                        newTitle = title.replace(arg[0], arg[1]);
                        newSubTitle = subTitle.replace(arg[0], arg[1]);
                    }
                    messageLoader.sendTitle(player, Component.text(newTitle), Component.text(newSubTitle), messageTitle.getFadeIn(), messageTitle.getStay(), messageTitle.getFadeOut());
                }

            }

            //Send ActionBar
            if (message.getMessageType().equals(MessageType.ACTION_BAR)) {
                MessageActionbar messageActionbar = (MessageActionbar) message;
                String placeHolder = messageActionbar.getMessageBar();

                if (replace.length == 0) {
                    messageLoader.sendActionBar(player, Component.text(placeHolder));
                    return;
                }
                for (String args : replace) {
                    String[] arg = args.split("=");
                    placeHolder = placeHolder.replace(arg[0], arg[1]);
                }
                messageLoader.sendActionBar(player, Component.text(placeHolder));
            }
        }
    }

}
