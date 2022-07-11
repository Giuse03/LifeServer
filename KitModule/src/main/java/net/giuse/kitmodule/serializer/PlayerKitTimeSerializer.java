package net.giuse.kitmodule.serializer;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.cooldownsystem.KitCooldown;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.kitmodule.serializer.serializedobject.PlayerKitTimeSerialized;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;

import javax.inject.Inject;
import java.util.UUID;


/**
 * PlayerKitSerializer for save in database
 */

public class PlayerKitTimeSerializer implements Serializer<PlayerKitTimeSerialized> {
    private final KitModule kitModule;

    @Inject
    public PlayerKitTimeSerializer(MainModule mainModule) {
        this.kitModule = (KitModule) mainModule.getService(KitModule.class);
    }

    /**
     * Convert String to PlayerTimerSystem
     */

    @Override
    public String encode(PlayerKitTimeSerialized playerKitTimeSerialized) {
        StringBuilder stringBuilder = new StringBuilder();

        //Set UUID to stringBuilder
        stringBuilder.append(playerKitTimeSerialized.getUuid().toString()).append(";");

        //Encode KitCooldown to string
        int size = playerKitTimeSerialized.getPlayerTimerSystem().getKitsCooldown().size();
        for (int i = 0; i < size; i++) {
            if (i == (size - 1)) {
                stringBuilder.append(playerKitTimeSerialized.getPlayerTimerSystem().getKitsCooldown().get(i).toString());
            } else {
                stringBuilder.append(playerKitTimeSerialized.getPlayerTimerSystem().getKitsCooldown().get(i).toString()).append(",");
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Convert  PlayerTimerSystem to String
     */
    @Override
    public PlayerKitTimeSerialized decoder(String str) {
        String[] decodePlayerKit = str.split(";");
        PlayerTimerSystem playerTimerSystem = new PlayerTimerSystem();

        for (String kitCooldownArg : decodePlayerKit[1].split(",")) {
            String[] kitCooldownStr = kitCooldownArg.split("_");

            //Create kitCooldown with the args
            KitCooldown kitCooldown = new KitCooldown(kitCooldownStr[0], kitModule.getKit(kitCooldownStr[0]));
            kitCooldown.setVariableCoolDown(Integer.parseInt(kitCooldownStr[1]));

            //add kitCooldown to PlayerTimerSystem
            playerTimerSystem.getKitsCooldown().add(kitCooldown);
        }

        return new PlayerKitTimeSerialized(UUID.fromString(decodePlayerKit[0]), playerTimerSystem);
    }
}
