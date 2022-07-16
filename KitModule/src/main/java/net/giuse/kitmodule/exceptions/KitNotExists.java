package net.giuse.kitmodule.exceptions;

import lombok.RequiredArgsConstructor;


public class KitNotExists extends NullPointerException{

    @Override
    public String getMessage() {
        return "kit-doesnt-exists";
    }
}
