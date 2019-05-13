package com.example.kyrillosnagywadieyas.advlec1;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.LinearLayout.LayoutParams;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class WinActivity extends AppCompatActivity {
    private String NAME ="" ;
    private long SCORE =0 ;
    DBAdapter myDb;

    public  LinearLayout.LayoutParams oneoneWEIGHT = new LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            0,
            0f
    );

    ListView lv;
    Button Reset;
    Button Exit;
    TextView tv;

    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        openDB();
        oneoneWEIGHT.gravity = Gravity.CENTER;
        oneoneWEIGHT.gravity = Gravity.CENTER;
        Bundle bundle = getIntent().getExtras();
        tv = (TextView) findViewById(R.id.finalText);
        Reset = (Button) findViewById(R.id.RestBtn);
        Exit = (Button) findViewById(R.id.ExitBtn);
        lv = (ListView) findViewById(R.id.LV1);
        Reset.setVisibility(View.INVISIBLE);
        Exit.setVisibility(View.INVISIBLE);
        int gravity = Reset.getGravity();

        Reset.setLayoutParams(CONSTANTS.ZEROWEIGHT);
        Exit.setLayoutParams(CONSTANTS.ZEROWEIGHT);
        tv.setLayoutParams(CONSTANTS.ZEROWEIGHT);

        if(bundle !=null)
        {
            Reset.setLayoutParams(oneoneWEIGHT);
            Exit.setLayoutParams(oneoneWEIGHT);
            tv.setLayoutParams(oneoneWEIGHT);
            Reset.setGravity(gravity);
            Exit.setGravity(gravity);
            tv.setGravity(gravity);
            NAME= bundle.getString(CONSTANTS.USERNAME);
            SCORE = bundle.getLong(CONSTANTS.USERSCORE);
            tv.setText("Thanks "+ NAME +" for playing \n your score is: "+ SCORE);
            Reset.setVisibility(View.VISIBLE);
            Exit.setVisibility(View.VISIBLE);
            tv.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
            long newId = myDb.insertRow(NAME, (int)SCORE);
        }


        users = new ArrayList<User>();


        Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                exit();
            }
        });

        Cursor cursor = myDb.getAllRows();
        displayRecordSet(cursor);



    }

    private void exit() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    private void displayRecordSet(Cursor cursor) {
        String message = "";
        // populate the message from the cursor

        // Reset cursor to start, checking to see if there's data:
        if (cursor.moveToFirst()) {
            do {
                // Process the data:
                int id = cursor.getInt(DBAdapter.COL_ROWID);
                String name = cursor.getString(DBAdapter.COL_NAME);
                int Score = cursor.getInt(DBAdapter.COL_STUDENTSCORE);
                String TimeStamp = cursor.getString(DBAdapter.COL_TIME);

                // Append data to the message:
                message += "id=" + id
                        +", name=" + name
                        +", score=" + Score
                        +", time=" + TimeStamp
                        +"\n";

                // [TO_DO_B6]
                User user = new User(id,name,Score,TimeStamp);
                users.add(user);
            } while(cursor.moveToNext());
        }

        // Close the cursor to avoid a resource leak.
        cursor.close();


        // Update the list view
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.listviewadapter,users );
        lv.setAdapter(adapter);

    }
    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }
    private void closeDB() {
        myDb.close();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
    }
}
