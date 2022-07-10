package net.giuse.kitmodule.serializer.serializedobject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.giuse.kitmodule.builder.KitBuilder;

@RequiredArgsConstructor
@Getter
public class KitSerialized {
    private final String name;
    private final KitBuilder kitBuilder;
}
