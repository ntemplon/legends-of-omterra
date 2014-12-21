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

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.omterra.audio.AudioService;
import com.omterra.audio.LocalAudioService;
import com.omterra.entity.EmergenceEntityEngine;
import com.omterra.io.FileLocations;
import com.omterra.io.OmterraAssetManager;
import com.omterra.screen.LevelScreen;
import com.omterra.screen.LoadingScreen;
import com.omterra.screen.MainMenuScreen;
import com.omterra.world.Level;
import com.omterra.world.World;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class EmergenceGame extends Game {

    // Constants
    public static final String TITLE = "Legends of Omterra";
    public static final int MAJOR_VERSION = 0;
    public static final int MINOR_VERSION = 1;
    public static final int REVISION = 0;
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION;

    public static final float SCALE = 2.0f; // The number of pixels on-screen for each pixel in the resource

    public static final boolean DEBUG = true;
    
    
    // Static Fields
    private static final EmergenceGame instance = new EmergenceGame();
    
    
    // Static Methods
    public static EmergenceGame getGame() {
        return instance;
    }
    

    // Enumerations
    public enum GameStates implements State<EmergenceGame> {

        LOADING() {

                    // State Imiplementation
                    @Override
                    public void enter(EmergenceGame game) {
                        game.setScreen(game.loadingScreen);
                    }

                    @Override
                    public void update(EmergenceGame game) {

                    }

                    @Override
                    public void exit(EmergenceGame game) {

                    }

                    @Override
                    public boolean onMessage(EmergenceGame game, Telegram telegram) {
                        return false;
                    }

                },
        MAIN_MENU() {

                    @Override
                    public void enter(EmergenceGame game) {
                        game.setScreen(game.mainMenuScreen);
                    }

                    @Override
                    public void update(EmergenceGame e) {

                    }

                    @Override
                    public void exit(EmergenceGame e) {

                    }

                    @Override
                    public boolean onMessage(EmergenceGame e, Telegram tlgrm) {
                        return false;
                    }

                },
        LEVEL() {

                    @Override
                    public void enter(EmergenceGame game) {
                        // debug code only
                        game.setWorld(game.worlds.get(game.worlds.keySet().toArray()[0].toString())); // The first world in the list
                        game.setLevel(game.getCurrentWorld().getStartingLevel());

                        game.levelScreen.setLevel(game.getCurrentLevel());
                        game.setScreen(game.levelScreen);
                    }

                    @Override
                    public void update(EmergenceGame game) {

                    }

                    @Override
                    public void exit(EmergenceGame game) {

                    }

                    @Override
                    public boolean onMessage(EmergenceGame game, Telegram tlgrm) {
                        return false;
                    }

                },
        PAUSED() {

                    @Override
                    public void enter(EmergenceGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void update(EmergenceGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void exit(EmergenceGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public boolean onMessage(EmergenceGame e, Telegram tlgrm) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                }

    }


    // Fields
    private LevelScreen levelScreen;
    private Screen loadingScreen;
    private Screen mainMenuScreen;
    private Map<String, World> worlds;
    private final EmergenceEntityEngine engine = new EmergenceEntityEngine(); // Ashley entity framework engine
    private final AudioService audio = new LocalAudioService();

    private World currentWorld;
    private Level currentLevel;
    private final StackStateMachine<EmergenceGame> stateMachine;

    private OmterraAssetManager assetManager;


    // Properties
    public OmterraAssetManager getAssetManager() {
        return this.assetManager;
    }

    public EmergenceEntityEngine getEntityEngine() {
        return this.engine;
    }

    public AudioService getAudioService() {
        return this.audio;
    }

    public World getCurrentWorld() {
        return this.currentWorld;
    }

    public Level getCurrentLevel() {
        return this.currentLevel;
    }


    // Initialization
    private EmergenceGame() {
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
        this.worlds = new HashMap<>();

        File worldsDir = FileLocations.WORLD_DIRECTORY;

        if (!worldsDir.exists() || !worldsDir.isDirectory()) {
            // This is problematic
            return;
        }

        // At this point, we know that the worlds directory exists
        for (File file : worldsDir.listFiles()) {
            if (file.isDirectory()) {
                try {
                    World nextWorld = World.fromDirectory(file);
                    this.worlds.put(nextWorld.getName(), nextWorld);
                }
                catch (Exception ex) {
                    if (DEBUG) {
                        System.out.println(
                                "Trouble loading world: " + file.getPath() + "\n\t" + ex.getLocalizedMessage());
                        System.out.println(Arrays.toString(ex.getStackTrace()));
                    }
                }
            }
        }
    }


    // ApplicationAdapter Implementation
    @Override
    public void create() {
        // Load Resources
        this.assetManager = new OmterraAssetManager(); // The loading screen will take care of actually loading the resources

        // Load the static World data (including levels) from the disk
//        this.loadWorldData();
        // Create our various screens
        this.loadingScreen = new LoadingScreen(this);
        this.mainMenuScreen = new MainMenuScreen(this);
        this.levelScreen = new LevelScreen(this);

        // Set the state machine - be careful to not do this until all screens
        //  are initialized!
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
        if (this.loadingScreen != null) {
            this.loadingScreen.dispose();
        }
        if (this.mainMenuScreen != null) {
            this.mainMenuScreen.dispose();
        }
        if (this.levelScreen != null) {
            this.levelScreen.dispose();
        }
        if (this.worlds != null) {
            this.worlds.keySet().stream().forEach((String name) -> {
                worlds.get(name).dispose();
            });
        }
        if (this.assetManager != null) {
            this.assetManager.dispose();
        }
    }
}
