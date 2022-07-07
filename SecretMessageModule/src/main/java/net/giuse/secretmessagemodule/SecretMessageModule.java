package net.giuse.secretmessagemodule;

import lombok.Getter;
import lombok.SneakyThrows;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.files.reflections.ReflectionsFiles;
import net.giuse.mainmodule.services.Services;
import net.giuse.secretmessagemodule.files.FileManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public final class SecretMessageModule extends Services {
    @Getter
    private final ArrayList<SecretChatBuilder> secretsChats = new ArrayList<>();
    @Getter
    private final Set<Player> playerMsgToggle = new HashSet<>();
    @Getter
    private final Set<Player> playerSocialSpy = new HashSet<>();
    @Getter
    private final HashMap<String, String> message = new HashMap<>();
    @Inject
    private MainModule mainModule;
    @Getter
    private FileManager fileManager;

    /*
     * Load Service
     */
    @Override
    @SneakyThrows
    public void load() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eSecretChatModule§9] §7Loading SecretChats...");
        //Load Files
        ReflectionsFiles.loadFiles(fileManager = new FileManager());
        if (Files.size(Paths.get("plugins/LifeServer/messages/messages_secret_chat.yml")) == 0) {
            fileManager.messagesMsgLoader();
        }

        //Load Messages
        for (String messageConfig : fileManager.getMessagesSecretChatYaml().getConfigurationSection("messages.msg").getKeys(true)) {
            message.put(messageConfig, fileManager.getMessagesSecretChatYaml().getString("messages.msg." + messageConfig));
        }
        message.put("no-perms", mainModule.getConfig().getString("no-perms"));

    }

    /*
     * Unload Service
     */
    @Override
    public void unload() {
        mainModule.getLogger().info("§8[§2Life§aServer §7>> §eSecretChatModule§9] §7Unloading SecretChats...");
    }

    /*
     * Priority of Service
     */
    @Override
    public int priority() {
        return 0;
    }

    /*
     * Search from UUID a Player which is a sender
     */
    public SecretChatBuilder getSenderSecretChat(UUID uuid) {
        return secretsChats.stream()
                .filter(secretsChats -> secretsChats.getSender().getUniqueId().equals(uuid))
                .findFirst().orElse(null);
    }

    /*
     * Search from UUID a Player which is a Receiver
     */
    public SecretChatBuilder getReceiverSecretChat(UUID uuid) {
        return secretsChats.stream()
                .filter(secretsChats -> secretsChats.getReceiver().getUniqueId().equals(uuid))
                .findFirst().orElse(null);
    }

    /*
     * Get Message from a HashMap
     */
    public String getMessages(String key) {
        return ChatColor.translateAlternateColorCodes('&', message.get(key));
    }


}
