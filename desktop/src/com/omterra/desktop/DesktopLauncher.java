package com.omterra.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.omterra.EmergenceGame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

public class DesktopLauncher {

    // Constants
    public static final boolean RESIZEABLE = true;


    // Static Methods
    public static String getWindowTitle() {
        return EmergenceGame.TITLE + " " + EmergenceGame.VERSION;
    }


    // Main Method
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = DesktopLauncher.getWindowTitle();
        config.resizable = RESIZEABLE;

        final EmergenceGame game = EmergenceGame.game;
        
        LwjglFrame frame = new LwjglFrame(game, config);

        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game.pause();
                game.dispose();
            }
        });
    }
}
