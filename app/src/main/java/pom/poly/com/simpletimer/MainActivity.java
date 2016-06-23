package pom.poly.com.simpletimer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.btStart)
    Button btStart;
    @BindView(R.id.btPause)
    Button btPause;
    @BindView(R.id.btReset)
    Button btReset;

    private Timer timer;
    private Handler mHandler;
    private int second = 0;
    private SharedPreferences sharePreference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tvTime = (TextView) findViewById(R.id.tvTime);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == 1) {
                    tvTime.setText(msg.arg1 + "");
                }
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeTheTimer();

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAndSavethetimer();
    }

    private void stopAndSavethetimer() {
        if (timer != null) {
            timer.cancel();
            SharedPreferences.Editor editer = sharePreference.edit();
            editer.putInt(getString(R.string.key_preference_timer), second);
            editer.commit();
        }


    }

    private void resumeTheTimer() {
        sharePreference = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        second = sharePreference.getInt(getString(R.string.key_preference_timer), 0);
        tvTime.setText(second + "");
    }

    private void startTheTimer() {
        timer = new Timer(true);
        timer.schedule(new MyTimerTask(), 1000, 1000);

    }

    private void resetTheTimer() {
        if (timer != null) {
            timer.cancel();
        }
        SharedPreferences.Editor editer = sharePreference.edit();
        editer.putInt(getString(R.string.key_preference_timer), 0);
        editer.commit();
        tvTime.setText(0 + "");
    }


    @OnClick(R.id.btStart)
    public void onClick1() {
        resumeTheTimer();
        startTheTimer();
    }

    @OnClick(R.id.btPause)
    public void onClick2() {
        stopAndSavethetimer();
    }

    @OnClick(R.id.btReset)
    public void onClick3() {
        resetTheTimer();
    }

    private class MyTimerTask extends TimerTask {


        @Override
        public void run() {
            second++;
            Log.d("second", second + "");
            Message message = new Message();
            message.what = 1;
            message.arg1 = second;
            mHandler.sendMessage(message);

        }
    }
}
