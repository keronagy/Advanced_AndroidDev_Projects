package com.example.kyrillosnagywadieyas.advlec1;

import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class GridViewAdap extends BaseAdapter {
    int [] imgsId= {R.drawable.i1,R.drawable.i1,R.drawable.i2,R.drawable.i2,R.drawable.i3,R.drawable.i3,R.drawable.i4,R.drawable.i4};
    int [] Vid = {R.raw.v1,R.raw.v1,R.raw.v2,R.raw.v2,R.raw.v3,R.raw.v3,R.raw.v4,R.raw.v4};

    int [] RandIId= {R.drawable.i1,R.drawable.i1,R.drawable.i2,R.drawable.i2,R.drawable.i3,R.drawable.i3,R.drawable.i4,R.drawable.i4};
    int [] RamdVid = {R.raw.v1,R.raw.v1,R.raw.v2,R.raw.v2,R.raw.v3,R.raw.v3,R.raw.v4,R.raw.v4};
    ArrayList<MyImageButton> Imgs ;
    Context context ;
    public GridViewAdap(Context context)
    {

        Imgs = new ArrayList<MyImageButton>();

        HashSet<Integer> random = getNRandom(8,8);
        Integer[] arr = random.toArray(new Integer[random.size()]);

        for (int i =0; i<8 ; i++)
        {
            int k = random.iterator().next();

            RandIId[i] =  imgsId[k];
            RamdVid[i] =  Vid[k];
            MyImageButton temp = new MyImageButton(context,Vid[arr[i]],imgsId[arr[i]]);
            Imgs.add(temp);
            this.context =  context;
        }



    }
    @Override
    public int getCount() {
        return Imgs.size();
    }

    @Override
    public Object getItem(int position) {
        return Imgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder
    {
        MyImageButton myImageButton;
        ViewHolder(View view)
        {
            //myImageButton = (MyImageButton) view.findViewById(R.id.imageView);
        }
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



//        View row = convertView;
//        ViewHolder holder = null;
//        if(convertView == null)
//        {
//            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//            layoutInflater.inflate(R.layout.adapter_item,parent,false);
//            holder = new ViewHolder(row);
//            row.setTag(holder);
//        }
//        else
//        {
//            holder = (ViewHolder) row.getTag();
//        }
        MyImageButton my= Imgs.get(position);
//        my.setImageResource(my.ImageID);
//        my.setLayoutParams(new GridView.LayoutParams(100, 100));

        return my;
    }







    public HashSet<Integer> getNRandom(int n, int max) {
        HashSet<Integer> set = new HashSet<Integer>();
        Random random = new Random();

        while(set.size() <n){
            int thisOne = random.nextInt(max);
            set.add(thisOne);
        }

        return set;
    }
}
