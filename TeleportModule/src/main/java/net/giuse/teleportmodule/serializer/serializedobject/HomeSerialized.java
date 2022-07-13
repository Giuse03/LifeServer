package net.giuse.teleportmodule.serializer.serializedobject;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class HomeSerialized {

    private final UUID owner;
    private final Object2ObjectMap<String, Location> locations;

}
