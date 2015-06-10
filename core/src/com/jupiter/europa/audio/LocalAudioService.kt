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
package com.jupiter.europa.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.files.FileHandle
import com.jupiter.europa.io.FileLocations
import com.jupiter.europa.io.FileUtils
import java.io.IOException
import java.nio.file.Files
import java.util.HashMap
import java.util.HashSet
import java.util.Random

/**

 * @author Nathan Templon
 */
public class LocalAudioService : AudioService() {

    // Constants
    private val MASTER_VOLUME_ADJUSTMENT = 1f


    // Fields
    private val musicSets: Map<String, Array<Music?>>
    private val random = Random(System.currentTimeMillis())

    private var currentSet: Array<Music?>? = null
    private var currentTrack: Music? = null
    private var currentTrackIndex: Int = 0
    private var musicVolume = 1.0f


    init {
        val tempSets = HashMap<String, Array<Music?>>()

        for (type in AudioService.MUSIC_TYPES) {
            val typeFolder = FileLocations.AUDIO_DIRECTORY.resolve(type)
            if (Files.exists(typeFolder) && Files.isDirectory(typeFolder)) {
                val music = HashSet<Music>()

                try {
                    Files.newDirectoryStream(typeFolder).use { paths ->
                        paths.asSequence().forEach {
                            musicFile ->
                            if (AudioService.MUSIC_EXTENSIONS.contains(FileUtils.getExtension(musicFile))) {
                                val instance = Gdx.audio.newMusic(FileHandle(musicFile.toString()))
                                instance.setOnCompletionListener { music1 -> this.trackCompleted(music1) }
                                music.add(instance)
                            }
                        }
                    }
                } catch (ex: IOException) {
                }


                tempSets.put(type, music.toArray<Music>(arrayOfNulls<Music>(music.size())))
            } else {
                tempSets.put(type, arrayOfNulls<Music>(0))
            }
        }

        this.musicSets = mapOf(tempSets.entrySet().map { entry -> Pair(entry.getKey(), entry.getValue()) })
    }


    // Public Methods
    override fun playMusic(type: String) {
        this.stop()

        if (this.musicSets.containsKey(type)) {
            this.currentSet = this.musicSets.get(type)

            if (this.currentSet!!.size() == 0) {
                this.currentTrack = null
                this.currentTrackIndex = 0
            } else {
                this.currentTrackIndex = random.nextInt(this.currentSet!!.size())
                this.currentTrack = this.currentSet!![this.currentTrackIndex]
                this.currentTrack!!.setVolume(this.musicVolume)
                this.currentTrack!!.play()
            }
        }
    }

    override fun pause() {
        if (this.currentTrack != null) {
            this.currentTrack!!.pause()
        }
    }

    override fun resume() {
        if (this.currentTrack != null) {
            this.currentTrack!!.play()
        }
    }

    override fun stop() {
        if (this.currentTrack != null) {
            this.currentTrack!!.stop()
        }

        this.currentTrack = null
        this.currentSet = null
        this.currentTrackIndex = -1
    }

    override fun setMusicVolume(volume: Float) {
        this.musicVolume = volume * MASTER_VOLUME_ADJUSTMENT
        if (this.currentTrack != null) {
            this.currentTrack!!.setVolume(this.musicVolume)
        }
    }

    override fun dispose() {
        for ((key, musicList) in this.musicSets) {
            for (music in musicList) {
                music?.dispose()
            }
        }
    }


    // Private Methods
    private fun trackCompleted(music: Music) {
        if (this.currentSet!!.size() == 1) {
            this.currentTrackIndex = 0
            this.currentTrack = this.currentSet!![0]
            this.currentTrack!!.setVolume(this.musicVolume)
            this.currentTrack!!.play()
        } else {
            var nextTrackIndex = this.random.nextInt(this.currentSet!!.size())
            while (nextTrackIndex == this.currentTrackIndex) {
                nextTrackIndex = this.random.nextInt(this.currentSet!!.size())
            }
            this.currentTrackIndex = nextTrackIndex
            this.currentTrack = this.currentSet!![this.currentTrackIndex]
            this.currentTrack!!.setVolume(this.musicVolume)
            this.currentTrack!!.play()
        }
    }

}
