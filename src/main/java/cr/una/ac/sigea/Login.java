package cr.una.ac.sigea;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cr.una.ac.sigea.Domain.Usuario;

public class Login extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener{

    //inicializar elementos interfaz
    private EditText et_usuario;
    private EditText et_clave;

    /* Variables http */
    private ProgressDialog loading;
    private RequestQueue request;
    private JsonObjectRequest json;

    //usuario logeado
    private Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //elementos interfaz
        et_usuario = findViewById(R.id.et_usuario);
        et_clave = findViewById(R.id.et_clave);

        /* Variables http */
        request = Volley.newRequestQueue(this);
    }

    public void limpiarEntradas() {
        et_usuario.setText("");
        et_clave.setText("");
    }

    public void iniciarSesion(){
        Intent intent = new Intent(Login.this, MainActivity.class);
        intent.putExtra( "usuario", user);
        startActivity(intent);
    }

    public void enviarPeticionLogin(String usuario, String clave){

        try {
            //url api
            String ws_url =  "http://172.22.144.1/Programas/service/controller";
            //definir consulta consulta
            String url = ws_url + "/controller_usuario.php?consulta=5";
            //Log.d( "URL => ",url);
            Map<String, String> params = new HashMap();
            params.put("dni_usuario", usuario);
            params.put("clave_usuario", clave);

            JSONObject parameters = new JSONObject(params);

            json = new JsonObjectRequest(Request.Method.POST, url, parameters, this, this);
            request.add(json);

        }catch (Exception e){
            Log.d("Try catch HTTP: ", e.getMessage());
        }
    }


    public void btnIniciarSesion(View view){

        //recuperar datos ingresados
        String usuario = et_usuario.getText().toString();
        String clave= et_clave.getText().toString();
        //Log.d( "Usuario => ",usuario);
        //Log.d( "Clave => ",clave);

        //validacion de formulario
        if( usuario.equals("") ||  clave.equals("") ){
            Toast.makeText(this, "Debe ingresar un usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        //mensaje de carga mientras se hace consulta a API
        //loading = ProgressDialog.show(this,"Verificando usuario...","Espere por favor...",false,false);
        loading = new ProgressDialog( Login.this);
        loading.show();
        loading.setContentView(R.layout.progress_dialog);
        loading.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent
        );

        //realizar peticion
        enviarPeticionLogin(usuario, clave);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(this,  "Ocurrio un error durante el logeo: " + error.getMessage(), Toast.LENGTH_SHORT).show();
        loading.dismiss();
    }

    @Override
    public void onResponse(JSONObject response) {
        user = new Usuario();

        try {
            JSONObject jsonObject  = response.getJSONObject("usuario");

            user.setDni_usuario( jsonObject.optString("dni_usuario"));
            user.setNombre_usuario( jsonObject.optString("nombre_usuario"));
            user.setEmail_usuario( jsonObject.optString("email_usuario"));

            //Log.d("usuario LOGIN =>", user.toString());

        }catch (JSONException  e){
            Toast.makeText(this, "Usuario o contraseña incorrecta!", Toast.LENGTH_SHORT).show();
            Log.d("Error Login: ", e.getMessage());
            e.printStackTrace();

        }finally {

            if ( user.getDni_usuario() != null ) {
                //ocultar mensaje de carga
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //codigo a ejecutar
                        loading.dismiss();
                    }
                }, 2000);

                //enviar a inicio
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //codigo a ejecutar

                        limpiarEntradas();
                        iniciarSesion();
                    }
                }, 2000);
            }else{
                //ocultar mensaje de carga
                loading.dismiss();
            }

        }
    }

}