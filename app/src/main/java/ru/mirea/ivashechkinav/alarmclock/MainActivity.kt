package ru.mirea.ivashechkinav.alarmclock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.mirea.ivashechkinav.alarmclock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}