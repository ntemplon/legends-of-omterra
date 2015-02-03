/*
 * The MIT License
 *
 * Copyright 2014 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.jupiter.europa.io;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.jupiter.europa.util.ArrayUtils;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author Nathan Templon
 */
public class EmergenceAssetManager extends AssetManager {

    // Constants
    public final String[] ATLAS_EXTENSIONS = new String[]{"atlas", "ATLAS"};
    public final String[] MAP_EXTENSIONS = new String[]{"tmx", "TMX"};
    public final String[] FONT_EXTENSIONS = new String[]{"fnt", "FNT"};
    public final String[] SKIN_EXTENSIONS = new String[]{"skin", "SKIN"};
    public final String[] TEXTURE_EXTENTIONS = new String[] {"png", "PNG"};


    // Initialization
    public EmergenceAssetManager() {
        super();

        // Support for tmx map loading
        this.setLoader(TiledMap.class, new TmxMapLoader());
    }


    // Public Methods
    public void loadInternalResources() {
        this.loadResourcesRecursive(FileLocations.ASSET_DIRECTORY);
    }


    // Custom AssetManager Methods
    @Override
    public synchronized <T> T get(String fileName) {
        // Replace "\\" with "/" to match libGDX internal file naming
        return super.get(FileUtils.crossPlatformFilePath(fileName));
    }

    @Override
    public synchronized <T> T get(String fileName, Class<T> type) {
        // Replace "\\" with "/" to match libGDX internal file naming
        return super.get(FileUtils.crossPlatformFilePath(fileName), type);
    }


    // Private Methods
    private void loadResourcesRecursive(Path directory) {
        try (DirectoryStream<Path> subpaths = Files.newDirectoryStream(directory)) {
            subpaths.iterator().forEachRemaining((Path subpath) -> {
                if (Files.isDirectory(subpath)) {
                    this.loadResourcesRecursive(subpath);
                }
                else {
                    String extension = FileUtils.getExtension(subpath);
                    if (ArrayUtils.contains(MAP_EXTENSIONS, extension)) {
                        this.load(subpath.toString(), TiledMap.class);
                    }
                    else if (ArrayUtils.contains(FONT_EXTENSIONS, extension)) {
                        this.load(subpath.toString(), BitmapFont.class);
                    }
                    else if (ArrayUtils.contains(ATLAS_EXTENSIONS, extension)) {
                        this.load(subpath.toString(), TextureAtlas.class);
                    }
                    else if (ArrayUtils.contains(SKIN_EXTENSIONS, extension)) {
                        this.load(subpath.toString(), Skin.class);
                    }
                    else if (ArrayUtils.contains(TEXTURE_EXTENTIONS, extension)) {
                        this.load(subpath.toString(), Texture.class);
                    }
                }
            });
        }
        catch (IOException ex) {
            
        }
    }

}
