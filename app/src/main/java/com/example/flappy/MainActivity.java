package com.example.flappy;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static TextView txt_score,txt_best_score, txt_score_over;
    public static RelativeLayout r1_game_over;
    public static ImageButton btn_start;
    private GameView gv;
    static MediaPlayer bgm;//배경음악
    static MediaPlayer lose;//사망시

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constants.SCREEN_WIDTH=dm.widthPixels;
        Constants.SCREEN_HEIGHT=dm.heightPixels;
        setContentView(R.layout.activity_main);
        txt_score = findViewById(R.id.txt_score);
        txt_best_score = findViewById(R.id.txt_best_score);
        txt_score_over=findViewById(R.id.txt_score_over);
        r1_game_over = findViewById(R.id.r1_game_over);
        btn_start=findViewById(R.id.btn_start);
        gv = findViewById(R.id.gv);

        SoundManager.getInstance().Init(this);//사운드 매니저 호출
        SoundManager.getInstance().addSound(1, R.raw.crush);//효과음 1
        SoundManager.getInstance().addSound(2, R.raw.coin);//효과음 2
        bgm = MediaPlayer.create(this, R.raw.moon);//배경음악
        lose = MediaPlayer.create(this, R.raw.lose);//사망시

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gv.setStart(true);
                bgm.start();//배경음악 재생
                txt_score.setVisibility(View.VISIBLE);
                btn_start.setVisibility(View.INVISIBLE);
            }
        });
        r1_game_over.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_start.setVisibility(View.VISIBLE);
                r1_game_over.setVisibility(View.INVISIBLE);
                gv.setStart(false);
                gv.reset();
                bgm.prepareAsync();
                lose.stop();
                lose.prepareAsync();
            }
        });
    }

    @Override
    protected void onDestroy(){//MediaPlayer 릴리즈
        super.onDestroy();
        if(bgm != null){
            bgm.release();
            lose.release();
            bgm = null;
        }
    }
}