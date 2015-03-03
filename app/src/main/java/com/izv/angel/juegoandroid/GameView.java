package com.izv.angel.juegoandroid;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap bmp;
    private int alto, ancho;
    private HebraJuego hebraJuego;
    private List<Sprite> sprites = new ArrayList<Sprite>();
    private List<SpriteTemp> temps = new ArrayList<SpriteTemp>();
    private long lastClick=0;
    private Bitmap bmpBlood;
    private Bitmap floor;
    private double tiempoI, tiempoT;
    boolean terminado = false;
    public static int poblacion = 4;
    private SoundPool sound;
    private int smash;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        hebraJuego = new HebraJuego(this);
        bmpBlood = BitmapFactory.decodeResource(getResources(), R.mipmap.blood1);
        floor = BitmapFactory.decodeResource(getResources(), R.mipmap.floor);
        tiempoI = (double)Calendar.getInstance().getTimeInMillis();
        sound = new SoundPool(10, AudioManager.STREAM_MUSIC,0);
        smash = sound.load(getContext(),R.raw.smash,1);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        for (int i = 0; i < poblacion; i++) {
            createSprites();
        }
        Bitmap background = BitmapFactory.decodeResource(getResources(), R.mipmap.floor);
        float scale = (float) background.getHeight() / (float) getHeight();
        int newWidth = Math.round(background.getWidth() / scale);
        int newHeight = Math.round(background.getHeight() / scale);
        floor = Bitmap.createScaledBitmap(background, newWidth, newHeight, true);
        hebraJuego.setFuncionando(true);
        hebraJuego.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        alto = height;
        ancho = width;
        Sprite.setDimension(ancho, alto);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean reintentar = true;
        hebraJuego.setFuncionando(false);
        while (reintentar) {
            try {
                hebraJuego.join();
                reintentar = false;
            } catch (InterruptedException e) {
            }
        }


    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawBitmap(floor, 0, 0, null);
        for (int i = temps.size() - 1; i >= 0; i--) {
            temps.get(i).onDraw(canvas);
        }
        for (Sprite sprite : sprites) {
            sprite.onDraw(canvas);
        }
    }

    private void createSprites() {
        sprites.add(createSprite(R.mipmap.pj1));
        sprites.add(createSprite(R.mipmap.pj2));
        sprites.add(createSprite(R.mipmap.pj3));
        sprites.add(createSprite(R.mipmap.pj4));
        sprites.add(createSprite(R.mipmap.pj5));
        sprites.add(createSprite(R.mipmap.pj6));
        sprites.add(createSprite(R.mipmap.pj7));
        sprites.add(createSprite(R.mipmap.pj8));
        sprites.add(createSprite(R.mipmap.pj9));
        sprites.add(createSprite(R.mipmap.pj10));
    }

    private Sprite createSprite(int resouce) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resouce);
        return new Sprite(this,bmp);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 150) {
            lastClick = System.currentTimeMillis();
            float x = event.getX();
            float y = event.getY();
            synchronized (getHolder()) {
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    if (sprite.isCollition(x, y)) {
                        sprites.remove(sprite);
                        sound.play(smash,0.8f,0.8f,0,0,1.5f);
                        temps.add(new SpriteTemp(temps, this, x, y, bmpBlood));
                        break;
                    }
                }
            }
        }
        if(sprites.size()==0 && terminado == false){
            terminado = true;
            tiempoT = (double) ((Calendar.getInstance().getTimeInMillis() - tiempoI)/1000.00);
            Log.v("tiempo",tiempoT+"");
            Intent i = new Intent(this.getContext(),Principal.class);
            i.putExtra("tiempo", tiempoT);
            ((Activity) getContext()).setResult(Activity.RESULT_OK,i);
            ((Activity) getContext()).finish();
        }
        return true;
    }
}
