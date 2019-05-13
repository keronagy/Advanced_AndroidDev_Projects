package com.example.deadlock.assignment3;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpcompingEvents extends AppCompatActivity {

    private ArrayList<EventClass> events;
    ListView lv;
    ListViewAdapter adapter;

    int previousPosition=-1;
    int counter=0;
    Handler h = new Handler();
    private static final String DEBUG_TAG = "MyActivity";
    public static final String[] INSTANCE_PROJECTION = new String[]{
            CalendarContract.Instances.EVENT_ID,      // 0
            CalendarContract.Instances.BEGIN,         // 1
            CalendarContract.Instances.TITLE          // 2
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_BEGIN_INDEX = 1;
    private static final int PROJECTION_TITLE_INDEX = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcomping_events);
        lv = findViewById(R.id.LV);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long eventID = events.get(position).getID();
                Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                Intent intent = new Intent(Intent.ACTION_VIEW)
                        .setData(uri);
                startActivity(intent);
                return false;
            }
        });
//        in case showing the event
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
//        {
//            @Override
//            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3)
//            {
//
//            }
//
//        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(counter) {
                    case 0: //first tap
                        counter++; //increase the counter
                        previousPosition = position;
                        h.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                counter = 0;
                                previousPosition = -1;
                            }
                        }, ViewConfiguration.getDoubleTapTimeout());
                        break;
                    case 1: //second tap
                        counter = 0; //reset the counter
                        if(previousPosition == position)
                        {
                            long eventID = events.get(position).getID();
                            ContentResolver cr = getContentResolver();
                            Uri deleteUri = null;
                            deleteUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                            int rows = cr.delete(deleteUri, null, null);
                            events.remove(position);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(UpcompingEvents.this, "Event Deleted Successfully",Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        });
        events = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(UpcompingEvents.this,"Please make sure that the permissions are GRANTED",Toast.LENGTH_LONG).show();
            return;
        }
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
                calId = AddEventActivity.createLocalCalendar(UpcompingEvents.this);
        }

        //start getting events

        Cursor cursor = this.getContentResolver()
                .query(
                        CalendarContract.Events.CONTENT_URI,
                        new String[]{CalendarContract.Events.CALENDAR_ID, CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART,CalendarContract.Events._ID,
                                 CalendarContract.Events.DTEND, CalendarContract.Events.EVENT_LOCATION}, null,
                        null, null);
        cursor.moveToFirst();

        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id

        for (int i = 0; i < CNames.length; i++) {
            if(Long.parseLong(cursor.getString(0))== calId)
            {
                Long ID = Long.parseLong(cursor.getString(3));
                String ename= cursor.getString(1);
                String edate=  getDate(Long.parseLong(cursor.getString(4)));
                if(Long.parseLong(cursor.getString(4))> new Date().getTime()) {
                    events.add(new EventClass(ID, ename, edate));
                    Log.d("dataaaaa", "ID = " + cursor.getString(0));
                    Log.d("dataaaaa", "Title = " + ename);
                    Log.d("dataaaaa", "Dstart = " + edate);
                    Log.d("datelong", cursor.getString(2) + "");
                }
            }
            CNames[i] = cursor.getString(1);

            cursor.moveToNext();


        }
        cursor.close();

        adapter = new ListViewAdapter(this, R.layout.listviewadapter,events );
        lv.setAdapter(adapter);

    }

    public static String getDate(long milliSeconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd HH:mm:ss yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());


    }
}
