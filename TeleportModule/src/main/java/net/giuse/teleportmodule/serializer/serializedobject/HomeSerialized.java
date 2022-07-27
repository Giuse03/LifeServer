package net.giuse.teleportmodule.serializer.serializedobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class HomeSerialized {

    private final UUID owner;
    private final HashMap<String, Location> locations;

}
