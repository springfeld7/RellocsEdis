package com.thomasspringfeldt.rellorcsedis

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private val tag = "MainActivity"
    private lateinit var game : Game

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(tag, "OnCreate()")
        super.onCreate(savedInstanceState)
        game = Game(this)
        setContentView(game)

    }

    override fun onPause() {
        game.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        game.onResume()
    }

    override fun onDestroy() {
        game.onDestroy()
        super.onDestroy()
    }
}