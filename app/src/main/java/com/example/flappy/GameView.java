package com.example.flappy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GameView extends View {
    private Bird bird;
    private Handler handler;
    private Runnable r;
    private ArrayList<Pipe> arrPipes;
    private int sumpipe, distance;
    private int score, bestscore=0;
    private boolean start;
    private Context context;
    private boolean isCrash = false;

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        SharedPreferences sp = context.getSharedPreferences("gamesetting",Context.MODE_PRIVATE);
        if(sp!=null){
            bestscore = sp.getInt("bestscore",0);
        }
        score=0;
        bestscore=0;
        start =false;
        initBird();
        initPipe();
        handler = new Handler();
        r= new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
    }
    private void initPipe(){
        sumpipe = 5;
        distance =300*Constants.SCREEN_HEIGHT/1920;
        arrPipes = new ArrayList<>();
        for(int i=0;i<sumpipe;i++){
            if(i<sumpipe/2){
                this.arrPipes.add(new Pipe(Constants.SCREEN_WIDTH+i*((Constants.SCREEN_WIDTH+200*Constants.SCREEN_WIDTH/1080)/(sumpipe/2)),
                        0, 200*Constants.SCREEN_WIDTH/1080,Constants.SCREEN_HEIGHT/2));
                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(),R.drawable.bottomtube));
                this.arrPipes.get(this.arrPipes.size()-1).randomY();
            }else{
                this.arrPipes.add(new Pipe(this.arrPipes.get(i-sumpipe/2).getX(),this.arrPipes.get(i-sumpipe/2).getY()
                +this.arrPipes.get(i-sumpipe/2).getHeight()+this.distance,200*Constants.SCREEN_WIDTH/1080,Constants.SCREEN_HEIGHT/2));
                this.arrPipes.get(this.arrPipes.size()-1).setBm(BitmapFactory.decodeResource(this.getResources(),R.drawable.toptube));
            }
        }
    }
    private void initBird(){
        bird = new Bird();
        bird.setWidth(80*Constants.SCREEN_WIDTH/1080);
        bird.setHeight(60*Constants.SCREEN_HEIGHT/1920);
        bird.setX(100*Constants.SCREEN_WIDTH/1080);
        bird.setY(Constants.SCREEN_HEIGHT/2-bird.getHeight()/2);
        ArrayList<Bitmap> arrBms = new ArrayList<>();
        arrBms.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird));
        arrBms.add(BitmapFactory.decodeResource(this.getResources(),R.drawable.bird2));
        bird.setArrBms(arrBms);
    }
    public void draw(Canvas canvas){
        super.draw(canvas);
        if(start){
            bird.draw(canvas);
            for(int i=0;i<sumpipe;i++){
                if(isCrash == false) {
                    if (bird.getRect().intersect(arrPipes.get(i).getRect()) || bird.getY() - bird.getHeight() < 0 || bird.getY() > Constants.SCREEN_HEIGHT) {
                        Pipe.speed = 0;
                        MainActivity.txt_score_over.setText(MainActivity.txt_score.getText());
                        MainActivity.txt_best_score.setText("best" + bestscore);
                        MainActivity.txt_score.setVisibility(INVISIBLE);
                        MainActivity.r1_game_over.setVisibility(VISIBLE);
                        MainActivity.bgm.stop();
                        MainActivity.lose.start();
                        isCrash = true;
                        SoundManager.getInstance().play(1);//효과음 1 출력
                    }
                }
                if(this.bird.getX()+this.bird.getWidth()>arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2
                        &&this.bird.getX()+this.bird.getWidth()<=arrPipes.get(i).getX()+arrPipes.get(i).getWidth()/2+Pipe.speed
                        &&i<sumpipe/2){
                    SoundManager.getInstance().play(2);//효과음 2 출력
                    score++;
                    if(score>bestscore){
                        bestscore = score;
                        SharedPreferences sp = context.getSharedPreferences("gameSetting",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("bestscore",bestscore);
                        editor.apply();
                    }
                    MainActivity.txt_score.setText(""+score);
                }
                if(this.arrPipes.get(i).getX()<  -arrPipes.get(i).getWidth()){
                    this.arrPipes.get(i).setX(Constants.SCREEN_WIDTH);
                    if(i<sumpipe/2){
                        arrPipes.get(i).randomY();
                    }else{
                        arrPipes.get(i).setY(this.arrPipes.get(i-sumpipe/2).getY()
                                +this.arrPipes.get(i-sumpipe/2).getHeight()+this.distance);
                    }
                }
                this.arrPipes.get(i).draw(canvas);
            }
        }else{
            if(bird.getY()>Constants.SCREEN_HEIGHT/2){
                bird.setDrop(-15*Constants.SCREEN_HEIGHT/1920);
            }
            bird.draw(canvas);
        }

        handler.postDelayed(r,10);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            bird.setDrop(-15);
        }
        return true;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }
    public void reset(){
        MainActivity.txt_score.setText("0");
        score=0;
        isCrash = false;
        initBird();
        initPipe();;
    }
}
