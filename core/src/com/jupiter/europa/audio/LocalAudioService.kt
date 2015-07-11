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
package com.jupiter.europa.audio

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.files.FileHandle
import com.jupiter.europa.io.FileLocations
import com.jupiter.europa.io.FileUtils
import java.nio.file.Files
import java.util.Random

/**

 * @author Nathan Templon
 */
public class LocalAudioService : AudioService() {

    // Constants
    private val MASTER_VOLUME_ADJUSTMENT = 1f


    // Fields
    private val musicSets: Map<String, List<Music>>
    private val random = Random(System.currentTimeMillis())

    private var currentSet: List<Music> = listOf()
    private var currentTrack: Music? = null
    private var currentTrackIndex: Int = 0
    private var musicVolume = 1.0f


    init {
        // The Kotlin Way
        this.musicSets = AudioService.MUSIC_TYPES.asSequence().map { // For Each Music Type: (typeName, containingFolder)
            type ->
            Pair(type, FileLocations.AUDIO_DIRECTORY.resolve(type))
        }.filter { // Only take valid files
            pair ->
            Files.exists(pair.second) && Files.isDirectory(pair.second)
        }.toMap({ // Keys are String Names of Types
            folderPair ->
            folderPair.first
        }, { // Values are lists of valid music from the folder
            folderPair ->
            Files.newDirectoryStream(folderPair.second).asSequence().filter {
                file ->
                AudioService.MUSIC_EXTENSIONS.contains(FileUtils.getExtension(file))
            }.map {
                file ->
                val instance = Gdx.audio.newMusic(FileHandle(file.toString()))
                instance.setOnCompletionListener { music1 -> this.trackCompleted(music1) }
                instance
            }.toList()
        })

        // The Java-in-Kotlin Way
        //        val tempSets = HashMap<String, List<Music>>()
        //
        //        for (type in AudioService.MUSIC_TYPES) {
        //            val typeFolder = FileLocations.AUDIO_DIRECTORY.resolve(type)
        //            if (Files.exists(typeFolder) && Files.isDirectory(typeFolder)) {
        //                val music = HashSet<Music>()
        //
        //                try {
        //                    Files.newDirectoryStream(typeFolder).use { paths ->
        //                        paths.asSequence().forEach {
        //                            musicFile ->
        //                            if (AudioService.MUSIC_EXTENSIONS.contains(FileUtils.getExtension(musicFile))) {
        //                                val instance = Gdx.audio.newMusic(FileHandle(musicFile.toString()))
        //                                instance.setOnCompletionListener { music1 -> this.trackCompleted(music1) }
        //                                music.add(instance)
        //                            }
        //                        }
        //                    }
        //                } catch (ex: IOException) {
        //                }
        //                tempSets.put(type, music.toList<Music>())
        //            } else {
        //                tempSets.put(type, listOf<Music>())
        //            }
        //        }
        //
        //        this.musicSets = mapOf(*tempSets.entrySet().map { entry -> entry.getKey() to entry.getValue() }.toTypedArray())
    }


    // Public Methods
    override fun playMusic(type: String) {
        this.stop()

        if (this.musicSets.containsKey(type)) {
            this.currentSet = this.musicSets[type]!! // Safe because it is a kotlin-only, non-null variable

            if (this.currentSet.size() == 0) {
                this.currentTrack = null
                this.currentTrackIndex = 0
            } else {
                this.currentTrackIndex = random.nextInt(this.currentSet.size())
                this.currentTrack = this.currentSet[this.currentTrackIndex]
                this.currentTrack?.setVolume(this.musicVolume)
                this.currentTrack?.play()
            }
        }
    }

    override fun pause() {
        this.currentTrack?.pause()
    }

    override fun resume() {
        this.currentTrack?.play()
    }

    override fun stop() {
        this.currentTrack?.stop()

        this.currentTrack = null
        this.currentSet = listOf()
        this.currentTrackIndex = -1
    }

    override fun setMusicVolume(volume: Float) {
        this.musicVolume = volume * MASTER_VOLUME_ADJUSTMENT
        this.currentTrack?.setVolume(this.musicVolume)
    }

    override fun dispose() {
        for ((key, musicList) in this.musicSets) {
            for (music in musicList) {
                music.dispose()
            }
        }
    }


    // Private Methods
    private fun trackCompleted(music: Music) {
        if (this.currentSet.size() == 1) {
            this.currentTrackIndex = 0
            this.currentTrack = this.currentSet[0]
            this.currentTrack?.setVolume(this.musicVolume)
            this.currentTrack?.play()
        } else {
            var nextTrackIndex = this.random.nextInt(this.currentSet.size())
            while (nextTrackIndex == this.currentTrackIndex) {
                nextTrackIndex = this.random.nextInt(this.currentSet.size())
            }
            this.currentTrackIndex = nextTrackIndex
            this.currentTrack = this.currentSet[this.currentTrackIndex]
            this.currentTrack?.setVolume(this.musicVolume)
            this.currentTrack?.play()
        }
    }

}
