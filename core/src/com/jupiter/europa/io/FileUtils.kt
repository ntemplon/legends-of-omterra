/*
 * The MIT License
 *
 * Copyright 2015 Nathan Templon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
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
 *
 */

package com.jupiter.europa.io

import com.badlogic.gdx.files.FileHandle
import java.io.File
import java.nio.file.Path

/**

 * @author Nathan Templon
 */
public object FileUtils {

    // Static Methods
    public fun getExtension(filePath: String): String {
        val periodIndex = filePath.lastIndexOf(".")
        val folderIndex = Math.max(filePath.lastIndexOf("/"), filePath.lastIndexOf("\\")) // "/" for linux, "\\" for windows

        if (periodIndex < 0 || (folderIndex > periodIndex)) {
            return ""
        }

        return filePath.substring(periodIndex + 1)
    }

    public fun getExtension(file: File): String {
        return getExtension(file.getPath())
    }

    public fun getExtension(handle: FileHandle): String {
        return getExtension(handle.path())
    }

    public fun getExtension(path: Path): String {
        return getExtension(path.toString())
    }

    public fun combine(firstPath: String, secondPath: String): String {
        return File(File(firstPath), secondPath).getPath()
    }

    public fun crossPlatformFilePath(filePath: String): String {
        return filePath.replace("\\", "/")
    }

}
