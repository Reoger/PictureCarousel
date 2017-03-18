package com.hut.reoger.picturecarousel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.hut.reoger.pictruecarousel.imageBanner;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private imageBanner ban;

    private int[] id = {R.mipmap.ban1,R.mipmap.ban2,R.mipmap.ban3,R.mipmap.ic_launcher_round};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ban = (imageBanner) findViewById(R.id.ban);
        getPhoneWidth();
        initData();


        ban.setOnImageViewSelectClick(new imageBanner.OnImageViewSelectClick() {
            @Override
            public void OnClickListener(int position) {
                Toast.makeText(MainActivity.this,"hello"+position,Toast.LENGTH_SHORT).show();
            }
        });
    }

    List<Bitmap> bitmaps = new ArrayList<>();
    private void initData() {
        for (int i = 0; i < id.length; i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),id[i]);
            bitmaps.add(bitmap);
        }
        ban.addImageToImageBarnner(bitmaps);
    }

    private int  getPhoneWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}
