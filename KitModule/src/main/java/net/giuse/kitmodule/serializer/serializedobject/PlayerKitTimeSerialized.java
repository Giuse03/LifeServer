package net.giuse.kitmodule.serializer.serializedobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.giuse.kitmodule.cooldownsystem.PlayerTimerSystem;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class PlayerKitTimeSerialized {
    private final UUID uuid;
    private final PlayerTimerSystem playerTimerSystem;
}
