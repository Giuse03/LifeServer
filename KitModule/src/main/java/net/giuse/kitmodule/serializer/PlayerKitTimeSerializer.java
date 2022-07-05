package net.giuse.kitmodule.serializer;

import jdk.jfr.internal.tool.Main;
import net.giuse.kitmodule.KitModule;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;
import net.giuse.mainmodule.MainModule;
import net.giuse.mainmodule.serializer.Serializer;

import javax.inject.Inject;
import java.util.UUID;

public class PlayerKitTimeSerializer implements Serializer<PlayerTimerSystem> {
    private final KitModule kitModule;

    @Inject
    public PlayerKitTimeSerializer(MainModule mainModule) {
        this.kitModule = (KitModule) mainModule.getService(KitModule.class);
    }

    @Override
    public String encode(PlayerTimerSystem playerTimerSystem) {
        return playerTimerSystem.getUuid().toString() + ";" + playerTimerSystem.getNameKit() + ";" + playerTimerSystem.getVariableCoolDown();
    }

    @Override
    public PlayerTimerSystem decoder(String str) {
        String[] decodePlayerKit = str.split(";");
    return new PlayerTimerSystem(decodePlayerKit[1], UUID.fromString(decodePlayerKit[0]), kitModule.getKit(decodePlayerKit[1]).getCoolDown(), Integer.parseInt(decodePlayerKit[2]));
    }
}
