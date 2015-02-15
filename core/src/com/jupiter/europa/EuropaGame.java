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
package com.jupiter.europa;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ai.fsm.StackStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonValue.PrettyPrintSettings;
import com.badlogic.gdx.utils.JsonWriter.OutputType;
import com.jupiter.europa.audio.AudioService;
import com.jupiter.europa.audio.LocalAudioService;
import com.jupiter.europa.entity.CollisionSystem;
import com.jupiter.europa.entity.EffectsSystem;
import com.jupiter.europa.entity.MovementSystem;
import com.jupiter.europa.entity.Party;
import com.jupiter.europa.entity.RenderingMaintenenceSystem;
import com.jupiter.europa.entity.messaging.MessageSystem;
import com.jupiter.europa.entity.messaging.SelfSubscribingListener;
import com.jupiter.europa.entity.messaging.SimpleMessageSystem;
import com.jupiter.europa.io.FileLocations;
import com.jupiter.europa.io.EmergenceAssetManager;
import com.jupiter.europa.io.FileUtils;
import com.jupiter.europa.save.SaveGame;
import com.jupiter.europa.screen.LevelScreen;
import com.jupiter.europa.screen.LoadingScreen;
import com.jupiter.europa.screen.MainMenuScreen;
import com.jupiter.europa.settings.Settings;
import com.jupiter.europa.util.GameTimer;
import com.jupiter.europa.util.Initializable;
import com.jupiter.europa.world.Level;
import com.jupiter.europa.world.World;
import com.jupiter.ganymede.property.Property.PropertyChangedArgs;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.JFrame;

public class EuropaGame extends Game implements InputProcessor {

    // Constants
    public static final String TITLE = "Legends of Omterra";
    public static final String SUBTITLE = "Necromancer Rising";
    public static final int MAJOR_VERSION = 0;
    public static final int MINOR_VERSION = 1;
    public static final int REVISION = 0;
    public static final String VERSION = MAJOR_VERSION + "." + MINOR_VERSION + "." + REVISION;

    public static final float SCALE = 2.0f; // The number of pixels on-screen for each pixel in the resource

    public static final int MOVEMENT_SYSTEM_PRIORITY = 1;
    public static final int COLLISION_SYSTEM_PRIORITY = 9;
    public static final int RENDERING_SYSTEM_PRIORITY = 10;
    public static final int EFFECTS_SYSTEM_PRIORITY = 20;

    public static final char MOVE_RIGHT_KEY = 'd';
    public static final char MOVE_LEFT_KEY = 'a';
    public static final char MOVE_UP_KEY = 'w';
    public static final char MOVE_DOWN_KEY = 's';

    private static final Path SETTINGS_FILE = FileLocations.CONFIGURATION_DIRECTORY.resolve("settings.cfg");

    public static final boolean DEBUG = true;


    // Static Fields
    public static final EuropaGame game = new EuropaGame();
    public static final PrettyPrintSettings PRINT_SETTINGS = new PrettyPrintSettings();
    static {
        PRINT_SETTINGS.outputType = OutputType.json;
    }


    // Enumerations
    public enum GameStates implements State<EuropaGame> {

        LOADING() {

                    // State Imiplementation
                    @Override
                    public void enter(EuropaGame game) {
                        game.setScreen(game.loadingScreen);
                    }

                    @Override
                    public void update(EuropaGame game) {

                    }

                    @Override
                    public void exit(EuropaGame game) {

                    }

                    @Override
                    public boolean onMessage(EuropaGame game, Telegram telegram) {
                        return false;
                    }

                },
        MAIN_MENU() {

                    @Override
                    public void enter(EuropaGame game) {
                        // Save the Game, if necessary
                        if (game.save != null) {
                            game.saveGame();
                        }

                        game.setScreen(game.mainMenuScreen);
                        game.addInputProcessor(game.mainMenuScreen);

                        // Remove old party
                        if (game.party != null) {
                            for (Entity entity : game.party.getActivePartyMembers()) {
                                game.getCurrentLevel().getEntityLayer().removeEntity(entity);
                            }
                        }
                    }

                    @Override
                    public void update(EuropaGame e) {

                    }

                    @Override
                    public void exit(EuropaGame game) {
                        game.removeInputProcessor(game.mainMenuScreen);
                    }

                    @Override
                    public boolean onMessage(EuropaGame e, Telegram tlgrm) {
                        return false;
                    }

                },
        LEVEL() {

                    @Override
                    public void enter(EuropaGame game) {
                        game.levelScreen.setLevel(game.getCurrentLevel());
                        game.setScreen(game.levelScreen);
                        game.addInputProcessor(game.levelScreen);
                    }

                    @Override
                    public void update(EuropaGame game) {

                    }

                    @Override
                    public void exit(EuropaGame game) {
                        game.removeInputProcessor(game.levelScreen);
                    }

                    @Override
                    public boolean onMessage(EuropaGame game, Telegram tlgrm) {
                        return false;
                    }

                },
        PAUSED() {

                    @Override
                    public void enter(EuropaGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void update(EuropaGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public void exit(EuropaGame e) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public boolean onMessage(EuropaGame e, Telegram tlgrm) {
                        throw new UnsupportedOperationException(
                                "Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                }

    }


    // Fields
    public final Engine entityEngine = new Engine(); // Ashley entity framework engine
    public final Map<Long, Entity> lastIdMapping = new HashMap<>();
    
    private final GameTimer timer = new GameTimer();
    
    private JFrame frame;

    private LevelScreen levelScreen;
    private Screen loadingScreen;
    private MainMenuScreen mainMenuScreen;
    private Map<String, World> worlds;
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();
    private AudioService audio;

    private World currentWorld;
    private Level currentLevel;
    private final StackStateMachine<EuropaGame> stateMachine;

    private EmergenceAssetManager assetManager;

    private final MessageSystem messageSystem = new SimpleMessageSystem();
    private final MovementSystem movementSystem = new MovementSystem();
    private final CollisionSystem collisionSystem = new CollisionSystem();
    private final EffectsSystem effectsSystem = new EffectsSystem();
    private final RenderingMaintenenceSystem renderingSystem = new RenderingMaintenenceSystem();

    private boolean suspended;

    private SaveGame save;
    private Settings settings;
    private String credits = "";
    private final Set<String> saveNames = new TreeSet<>();
    private Party party;


    // Properties
    public final EmergenceAssetManager getAssetManager() {
        return this.assetManager;
    }

    public final AudioService getAudioService() {
        return this.audio;
    }

    public final World getCurrentWorld() {
        return this.currentWorld;
    }

    public final Level getCurrentLevel() {
        return this.currentLevel;
    }

    public final void setWorld(World world) {
        this.currentWorld = world;
    }

    public final void setLevel(Level level) {
        this.currentLevel = level;

        for (Entity entity : this.party.getActivePartyMembers()) {
            this.currentLevel.getEntityLayer().addEntity(entity);
        }

        this.currentLevel.setControlledEntity(this.getParty().getActivePartyMembers()[0]);
    }

    public final MessageSystem getMessageSystem() {
        return this.messageSystem;
    }

    public final Party getParty() {
        return this.party;
    }

    public final boolean isSuspended() {
        return this.suspended;
    }

    public World getWorld(String name) {
        return this.worlds.get(name.toUpperCase());
    }

    public String[] getSaveNames() {
        return this.saveNames.toArray(new String[this.saveNames.size()]);
    }

    public String[] getWorldNames() {
        return this.worlds.keySet().toArray(new String[this.worlds.keySet().size()]);
    }

    public String getCredits() {
        return this.credits;
    }

    public Settings getSettings() {
        return this.settings;
    }
    
    public JFrame getContainingFrame() {
        return this.frame;
    }
    
    public void setContainingFrame(JFrame frame) {
        this.frame = frame;
    }


    // Initialization
    private EuropaGame() {
        this.stateMachine = new StackStateMachine<>(this);

        // Add systems to the engine
        this.movementSystem.priority = MOVEMENT_SYSTEM_PRIORITY;
        this.collisionSystem.priority = COLLISION_SYSTEM_PRIORITY;
        this.renderingSystem.priority = RENDERING_SYSTEM_PRIORITY;
        this.effectsSystem.priority = EFFECTS_SYSTEM_PRIORITY;

        this.entityEngine.addSystem(this.movementSystem);
        this.entityEngine.addSystem(this.collisionSystem);
        this.entityEngine.addSystem(this.renderingSystem);
        this.entityEngine.addSystem(this.effectsSystem);

        for (EntitySystem system : this.entityEngine.getSystems()) {
            if (system instanceof SelfSubscribingListener) {
                ((SelfSubscribingListener) system).subscribe(this.entityEngine, this.getMessageSystem());
            }
        }
    }


    // Public Methods
    public void setState(GameStates state) {
        this.stateMachine.changeState(state);
    }

    public void loadWorldData() {
        this.worlds = new HashMap<>();

        Path worldsDir = FileLocations.WORLD_DIRECTORY;

        if (!Files.exists(worldsDir) || !Files.isDirectory(worldsDir)) {
            // This is problematic
            return;
        }

        // At this point, we know that the worlds directory exists
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(worldsDir)) {
            paths.iterator().forEachRemaining((Path path) -> {
                if (Files.isDirectory(path)) {
                    try {
                        World nextWorld = World.fromDirectory(path);
                        this.worlds.put(nextWorld.getName().toUpperCase(), nextWorld);
                    }
                    catch (Exception ex) {
                        if (DEBUG) {
                            System.out.println(
                                    "Trouble loading world: " + path.toString() + "\n\t" + ex.getLocalizedMessage());
                            System.out.println(Arrays.toString(ex.getStackTrace()));
                        }
                    }
                }
            });
        }
        catch (IOException ex) {

        }
    }

    public void startGame(SaveGame save) {
        this.save = save;
        this.party = this.save.getParty();

        game.setWorld(this.save.getWorld()); // The first world in the list
        game.setLevel(game.getCurrentWorld().getStartingLevel());

        this.entityEngine.removeAllEntities();
        for (Entity entity : party.getActivePartyMembers()) {
            this.entityEngine.addEntity(entity);
        }

        for (EntitySystem system : this.entityEngine.getSystems()) {
            if (system instanceof Initializable) {
                ((Initializable) system).initialize();
            }
        }

        this.setState(GameStates.LEVEL);
    }

    public void startGame(String name, World world, Party party) {
        this.startGame(new SaveGame(name, world, party));
    }

    public void addInputProcessor(InputProcessor processor) {
        this.inputMultiplexer.addProcessor(processor);
    }

    public void removeInputProcessor(InputProcessor processor) {
        this.inputMultiplexer.removeProcessor(processor);
    }

    /**
     * When a pause button is pressed - the actual pause() function is used for application close in desktop libGDX
     */
    public void suspend() {
        this.suspended = true;
        this.timer.pause();
    }

    /**
     * Unpause - see suspend()
     */
    public void wake() {
        this.suspended = false;
        this.timer.resume();
    }

    public void inspectSaves() {
        this.saveNames.clear();

        try {
            if (!Files.exists(FileLocations.SAVE_DIRECTORY)) {
                Files.createDirectories(FileLocations.SAVE_DIRECTORY);
            }
        }
        catch (IOException ex) {

        }
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(FileLocations.SAVE_DIRECTORY)) {
            paths.iterator().forEachRemaining((Path saveFile) -> {
                String ext = FileUtils.getExtension(saveFile);
                if (ext.equals(SaveGame.SAVE_EXTENSION)) {
                    this.saveNames.add(saveFile.getFileName().toString().replace("." + ext, ""));
                }
            });
        }
        catch (IOException ex) {

        }
    }

    public void deleteSave(String save) {
        Path saveFile = FileLocations.SAVE_DIRECTORY.resolve(save + "." + SaveGame.SAVE_EXTENSION);

        try {
            Files.deleteIfExists(saveFile);
        }
        catch (IOException ex) {

        }

        this.inspectSaves();
    }

    public void saveSettings() {
        if (!Files.exists(SETTINGS_FILE)) {
            try {
                Files.createDirectories(SETTINGS_FILE.getParent());
            }
            catch (IOException ex) {

            }
        }

        try (BufferedWriter bw = Files.newBufferedWriter(SETTINGS_FILE)) {
            Json json = new Json();
            bw.write(json.prettyPrint(this.settings, PRINT_SETTINGS) + System.lineSeparator());
            bw.flush();
        }
        catch (IOException ex) {

        }
    }


    // ApplicationAdapter Implementation
    @Override
    public void create() {
        // Load Resources
        this.assetManager = new EmergenceAssetManager(); // The loading screen will take care of actually loading the resources
        this.audio = new LocalAudioService();
        this.inspectSaves();
        this.loadCredits();
        this.loadSettings();

        // Configure Property Change listeners
        this.settings.musicVolume.addPropertyChangedListener((PropertyChangedArgs<Float> args) -> {
            this.audio.setMusicVolume(args.newValue);
        });

        // Create our various screens
        this.loadingScreen = new LoadingScreen(this);
        this.mainMenuScreen = new MainMenuScreen();
        this.levelScreen = new LevelScreen();

        // Set the state machine - be careful to not do this until all screens
        //  are initialized!
        this.setState(GameStates.LOADING);

        // Take Over Input
        Gdx.input.setInputProcessor(this.inputMultiplexer);
        this.addInputProcessor(this);

        // Start the clock
        this.timer.start();
    }

    @Override
    public void render() {
        float deltaT = timer.tick();

        if (!this.isSuspended()) {
            this.messageSystem.update(true);
            this.entityEngine.update(deltaT);
        }

        super.render();
    }

    /**
     * Called right before dispose() when the game is closing - I should save the game, etc. here
     */
    @Override
    public void pause() {
        this.saveGame();
        this.saveSettings();
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


    // InputProcessorImplementation
    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(int i) {
        return false;
    }


    // Private Methods
    private void saveGame() {
        if (this.save == null) {
            return;
        }

        Path savePath = FileLocations.SAVE_DIRECTORY.resolve(this.save.getName() + "." + SaveGame.SAVE_EXTENSION);
        Json json = new Json();
        String text = json.prettyPrint(this.save, PRINT_SETTINGS);

        if (!Files.exists(FileLocations.SAVE_DIRECTORY)) {
            try {
                Files.createDirectories(FileLocations.SAVE_DIRECTORY);
            }
            catch (IOException ex) {

            }
        }

        try (BufferedWriter bw = Files.newBufferedWriter(savePath)) {
            bw.write(text + System.lineSeparator());
            bw.flush();
        }
        catch (IOException ex) {

        }
    }

    private void loadCredits() {
        Path creditsFile = FileLocations.ASSET_DIRECTORY.resolve("credits.txt");

        try (BufferedReader br = Files.newBufferedReader(creditsFile)) {
            this.credits = "Credits:" + System.lineSeparator();
            br.lines().forEach((String line) -> {
                this.credits += line + System.lineSeparator();
            });
        }
        catch (IOException ex) {
            this.credits = "";
        }
    }

    private void loadSettings() {
        Json json = new Json();
        try (BufferedReader reader = Files.newBufferedReader(SETTINGS_FILE)) {
            JsonValue value = new JsonReader().parse(reader);

            this.settings = json.fromJson(Settings.class, value.toString());
        }
        catch (Exception ex) {
            this.settings = new Settings();
            this.saveSettings();
        }

        // Load the settings to their respective systems
        this.audio.setMusicVolume(this.settings.musicVolume.get());
    }

}
