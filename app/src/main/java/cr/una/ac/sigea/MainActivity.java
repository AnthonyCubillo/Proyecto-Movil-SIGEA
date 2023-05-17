package cr.una.ac.sigea;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cr.una.ac.sigea.Business.Sonido;
import cr.una.ac.sigea.Domain.Activo;
import cr.una.ac.sigea.Domain.Ubicacion;
import cr.una.ac.sigea.Domain.Usuario;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    //menu
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private View encabezadoLayout;
    private Button btnFormActivo;
    private Button btnCancelarActivo;

    //datos de usuario
    private TextView nombreUsuario;
    private TextView correoUsuario;
    private Usuario sesionUsuario ;

    //codigo QR
    private String nEtiquetaActivo;
    private TextView codigoQR;
    private LinearLayout contendorQR;
    private View view;

    /* Variables http */
    private RequestQueue request;
    private JsonObjectRequest json;

    //Datos para el form
    private ArrayList<Ubicacion> listaUbicacion;
    private Activo activo;

    private static Sonido sonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sonido = new Sonido( MainActivity.this );

        /* Variables http */
        request = Volley.newRequestQueue(MainActivity.this);

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

        btnFormActivo = findViewById(R.id.btnFormActivo);
        btnCancelarActivo = findViewById(R.id.btnCancelarActivo);

        //lista ubicaciones para el spinner
        listaUbicacion = new ArrayList<Ubicacion>();
        getUbicaciones();

    }

    //cargar en el encabezado los datos de sesion de usuario
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

    public void animarContenedorEntrada() {
            LinearLayout contenedorQr = findViewById(R.id.contenedorQr);
            Animation animacion = AnimationUtils.loadAnimation(this, R.anim.opacidad_entrada);
            contenedorQr.setVisibility(View.VISIBLE);
            contenedorQr.startAnimation(animacion);
    }

    public void animarContenedorSalida() {
        LinearLayout contenedorQr = findViewById(R.id.contenedorQr);
        Animation animacion = AnimationUtils.loadAnimation(this, R.anim.opacidad_salida);
        contenedorQr.setVisibility(View.GONE);
        contenedorQr.startAnimation(animacion);
    }

    //metodo para dar accion a boton de superior codigo QR
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.lectorCodigoQr:
                sonido.playBoton();
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
       super.onActivityResult(requestCode,resultCode, data);
        IntentResult resultado = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if( resultado != null ){
            if( resultado.getContents() != null ){
                nEtiquetaActivo = resultado.getContents();
                codigoQR.setText("" + nEtiquetaActivo);
                //contendorQR.setVisibility(view.VISIBLE);
                animarContenedorEntrada();

                //buscar activo
                getActivo();
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
        sonido.playBoton();
        Intent intent = new Intent(this, FormActivo.class);
        intent.putExtra( "usuario", sesionUsuario);
        intent.putExtra( "activo", activo);
        intent.putExtra( "listaUbicacion", listaUbicacion);
        //intent.putExtra( "nEtiqueta", nEtiquetaActivo);
        startActivity(intent);
    }

    public void cancelarForm(View view){
        sonido.playBoton();
        nEtiquetaActivo="";
        //contendorQR.setVisibility(view.GONE);
        animarContenedorSalida();
    }

    //menu de opciones
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;

        switch (item.getItemId()) {
            case R.id.inicio:
                sonido.playBoton();
                intent = new Intent(this, MainActivity.class);
                intent.putExtra( "usuario", sesionUsuario);
                startActivity(intent);

                break;
            case R.id.acercaDe:
                sonido.playBoton();
                intent = new Intent(this, AcercaDe.class);
                intent.putExtra( "usuario", sesionUsuario);
                startActivity(intent);
                break;

            case R.id.cerrarSesion:
                sonido.playCerrar();
                Toast.makeText(this, "Cerrando Aplicación", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //codigo a ejecutar
                        finishAffinity();
                    }
                }, 500);


                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getActivo(){

        try {
            //url api
            String ws_url =  "http://192.168.100.116/service/controller";
            //definir consulta consulta
            String url = ws_url + "/controller_activo.php?consulta=5";
            //Log.d( "URL => ",url);
            Map<String, String> params = new HashMap();
            params.put("n_etiqueta", nEtiquetaActivo);
            Log.d("n_etiqueta =>", "-"+nEtiquetaActivo+"-");

            JSONObject parameters = new JSONObject(params);

            json = new JsonObjectRequest(Request.Method.POST, url,
                    parameters,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            activo = new Activo();

                            try {
                                JSONObject jsonObject  = response.getJSONObject("activo");

                                activo.setN_etiqueta(jsonObject.optString("n_etiqueta"));
                                activo.setMarca(jsonObject.optString("marca"));
                                activo.setModelo(jsonObject.optString("modelo"));
                                activo.setSerie(jsonObject.optString("serie"));
                                activo.setDescripcion(jsonObject.optString("descripcion"));
                                activo.setId_ubicacion(jsonObject.optInt("id_ubicacion"));
                                activo.setNombre_ubicacion(jsonObject.optString("nombre_ubicacion"));
                                activo.setValor_libro(jsonObject.optString("valor_libro"));
                                activo.setCondicion(jsonObject.optString("condicion"));
                                activo.setClase_activo(jsonObject.optString("clase_activo"));
                                activo.setId_funcionario(jsonObject.optInt("id_funcionario"));
                                activo.setDni_funcionario(jsonObject.optString("dni_funcionario"));
                                activo.setNombre_funcionario(jsonObject.optString("nombre_funcionario"));

                                Log.d("Activo =>", activo.toString());
                                btnFormActivo.setVisibility(view.VISIBLE);

                            }catch (JSONException e){
                                Log.d("Error consultar al activo: ", e.getMessage());
                                e.printStackTrace();

                                btnFormActivo.setVisibility(view.GONE);
                                Toast.makeText(MainActivity.this, "El activo no esta registrado en el sistema!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(MainActivity.this,"Ocurrio un error al consultar activo: " + volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            request.add(json);

        }catch (Exception e){
            Log.d("Try catch HTTP: ", e.getMessage());
        }
    }

    public void getUbicaciones(){

        try {
            //url api
            String ws_url =  "http://192.168.100.116/service/controller";
            //definir consulta consulta
            String url = ws_url + "/controller_ubicacion.php?consulta=1";
            //Log.d( "URL => ",url);
            Map<String, String> params = new HashMap();

            JSONObject parameters = new JSONObject(params);

            json = new JsonObjectRequest(Request.Method.POST,
                    url, parameters,
                    new Response.Listener<JSONObject>() {
                        public void onResponse(JSONObject response) {
                            JSONArray jsonArray = response.optJSONArray("ubicaciones");
                            JSONObject jsonObject = null;
                            Ubicacion ubicacion = null;

                            if (jsonArray != null) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ubicacion = new Ubicacion();

                                    try {
                                        jsonObject = jsonArray.getJSONObject(i);

                                        ubicacion.setId(jsonObject.optInt("id_ubicacion"));
                                        ubicacion.setNombre(jsonObject.optString("nombre_ubicacion"));
                                        ubicacion.setDescripcion(jsonObject.optString("descripcion_ubicacion"));

                                        Log.d("Ubicacion =>", ubicacion.toString());
                                        listaUbicacion.add(ubicacion);

                                    } catch (JSONException e) {
                                        Toast.makeText(MainActivity.this, "Las ubicaciones no fueron listadas", Toast.LENGTH_SHORT).show();
                                        Log.d("Error consultar al ubicaciones: ", e.getMessage());
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(MainActivity.this,"Ocurrio un error al listar ubicaciones: " + volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        }
                    });
            request.add(json);

        }catch (Exception e){
            Log.d("Try catch HTTP: ", e.getMessage());
        }
    }


}