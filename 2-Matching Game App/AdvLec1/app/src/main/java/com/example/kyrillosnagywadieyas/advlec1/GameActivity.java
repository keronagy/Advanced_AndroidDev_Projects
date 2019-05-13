package com.example.kyrillosnagywadieyas.advlec1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;

public class GameActivity extends AppCompatActivity {
    final long startTime = System.currentTimeMillis();
    private String NAME ="";
    GridView gridView;
    ArrayList<MyImageButton> imgs;
    MyImageButton id1;
    MyImageButton id2;
    private static final int [] imgsId= {R.drawable.i1,R.drawable.i1,R.drawable.i2,R.drawable.i2,R.drawable.i3,R.drawable.i3,R.drawable.i4,R.drawable.i4};
    private static final int [] Vid = {R.raw.v1,R.raw.v1,R.raw.v2,R.raw.v2,R.raw.v3,R.raw.v3,R.raw.v4,R.raw.v4};
   // private int [] Shuffelarr = {0,1,2,3,4,5,6,7};
    private static final int IMG_SIZE = 8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        Bundle bundle = getIntent().getExtras();
         NAME = bundle.getString(CONSTANTS.USERNAME);
//        gridView = findViewById(R.id.gridview);
//        gridView.setAdapter(new GridViewAdap(this));

        imgs= new ArrayList<MyImageButton>();
        for (int i = 0; i < IMG_SIZE; i++) {
            int resID = getResources().getIdentifier("my"+(i+1), "id", getPackageName());
            imgs.add((MyImageButton) findViewById(resID));
        }


        LinkedHashSet<Integer> random = getNRandom(8,8);
        Integer[] arr = random.toArray(new Integer[random.size()]);

        for (int i =0; i<IMG_SIZE ; i++)
        {
            int k =i;
            imgs.get(i).setVoiceID(Vid[arr[i]]);
            imgs.get(i).setImageID(imgsId[arr[i]]);
            imgs.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stopAllSounds();
                    MyImageButton m = (MyImageButton)v;
                    if(canFlip())
                    {
                        m.flip();
                        if(howManyFlipped() == 2)
                        {
                            if(!checkMatch())
                            {
                                new CountDownTimer(500,500) {

                                    @Override
                                    public void onTick(long arg0) {}

                                    @Override
                                    public void onFinish() {
                                        falseFlip();                                    }
                                }.start();

                            }
                        }
                    }
                }
            });
        }
    }

    public void falseFlip() {
        for (int i = 0; i < imgs.size(); i++) {
            MyImageButton temp = imgs.get(i);
            if(!temp.isMatched()&& temp.isFlipped())
            {
                imgs.get(i).flip();
            }
        }
    }

    public void calculateScore() {
        long duration = (System.currentTimeMillis()/1000) - (startTime/1000);
        duration = (long) (1000/duration);

        Toast.makeText(this,"Congratulations "+ NAME+ " Your score is: "+ duration, Toast.LENGTH_LONG).show();
        finish();
        Intent i = new Intent(GameActivity.this,WinActivity.class);
        i.putExtra(CONSTANTS.USERNAME, NAME);
        i.putExtra(CONSTANTS.USERSCORE,duration);
        startActivity(i);
    }

    public boolean win() {
        for (int i = 0; i < imgs.size(); i++) {
            if(!imgs.get(i).isMatched())
            {
                return false;
            }
        }
        return true;
    }

    public LinkedHashSet<Integer> getNRandom(int n, int max) {

        LinkedHashSet<Integer> generated = new LinkedHashSet<Integer>();
        Random random = new Random();

        while(generated.size() <n){
            Integer thisOne = random.nextInt(max);
            generated.add(thisOne);
        }

        return generated;
    }

    public int howManyFlipped()
    {
        int count = 0;
        for (int i = 0; i < imgs.size(); i++) {
            MyImageButton temp = imgs.get(i);
            if(temp.isFlipped()&& !temp.isMatched())
                count++;
        }
        return count;
    }
    public boolean canFlip()
    {
        if(howManyFlipped() <2 )
            return true;
        return false;
    }
    public void stopAllSounds()
    {
        for (int i = 0; i < imgs.size(); i++) {
            imgs.get(i).stop();
        }
    }

    public boolean checkMatch()
    {
        id1 = null;
        id2= null;
        for (int i = 0; i < imgs.size(); i++) {
            MyImageButton temp= imgs.get(i) ;
            if(temp.isFlipped() &&!temp.isMatched() && id1 ==null)
            {
                id1=imgs.get(i);
            }
            else if(temp.isFlipped() && !temp.isMatched() )
            {
                id2=imgs.get(i);
                break;
            }
        }
        if(id1!= null && id2!=null && id1.getImageID()==id2.getImageID())
        {

            new CountDownTimer(500,500){

                @Override
                public void onTick(long arg0) {}

                @Override
                public void onFinish() {
                    id1.setMatched(true);
                    id2.setMatched(true);
                    id1.setVisibility(View.INVISIBLE);
                    id2.setVisibility(View.INVISIBLE);
                    if(win())
                    {
                        calculateScore();
                    }
                }
            }.start();
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopAllSounds();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAllSounds();
    }
}
