package com.example.ravi.changewallpaper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnChange,btnMobileData,btnRingChange;
    Bitmap bitmap;
    SeekBar seekBar;
    TextView textView;

    Ringtone ringtone;
    int count;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ;

        btnMobileData = findViewById(R.id.btnMobileData);
        btnChange = findViewById(R.id.btnChange);
        seekBar = findViewById(R.id.seek);
        textView =findViewById(R.id.txtmsg);
        btnRingChange = findViewById(R.id.btnRing);





    //    audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,progress,0);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {



            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {



            }
        });


        btnChange.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

//                WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.this);
//                try {
//
//                    wallpaperManager.setResource(R.drawable.one);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Re"),1);

            }
        });

        btnMobileData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                WifiManager wifiManager= (WifiManager) MainActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
              wifiManager.setWifiEnabled(true);
                Toast.makeText(MainActivity.this, "Wifi is on", Toast.LENGTH_SHORT).show();


            }
        });

        btnRingChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                Intent intent = new Intent();
//                intent.setType("audio/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent,"Re"),66);

                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                startActivityForResult(intent,66);


            }
        });


        try
        {

        }
        catch (Exception e)
        {

        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1)
        {
            Uri uri= data.getData();
            try {
                 bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }

            WallpaperManager wallpaperManager = WallpaperManager.getInstance(MainActivity.this);
              try {

                    wallpaperManager.setBitmap(bitmap);
                  Toast.makeText(this, "WallPaper Sucseefully Changed..", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        if (requestCode ==2)
        {

          Uri uri = data.getData();

            Toast.makeText(this, " "+uri.toString(), Toast.LENGTH_SHORT).show();

                String s = uri.getPath();
//


            File k = new File(s);
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DATA,k.getAbsolutePath());
            contentValues.put(MediaStore.MediaColumns.TITLE,"ring");
            contentValues.put(MediaStore.MediaColumns.SIZE,k.length());
            contentValues.put(MediaStore.Audio.Media.ARTIST,R.string.app_name);
            contentValues.put(MediaStore.Audio.Media.IS_RINGTONE,true);


            contentValues.put(MediaStore.MediaColumns.MIME_TYPE,"audio/mp3");






            Uri uri1 = MediaStore.Audio.Media.getContentUriForPath(k.getAbsolutePath());
     getContentResolver().delete(uri,MediaStore.MediaColumns.DATA+ "=\"",null);


            Uri uri2 = this.getContentResolver().insert(uri1,contentValues);





settingPermision();

                RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this,RingtoneManager.TYPE_RINGTONE,uri2);
                Toast.makeText(this, "Completed", Toast.LENGTH_SHORT).show();



        }


        if(requestCode ==66)
        {



            Uri uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            settingPermision();
            RingtoneManager.setActualDefaultRingtoneUri(MainActivity.this,RingtoneManager.TYPE_RINGTONE,uri);

        }

    }



    public void settingPermision()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {

            if (!Settings.System.canWrite(getApplicationContext()))
            {

                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,Uri.parse("package:"+getPackageName()));
                startActivityForResult(intent,200);

            }
        }


    }
}
