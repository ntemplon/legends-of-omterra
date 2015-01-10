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

import com.badlogic.gdx.files.FileHandle;
import java.io.File;
import java.nio.file.Path;

/**
 *
 * @author Nathan Templon
 */
public final class FileUtils {
    
    // Static Methods
    public static String getExtension(String filePath) {
        int periodIndex = filePath.lastIndexOf(".");
        int folderIndex = Math.max(filePath.lastIndexOf("/"), filePath.lastIndexOf("\\")); // "/" for linux, "\\" for windows
        
        if (periodIndex < 0 || (folderIndex > periodIndex)) {
            return "";
        }
        
        return filePath.substring(periodIndex + 1);
    }
    
    public static String getExtension(File file) {
        return getExtension(file.getPath());
    }
    
    public static String getExtension(FileHandle handle) {
        return getExtension(handle.path());
    }
    
    public static String getExtension(Path path) {
        return getExtension(path.toString());
    }
    
    public static String combine(String firstPath, String secondPath) {
        return new File(new File(firstPath), secondPath).getPath();
    }
    
    public static String crossPlatformFilePath(String filePath) {
        return filePath.replace("\\", "/");
    }
    
    
    // Initialization
    //  Private constructor to prevent instances
    private FileUtils() {
        
    }
    
}
