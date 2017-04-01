package com.mygdx.ethlab;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Utils {
    public static Rectangle getBoundingBoxFromTexture(TextureRegion region) {
        return new Rectangle(0, 0, region.getRegionWidth(), region.getRegionHeight());
    }
}
