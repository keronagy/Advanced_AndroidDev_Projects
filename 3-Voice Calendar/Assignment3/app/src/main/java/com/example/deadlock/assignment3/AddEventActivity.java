package com.example.deadlock.assignment3;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddEventActivity extends AppCompatActivity {

    EditText EventDateTxt;
    EditText EventTime;
    EditText EventNameTxt;
    String[] eventDetails;
    Calendar myCalendar = Calendar.getInstance();
    Button Done;
    Button Cancel;
    String s = "";
    String EventName="";
    String EventDate="";
    Date date = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);


        s = getIntent().getExtras().getString(MainActivity.EVENTDETAILS);
        eventDetails = s.split("\\b((mon|tues|wed(nes)?|thur(s)?|fri|sat(ur)?|sun)(day)?) \\b");
        EventName = eventDetails[0];
        EventDate = eventDetails[1];
        //                                                         Nov 5 2018 at 7:30 pm
        //SimpleDateFormat formatter = new SimpleDateFormat("MMM dd yyyy 'at' HH:mm a");



        date = MainActivity.tryParse(EventDate);
        myCalendar.setTime(date);




        EventDateTxt = findViewById(R.id.EventDate);
        EventTime = findViewById(R.id.EventTime);
        EventNameTxt = findViewById(R.id.EventName);
        Done = findViewById(R.id.DoneBtn);
        Cancel = findViewById(R.id.CancelBtn);

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        EventDateTxt.setText(sdf.format(myCalendar.getTime()));

        EventNameTxt.setText(EventName);

        String AM_PM ;
        int hour =0;
        if(myCalendar.get(Calendar.HOUR_OF_DAY) <12) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
        }
        String timeToPut = myCalendar.get(Calendar.HOUR) + " : " + myCalendar.get(Calendar.MINUTE) + " " + AM_PM;
        EventTime.setText(timeToPut);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalenderAddEvent();
                finish();
                Intent i = new Intent(AddEventActivity.this,UpcompingEvents.class);
                startActivity(i);
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                EventDateTxt.setText(sdf.format(myCalendar.getTime()));
            }

        };
        final TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                String AM_PM ;
                if(hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    hourOfDay = hourOfDay-12;
                    AM_PM = "PM";
                }

                EventTime.setText(hourOfDay + " : " + minute + " " + AM_PM );

            }
        };

        EventDateTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        EventTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(AddEventActivity.this, time, myCalendar.get(Calendar.HOUR_OF_DAY), myCalendar.get(Calendar.MINUTE),false).show();

            }
        });



    }

    public static long createLocalCalendar(Context context)
    {
        ContentValues values = new ContentValues();
        values.put(
                CalendarContract.Calendars.ACCOUNT_NAME,
                MainActivity.EVENTAPP); //Account name becomes equal to "nav_shift_manager" !!TEST!!
        values.put(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(
                CalendarContract.Calendars.NAME,
                MainActivity.EVENTAPP);
        values.put(
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                MainActivity.EVENTAPP);
        values.put(
                CalendarContract.Calendars.CALENDAR_COLOR,
                0xffff0000);
        values.put(
                CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(
                CalendarContract.Calendars.CALENDAR_TIME_ZONE,
                Calendar.getInstance().getTimeZone().getID());
        Uri.Builder builder =
                CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_NAME,
                "com.keroeventapp");
        builder.appendQueryParameter(
                CalendarContract.Calendars.ACCOUNT_TYPE,
                CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(
                CalendarContract.CALLER_IS_SYNCADAPTER,
                "true");
        Uri uri = context.getContentResolver().insert(builder.build(), values);

        // Now get the CalendarID :
        Long id = Long.parseLong(uri.getLastPathSegment());
        Log.d("iddddd",id+"");
        return id;
    }

    public  void CalenderAddEvent()
    {
        String calName;
        long calId=Integer.MAX_VALUE;
        String[] projection = new String[]{CalendarContract.Calendars.NAME, CalendarContract.Calendars._ID};
        Cursor managedCursor = managedQuery(CalendarContract.Calendars.CONTENT_URI, projection, null, null, null);
        if (managedCursor.moveToFirst())
        {
            int nameColumn = managedCursor.getColumnIndex(CalendarContract.Calendars.NAME);
            int idColumn = managedCursor.getColumnIndex(CalendarContract.Calendars._ID);
            do {
                calName = managedCursor.getString(nameColumn);
                if(MainActivity.EVENTAPP.equals(calName))
                {
                    calId = Long.parseLong(managedCursor.getString(idColumn));
                    break;
                }
            } while (managedCursor.moveToNext());
            if(calId==Integer.MAX_VALUE)
                calId = createLocalCalendar(AddEventActivity.this);
        }
        ContentResolver cr = AddEventActivity.this.getContentResolver();
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, calId);
        event.put(CalendarContract.Events.TITLE, EventNameTxt.getText().toString());
        Log.d("datelong",myCalendar.getTimeInMillis()+"" );
        event.put(CalendarContract.Events.DTSTART, myCalendar.getTimeInMillis());
        event.put(CalendarContract.Events.DTEND,myCalendar.getTimeInMillis());
        event.put(CalendarContract.Events.HAS_ALARM, 1);
        event.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        if (ActivityCompat.checkSelfPermission(AddEventActivity.this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(AddEventActivity.this,"please make sure you GRANTED the calendar permission]",Toast.LENGTH_LONG).show();
            return;
        }
        Uri u = cr.insert(CalendarContract.Events.CONTENT_URI, event);
        Toast.makeText(AddEventActivity.this, "done",Toast.LENGTH_LONG).show();
    }


}
