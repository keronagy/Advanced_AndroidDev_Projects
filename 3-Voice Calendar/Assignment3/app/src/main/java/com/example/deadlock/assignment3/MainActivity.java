package com.example.deadlock.assignment3;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    public static final String EVENTDETAILS = "EventDetails";
    public static final String EVENTAPP = "EventApp";
//5 image button and 4 text view

    ImageButton ExitBtn;
    ImageButton HelpBtn;
    ImageButton OptionBtn;
    ImageButton UpcomingBtn;
    ImageButton AddEventBtn;

    TextView ExitTxt;
    TextView HelpTxt;
    TextView OptionTxt;
    TextView AddEventTxt;



    private int RECORD_AUDIO_PERMISSION_CODE = 1;
    private int READ_CALENDAR_PERMISSION_CODE = 2;
    private int WRITE_CALENDAR_PERMISSION_CODE = 3;

    private TextToSpeech TTS;
    private SpeechRecognizer speechRecognizer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExitBtn = findViewById(R.id.ExitBtn);
        OptionBtn = findViewById(R.id.OptionBtn);
        UpcomingBtn = findViewById(R.id.UpcomingBtn);
        AddEventBtn = findViewById(R.id.AddEventImgBtn);
        HelpBtn = findViewById(R.id.HelpBtn);

        ExitTxt = findViewById(R.id.ExitTxt);
        HelpTxt = findViewById(R.id.HelpTxt);
        OptionTxt = findViewById(R.id.OptionTxt);
        AddEventTxt = findViewById(R.id.AddEventTxtView);

        ExitTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exit();
            }
        });
        ExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exit();
            }
        });

        HelpTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Help();
            }
        });
        HelpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Help();
            }
        });
        OptionTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Option();
            }
        });
        OptionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Option();
            }
        });
        AddEventTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestPermission();
            }
        });
        AddEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TTS.isSpeaking())
                {
                    TTS.stop();
                }
                RequestPermission();
            }
        });
        UpcomingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpcomingEvents();
            }
        });

        initTextToSpeech();
        initSpeechRecognizer();



    }

    private void initTextToSpeech() {
        TTS = new TextToSpeech(MainActivity.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(TTS.getEngines().size()== 0)
                {


                    Toast.makeText(MainActivity.this,"sorry, there is no Text To Speech Engine on your device", Toast.LENGTH_LONG).show();
                    finish();
                }
                else
                {
                    TTS.setLanguage(Locale.US);
                    Speak("Hey! Speech engine is ready, Please tell me the event to remind you");
                }
            }
        });
    }

    public void Speak(String s) {
        if (Build.VERSION.SDK_INT>=21) {
            TTS.speak(s, TextToSpeech.QUEUE_FLUSH, null, null);
        }
        else
        {
            TTS.speak(s, TextToSpeech.QUEUE_FLUSH, null);

        }
    }

    private void UpcomingEvents() {
        if(TTS.isSpeaking())
        {
            TTS.stop();
        }
        Intent i = new Intent(MainActivity.this,UpcompingEvents.class);
        startActivity(i);
    }

    private void RequestPermission() {
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALENDAR)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALENDAR)== PackageManager.PERMISSION_GRANTED)
        {
            AddNewEvent();
        }
        else
        {
            RequestRecordPermission();
        }
    }



    private void RequestRecordPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.RECORD_AUDIO) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.WRITE_CALENDAR) && ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_CALENDAR))
        {
            new AlertDialog.Builder(MainActivity.this).setTitle("Permission Needed").setMessage("This permissions are needed to record your audio and add the event for you").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR},RECORD_AUDIO_PERMISSION_CODE);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this,new String[] { Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_CALENDAR},RECORD_AUDIO_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == RECORD_AUDIO_PERMISSION_CODE)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                RequestPermission();
            }
            else
            {
                Toast.makeText(MainActivity.this," you should grant the record audio permission",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void AddNewEvent() {

//        Toast.makeText(MainActivity.this, "Creating Event ", Toast.LENGTH_LONG).show();

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        speechRecognizer.startListening(i);


    }

    private void initSpeechRecognizer() {
        if(SpeechRecognizer.isRecognitionAvailable(MainActivity.this))
        {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {

                }

                @Override
                public void onBeginningOfSpeech() {

                }

                @Override
                public void onRmsChanged(float rmsdB) {

                }

                @Override
                public void onBufferReceived(byte[] buffer) {

                }

                @Override
                public void onEndOfSpeech() {

                }

                @Override
                public void onError(int error) {

                }

                @Override
                public void onResults(Bundle results) {
                    List<String> result = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    ProcessResult(result.get(0));
                }

                @Override
                public void onPartialResults(Bundle partialResults) {

                }

                @Override
                public void onEvent(int eventType, Bundle params) {

                }
            });
        }
    }

    private void ProcessResult(String s) {

            try {
                s = s.toLowerCase();
                s= s.replaceAll("([0-9]+)(th|nd|rd|st)","$1");
                s = s.replace(".","");

                Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                //s = "team lunch mon november 5 2018 at 15:30";

                if( Pattern.compile("\\b((mon|tues|wed(nes)?|thur(s)?|fri|sat(ur)?|sun)(day)?).*at\\b").matcher(s).find()) {
                    String[] eventDetails = s.split("\\b((mon|tues|wed(nes)?|thur(s)?|fri|sat(ur)?|sun)(day)?) \\b");
                    String EventDate = eventDetails[1];

                    //                                                         Nov 5 2018 at 7:30 pm
                    //SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy 'at' HH:mm a");

//                DateFormat df = new DateFormat() {
// //                                                 Nov 5 2018 at 7:30 pm
//                    static final String FORMAT1 = " MMM dd yyyy 'at' HH:mm aa";
//                    static final String FORMAT2 = " MMM dd yyyy 'at' HH:mm";
//                    static final String FORMAT3 = " MMM dd yyyy 'at' HH";
//                    static final String FORMAT4 = " MMM dd yyyy 'at' HH aa";
//                    static final String FORMAT4 = " MMM dd yyyy 'at' HH aa";
//                    static final String FORMAT4 = " MMM dd yyyy 'at' HH aa";
//                    static final String FORMAT4 = " MMM dd yyyy 'at' HH aa";
//                    static final String FORMAT4 = " MMM dd yyyy 'at' HH aa";
//
//                    final SimpleDateFormat sdf1 = new SimpleDateFormat(FORMAT1);
//                    final SimpleDateFormat sdf2 = new SimpleDateFormat(FORMAT2);
//                    final SimpleDateFormat sdf3 = new SimpleDateFormat(FORMAT3);
//                    final SimpleDateFormat sdf4 = new SimpleDateFormat(FORMAT4);
//
//                    @Override
//                    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
//                        throw new UnsupportedOperationException();
//                    }
//
//                    @Override
//                    public Date parse(String source, ParsePosition pos) {
//                        Log.d("lengthhhh", source.length() - pos.getIndex()+"");
//                        Log.d("lengthhhh", FORMAT1.length()+"");
//                        Log.d("lengthhhh", FORMAT2.length()+"");
//                        Log.d("lengthhhh", FORMAT3.length()+"");
//                        Log.d("lengthhhh", FORMAT4.length()+"");
//
//
//                        if (source.length() - pos.getIndex() == FORMAT1.length()) {return sdf1.parse(source, pos);}
//                        else if(source.length() - pos.getIndex() == FORMAT2.length()) {return sdf2.parse(source, pos);}
//                        else if(source.length() - pos.getIndex() == FORMAT3.length()) {return sdf3.parse(source, pos);}
//                        else return sdf4.parse(source, pos);
//                    }
//                };
                    Intent i2 = new Intent(MainActivity.this, AddEventActivity.class);
                    Date date = tryParse(EventDate);
                    i2.putExtra(EVENTDETAILS, s);
                    startActivity(i2);
                }
                else{
                    Toast.makeText(MainActivity.this,"Please say it the right way\nEx: team lunch monday nov 5 2018 at 7:30 pm", Toast.LENGTH_LONG).show();
                    Speak("Please say it the right way.\nExample: team lunch monday nov 5 2018 at 7:30 pm");
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this,"Please say it the right way\nEx: team lunch monday nov 5 2018 at 7:30 pm", Toast.LENGTH_LONG).show();
                Speak("Please say it the right way.\nExample: team lunch monday nov 5 2018 at 7:30 pm");
            }

        }




        //what is the time


        //open browser


    private void Option() {
    }

    private void Help() {

    }

    private void Exit() {
        System.exit(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TTS.shutdown();
        speechRecognizer.stopListening();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TTS.shutdown();
        speechRecognizer.destroy();
    }

    static List<String> formatStrings = Arrays.asList("MMM dd yyyy 'at' HH:mm aa","MMM dd yyyy 'at' HH aa",  "MMM dd yyyy 'at' HH:mm", "MMM dd yyyy 'at' HH");

    static Date tryParse(String dateString)
    {
        for (String formatString : formatStrings)
        {
            try
            {
                Date f = new SimpleDateFormat(formatString, Locale.ENGLISH).parse(dateString);
                if(dateString.substring(dateString.length()-3).contains("pm"))
                    if(f.getHours()<12)
                        f.setHours(f.getHours()+12);
                return f;            }
            catch (ParseException e) {}
        }

        return null;
    }

}
