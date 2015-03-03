package com.izv.angel.juegoandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class Principal extends ActionBarActivity {

    public static final int RESUL_JUEGO=1;
    private SharedPreferences pc;
    private String dificultad = "normal";
    private TextView tvPuntuacion;
    private Button btNormal, btDificil, btInsane;
    private MediaPlayer music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actividad_principal);
        pc = getPreferences(Context.MODE_PRIVATE);
        tvPuntuacion = (TextView) findViewById(R.id.tvPuntuacion);
        btNormal = (Button) findViewById(R.id.btNormal);
        btDificil = (Button) findViewById(R.id.btDificil);
        btInsane = (Button) findViewById(R.id.btInsane);
        music = MediaPlayer.create(Principal.this,R.raw.music);
        music.setLooping(true);
        music.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void iniciar(View view){
       Intent i = new Intent(this,Juego.class);
       startActivityForResult(i, RESUL_JUEGO);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        music.release();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESUL_JUEGO) {

            if (resultCode == RESULT_OK) {
                final double score=data.getDoubleExtra("tiempo",0);
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("Puntuacion:");
                LayoutInflater inflater = LayoutInflater.from(this);
                final View vista = inflater.inflate(R.layout.dialogo_puntuacion, null);
                dialog.setView(vista);
                final TextView tv1;
                tv1 = (TextView) vista.findViewById(R.id.tvPuntuacionObtenida);
                tv1.setText(score + "");
                dialog.setPositiveButton("Aceptar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                SharedPreferences.Editor ed = pc.edit();
                                if (dificultad.equals("normal")) {
                                    String p = pc.getString("pnormal", "---");
                                    if(p.equals("---")){
                                        p="999999999.00";
                                    }
                                    if (score < Double.parseDouble(p)) {
                                        ed.putString("pnormal", tv1.getText().toString());
                                        ed.commit();
                                    }
                                } else if (dificultad.equals("dificil")) {
                                    String p = pc.getString("pdificil", "---");
                                    if(p.equals("---")){
                                        p="999999999.00";
                                    }
                                    if (score < Double.parseDouble(p)) {
                                        ed.putString("pdificil", tv1.getText().toString());
                                        ed.commit();
                                    }
                                } else if (dificultad.equals("insane")) {
                                    String p = pc.getString("pinsane", "---");
                                    if(p.equals("---")){
                                        p="999999999.00";
                                    }
                                    if (score < Double.parseDouble(p)) {
                                        ed.putString("pinsane", tv1.getText().toString());
                                        ed.commit();
                                    }
                                }
                            }
                        });
                dialog.show();
            }
        }
    }

    public void normal(View view){
        dificultad = "normal";
        GameView.poblacion = 4;
        String p = pc.getString("pnormal", "---");
        tvPuntuacion.setText(p+" seg");
        btNormal.setBackgroundColor(Color.GREEN);
        btDificil.setBackgroundColor(Color.TRANSPARENT);
        btInsane.setBackgroundColor(Color.TRANSPARENT);
    }

    public void dificil(View view){
        dificultad = "dificil";
        GameView.poblacion = 7;
        String p = pc.getString("pdificil", "---");
        tvPuntuacion.setText(p+" seg");
        btDificil.setBackgroundColor(Color.BLUE);
        btNormal.setBackgroundColor(Color.TRANSPARENT);
        btInsane.setBackgroundColor(Color.TRANSPARENT);
    }

    public void insane(View view){
        dificultad= "insane";
        GameView.poblacion = 15;
        String p = pc.getString("pinsane", "---");
        tvPuntuacion.setText(p+" seg");
        btInsane.setBackgroundColor(Color.RED);
        btNormal.setBackgroundColor(Color.TRANSPARENT);
        btDificil.setBackgroundColor(Color.TRANSPARENT);
    }
}
