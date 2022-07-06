package net.giuse.kitmodule.builder;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * KitBuilder for create Concrete Kits
 */

@Setter
@Getter
public class KitBuilder {
    private final ArrayList<String> items = new ArrayList<>();
    private String name;
    private int coolDown;
}