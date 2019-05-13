package com.example.deadlock.shakeit;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.concurrent.TimeUnit;




public class MainActivity extends AppCompatActivity implements SensorEventListener{
    private TextToSpeech TTS;
    boolean calibrating = true;
    int counter =0;
    double max = 0;
    double min = 0;
    Button ResetBtn;
    Button EnterPassBtn;
    Button withoutCal;
    ProgressBar progressBar;
    TextView Pattern;
    TextView timer;
    TextView pass;
    private long lastUpdate = 0;
    private int SHAKE_THRESHOLD = 10;
    CountDownTimer cTimer = null;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    double LastAcceleration = 0;
    private CountDownTimer CalTimer;
    private Button Calibrate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Pattern = findViewById(R.id.Pattern);
        pass = findViewById(R.id.Pass);
        timer = findViewById(R.id.timer);
        progressBar = findViewById(R.id.progress_horizontal);
        progressBar.setMax(100);
        progressBar.setScaleY(3f);
        progressBar.setProgress(100);
        ResetBtn = findViewById(R.id.ResetBtn);
        ResetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Pattern.setText("");
                pass.setText(R.string.enter);
                cTimer=null;
                CalTimer = null;
                cancelTimer();
                CalcancelTimer();
                progressBar.setProgress(100);
                timer.setText("Timer");
            }
        });
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        EnterPassBtn = findViewById(R.id.EnterPass);
        EnterPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sensorManager.registerListener(MainActivity.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                counter=0;
                Pattern.setText("");
                startTimer();
            }
        });
        Calibrate = findViewById(R.id.CalibrateBtn);
        Calibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Speak("Please prepare for calibrating.\n" +
//                        "OK!, Now please Shake the phone at least three times fast.");
//                while (TTS.isSpeaking()) {
//
//                }
                //ProgBarAnim(5000);
                CalcancelTimer();
                CalstartTimer();
                calibrating = true;


            }
        });
        withoutCal = findViewById(R.id.WithoutCal);
        withoutCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SHAKE_THRESHOLD =10;
            }
        });

        initTextToSpeech();
    }
        private void initTextToSpeech() {
        TTS = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (TTS.getEngines().size() == 0) {


                    Toast.makeText(MainActivity.this, "sorry, there is no Text To Speech Engine on your device", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    TTS.setLanguage(Locale.US);
                    Speak("Please prepare for calibrating.\n" +
                            "OK!, Now please Shake the phone at least three times fast.");
                    while (TTS.isSpeaking()) {

                    }
                    CalcancelTimer();
                    CalstartTimer();



                }


            }
        });
    }


        public void Speak(String s) {
        if (Build.VERSION.SDK_INT >= 21) {
            TTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            TTS.speak(s, TextToSpeech.QUEUE_FLUSH, null);

        }
        while (TTS.isSpeaking()) {

        }
    }
    public void ProgBarAnim(long Duration) {
        ProgressBarAnimation progressBarAnimation = new ProgressBarAnimation(this, progressBar, 0f, 100f);
        progressBarAnimation.setDuration(Duration);
        progressBar.setAnimation(progressBarAnimation);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if(!calibrating) {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];

                long curTime = System.currentTimeMillis();

                if ((curTime - lastUpdate) > 100) {

                    lastUpdate = curTime;

                    double acceleration = Math.sqrt(Math.pow(x, 2) +
                            Math.pow(y, 2));

                    Log.d("kerooooo", acceleration + "");
                    Log.d("kerooooon", (acceleration - LastAcceleration) + "");

                    if (acceleration - LastAcceleration > SHAKE_THRESHOLD) {
                        if (cTimer != null) {
                            Pattern.setText(addone());
                        }
                    }
                    LastAcceleration = acceleration;
                }
            }
            else
            {
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];

                double acceleration = Math.sqrt(Math.pow(x, 2) +
                        Math.pow(y, 2)
                );


                if (acceleration > max) {
                    max = acceleration;
                } else if (acceleration < min) {
                    min = acceleration;
                }

                if (CalTimer == null) {
                    CalstartTimer();
                }
            }
        }
    }

    private void CalstartTimer() {
        EnterPassBtn.setEnabled(false);
        Calibrate.setEnabled(false);
        ResetBtn.setEnabled(false);
       // withoutCal.setEnabled(false);
        ProgBarAnim(10000);
        CalTimer = new CountDownTimer(10000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText(TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) + " sec");
            }

            public void onFinish() {

                SHAKE_THRESHOLD = (int) ((max - min) * 1 / 2);
                Log.d("kerooooo", SHAKE_THRESHOLD + "");
                Speak("You are done Calibrating now. \nThank you!");

                calibrating = false;
                max = 0;
                min = 0;
                LastAcceleration = SHAKE_THRESHOLD;
                counter = 0;
                EnterPassBtn.setEnabled(true);
                ResetBtn.setEnabled(true);
                Calibrate.setEnabled(true);
                //withoutCal.setEnabled(true);

            }
        };

        CalTimer.start();

    }
    private void startTimer() {
        ProgBarAnim(5000);
        EnterPassBtn.setEnabled(false);
        Calibrate.setEnabled(false);
        ResetBtn.setEnabled(false);
       // withoutCal.setEnabled(false);
        cTimer = new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                timer.setText( TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) +" sec");
            }
            public void onFinish() {
                counter++;
                if(counter <3) {
                    Pattern.append("-");
                    startTimer();

                }
                else
                {
                    EnterPassBtn.setEnabled(true);
                    ResetBtn.setEnabled(true);
                    Calibrate.setEnabled(true);
                    //withoutCal.setEnabled(true);
                    if(CheckPattern())
                    {
                        pass.setText("Correct");
                    }
                    else
                    {
                        pass.setText("Wrong");
                    }
                    cancelTimer();
                    cTimer=null;


                }
            }
        };
        cTimer.start();
    }

    private void cancelTimer() {
        if (cTimer != null) {
            cTimer.cancel();

        }
    }
    private String addone() {
        String str = Pattern.getText().toString();
        String LastChar = "";
        if(!str.equals(""))
        {
            LastChar= str.substring(str.length() - 1);
        }
        if (LastChar.equals("")||LastChar.equals("-")) {
            return str+"1";

        } else {
            int ShakeCount = Integer.parseInt(str.charAt(str.length() - 1) + "");
            ShakeCount++;
            return str.substring(0, str.length() - 1) + (ShakeCount+"");
        }

    }
    private boolean CheckPattern() {
        return Pattern.getText().toString().trim().equals("1-2-3");
    }



    private void CalcancelTimer() {
        if (CalTimer != null)
            CalTimer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        TTS.shutdown();
    }

    @Override
    protected void onPause() {
        super.onPause();
        TTS.shutdown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TTS.shutdown();
    }

}