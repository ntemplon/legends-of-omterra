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
package com.emergence.io;

import java.io.File;
import java.nio.file.Paths;

/**
 * A static enumeration of file locations for Legends of Omterra
 *
 * @author Nathan Templon
 */
public final class FileLocations {

    // Constants
    public static final File ASSET_DIRECTORY = Paths.get("./data").toFile();
    
    public static final File WORLD_DIRECTORY = new File(ASSET_DIRECTORY, "worlds");
    
    public static final File UI_DIRECTORY = new File(ASSET_DIRECTORY, "ui");
    public static final File FONTS_DIRECTORY = new File(UI_DIRECTORY, "fonts");
    public static final File SKINS_DIRECTORY = new File(UI_DIRECTORY, "skins");
    
    public static final File GRAPHICS_DIRECTORY = new File(ASSET_DIRECTORY, "graphics");
    public static final File SPRITES_DIRECTORY = new File(GRAPHICS_DIRECTORY, "sprites");
    
    public static final File AUDIO_DIRECTORY = new File(ASSET_DIRECTORY, "audio");
    
    private static final File USER_HOME = new File(System.getProperty("user.home"));
    private static final File LOCAL_DATA_DIRECTORY = new File(USER_HOME, ".emergence");
    public static final File SAVE_DIRECTORY = new File(LOCAL_DATA_DIRECTORY, "saves");
    public static final File CONFIGURATION_DIRECTORY = new File(LOCAL_DATA_DIRECTORY, "config");
    
    
    

    // Initialization
    //   Private constructor prevents instances of this class from being instantiated
    private FileLocations() {

    }

}
