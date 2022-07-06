package net.giuse.teleportmodule.builder;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;

@RequiredArgsConstructor
@Getter
public class WarpBuilder {
    private final String name;
    private final Location location;


    @Override
    public String toString() {
        return name + "-" +
                location.getWorld().getName() +
                "," + location.getX() +
                "," + location.getY() +
                "," + location.getZ() +
                "," + location.getYaw() +
                "," + location.getPitch();
    }
}
