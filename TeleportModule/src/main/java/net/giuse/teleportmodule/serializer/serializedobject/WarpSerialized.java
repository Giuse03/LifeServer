package net.giuse.teleportmodule.serializer.serializedobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@RequiredArgsConstructor
@Getter
public class WarpSerialized {
    private final String name;
    private final Location location;

}
