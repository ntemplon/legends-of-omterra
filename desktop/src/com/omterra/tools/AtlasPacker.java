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
package com.omterra.tools;

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.omterra.io.FileLocations;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 *
 * @author Nathan Templon
 */
public class AtlasPacker implements Runnable {

    // Constants
    public static final String CONFIGURATION_FILE = "atlas-packer-config.json";


    // Initialization
    public AtlasPacker() {

    }


    // Public Methods
    @Override
    public void run() {
        File configFile = new File(FileLocations.SPRITES_DIRECTORY, CONFIGURATION_FILE);
        JsonReader reader = new JsonReader();
        String contents = "";

        try {
            contents = new String(Files.readAllBytes(configFile.toPath()), StandardCharsets.UTF_8);
        }
        catch (IOException ex) {
            System.out.println("Atlas building failed.");
            return;
        }

        JsonValue value = reader.parse(contents);
        JsonValue atlasList = value.get("atlas");

        for (int i = 0; i < atlasList.size; i++) {
            JsonValue currentAtlas = atlasList.get(i);
            String fileName = currentAtlas.getString("name");
            String imageName = currentAtlas.getString("image");
            int size = currentAtlas.getInt("size");
            String[] rows = currentAtlas.get("rows").asStringArray();
            String[] columns = currentAtlas.get("columns").asStringArray();
            
//            String imageName = fileName.replace(".atlas", ".png");

            File output = new File(FileLocations.SPRITES_DIRECTORY, fileName);
            try (FileWriter fw = new FileWriter(output);
                    PrintWriter pw = new PrintWriter(fw)) {
                pw.println(imageName);
                pw.println("format: RGBA8888");
                pw.println("filter: Nearest,Nearest");
                pw.println("repeat: none");
                
                for(int row = 0; row < rows.length; row++) {
                    for(int column = 0; column < columns.length; column++) {
                        String patchName = rows[row] + "-" + columns[column];
                        int x = column * size;
                        int y = row * size;
                        
                        pw.println(patchName);
                        pw.println("  rotate: false");
                        pw.println("  xy: " + x + ", " + y);
                        pw.println("  size: " + size + ", " + size);
                        pw.println("  orig: 0, 0");
                        pw.println("  offset: 0, 0");
                        pw.println("  index: -1");
                    }
                }
            }
            catch (IOException ex) {
                System.out.println("Problem with atlas for " + fileName);
            }
        }

        System.out.println("Atlas building completed.");
    }


    public static void main(String[] args) {
        AtlasPacker packer = new AtlasPacker();
        packer.run();
    }

}
