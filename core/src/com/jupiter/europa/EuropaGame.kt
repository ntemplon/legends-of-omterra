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

package com.jupiter.europa

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.*
import com.badlogic.gdx.ai.fsm.StackStateMachine
import com.badlogic.gdx.ai.fsm.State
import com.badlogic.gdx.ai.msg.Telegram
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue.PrettyPrintSettings
import com.badlogic.gdx.utils.JsonWriter.OutputType
import com.jupiter.europa.audio.AudioService
import com.jupiter.europa.audio.LocalAudioService
import com.jupiter.europa.entity.*
import com.jupiter.europa.entity.messaging.MessageSystem
import com.jupiter.europa.entity.messaging.SelfSubscribingListener
import com.jupiter.europa.entity.messaging.SimpleMessageSystem
import com.jupiter.europa.io.EmergenceAssetManager
import com.jupiter.europa.io.FileLocations
import com.jupiter.europa.io.FileUtils
import com.jupiter.europa.save.SaveGame
import com.jupiter.europa.screen.LevelScreen
import com.jupiter.europa.screen.LoadingScreen
import com.jupiter.europa.screen.MainMenuScreen
import com.jupiter.europa.settings.Settings
import com.jupiter.europa.util.GameTimer
import com.jupiter.europa.util.Initializable
import com.jupiter.europa.world.Level
import com.jupiter.europa.world.World
import com.jupiter.ganymede.event.Listener
import java.awt.Insets
import java.awt.Point
import java.io.IOException
import java.nio.file.Files
import java.util.HashMap
import java.util.TreeSet
import javax.swing.JFrame

public class EuropaGame// Initialization
private constructor() : Game(), InputProcessor {


    // Enumerations
    public enum class GameStates : State<EuropaGame> {

        LOADING {

            // State Imiplementation
            override fun enter(game: EuropaGame) {
                game.setScreen(game.loadingScreen)
            }

            override fun update(game: EuropaGame) {
            }

            override fun exit(game: EuropaGame) {
            }

            override fun onMessage(game: EuropaGame, telegram: Telegram): Boolean {
                return false
            }

        },
        MAIN_MENU {

            override fun enter(game: EuropaGame) {
                // Save the Game, if necessary
                if (game.save != null) {
                    game.saveGame()
                }

                game.setScreen(game.mainMenuScreen)
                game.addInputProcessor(game.mainMenuScreen!!)

                // Remove old party
                if (game.party != null) {
                    for (entity in game.party!!.getActivePartyMembers()) {
                        game.currentLevel!!.entityLayer!!.removeEntity(entity)
                    }
                }
            }

            override fun update(e: EuropaGame) {
            }

            override fun exit(game: EuropaGame) {
                game.removeInputProcessor(game.mainMenuScreen!!)
            }

            override fun onMessage(e: EuropaGame, tlgrm: Telegram): Boolean {
                return false
            }

        },
        LEVEL {

            override fun enter(game: EuropaGame) {
                game.levelScreen!!.setLevel(game.currentLevel!!)
                game.setScreen(game.levelScreen)
                game.addInputProcessor(game.levelScreen!!)
            }

            override fun update(game: EuropaGame) {
            }

            override fun exit(game: EuropaGame) {
                game.removeInputProcessor(game.levelScreen!!)
            }

            override fun onMessage(game: EuropaGame, tlgrm: Telegram): Boolean {
                return false
            }

        },
        PAUSED {

            override fun enter(e: EuropaGame) {
                throw UnsupportedOperationException("Not supported yet.") //To change body of generated methods, choose Tools | Templates.
            }

            override fun update(e: EuropaGame) {
                throw UnsupportedOperationException("Not supported yet.") //To change body of generated methods, choose Tools | Templates.
            }

            override fun exit(e: EuropaGame) {
                throw UnsupportedOperationException("Not supported yet.") //To change body of generated methods, choose Tools | Templates.
            }

            override fun onMessage(e: EuropaGame, tlgrm: Telegram): Boolean {
                throw UnsupportedOperationException("Not supported yet.") //To change body of generated methods, choose Tools | Templates.
            }

        }

    }


    // Fields
    public val entityEngine: Engine = Engine() // Ashley entity framework engine
    public val lastIdMapping: MutableMap<Long, Entity> = HashMap()

    private val timer = GameTimer()

    private var frame: JFrame? = null

    private var levelScreen: LevelScreen? = null
    private var loadingScreen: Screen? = null
    private var mainMenuScreen: MainMenuScreen? = null
    private var worlds: MutableMap<String, World>? = null
    private val inputMultiplexer = InputMultiplexer()
    public var audioService: AudioService? = null
        private set

    public var currentWorld: World? = null
        private set
    public var currentLevel: Level? = null
        private set
    private val stateMachine: StackStateMachine<EuropaGame>

    public var assetManager: EmergenceAssetManager? = null
        private set

    public val messageSystem: MessageSystem = SimpleMessageSystem()
    private val movementSystem = MovementSystem()
    private val collisionSystem = CollisionSystem()
    private val effectsSystem = EffectsSystem()
    private val renderingSystem = RenderingMaintenanceSystem()

    private var suspended: Boolean = false

    private var save: SaveGame? = null
    public var settings: Settings? = null
        private set
    public var credits: String = ""
        private set
    private val saveNames = TreeSet<String>()
    public var party: Party? = null
        private set

    public fun setWorld(world: World) {
        this.currentWorld = world
    }

    public fun setLevel(level: Level) {
        this.currentLevel = level

        for (entity in this.party!!.getActivePartyMembers()) {
            this.currentLevel!!.entityLayer!!.addEntity(entity)
        }

        this.currentLevel!!.controlledEntity = this.party!!.getActivePartyMembers()[0]
    }

    public fun isSuspended(): Boolean {
        return this.suspended
    }

    public fun getWorld(name: String): World {
        return this.worlds!!.get(name.toUpperCase())!!
    }

    public fun getSaveNames(): Array<String> {
        return this.saveNames.toArray<String>(arrayOfNulls<String>(this.saveNames.size()))
    }

    public fun getWorldNames(): Array<String> {
        return this.worlds!!.keySet().toTypedArray()
    }

    public fun getContainingInsets(): Insets {
        if (this.frame != null) {
            return this.frame!!.getInsets()
        }
        return Insets(0, 0, 0, 0)
    }

    public fun getFrameLocation(): Point {
        if (this.frame != null) {
            return this.frame!!.getLocationOnScreen()
        }
        return Point(0, 0)
    }

    public fun setContainingFrame(frame: JFrame) {
        this.frame = frame
    }


    init {
        this.stateMachine = StackStateMachine(this)

        // Add systems to the engine
        this.movementSystem.priority = MOVEMENT_SYSTEM_PRIORITY
        this.collisionSystem.priority = COLLISION_SYSTEM_PRIORITY
        this.renderingSystem.priority = RENDERING_SYSTEM_PRIORITY
        this.effectsSystem.priority = EFFECTS_SYSTEM_PRIORITY

        this.entityEngine.addSystem(this.movementSystem)
        this.entityEngine.addSystem(this.collisionSystem)
        this.entityEngine.addSystem(this.renderingSystem)
        this.entityEngine.addSystem(this.effectsSystem)

        for (system in this.entityEngine.getSystems()) {
            if (system is SelfSubscribingListener) {
                system.subscribe(this.entityEngine, this.messageSystem)
            }
        }
    }


    // Public Methods
    public fun setState(state: GameStates) {
        this.stateMachine.changeState(state)
    }

    public fun loadWorldData() {
        this.worlds = HashMap<String, World>()

        val worldsDir = FileLocations.WORLD_DIRECTORY

        if (!Files.exists(worldsDir) || !Files.isDirectory(worldsDir)) {
            // This is problematic
            return
        }

        // At this point, we know that the worlds directory exists
        try {
            Files.newDirectoryStream(worldsDir).use { paths ->
                paths.forEach { path ->
                    try {
                        val nextWorld = World.fromDirectory(path)!!
                        this.worlds?.put(nextWorld.name!!.toUpperCase(), nextWorld)
                    } catch (ex: Exception) {

                    }
                }
            }
        } catch (ex: IOException) {
        }

    }

    public fun startGame(save: SaveGame) {
        this.save = save
        this.party = this.save!!.party

        game.setWorld(this.save!!.world!!) // The first world in the list
        game.setLevel(game.currentWorld!!.getStartingLevel()!!)

        this.entityEngine.removeAllEntities()
        for (entity in party!!.getActivePartyMembers()) {
            this.entityEngine.addEntity(entity)
        }

        for (system in this.entityEngine.getSystems()) {
            if (system is Initializable) {
                system.initialize()
            }
        }

        this.setState(GameStates.LEVEL)
    }

    public fun startGame(name: String, world: World, party: Party) {
        this.startGame(SaveGame(name, world, party))
    }

    public fun addInputProcessor(processor: InputProcessor) {
        this.inputMultiplexer.addProcessor(processor)
    }

    public fun removeInputProcessor(processor: InputProcessor) {
        this.inputMultiplexer.removeProcessor(processor)
    }

    /**
     * When a pause button is pressed - the actual pause() function is used for application close in desktop libGDX
     */
    public fun suspend() {
        this.suspended = true
        this.timer.pause()
    }

    /**
     * Unpause - see suspend()
     */
    public fun wake() {
        this.suspended = false
        this.timer.resume()
    }

    public fun inspectSaves() {
        this.saveNames.clear()

        try {
            if (!Files.exists(FileLocations.SAVE_DIRECTORY)) {
                Files.createDirectories(FileLocations.SAVE_DIRECTORY)
            }
        } catch (ex: IOException) {
        }

        try {
            Files.newDirectoryStream(FileLocations.SAVE_DIRECTORY).use { paths ->
                paths.forEach { saveFile ->
                    val ext = FileUtils.getExtension(saveFile)
                    if (ext.equals(SaveGame.SAVE_EXTENSION)) {
                        this.saveNames.add(saveFile.getFileName().toString().replace("." + ext, ""))
                    }
                }
            }
        } catch (ex: IOException) {
        }

    }

    public fun deleteSave(save: String) {
        val saveFile = FileLocations.SAVE_DIRECTORY.resolve(save + "." + SaveGame.SAVE_EXTENSION)

        try {
            Files.deleteIfExists(saveFile)
        } catch (ex: IOException) {
        }


        this.inspectSaves()
    }

    public fun saveSettings() {
        if (!Files.exists(SETTINGS_FILE)) {
            try {
                Files.createDirectories(SETTINGS_FILE.getParent())
            } catch (ex: IOException) {
            }

        }

        try {
            Files.newBufferedWriter(SETTINGS_FILE).use { bw ->
                val json = Json()
                bw.write(json.prettyPrint(this.settings, PRINT_SETTINGS) + System.lineSeparator())
                bw.flush()
            }
        } catch (ex: IOException) {
        }

    }


    // ApplicationAdapter Implementation
    override fun create() {
        // Load Resources
        this.assetManager = EmergenceAssetManager() // The loading screen will take care of actually loading the resources
        this.audioService = LocalAudioService()
        this.inspectSaves()
        this.loadCredits()
        this.loadSettings()

        // Configure Property Change listeners
        this.settings!!.musicVolume.addPropertyChangedListener(Listener { args -> this.audioService!!.setMusicVolume(args.newValue) })

        // Create our various screens
        this.loadingScreen = LoadingScreen(this)
        this.mainMenuScreen = MainMenuScreen()
        this.levelScreen = LevelScreen()

        // Set the state machine - be careful to not do this until all screens
        //  are initialized!
        this.setState(GameStates.LOADING)

        // Take Over Input
        Gdx.input.setInputProcessor(this.inputMultiplexer)
        this.addInputProcessor(this)

        // Start the clock
        this.timer.start()
    }

    override fun render() {
        val deltaT = timer.tick()

        if (!this.isSuspended()) {
            this.messageSystem.update(true)
            this.entityEngine.update(deltaT)
        }

        super<Game>.render()
    }

    /**
     * Called right before dispose() when the game is closing - I should save the game, etc. here
     */
    override fun pause() {
        this.saveGame()
        this.saveSettings()
    }

    override fun dispose() {
        if (this.loadingScreen != null) {
            this.loadingScreen!!.dispose()
        }
        if (this.mainMenuScreen != null) {
            this.mainMenuScreen!!.dispose()
        }
        if (this.levelScreen != null) {
            this.levelScreen!!.dispose()
        }
        if (this.worlds != null) {
            for (world in worlds?.values() ?: setOf<World>()) {
                world.dispose()
            }
        }
        if (this.assetManager != null) {
            this.assetManager!!.dispose()
        }
    }


    // InputProcessorImplementation
    override fun keyDown(i: Int): Boolean {
        return false
    }

    override fun keyUp(i: Int): Boolean {
        return false
    }

    override fun keyTyped(c: Char): Boolean {
        return false
    }

    override fun touchDown(i: Int, i1: Int, i2: Int, i3: Int): Boolean {
        return false
    }

    override fun touchUp(i: Int, i1: Int, i2: Int, i3: Int): Boolean {
        return false
    }

    override fun touchDragged(i: Int, i1: Int, i2: Int): Boolean {
        return false
    }

    override fun mouseMoved(i: Int, i1: Int): Boolean {
        return false
    }

    override fun scrolled(i: Int): Boolean {
        return false
    }


    // Private Methods
    private fun saveGame() {
        if (this.save == null) {
            return
        }

        val savePath = FileLocations.SAVE_DIRECTORY.resolve(this.save!!.name + "." + SaveGame.SAVE_EXTENSION)
        val json = Json()
        val text = json.prettyPrint(this.save, PRINT_SETTINGS)

        if (!Files.exists(FileLocations.SAVE_DIRECTORY)) {
            try {
                Files.createDirectories(FileLocations.SAVE_DIRECTORY)
            } catch (ex: IOException) {
            }

        }

        try {
            Files.newBufferedWriter(savePath).use { bw ->
                bw.write(text + System.lineSeparator())
                bw.flush()
            }
        } catch (ex: IOException) {
        }

    }

    private fun loadCredits() {
        val creditsFile = FileLocations.ASSET_DIRECTORY.resolve("credits.txt")

        try {
            Files.newBufferedReader(creditsFile).use { br ->
                this.credits = "Credits:" + System.lineSeparator()
                br.lines().forEach { line ->
                    this.credits += line + System.lineSeparator()
                }
            }
        } catch (ex: IOException) {
            this.credits = ""
        }

    }

    private fun loadSettings() {
        val json = Json()
        try {
            Files.newBufferedReader(SETTINGS_FILE).use { reader ->
                val value = JsonReader().parse(reader)

                this.settings = json.fromJson(javaClass<Settings>(), value.toString())
            }
        } catch (ex: Exception) {
            this.settings = Settings()
            this.saveSettings()
        }


        // Load the settings to their respective systems
        this.audioService!!.setMusicVolume(this.settings!!.musicVolume.get())
    }

    companion object {

        // Constants
        public val TITLE: String = "Legends of Omterra"
        public val SUBTITLE: String = "Necromancer Rising"
        public val MAJOR_VERSION: Int = 0
        public val MINOR_VERSION: Int = 1
        public val REVISION: Int = 0
        public val VERSION: String = MAJOR_VERSION.toString() + "." + MINOR_VERSION + "." + REVISION

        public val SCALE: Float = 2.0f // The number of pixels on-screen for each pixel in the resource

        public val MOVEMENT_SYSTEM_PRIORITY: Int = 1
        public val COLLISION_SYSTEM_PRIORITY: Int = 9
        public val RENDERING_SYSTEM_PRIORITY: Int = 10
        public val EFFECTS_SYSTEM_PRIORITY: Int = 20

        public val MOVE_RIGHT_KEY: Char = 'd'
        public val MOVE_LEFT_KEY: Char = 'a'
        public val MOVE_UP_KEY: Char = 'w'
        public val MOVE_DOWN_KEY: Char = 's'

        private val SETTINGS_FILE = FileLocations.CONFIGURATION_DIRECTORY.resolve("settings.cfg")

        public val DEBUG: Boolean = true


        // Static Fields
        public val game: EuropaGame = EuropaGame()
        public val PRINT_SETTINGS: PrettyPrintSettings = PrettyPrintSettings()

        init {
            PRINT_SETTINGS.outputType = OutputType.json
        }
    }

}
