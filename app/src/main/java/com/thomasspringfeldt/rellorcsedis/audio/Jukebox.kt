package com.thomasspringfeldt.rellorcsedis.audio

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.util.Log
import com.thomasspringfeldt.rellorcsedis.Game
import com.thomasspringfeldt.rellorcsedis.levels.GameEvent
import java.io.IOException

const val DEFAULT_SFX_VOLUME = 0.8f
const val DEFAULT_MUSIC_VOLUME = 0.6f
const val MAX_STREAMS = 5
private const val SOUNDS_PREF_KEY = "sounds_pref_key"
private const val MUSIC_PREF_KEY = "music_pref_key"

/**
 * Sound and music player.
 * @author Thomas Springfeldt
 */
class Jukebox(val engine: Game) {

    private val tag = "Jukebox"
    private var mSoundPool: SoundPool? = null
    private var mBgPlayer: MediaPlayer? = null
    private val mSoundsMap = HashMap<GameEvent, Int>()
    private var mSoundEnabled = true
    private var mMusicEnabled = true

    init {
        engine.getActivity().volumeControlStream = AudioManager.STREAM_MUSIC
        val prefs = engine.getPreferences()
        mSoundEnabled = prefs.getBoolean(SOUNDS_PREF_KEY, true)
        mMusicEnabled = prefs.getBoolean(MUSIC_PREF_KEY, true)
        loadIfNeeded()
    }

    private fun loadIfNeeded() {
        if (mSoundEnabled) {
            loadSounds()
        }
        if (mMusicEnabled) {
            loadMusic()
        }
    }

    private fun loadSounds() {
        val attr = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        mSoundPool = SoundPool.Builder()
            .setAudioAttributes(attr)
            .setMaxStreams(MAX_STREAMS)
            .build()

        mSoundsMap.clear()
        loadEventSound(GameEvent.Jump, "sfx/jump.wav")
        loadEventSound(GameEvent.TakeDamage, "sfx/take_damage.wav")
        loadEventSound(GameEvent.GameOver, "sfx/gameover.wav")
        loadEventSound(GameEvent.CoinPickup, "sfx/coin_pickup.wav")
    }

    private fun loadEventSound(event: GameEvent, fileName: String) {
        try {
            val afd = engine.getAssets().openFd(fileName)
            val soundId = mSoundPool!!.load(afd, 1)
            mSoundsMap[event] = soundId
        } catch (e: IOException) {
            Log.e(tag, "Error loading sound $e")
        }
    }

    private fun unloadSounds() {
        if (mSoundPool == null) {
            return
        }
        mSoundPool!!.release()
        mSoundPool = null
        mSoundsMap.clear()
    }

    fun playEventSound(event: GameEvent) {
        if (!mSoundEnabled) {
            return
        }
        val leftVolume = DEFAULT_SFX_VOLUME
        val rightVolume = DEFAULT_SFX_VOLUME
        val priority = 1
        val loop = 0 //-1 loop forever, 0 play once
        val rate = 1.0f
        val soundID = mSoundsMap[event]
        if (soundID == null) {
            Log.e(tag, "Attempting to play non-existent event sound: {event}")
            return
        }
        if (soundID > 0) { //if soundID is 0, the file failed to load. Make sure you catch this in the loading routine.
            mSoundPool!!.play(soundID, leftVolume, rightVolume, priority, loop, rate)
        }
    }

    private fun loadMusic() {
        try {
            mBgPlayer = MediaPlayer()
            val afd = engine.getAssets().openFd("bgm/bgm_1.wav")
            mBgPlayer!!.setDataSource(
                afd.fileDescriptor,
                afd.startOffset,
                afd.length
            )
            mBgPlayer!!.isLooping = true
            mBgPlayer!!.setVolume(DEFAULT_MUSIC_VOLUME, DEFAULT_MUSIC_VOLUME)
            mBgPlayer!!.prepare()
        } catch (e: IOException) {
            Log.e(tag, "Unable to create MediaPlayer.", e)
        }
    }

    fun pauseBgMusic() {
        if (!mMusicEnabled) {
            return
        }
        mBgPlayer!!.pause()
    }

    fun resumeBgMusic() {
        if (!mMusicEnabled) {
            return
        }
        mBgPlayer!!.start()
    }

    private fun unloadMusic() {
        if (mBgPlayer == null) {
            return
        }
        mBgPlayer!!.stop()
        mBgPlayer!!.release()
    }

    fun getSoundStatus(): Boolean {
        return mSoundEnabled
    }

    fun getMusicStatus(): Boolean {
        return mMusicEnabled
    }

    fun toggleSoundStatus() {
        mSoundEnabled = !mSoundEnabled
        if (mSoundEnabled) {
            loadSounds()
        } else {
            unloadSounds()
        }
        engine.savePreference(SOUNDS_PREF_KEY, mSoundEnabled)
    }
}