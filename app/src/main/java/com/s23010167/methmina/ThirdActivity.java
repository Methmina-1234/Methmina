package com.s23010167.methmina;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private TextView temperatureText;
    private MediaPlayer mediaPlayer;
    private Button stopButton;

    private static final float TEMPERATURE_THRESHOLD = 67; // Replace with your SID last two digits

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        temperatureText = findViewById(R.id.temperatureText);
        stopButton = findViewById(R.id.button3);

        // Initialize media player with audio in res/raw/alert.mp3
        mediaPlayer = MediaPlayer.create(this, R.raw.alert);

        // Sensor setup
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
            if (temperatureSensor != null) {
                sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                temperatureText.setText("Ambient Temperature Sensor not available.");
            }
        }

        // STOP button logic → stops the sound if playing
        stopButton.setOnClickListener(view -> {
            if (isPlaying) {
                mediaPlayer.pause();
                mediaPlayer.seekTo(0); // Reset to start
                isPlaying = false;
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float temperature = event.values[0];
        temperatureText.setText("Temperature: " + temperature + " °C");

        // Play if above threshold and not already playing
        if (temperature >= TEMPERATURE_THRESHOLD && !isPlaying) {
            mediaPlayer.start();
            isPlaying = true;
        }

        // Auto-stop if temp falls below threshold and is playing
        if (temperature < TEMPERATURE_THRESHOLD && isPlaying) {
            mediaPlayer.pause();
            mediaPlayer.seekTo(0);
            isPlaying = false;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        sensorManager.unregisterListener(this);
    }
}
