package com.jupiter.europa.desktop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.jupiter.europa.EuropaGame;
import com.jupiter.europa.tools.AtlasPacker;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

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
