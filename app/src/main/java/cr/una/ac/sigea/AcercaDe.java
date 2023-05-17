package cr.una.ac.sigea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.navigation.NavigationView;

import cr.una.ac.sigea.Business.Sonido;
import cr.una.ac.sigea.Domain.Usuario;

public class AcercaDe extends AppCompatActivity {
    //Menu
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private View encabezadoLayout;
    private ImageView logo_sigea;


    //sesion de usuario
    private Usuario sesionUsuario ;
    private static Sonido sonido;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        sonido = new Sonido( AcercaDe.this );

        logo_sigea = findViewById(R.id.logo_sigea);

        //sesion de usuario
        getUserSession();

        rotarAnimacion();
    }

    public void rotarAnimacion(){
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(logo_sigea, "rotationY", 0, 360);
        rotateAnimator.setDuration(800);
        rotateAnimator.setRepeatCount(0);
        rotateAnimator.start();
    }

    public void getUserSession(){
        Bundle bundle = getIntent().getExtras();

        if( bundle != null ){
            /* recuperar datos del bundle */
            sesionUsuario = (Usuario) bundle.getSerializable("usuario");
        }

        if( sesionUsuario == null ){
            //no esta logeado enviar a login
            Intent intent = new Intent( this, Login.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.menu_go_back, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.goBack:
                sonido.playCerrar();
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("usuario", sesionUsuario);
                startActivity(intent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }
}