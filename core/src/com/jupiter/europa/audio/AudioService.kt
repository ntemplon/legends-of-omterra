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

import com.badlogic.gdx.utils.Disposable

/**

 * @author Nathan Templon
 */
public abstract class AudioService : Disposable {

    // Abstract Methods
    public abstract fun playMusic(type: String)

    public abstract fun setMusicVolume(volume: Float)
    public abstract fun pause()
    public abstract fun resume()
    public abstract fun stop()

    companion object {
        public val TITLE_MUSIC: String = "title"
        public val COMBAT_MUSIC: String = "combat"
        public val DUNGEON_MUSIC: String = "dungeon"
        public val AMBIENT_MUSIC: String = "ambient"
        public val DEFAULT_MUSIC: String = AMBIENT_MUSIC

        public val MUSIC_TYPES: Array<String> = arrayOf(TITLE_MUSIC, COMBAT_MUSIC, DUNGEON_MUSIC, AMBIENT_MUSIC)
        public val MUSIC_EXTENSIONS: Array<String> = arrayOf("mp3", "wav")
    }

}
