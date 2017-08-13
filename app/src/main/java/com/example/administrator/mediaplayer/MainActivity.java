package com.example.administrator.mediaplayer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private int i = 0;
    private List<Medias> list;
    boolean flag=false;
/**
 * 播放
 */
    private Button mButPaly;
    /**
     * 上一首
     */
    private Button mButShang;
    /**
     * 下一首
     */
    private Button mButXia;
    /**
     * 暂停
     */
    private Button mButPause;
    /**
     * 重新播放
     */
    private Button mButRev;
    private int duration;
    private SeekBar mBar;
    /**
     * 00.00
     */
    private TextView mTvLeft;
    /**
     * 00.00
     */
    private TextView mTvRight;
    private LinearLayout mActivityMain;
    /**
     * 听音乐,用酷我
     */
    private TextView mTvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        list = new ArrayList<>();
        list.add(new Medias("sdcard/dcim/trans/a.mp3", "喜帖街"));
        list.add(new Medias("sdcard/dcim/trans/b.mp3", "最佳损友"));
        list.add(new Medias("sdcard/dcim/trans/c.mp3", "讲不出再见"));
        mediaPlayer = new MediaPlayer();

        mBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {


                mediaPlayer.seekTo(seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentPosition = mediaPlayer.getCurrentPosition();
            mBar.setProgress(currentPosition);
            int i1 = currentPosition / 1000;//秒
            int i2 = i1 / 60;//分
            if(i1%60<10){
                mTvRight.setText("0"+i2+":0"+i1%60);
            }else{
                mTvRight.setText("0"+i2+":"+i1%60);
            }
        }
    };
    public void play() {

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(list.get(i).path);
            mediaPlayer.prepare();
            duration = mediaPlayer.getDuration();
            mBar.setMax(duration);
            final int time = duration / 1000;//秒
            int fen = time / 60;//分  这里只判断了10分以下的
            if(time%60<10){
                mTvLeft.setText("0"+fen+":0"+time%60);
            }else{
                mTvLeft.setText("0"+fen+":"+time%60);
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
//                    new Thread(){
//                        @Override
//                        public void run() {
//                            super.run();
//                            while (true) {
//                                handler.sendEmptyMessage(0);
//                            }
//                        }
//                    }.start();
                    Timer timer=new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(0);
                        }
                    },1000,1000);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void initView() {
        mButPaly = (Button) findViewById(R.id.butPaly);
        mButPaly.setOnClickListener(this);
        mButShang = (Button) findViewById(R.id.butShang);
        mButShang.setOnClickListener(this);
        mButXia = (Button) findViewById(R.id.butXia);
        mButXia.setOnClickListener(this);
        mButPause = (Button) findViewById(R.id.butPause);
        mButPause.setOnClickListener(this);
        mButRev = (Button) findViewById(R.id.butRev);
        mButRev.setOnClickListener(this);
        mBar = (SeekBar) findViewById(R.id.bar);
        mTvLeft = (TextView) findViewById(R.id.tvLeft);
        mTvRight = (TextView) findViewById(R.id.tvRight);
        mActivityMain = (LinearLayout) findViewById(R.id.activity_main);
        mTvName = (TextView) findViewById(R.id.tvName);
        mBar.setOnClickListener(this);
        mTvLeft.setOnClickListener(this);
        mTvRight.setOnClickListener(this);
        mActivityMain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.butPaly:
                if (mediaPlayer != null) {
                    play();
                    mTvName.setText(list.get(i).getName());
                }
                break;
            case R.id.butShang:
                mBar.setProgress(0);
                if(i<=0){
                    i=list.size();
                }
                i--;
                play();
                mTvName.setText("点了上一首,这首是"+list.get(i).getName());
                break;
            case R.id.butXia:
                mBar.setProgress(0);
                i++;
                i=i%list.size();
                play();
                //设置音乐的名称
                mTvName.setText("点了下一首,这首是"+list.get(i).getName());
                break;
            case R.id.butPause:
                if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                    mButPause.setText("继续");
                    flag=true;
                    //设置音乐的名称
                    mTvName.setText("暂停...."+list.get(i).getName());
                }else{
                    if(flag){
                        mediaPlayer.start();
                        mButPause.setText("暂停");
                        flag=false;
                        //设置音乐的名称
                        mTvName.setText("继续...."+list.get(i).getName());
                    }
                }
                break;
            case R.id.butRev:
                if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
                    //设置当前播放的进度
                    mediaPlayer.seekTo(0);
                    //设置音乐的名称
                    mTvName.setText(list.get(i).getName());
                }
                break;

        }
    }
}
