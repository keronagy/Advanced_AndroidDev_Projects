package com.example.kyrillosnagywadieyas.currencyconverter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    private Button ConvertBtn ;
    private Spinner From;
    private Spinner To;
    private EditText Value;
    private TextView Results;
    private final double USDTOEGP = 17.6;
    private final double EGPTOUSD = 1/17.6;
    private final String ConvertedValue = "ConvertedValue";
    private final String Result = "Result";
    private final String API = "http://www.floatrates.com/daily/usd.json";
    public static float data=0;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String MY_PREFS_KEY = "MyPrefsKey";
    private float OfflineConversionValue= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConvertBtn = (Button) findViewById(R.id.ConvertBtn);
        From = (Spinner) findViewById(R.id.FromSpin);
        To = (Spinner) findViewById(R.id.ToSpin);
        Value = (EditText) findViewById(R.id.ValueTxt);
        InputFilterEditText.context= this;
        Value.setFilters(new InputFilter[]{ new InputFilterEditText("0", Integer.MAX_VALUE+"")});
        Results = (TextView) findViewById(R.id.ResultTxt);


        FetchData fetchData = new FetchData();
        try {
            Object result = fetchData.execute().get();
            SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            OfflineConversionValue = prefs.getFloat(MY_PREFS_KEY, 0);
            if (OfflineConversionValue == 0) {
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putFloat(MY_PREFS_KEY, data);
            editor.apply();}
            if(data == 0)
            {
                data = OfflineConversionValue;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0)
                {
                    To.setSelection(1,true);
                }
                else
                {
                    To.setSelection(0,true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        To.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position==0)
                {
                    From.setSelection(1,true);
                }
                else
                {
                    From.setSelection(0,true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        ConvertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(From.getSelectedItemPosition() != To.getSelectedItemPosition()) {
                    if (!(Value.getText().toString().equals("")))
                    {
                        double convertionValue=0;
                        try {
                            FetchData fetchData = new FetchData();
                            Object result = fetchData.execute();
                            if(data!=0) {OfflineConversionValue = data; UpdateSharedPref(data);}
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(data==0&& OfflineConversionValue ==0)
                        {
                            convertionValue = USDTOEGP;
                        }

                        else if(data!=0) {
                            convertionValue= data;
                        }
                        else if(OfflineConversionValue!=0)
                        {
                            convertionValue = OfflineConversionValue;
                        }
                        if (String.valueOf(From.getSelectedItem()).equals(getString(R.string.USD))) {
                            Results.setText(round(Double.parseDouble(Value.getText().toString()) * convertionValue)+ "");
                        } else {
                            Results.setText(round(Double.parseDouble(Value.getText().toString()) * (1/convertionValue))  + "");

                        }

                    }
                    else
                    {
                        Toast.makeText(MainActivity.this, getString(R.string.EmptyERROR),Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, getString(R.string.CurrencyERROR),Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Result,Results.getText().toString());
        outState.putString(ConvertedValue,Value.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Value.setText( savedInstanceState.getString(ConvertedValue));
        Results.setText(savedInstanceState.getString(Result));
    }

    public void UpdateSharedPref(float v)
    {
        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putFloat(MY_PREFS_KEY, v);
        editor.apply();
    }

    public static float round(double number) {
        int pow = 10;
        for (int i = 1; i < 2; i++)
            pow *= 10;
        double tmp = number * pow;
        return ( (float) ( (int) ((tmp - (int) tmp) >= 0.5f ? tmp + 1 : tmp) ) ) / pow;
    }

}
