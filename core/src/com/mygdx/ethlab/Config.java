package com.mygdx.ethlab;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.mygdx.ethlab.GameObjects.Entity;
import com.mygdx.ethlab.GameObjects.GameObject;
import com.mygdx.ethlab.GameObjects.TerrainShape;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Created by Eoin on 22/05/2015.
 */
public class Config {
    public String[] textureNames;
    public Texture[] textureValues;
    private Dictionary<String, Texture> textureMap;

    public String[] spriteNames;
    public String[] baseEntityNames;

    public TextureAtlas atlas;

    public String atlasFileName;
    public String texturesFilePath;

    //Default Constructor for json deserialization
    public Config() {}

    public static Config loadConfig() {
        FileHandle file = Gdx.files.internal("editor.config");
        Json json = new Json();
        Config config = json.fromJson(Config.class, file);

        //
        //Load the texture atlas for each sprite
        //
        config.atlas = new TextureAtlas(Gdx.files.internal(config.atlasFileName));
        TextureAtlas.AtlasRegion[] atlasRegionList = config.atlas.getRegions().toArray(TextureAtlas.AtlasRegion.class);
        config.spriteNames = new String[atlasRegionList.length];

        ArrayList<String> idleSpriteNamesList = new ArrayList<String>();
        for (int i = 0; i < atlasRegionList.length; i++) {
            String regionName = atlasRegionList[i].name;
            config.spriteNames[i] = regionName;
            if (regionName.endsWith("/Idle")) {
                //Record this sprite, but remove the "/Idle" suffix and the "Entity/" prefix
                idleSpriteNamesList.add(regionName.substring(7, regionName.length() - 5));
            }
        }
        config.baseEntityNames = idleSpriteNamesList.toArray(new String[idleSpriteNamesList.size()]);

        //
        //Get the list of texture files and their names
        //
        FileHandle[] textureFiles = Gdx.files.internal(config.texturesFilePath).list();
        config.textureNames = new String[textureFiles.length];
        config.textureValues = new Texture[textureFiles.length];
        for (int i = 0; i < textureFiles.length; i++) {
            config.textureNames[i] = textureFiles[i].nameWithoutExtension();
            config.textureValues[i] = new Texture(textureFiles[i]);
        }

        config.textureMap = new Hashtable<String, Texture>();
        //
        //Create a dictionary of string to texture mappings
        //
        for (int i = 0; i < config.textureNames.length; i++) {
            config.textureMap.put(config.textureNames[i], config.textureValues[i]);
        }

        return config;
    }

    public void dispose() {
        atlas.dispose();
    }

    public TextureRegion getTexture(GameObject object) {
        TextureRegion r;

        String name = object.textureName;
        String fullname;

        if (object instanceof Entity) {
            fullname = "Entity/" + name + "/Idle";
            r = atlas.findRegion(fullname);
        }
        else if (object instanceof TerrainShape) {
            fullname = name;
            Texture t = textureMap.get(fullname);
            if (t != null) {
                t.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
                r = new TextureRegion(t);
            }
            else {
                r = null;
            }
        }
        else {
            fullname = name;
            r = atlas.findRegion(name);
        }

        //If no texture was found, make a debug texture to prevent the application from crashing
        if (r == null) {
            Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            pix.setColor(0xDD0000FF);
            pix.fill();
            Gdx.app.error("Resources", "Texture " + fullname + " not found!");
            r = new TextureRegion(new Texture(pix));
        }
        return r;
    }

    public String[] getTextureNames(GameObject object) {
        if (object instanceof Entity) {
            return baseEntityNames;
        } else if (object instanceof TerrainShape) {
            return textureNames;
        } else {
            return textureNames;
        }
    }
}
