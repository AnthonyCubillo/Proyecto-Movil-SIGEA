package cr.una.ac.sigea;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cr.una.ac.sigea.Business.Sonido;
import cr.una.ac.sigea.Domain.Activo;
import cr.una.ac.sigea.Domain.Ubicacion;
import cr.una.ac.sigea.Domain.Usuario;

public class FormActivo extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    //sesion de usuario
    private Usuario sesionUsuario ;

    //ubicaciones select
    private  String[] ubicaciones;

    private Ubicacion ubicacion;
    private ArrayList<Ubicacion> listaUbicacion;
    private  Spinner sp_ubicaciones;
    private ArrayAdapter<String> adapter;


    /* Variables http */
    private ProgressDialog loading;
    private RequestQueue request;
    private JsonObjectRequest json;

    //activo
    private Activo activo;
    private TextView tv_nEtiqueta;
    private TextView tv_descripcion;
    private TextView tv_marca;
    private TextView tv_modelo;
    private TextView tv_serie;
    private TextView tv_valor_libros;
    private TextView tv_condicion;
    private TextView tv_clase_activo;
    private TextView tv_dni_func;
    private TextView tv_nombre_func;

    private static Sonido sonido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_activo);

        sonido = new Sonido( FormActivo.this );

        /* Variables http */
        request = Volley.newRequestQueue(this);

        listaUbicacion = new ArrayList<Ubicacion>();

        //Variables de interfaz
        tv_nEtiqueta = findViewById(R.id.tv_nEtiqueta);
        tv_descripcion = findViewById(R.id.tv_descripcion);
        tv_marca = findViewById(R.id.tv_marca);
        tv_modelo = findViewById(R.id.tv_modelo);
        tv_serie = findViewById(R.id.tv_serie);
        tv_valor_libros = findViewById(R.id.tv_valor_libros);
        tv_condicion = findViewById(R.id.tv_condicion);
        tv_clase_activo = findViewById(R.id.tv_clase_activo);
        tv_dni_func = findViewById(R.id.tv_dni_func);
        tv_nombre_func = findViewById(R.id.tv_nombre_func);
        sp_ubicaciones = findViewById(R.id.sp_ubicaciones);

        // Sesion de Usuario
        getUserSession();

        //escuchar cambios en el select
        sp_ubicaciones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Este método se llama cuando se ha seleccionado ningún ítem
                sonido.playBoton();

                String selectedItem = parent.getItemAtPosition(position).toString();

                for(int i = 0; i < listaUbicacion.size(); i++){
                    if( listaUbicacion.get(i).getNombre().equals(selectedItem) ){
                        ubicacion = (Ubicacion) listaUbicacion.get(i);
                        i = listaUbicacion.size();
                        break;
                    }
                }
                //Log.d("Ubicacio elegida: =======>", ubicacion.toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Este método se llama cuando no se ha seleccionado ningún ítem
                ubicacion = (Ubicacion) listaUbicacion.get(0);
                //Log.d("Ubicacio elegida: =======>", ubicacion.toString());
            }
        });

    }

    public void getUserSession(){
        Bundle bundle = getIntent().getExtras();

        if( bundle != null ){
            /* recuperar datos del bundle */
            sesionUsuario = (Usuario) bundle.getSerializable("usuario");
            listaUbicacion = (ArrayList<Ubicacion>) bundle.getSerializable("listaUbicacion");
            activo = (Activo) bundle.getSerializable("activo");
        }

        if( sesionUsuario == null){
            //no esta logeado enviar a login
            Intent intent = new Intent( this, Login.class);
            startActivity(intent);
        }

        if(listaUbicacion == null || activo == null){
            //no se envio l etiqueta del activo
            enviarMainActivity();
        }

        getData();
    }

    public void getData(){
        //listar las ubicaciones
        ubicaciones = new String[ listaUbicacion.size() ];
        for (int i = 0; i < listaUbicacion.size(); i++) {
            ubicaciones[i]= listaUbicacion.get(i).getNombre();
        }

        adapter = new ArrayAdapter<String>(FormActivo.this, android.R.layout.simple_spinner_dropdown_item, ubicaciones);
        sp_ubicaciones.setAdapter(adapter);

        cargarDatosActivos();
    }

    public void cargarDatosActivos(){
        tv_nEtiqueta.setText(tv_nEtiqueta.getText().toString()   + activo.getN_etiqueta());
        tv_descripcion.setText(tv_descripcion.getText().toString()   + activo.getDescripcion());
        tv_marca.setText(tv_marca.getText().toString()   + activo.getMarca());
        tv_modelo.setText(tv_modelo.getText().toString()   + activo.getModelo());
        tv_serie.setText(tv_serie.getText().toString()   + activo.getSerie());
        tv_valor_libros.setText(tv_valor_libros.getText().toString()   + activo.getValor_libro());
        tv_condicion.setText(tv_condicion.getText().toString()   + activo.getCondicion());
        tv_clase_activo.setText(tv_clase_activo.getText().toString()   + activo.getClase_activo());
        tv_dni_func.setText(tv_dni_func.getText().toString()  + activo.getDni_funcionario());
        tv_nombre_func.setText(tv_nombre_func.getText().toString()  + activo.getNombre_funcionario());

        if(ubicaciones != null) {
            int position = 0;
            for (int i = 0; i < ubicaciones.length; i++) {
                if (ubicaciones[i].equals(activo.getNombre_ubicacion())) {
                    position = i;
                    break;
                }
            }

            ubicacion = (Ubicacion) listaUbicacion.get(position);

            sp_ubicaciones.setSelection(position);
            Log.d("Ubicacio elegida: =======>", ubicacion.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.menu_go_back, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void enviarMainActivity(){
        Intent intent = new Intent(FormActivo.this, MainActivity.class);
        intent.putExtra("usuario", sesionUsuario);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch ( item.getItemId() ){
            case R.id.goBack:
                sonido.playCerrar();
                enviarMainActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void actualizarActivo(View view){
        sonido.playBoton();

        try {
            //url api
            String ws_url =  "http://192.168.100.116/service/controller";
            //definir consulta consulta
            String url = ws_url + "/controller_activo.php?consulta=6";
            //Log.d( "URL => ",url);
            Map<String, String> params = new HashMap();
            params.put("n_etiqueta", activo.getN_etiqueta());
            params.put("marca", activo.getMarca());
            params.put("modelo", activo.getModelo());
            params.put("serie", activo.getSerie());
            params.put("descripcion", activo.getDescripcion());
            params.put("id_ubicacion", ""+ubicacion.getId());
            params.put("valor_libro", activo.getValor_libro());
            params.put("condicion", activo.getCondicion());
            params.put("clase_activo", activo.getClase_activo());
            params.put("id_funcionario", ""+activo.getId_funcionario());

            JSONObject parameters = new JSONObject(params);

            json = new JsonObjectRequest(Request.Method.POST, url, parameters,this,this);
            request.add(json);

            //mensaje de carga mientras se hace consulta a API
            loading = new ProgressDialog( FormActivo.this);
            loading.show();
            loading.setContentView(R.layout.progress_dialog);
            loading.getWindow().setBackgroundDrawableResource(
                    android.R.color.transparent
            );

        }catch (Exception e){
            Log.d("Try catch HTTP: ", e.getMessage());
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this,  "Ocurrio un error durante la consulta a BD: " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONObject response) {

        try {
            // Convertir la respuesta a un objeto JSONObject
            JSONObject jsonResponse = new JSONObject(response.toString());

            Log.d("Array[0] =====>", ""+jsonResponse.optBoolean("success"));
            if( jsonResponse.optBoolean("success") ){
                //si es exitosa
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //codigo a ejecutar

                        Toast.makeText(FormActivo.this, "El activo fue actualizado correctamente!", Toast.LENGTH_SHORT).show();

                        loading.dismiss();

                        enviarMainActivity();
                    }
                }, 1000);

            }

        }catch (JSONException e){
            Toast.makeText(FormActivo.this, "El activo no fue actualizado!", Toast.LENGTH_SHORT).show();
            Log.d("Error al actualizar el activo: ", e.getMessage());
            e.printStackTrace();
        }
    }
}