package com.izv.angel.juegoandroid;


import android.graphics.Canvas;

public class HebraJuego extends  Thread {

    private GameView vista;
    private boolean funcionando = false;
    private static final long FPS = 15;

    public HebraJuego(GameView vj) {
        this.vista = vj;
    }
    public void setFuncionando(boolean f) {
        funcionando = f;
    }

    @Override
    public void run() {
        long inicio;
        long ticksPS = 1100 / FPS;
        long tiempoEspera;
        while (funcionando) {
            Canvas canvas = null;
            inicio = System.currentTimeMillis();
            try {
                canvas = vista.getHolder().lockCanvas();
                synchronized (vista.getHolder()) {
                    vista.draw(canvas);
                }
            }catch(Exception e){
            } finally {
                if (canvas != null) {
                    vista.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            tiempoEspera = ticksPS - (System.currentTimeMillis() - inicio);
            try {
                if (tiempoEspera > 0)
                    sleep(tiempoEspera);
                else
                    sleep(10);
            } catch (InterruptedException e) {}
        }
    }
}



