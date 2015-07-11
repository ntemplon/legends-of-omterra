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

package com.jupiter.europa.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.tools.AtlasPacker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DesktopLauncher {

    // Constants
    public static final boolean RESIZEABLE = true;


    // Static Methods
    public static String getWindowTitle() {
        return EuropaGame.TITLE + " " + EuropaGame.VERSION;
    }


    // Main Method
    public static void main(String[] arg) {
        // Debug code
        AtlasPacker packer = new AtlasPacker();
        packer.run();
        
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = DesktopLauncher.getWindowTitle();
        config.resizable = RESIZEABLE;
        config.vSyncEnabled = false;

        final EuropaGame game = EuropaGame.game;
        
        LwjglFrame frame = new LwjglFrame(game, config);

        frame.setMinimumSize(new Dimension(1024, 720));
        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Gdx.app.exit();
            }
        });
        
        game.setContainingFrame(frame);
    }
}
