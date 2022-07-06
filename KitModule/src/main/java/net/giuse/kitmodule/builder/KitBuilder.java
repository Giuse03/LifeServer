package net.giuse.kitmodule.builder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;

/**
 * KitBuilder for create Concrete Kits
 */


@RequiredArgsConstructor
@Getter
public class KitBuilder {
    private final ArrayList<String> items = new ArrayList<>();
    private final String name;
    private final int coolDown;
}