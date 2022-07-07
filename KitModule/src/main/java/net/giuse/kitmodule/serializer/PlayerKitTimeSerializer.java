package net.giuse.kitmodule.serializer;

import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.cooldownsystem.KitCooldown;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;

import javax.inject.Inject;
import java.util.UUID;


/**
 * PlayerKitSerializer for save in database
 */

public class PlayerKitTimeSerializer implements Serializer<PlayerTimerSystem> {
    private final KitModule kitModule;

    @Inject
    public PlayerKitTimeSerializer(MainModule mainModule) {
        this.kitModule = (KitModule) mainModule.getService(KitModule.class);
    }

    /**
     * Convert String to PlayerTimerSystem
     */
    @Override
    public String encode(PlayerTimerSystem playerTimerSystem) {
        StringBuilder stringBuilder = new StringBuilder();

        //Set UUID to stringBuilder
        stringBuilder.append(playerTimerSystem.getUuid().toString()).append(";");

        //Encode KitCooldown to string
        int size = playerTimerSystem.getKitsCooldown().size();
        for (int i = 0; i < size; i++) {
            if (i == (size - 1)) {
                stringBuilder.append(playerTimerSystem.getKitsCooldown().get(i).toString());
            } else {
                stringBuilder.append(playerTimerSystem.getKitsCooldown().get(i).toString()).append(",");
            }
        }

        return stringBuilder.toString();
    }

    /**
     * Convert  PlayerTimerSystem to String
     */
    @Override
    public PlayerTimerSystem decoder(String str) {
        String[] decodePlayerKit = str.split(";");
        PlayerTimerSystem playerTimerSystem = new PlayerTimerSystem(UUID.fromString(decodePlayerKit[0]));

        for (String kitCooldownArg : decodePlayerKit[1].split(",")) {
            String[] kitCooldownStr = kitCooldownArg.split("_");

            //Create kitCooldown with the args
            KitCooldown kitCooldown = new KitCooldown(kitModule.getKit(kitCooldownStr[0]));
            kitCooldown.setVariableCoolDown(Integer.parseInt(kitCooldownStr[1]));

            //add kitCooldown to PlayerTimerSystem
            playerTimerSystem.getKitsCooldown().add(kitCooldown);
        }

        return playerTimerSystem;
    }
}
