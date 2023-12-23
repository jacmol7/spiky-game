package com.hannah.game;

import com.dongbat.jbump.Rect;

public class Utils {

    public static Rect copyRect(Rect toCopy) {
        return new Rect(toCopy.x, toCopy.y, toCopy.w, toCopy.h);
    }
}
