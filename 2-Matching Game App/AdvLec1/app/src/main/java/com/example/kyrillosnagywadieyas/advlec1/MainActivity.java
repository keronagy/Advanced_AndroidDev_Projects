package com.example.kyrillosnagywadieyas.advlec1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button LoginBtn;
    private EditText Username;
    private Button ScoreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginBtn = (Button) findViewById(R.id.LoginBtn);
        ScoreBtn = (Button) findViewById(R.id.ScoreBtn);
        Username = (EditText) findViewById(R.id.UserameTxt);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!( Username.getText().toString().equals(""))) {
                    Intent intent = new Intent(MainActivity.this, GameActivity.class);
                    intent.putExtra(CONSTANTS.USERNAME,Username.getText().toString());
                    startActivity(intent);


                }
                else
                {
                    Toast.makeText(MainActivity.this,"Please Write your USERNAME then pres PLAY",Toast.LENGTH_LONG).show();
                }
            }
        });
        ScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WinActivity.class);
                startActivity(intent);
            }
        });


    }



}
