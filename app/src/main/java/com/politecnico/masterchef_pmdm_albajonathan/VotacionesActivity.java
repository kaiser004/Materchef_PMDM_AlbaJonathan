package com.politecnico.masterchef_pmdm_albajonathan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VotacionesActivity extends AppCompatActivity {

    Toast toast;
    Button botonVotar , btnGuardar;
    RecyclerView recyclerView;
    ArrayList<String> equipos = new ArrayList<>();
    ArrayList<String> presentaciones = new ArrayList<>();
    ArrayList<String> servicios = new ArrayList<>();
    ArrayList<String> sabores = new ArrayList<>();
    ArrayList<String> imagenes = new ArrayList<>();
    ArrayList<String> tripticos = new ArrayList<>();

    Boolean comp = false;
    static Boolean listo = false;
    EventosActivity f = null;
    Boolean cerrar , esta = false;

    //SQLite
    SQLiteDatabase db;
    VotacionesDbHelper dbHelper;


    @Override
    public void onBackPressed() {
        if (esta == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("SALIR");
            builder.setMessage("¿Estas seguro que desesa salir sin mandar la votación?");

            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    cerrar = true;
                    salirApp(cerrar);
                }
            });

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    cerrar = false;
                    salirApp(cerrar);
                }
            });
            builder.create();
            builder.show();
        }else{
            Intent intent = new Intent(VotacionesActivity.this, EventosActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void salirApp(boolean cerrar) {
        if(cerrar) {
            //super.onBackPressed();

            //Hay que borrar la base de datos de SQLite antes de que salga para que no haya problemas.
                Toast.makeText(this, "Vuelve pronto.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(VotacionesActivity.this, EventoActivity.class);
                startActivity(intent);
                f.finish();
                finish();

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votaciones);
        getSupportActionBar().hide();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        if (EventosActivity.estadoEvento.equals("En Curso")) {
            //ID Evento
            buscarEquipos("http://10.0.2.2/masterchef/votaciones_buscar_equipos.php?id=" + CustomAdapter.idEvento);
        } else {
            //ID Evento
            buscarEquiposPasados("http://10.0.2.2/masterchef/votaciones_buscar_equipos_pasados.php?id=" + CustomAdapter.idEvento);
        }

        botonVotar = findViewById(R.id.botonVotar);
        botonVotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comp = CustomAdapterVotaciones.listo;
                if (comp){
                    recogerVotacion();
                    botonVotar.setEnabled(false);
                    listo = true;
                }else{
                    toast = Toast.makeText(VotacionesActivity.this, "Por favor, guarda todas las votaciones antes de enviarlas", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        });
    }

    private void buscarEquipos(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        equipos.add(jsonObject.getString("Nombre_equipo"));
                    } catch (JSONException ex) {
                        toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

                CustomAdapterVotaciones CustomAdapterVotaciones = new CustomAdapterVotaciones(VotacionesActivity.this, equipos);
                recyclerView.setAdapter(CustomAdapterVotaciones);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast = Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void buscarEquiposPasados(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        equipos.add(jsonObject.getString("Nombre_equipo"));
                        presentaciones.add(jsonObject.getString("Presentacion"));
                        servicios.add(jsonObject.getString("Servicio"));
                        sabores.add(jsonObject.getString("Sabor"));
                        imagenes.add(jsonObject.getString("Imagen"));
                        tripticos.add(jsonObject.getString("Triptico"));
                    } catch (JSONException ex) {
                        toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                        toast.show();
                    }
                }

                CustomAdapterVotacionesPasadas CustomAdapterVotacionesPasadas =
                        new CustomAdapterVotacionesPasadas(VotacionesActivity.this, equipos, presentaciones, servicios,  sabores,  imagenes, tripticos);
                recyclerView.setAdapter(CustomAdapterVotacionesPasadas);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                toast = Toast.makeText(getApplicationContext(), "ERROR DE CONEXIÓN", Toast.LENGTH_LONG);
                toast.show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    //Envía las votaciones a la BD
    private void enviarVotaciones(String URL, String[] array) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(VotacionesActivity.this, "Operación exitosa", Toast.LENGTH_SHORT).show();
                esta = true;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(),volleyError.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            protected Map<String , String> getParams() throws AuthFailureError {
                Map<String , String> param = new HashMap<String , String>();
                param.put("presentacion" , array[0]);
                param.put("servicio" , array[1]);
                param.put("sabor" , array[2]);
                param.put("imagen" , array[3]);
                param.put("triptico" , array[4]);
                param.put("juez" , array[5]);
                param.put("evento" , array[6]);
                param.put("equipo" , array[7]);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void recogerVotacion() {
        //Instanciamos la clase VotacionDbHelper
        dbHelper = new VotacionesDbHelper(VotacionesActivity.this);

        // Gets the data repository in write mode
        db = dbHelper.getWritableDatabase();

        //Leemos los datos
        Cursor cursor = db.query(
                Contract.Votaciones.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String presentacion = cursor.getString(0);
            String servicio = cursor.getString(1);
            String sabor = cursor.getString(2);
            String imagen = cursor.getString(3);
            String triptico = cursor.getString(4);
            String juez = cursor.getString(5);
            String evento = cursor.getString(6);
            String equipo = cursor.getString(7);

            String[] array = {presentacion, servicio, sabor, imagen, triptico, juez, evento, equipo};
            enviarVotaciones("http://10.0.2.2/masterchef/votaciones_enviar_votacion.php", array);
        }

        db.execSQL("DELETE FROM " + Contract.Votaciones.TABLE_NAME);
        cursor.close();
        db.close();
    }
}
