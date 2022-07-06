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
        return name.concat(":")
                .concat(location.getWorld().getName()).concat(",")
                .concat(String.valueOf(location.getX()))
                .concat(",").concat(String.valueOf(location.getY()))
                .concat(",").concat(String.valueOf(location.getZ()))
                .concat(",").concat(String.valueOf(location.getYaw()))
                .concat(",").concat(String.valueOf(location.getPitch()));
    }
}
