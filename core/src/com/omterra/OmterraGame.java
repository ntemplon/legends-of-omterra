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
package com.omterra;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.omterra.io.FileLocations;
import com.omterra.screen.LevelScreen;
import com.omterra.screen.TestScreen;
import com.omterra.world.World;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OmterraGame extends Game {

    // Constants
    public static final String TITLE = "Legends of Omterra";
    public static final int MAJOR_VERSION = 0;
    public static final int MINOR_VERSION = 1;
    public static final int REVISION = 0;
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION;

    public static final float SCALE = 2.0f; // The number of pixels on-screen for each pixel in the resource

    public static final boolean DEBUG = true;
    public static final String DEBUG_LEVEL = "Worlds/Omterra/Levels/TestForest.tmx";


    // Enumerations
    public enum GameStates implements State<OmterraGame> {

        TEST() {

                    // State Implementation
                    @Override
                    public void enter(OmterraGame e) {
                        e.setScreen(e.testScreen);
                    }

                    @Override
                    public void update(OmterraGame e) {

                    }

                    @Override
                    public void exit(OmterraGame e) {

                    }

                    @Override
                    public boolean onMessage(OmterraGame e, Telegram tlgrm) {
                        return false;
                    }

                },
        MAIN_MENU() {

                    @Override
                    public void enter(OmterraGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void update(OmterraGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void exit(OmterraGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public boolean onMessage(OmterraGame e, Telegram tlgrm) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                },
        LEVEL() {

                    @Override
                    public void enter(OmterraGame e) {
                        e.levelScreen.setMap(OmterraGame.DEBUG_LEVEL);
                        e.setScreen(e.levelScreen);
                    }

                    @Override
                    public void update(OmterraGame e) {

                    }

                    @Override
                    public void exit(OmterraGame e) {

                    }

                    @Override
                    public boolean onMessage(OmterraGame e, Telegram tlgrm) {
                        return false;
                    }

                },
        PAUSED() {

                    @Override
                    public void enter(OmterraGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void update(OmterraGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void exit(OmterraGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public boolean onMessage(OmterraGame e, Telegram tlgrm) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                }

    }


    // Fields
    private Screen testScreen;
    private LevelScreen levelScreen;
    private List<World> worlds;

    private final StackStateMachine<OmterraGame> stateMachine;


    // Initialization
    public OmterraGame() {
        this.stateMachine = new StackStateMachine<>(this);
    }


    // ApplicationAdapter Implementation
    @Override
    public void create() {
        // Load the static World data (including levels) from the disk
        this.loadWorldData();

        // Create our various screens
        this.testScreen = new TestScreen(640, 480);
        this.levelScreen = new LevelScreen();

        // Set the state machine - be careful to not do this until all screens
        //  are initialized!
        this.stateMachine.changeState(GameStates.LEVEL);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        // Empty shell to remind me to dispose resources if I add them later
        if (this.testScreen != null) {
            this.testScreen.dispose();
        }
    }


    // Private Methods
    private void loadWorldData() {
        this.worlds = new ArrayList<>();

        File worldsDir = new File(FileLocations.WORLD_DIRECTORY);

        if (!worldsDir.exists() || !worldsDir.isDirectory()) {
            // This is problematic
            return;
        }

        // At this point, we know that the worlds directory exists
        for (File file : worldsDir.listFiles()) {
            if (file.isDirectory()) {
                try {
                    this.worlds.add(World.fromDirectory(file));
                }
                catch (Exception ex) {
                    if (DEBUG) {
                        System.out.println(
                                "Trouble loading world: " + file.getPath() + "\n\t" + ex.getLocalizedMessage());
                    }
                }
            }
        }
    }
}
