package com.example.egg_horn;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final int MAX_TIME = 300;
    private static final int DEFAULT_TIME = 30;
    private SeekBar seekBar;
    private TextView txtTime;
    private int progressSet;
    private boolean counterActive;
    private Button btnGo;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        seekBar = findViewById(R.id.seekBar);
        txtTime = findViewById(R.id.txtTime);
        btnGo = findViewById(R.id.btnGo);
        seekBar.setMax(MAX_TIME);
        seekBar.setProgress(DEFAULT_TIME);

        counterActive = false;
        progressSet = seekBar.getProgress();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressSet = progress;
                int minutes = progress / 60;
                int seconds = progress % 60;
                txtTime.setText(getString(R.string.show_counter, minutes, seconds));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void controlCounter(View view) {
        if (!counterActive){
            counterActive = true;
            countDownTimer =  new CountDownTimer((progressSet *1000 + 100), 1000)
            { // + 100 to compensate the delay during process
                @Override
                public void onTick(long millisUntilFinished) {
                    int progress = (int)(millisUntilFinished/1000);
                    int minutes = progress / 60;
                    int seconds = progress % 60;
                    txtTime.setText(getString(R.string.show_counter, minutes, seconds));
                    seekBar.setProgress(progress);
                }

                @Override
                public void onFinish() {
                    txtTime.setText(getString(R.string.show_counter, 0, 0));
                    MediaPlayer.create(MainActivity.this, R.raw.airhorn).start();
                    resetTimer();
                }
            };
            countDownTimer.start();
            btnGo.setText(getString(R.string.stop_button));
            seekBar.setEnabled(false);
        }
       else {
            resetTimer();
            countDownTimer.cancel();
        }
    }

    private void resetTimer(){
        counterActive = false;
        seekBar.setProgress(30);
        seekBar.setEnabled(true);
        btnGo.setText(getString(R.string.go));
        txtTime.setText(getString(R.string.show_counter,0,30));
    }
}
