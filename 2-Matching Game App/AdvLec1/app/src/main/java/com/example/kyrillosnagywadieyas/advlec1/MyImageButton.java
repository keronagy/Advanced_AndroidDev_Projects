package com.example.kyrillosnagywadieyas.advlec1;


import android.content.Context;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageButton;
import android.widget.ImageButton;
import android.widget.Toast;

public class MyImageButton extends AppCompatImageButton {

    private int ImageID;
    private int VoiceID;
    private boolean Flipped;
    private MediaPlayer mp;
    private boolean matched;



    public MyImageButton(Context context, int VID,int IID) {
        super(context);
        this.Flipped =false;
        this.ImageID = IID;
        this.VoiceID = VID;
    }

    public MyImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public int dpFromPx(final Context context, final float px) {
        return (int) (px / context.getResources().getDisplayMetrics().density);
    }

    public void setImageID(int imageID) {
        ImageID = imageID;
    }

    public void setVoiceID(int voiceID) {
        VoiceID = voiceID;
        mp = MediaPlayer.create(getContext(), this.VoiceID);
    }

    public int getImageID() {
        return ImageID;
    }

    public void play()
    {
        if(!matched)
        {
            mp = MediaPlayer.create(getContext(), this.VoiceID);
            mp.start();
        }
    }
    public void stop()
    {
        if(mp.isPlaying())
        {
            mp.stop();
        }
    }
    public void flip()
    {
        if(Flipped && !matched)
        {
            this.setImageResource(R.drawable.think);
            stop();
            Flipped = false;
        }
        else
        {
            this.setImageResource(ImageID);
            play();
            Flipped = true;
        }
    }
    public boolean isFlipped()
    {
        return Flipped;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }
}

