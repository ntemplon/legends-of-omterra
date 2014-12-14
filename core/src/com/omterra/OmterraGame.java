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
import com.omterra.io.OmterraAssetManager;
import com.omterra.screen.LevelScreen;
import com.omterra.screen.LoadingScreen;
import com.omterra.screen.TestScreen;
import com.omterra.world.Level;
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
    public static final String DEBUG_LEVEL = "Worlds/Omterra/Levels/TestLevel.tmx";


    // Enumerations
    public enum GameStates implements State<OmterraGame> {

        LOADING() {

                    // State Imiplementation
                    @Override
                    public void enter(OmterraGame game) {
                        game.setScreen(game.loadingScreen);
                    }

                    @Override
                    public void update(OmterraGame game) {
                        
                    }

                    @Override
                    public void exit(OmterraGame game) {
                        
                    }

                    @Override
                    public boolean onMessage(OmterraGame game, Telegram telegram) {
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
                    public void enter(OmterraGame game) {
                        // debug code only
                        game.setWorld(game.worlds.get(0));
                        game.setLevel(game.getCurrentWorld().getStartingLevel());

                        game.levelScreen.setLevel(game.getCurrentLevel());
                        game.setScreen(game.levelScreen);
                    }

                    @Override
                    public void update(OmterraGame game) {

                    }

                    @Override
                    public void exit(OmterraGame game) {

                    }

                    @Override
                    public boolean onMessage(OmterraGame game, Telegram tlgrm) {
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
    private Screen loadingScreen;
    private List<World> worlds;

    private World currentWorld;
    private Level currentLevel;
    private final StackStateMachine<OmterraGame> stateMachine;

    private OmterraAssetManager assetManager;


    // Properties
    public OmterraAssetManager getAssetManager() {
        return this.assetManager;
    }

    public World getCurrentWorld() {
        return this.currentWorld;
    }

    public Level getCurrentLevel() {
        return this.currentLevel;
    }


    // Initialization
    public OmterraGame() {
        this.stateMachine = new StackStateMachine<>(this);
    }


    // Public Methods
    public void setWorld(World world) {
        this.currentWorld = world;
    }

    public void setLevel(Level level) {
        this.currentLevel = level;
    }

    public void setState(GameStates state) {
        this.stateMachine.changeState(state);
    }

    public void loadWorldData() {
        this.worlds = new ArrayList<>();

        File worldsDir = FileLocations.WORLD_DIRECTORY;

        if (!worldsDir.exists() || !worldsDir.isDirectory()) {
            // This is problematic
            return;
        }

        // At this point, we know that the worlds directory exists
        for (File file : worldsDir.listFiles()) {
            if (file.isDirectory()) {
                try {
                    this.worlds.add(World.fromDirectory(file, this.assetManager));
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


    // ApplicationAdapter Implementation
    @Override
    public void create() {
        // Load Resources
        this.assetManager = new OmterraAssetManager();
//        this.assetManager.loadInternalResources();
//        this.assetManager.finishLoading();

        // Load the static World data (including levels) from the disk
//        this.loadWorldData();
        // Create our various screens
        this.loadingScreen = new LoadingScreen(this);
        this.testScreen = new TestScreen(640, 480);
        this.levelScreen = new LevelScreen();

        // Debug only code - load default world and level
//        this.setWorld(this.worlds.get(0));
//        this.setLevel(this.getCurrentWorld().getStartingLevel());
        
        // Set the state machine - be careful to not do this until all screens
        //  are initialized!
//        this.setState(GameStates.LEVEL);
        this.setState(GameStates.LOADING);
    }

    @Override
    public void render() {
        super.render();
    }

    /**
     * Called right before dispose() when the game is closing - I should save the game, etc. here
     */
    @Override
    public void pause() {

    }

    @Override
    public void dispose() {
        if (this.testScreen != null) {
            this.testScreen.dispose();
        }
        if (this.levelScreen != null) {
            this.levelScreen.dispose();
        }
        if (this.worlds != null) {
            for (World world : this.worlds) {
                world.dispose();
            }
        }
        if (this.assetManager != null) {
            this.assetManager.dispose();
        }
    }
}
