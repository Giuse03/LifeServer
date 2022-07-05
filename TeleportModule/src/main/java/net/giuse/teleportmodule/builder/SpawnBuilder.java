package net.giuse.teleportmodule.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@Getter
@RequiredArgsConstructor
public class SpawnBuilder {

    private final Location location;


    @Override
    public String toString() {
        return location.getWorld().getName() + "," +
                location.getBlockX() + "," +
                location.getBlockY() + "," +
                location.getBlockZ() + "," +
                location.getYaw() + "," +
                location.getPitch();
    }

}
