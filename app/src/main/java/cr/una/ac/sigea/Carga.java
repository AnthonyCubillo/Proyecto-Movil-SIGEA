package cr.una.ac.sigea;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import cr.una.ac.sigea.Business.Sonido;


public class Carga extends AppCompatActivity {

    final int duracion = 5000;
    //Sonido
    private static Sonido sonido;

    private ImageView carga_sigea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);
        sonido = new Sonido( Carga.this );
        carga_sigea = findViewById(R.id.carga_sigea);

        rotarAnimacion();

        autoredirecionar();
    }

    public void rotarAnimacion(){
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(carga_sigea, "rotationY", 0, 360);
        rotateAnimator.setDuration(1000);
        rotateAnimator.setRepeatCount(0);
        rotateAnimator.start();
    }

    public void autoredirecionar(){

        sonido.playCarga();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* codigo que se ejecutara*/
                Intent intent = new Intent( Carga.this, Login.class);
                startActivity( intent );
                finish();
            }
        }, duracion);
    }

}