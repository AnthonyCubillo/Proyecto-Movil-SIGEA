package cr.una.ac.sigea.Business;

import android.content.Context;
import android.media.MediaPlayer;

import cr.una.ac.sigea.R;

public class Sonido {

    private Context context;

    private MediaPlayer login;
    private MediaPlayer carga;
    private MediaPlayer error;
    private MediaPlayer cerrar;

    private MediaPlayer boton;

    public Sonido(Context context){
        this.context = context;

        login = MediaPlayer.create(context, R.raw.login);
        carga = MediaPlayer.create(context, R.raw.carga);
        error = MediaPlayer.create(context, R.raw.error);
        cerrar = MediaPlayer.create(context, R.raw.cerrar);
        boton = MediaPlayer.create(context, R.raw.boton);

    }

    public void  playCarga(){
        if( login != null ){
            carga.start();
        }
    }


    public void  playLogin(){
        if( login != null ){
            login.start();
        }
    }

    public void  playError(){
        if( error != null ){
            error.start();
        }
    }

    public void  playCerrar(){
        if( cerrar != null ){
            cerrar.start();
        }
    }

    public void  playBoton(){
        if( boton != null ){
            boton.start();
        }
    }
}
