package net.giuse.teleportmodule.serializer.serializedobject;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class HomeSerialized {

    private final UUID owner;
    private final Cache<String, Location> locations;

}
