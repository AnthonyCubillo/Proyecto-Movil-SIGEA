package cr.una.ac.sigea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import cr.una.ac.sigea.Domain.Usuario;
import cr.una.ac.sigea.Fragments.Acerca_de;
import cr.una.ac.sigea.Fragments.CodigoQr;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //menu
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private View encabezadoLayout;

    //datos de usuario
    private TextView nombreUsuario;
    private TextView correoUsuario;
    private Usuario sesionUsuario ;

    //codigo QR
    private String nEtiquetaActivo;
    private TextView codigoQR;
    private LinearLayout contendorQR;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //menu
        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view_main);
        navigationView.setNavigationItemSelectedListener(this);

        /* Agrega el menu lateral*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //datos de usuario
        encabezadoLayout = navigationView.getHeaderView(0);
        nombreUsuario = encabezadoLayout.findViewById(R.id.nombreUsuario);
        correoUsuario = encabezadoLayout.findViewById(R.id.emailUsuario);
        cargarDatos();

        //codigo QR
        codigoQR = findViewById(R.id.tvnEtiquetaQr);
        contendorQR = findViewById(R.id.contenedorQr);
        contendorQR.setVisibility(view.GONE);
        nEtiquetaActivo = "";
    }

    //cargar en el fragment los datos de sesion de usuario
    public void cargarDatos(){
        Bundle bundle = getIntent().getExtras();

        if( bundle != null ){
            /* recuperar datos del bundle */
            sesionUsuario = (Usuario) bundle.getSerializable("usuario");

            /* rellenar informacion */
            nombreUsuario.setText( sesionUsuario.getNombre_usuario() );
            correoUsuario.setText( sesionUsuario.getEmail_usuario() );
        }

       if( sesionUsuario == null ){
            //no esta logeado enviar a login
           Intent intent = new Intent( this, Login.class);
           startActivity(intent);
        }
    }

    //metodo para agregar boton de compartir parte superior
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.menu_lector_codigoqr, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //metodo para dar accion a boton de superior codigo QR
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.lectorCodigoQr:
                new IntentIntegrator(this)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES) //solo leer qr code
                        .setTorchEnabled(false) //activar el flash
                        .setBeepEnabled(true) //sonido al leer qe
                        .setPrompt("Escanear codigoQr del Activo") //titulo
                        .initiateScan(); //inicio

                codigoQR.setText("Esperando... scaneo de codigo QR");
                nEtiquetaActivo="";
                contendorQR.setVisibility(view.GONE);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //metodo de escaneo del codigo QR
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       // super.onActivityResult(requestCode,resultCode, data);

        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if( resultado != null ){
            if( resultado.getContents() != null ){
                nEtiquetaActivo = resultado.getContents();
                codigoQR.setText("" + nEtiquetaActivo);
                contendorQR.setVisibility(view.VISIBLE);

            }else{
                Toast.makeText(this, "Lectura codigo Qr cancelada", Toast.LENGTH_SHORT).show();
                nEtiquetaActivo="";
                contendorQR.setVisibility(view.GONE);
            }

        }else{
            super.onActivityResult(requestCode, resultCode, data);
            nEtiquetaActivo="";
            contendorQR.setVisibility(view.GONE);
        }

    }


    //mostrar el formulario con los datos del activo
    public void mostrarForm(View view){
        Toast.makeText(this, "Mostrar Formulario", Toast.LENGTH_SHORT).show();
    }

    //menu de opciones
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.inicio:
                intent = new Intent(this, MainActivity.class);
                intent.putExtra( "usuario", sesionUsuario);
                startActivity(intent);

                break;
            case R.id.acercaDe:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Acerca_de()).commit();
                break;

            case R.id.cerrarSesion:
                Toast.makeText(this, "Cerraste Sesión", Toast.LENGTH_SHORT).show();

                intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}