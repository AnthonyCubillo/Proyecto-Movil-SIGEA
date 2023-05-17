package cr.una.ac.sigea;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;


public class Carga extends AppCompatActivity {

    final int duracion = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carga);

        autoredirecionar();
    }

    public void autoredirecionar(){
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