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

package com.jupiter.europa.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20.GL_COLOR_BUFFER_BIT
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.jupiter.europa.EuropaGame
import com.jupiter.europa.io.EmergenceAssetManager
import com.jupiter.europa.threading.NotifyingThread
import com.jupiter.ganymede.event.Listener
import java.text.DecimalFormat

/**

 * @author Nathan Templon
 */
public class LoadingScreen// Initialization
(private val game: EuropaGame // The game that we will wait to load data
) : Screen {


    // Fields
    private val camera: OrthographicCamera // The camera for viewing the map
    private val assetManager: EmergenceAssetManager // The asset manager who we will wait to load all assets
    private val layout = GlyphLayout()

    private var loadTime: Long = 0 // The time at which we started loading assets
    private var startedDataLoadingThread = false // Whether or not the data loading thread has started yet
    private var loadingComplete = false // True if all loading is completed
    private var dataLoadingThread: NotifyingThread? = null // The thread that handles the game loading data, separate from the assets.

    private val format = DecimalFormat("##")
    private val batch = SpriteBatch()
    private val font = BitmapFont()


    init {
        this.camera = OrthographicCamera(640f, 480f)
        this.assetManager = game.assetManager!!
    }


    // Screen implementation
    override fun render(delta: Float) {
        // OpenGL code to clear the screen
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f) // Clear black
        Gdx.gl.glClear(GL_COLOR_BUFFER_BIT)

        val progress = this.assetManager.getProgress()

        if (!this.startedDataLoadingThread && this.assetManager.update()) {
            this.dataLoadingThread = NotifyingThread(Runnable { this.game.loadWorldData() })
            this.dataLoadingThread!!.addThreadCompleteListener(Listener { args -> this.loadingComplete = true })
            this.dataLoadingThread!!.start()
            this.startedDataLoadingThread = true
        }

        if (loadingComplete && this.minimumTimeElapsed()) {
            this.game.setState(EuropaGame.GameStates.MAIN_MENU)
            this.dispose()
        }

        // Rendering code
        batch.setProjectionMatrix(camera.combined)

        batch.begin()

        val message = "Loading: " + this.format.format((progress * 100).toDouble()) + "%"
        this.layout.setText(this.font, message)
        this.font.draw(batch, message, -1 * (this.layout.width) / (2.0f * this.font.getScaleX()), (this.layout.height / (2.0f * this.font.getScaleY())))

        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        this.camera.viewportWidth = width.toFloat()
        this.camera.viewportHeight = height.toFloat()
        this.camera.update()
    }

    override fun show() {
        this.assetManager.loadInternalResources()
        this.loadTime = System.nanoTime()
    }

    override fun hide() {
    }

    override fun pause() {
    }

    override fun resume() {
    }

    override fun dispose() {
    }


    // Private Methods
    private fun minimumTimeElapsed(): Boolean {
        return (System.nanoTime() - this.loadTime) / 1000000 > MINIMUM_LOADING_TIME
    }

    companion object {

        // Constants
        public val MINIMUM_LOADING_TIME: Int = 250 // Minimum time that the loading screen will be displayed, in ms.
    }

}
