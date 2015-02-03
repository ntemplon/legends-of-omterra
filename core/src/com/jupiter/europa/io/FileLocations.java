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

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * A static enumeration of file locations for Legends of Omterra
 *
 * @author Nathan Templon
 */
public final class FileLocations {

    // Constants
    public static final Path ASSET_DIRECTORY = Paths.get("./data");

    public static final Path WORLD_DIRECTORY = ASSET_DIRECTORY.resolve("worlds");

    public static final Path UI_DIRECTORY = ASSET_DIRECTORY.resolve("ui");
    public static final Path FONTS_DIRECTORY = UI_DIRECTORY.resolve("fonts");
    public static final Path SKINS_DIRECTORY = UI_DIRECTORY.resolve("skins");
    public static final Path UI_IMAGES_DIRECTORY = UI_DIRECTORY.resolve("img");

    public static final Path GRAPHICS_DIRECTORY = ASSET_DIRECTORY.resolve("graphics");
    public static final Path SPRITES_DIRECTORY = GRAPHICS_DIRECTORY.resolve("sprites");

    public static final Path AUDIO_DIRECTORY = ASSET_DIRECTORY.resolve("audio");

    private static final Path USER_HOME = Paths.get(System.getProperty("user.home"));
    private static final Path LOCAL_DATA_DIRECTORY = USER_HOME.resolve(".emergence");
    public static final Path SAVE_DIRECTORY = LOCAL_DATA_DIRECTORY.resolve("saves");
    public static final Path CONFIGURATION_DIRECTORY = LOCAL_DATA_DIRECTORY.resolve("config");


    // Static Initialization
    static {
        Field[] fields = FileLocations.class.getDeclaredFields();
        for (Field field : fields) {
            if (File.class.isAssignableFrom(field.getType()) && java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                try {
                    File file = (File) field.get(null);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                }
                catch (IllegalArgumentException | IllegalAccessException ex) {
                    
                }
                finally {

                }
            }
        }
    }


    // Initialization
    //   Private constructor prevents instances of this class from being instantiated
    private FileLocations() {

    }

}
