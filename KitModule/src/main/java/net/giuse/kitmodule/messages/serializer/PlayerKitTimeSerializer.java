package net.giuse.kitmodule.messages.serializer;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.cooldownsystem.PlayerKitCooldown;
import net.giuse.kitmodule.messages.serializer.serializedobject.PlayerKitCooldownSerialized;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * PlayerKitSerializer for save in database
 */

public class PlayerKitTimeSerializer implements Serializer<PlayerKitCooldownSerialized> {
    private final KitModule kitModule;

    @Inject
    public PlayerKitTimeSerializer(MainModule mainModule) {
        this.kitModule = (KitModule) mainModule.getService(KitModule.class);
    }

    /**
     * Convert String to PlayerTimerSystem
     */

    @Override
    public String encode(PlayerKitCooldownSerialized playerKitTimeSerialized) {
        StringBuilder stringBuilder = new StringBuilder();

        //Set UUID to stringBuilder
        stringBuilder.append(playerKitTimeSerialized.getUuidPlayer().toString()).append(";");

        //Encode cooldown of kits to string
        int KitCooldownMapSize = playerKitTimeSerialized.getPlayerKitCooldown().getSizeKitCooldown();

        AtomicInteger loopCounter = new AtomicInteger();

        playerKitTimeSerialized.getPlayerKitCooldown().getCoolDownKits().keySet().forEach(kitName -> {
            loopCounter.getAndIncrement();
            boolean addComma = (loopCounter.get() != KitCooldownMapSize);

            int actualCooldown = playerKitTimeSerialized.getPlayerKitCooldown().getCoolDownKits().getInt(kitName);

            if (addComma) {
                stringBuilder.append(kitName).append("_").append(actualCooldown).append(",");
            } else {
                stringBuilder.append(kitName).append("_").append(actualCooldown);
            }
        });


        return stringBuilder.toString();
    }

    /**
     * Convert  PlayerTimerSystem to String
     */
    @Override
    public PlayerKitCooldownSerialized decoder(String str) {
        String[] playerKitDecoded = str.split(";");
        PlayerKitCooldown playerKitCooldown = new PlayerKitCooldown();

        for (String playerKitDecodedSplit : playerKitDecoded[1].split(",")) {
            String[] playerKitDecodedArguments = playerKitDecodedSplit.split("_");
            playerKitCooldown.addKit(playerKitDecodedArguments[0], Integer.parseInt(playerKitDecodedArguments[1]));
        }

        return new PlayerKitCooldownSerialized(UUID.fromString(playerKitDecoded[0]), playerKitCooldown);
    }
}
