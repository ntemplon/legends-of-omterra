package com.omterra.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFrame;
import com.omterra.OmterraGame;
import javax.swing.JFrame;

public class DesktopLauncher {

    // Constants
    public static final boolean RESIZEABLE = true;


    // Static Methods
    public static String getWindowTitle() {
        return OmterraGame.TITLE + " " + OmterraGame.VERSION;
    }


    // Main Method
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = DesktopLauncher.getWindowTitle();
        config.resizable = RESIZEABLE;
//        LwjglApplication app = new LwjglApplication(new OmterraGame(), config);
        LwjglFrame frame = new LwjglFrame(new OmterraGame(), config);

        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }
}
