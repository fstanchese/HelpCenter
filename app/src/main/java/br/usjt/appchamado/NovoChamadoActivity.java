package br.usjt.appchamado;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NovoChamadoActivity extends AppCompatActivity  {
    private String TAG = NovoChamadoActivity.class.getSimpleName();
    private ArrayList<Fila> listaFila = null;
    private ArrayList<SLA> listaSLA = null;
    private Spinner filasSpinner;
    private Spinner SLASpinner;
    private Usuario usuario;
    EditText editDescricao,editAssunto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTitle("Novo Chamado");
        setContentView(R.layout.activity_novo_chamado);

        Bundle args = getIntent().getBundleExtra("args");
        usuario = (Usuario) args.getSerializable("usuario");

        filasSpinner = (Spinner) findViewById(R.id.fila_spinner);
        SLASpinner = (Spinner) findViewById(R.id.sla_spinner);
        editDescricao = (EditText)findViewById(R.id.chamado_descricao);
        editAssunto = (EditText)findViewById(R.id.chamado_assunto);

        FilaRequester filaRequester = new FilaRequester(this);
        filaRequester.execute("http://107.170.41.209:8080/chamado/rest/v1/filas");

        SLARequester slaRequester = new SLARequester(this);
        slaRequester.execute("http://107.170.41.209:8080/chamado/rest/v1/sla?id="+usuario.getSla().getId());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class FilaRequester extends AsyncTask<String, String, String> {
        private ProgressDialog progress;
        private Context context;

        public FilaRequester(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setMessage("Carregando...");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonFilas = null;

            try{
                publishProgress("Carregando...");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(params[0]).build();
                Response response = client.newCall(request).execute(); //error aqui
                jsonFilas = response.body().string();

            }
            catch(IOException e){}

            return(jsonFilas);
        }

        @Override
        protected void onPostExecute(String jsonFilas) {
            progress.dismiss();
            depoisFilaRequester(jsonFilas);
        }
    }

    public void depoisFilaRequester(String jsonFilas) {
        listaFila = new ArrayList<Fila>();

        try {
            JSONArray root = new JSONArray(jsonFilas);
            JSONObject item = null;
            for (int i = 0; i < root.length(); i++) {
                item = (JSONObject) root.get(i);

                Long idFila = item.getLong("id");
                String descricao = item.getString("descricao");
                Usuario gerente = null;
                String nomeSolucionador = item.getString("nomeSolucionador");
                String JSONObject = item.getString("gerente");

                if(JSONObject != "null" && !JSONObject.isEmpty())
                {
                    JSONObject reader = new JSONObject(JSONObject);

                    Long idGerente = reader.getLong("id");
                    String nome = reader.getString("nome");
                    String celular = reader.getString("celular");
                    String email = reader.getString("email");
                    String login = reader.getString("login");
                    TipoUsuario tipoUsuario = TipoUsuario.valueOf(reader.getString("tipoUsuario"));
                    SLA sla = null;
                    gerente = new Usuario(idGerente, nome, celular, email, tipoUsuario, login, sla);

                }

                Fila fila = new Fila(idFila,descricao,gerente,nomeSolucionador);
                listaFila.add(fila);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<Fila> spinnerAdapter = new ArrayAdapter<Fila>(this,
                android.R.layout.simple_spinner_item, listaFila);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        filasSpinner.setAdapter(spinnerAdapter);
    }

    public class SLARequester extends AsyncTask<String, String, String> {
        private ProgressDialog progress;
        private Context context;

        public SLARequester(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(context);
            progress.setMessage("Carregando...");
            progress.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonSLA = null;

            try{
                publishProgress("Carregando...");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(params[0]).build();
                Response response = client.newCall(request).execute(); //error aqui
                jsonSLA = response.body().string();

            } catch(IOException e){
                e.printStackTrace();
            }

            return(jsonSLA);
        }

        @Override
        protected void onPostExecute(String jsonSLA) {
            progress.dismiss();
            depoisSLARequester(jsonSLA);
        }
    }

    public void depoisSLARequester(String jsonSLA) {
        listaSLA = new ArrayList<SLA>();

        try {
            JSONArray root = new JSONArray(jsonSLA);
            JSONObject item = null;
            for (int i = 0; i < root.length(); i++) {
                item = (JSONObject) root.get(i);

                Long idSLA = item.getLong("id");
                String descricao = item.getString("descricao");
                Integer slaTempo = item.getInt("slaTempo");
                SLA sla = new SLA(idSLA,descricao,slaTempo);
                listaSLA.add(sla);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayAdapter<SLA> spinnerAdapter = new ArrayAdapter<SLA>(this,
                android.R.layout.simple_spinner_item, listaSLA);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        SLASpinner.setAdapter(spinnerAdapter);
    }


    public void salvarChamado(View view) {
        int posicaoFila = filasSpinner.getSelectedItemPosition();
        int posicaoSLA = SLASpinner.getSelectedItemPosition();

        Fila fila = listaFila.get(posicaoFila);
        SLA sla = listaSLA.get(posicaoSLA);

        String descricao = editDescricao.getText().toString();
        String assunto = editAssunto.getText().toString();

        Chamado chamado = new Chamado(usuario,fila,sla,assunto,descricao);

        Gson gson = new Gson();
        String jsonChamado = gson.toJson(chamado);

        Boolean lDesc = Validator.validateNotNull(editDescricao, "Descrição não pode ser vazio");
        Boolean lAssunto = Validator.validateNotNull(editAssunto, "Assunto não pode ser vazio");

        if (lDesc && lAssunto) {
            ChamadoRequester chamadoRequester = new ChamadoRequester(this);
            chamadoRequester.execute("http://107.170.41.209:8080/chamado/rest/v1/chamado/", jsonChamado);
            finish();
        }
    }

    public class ChamadoRequester extends AsyncTask<String, String, String> {
        private Context context;

        public ChamadoRequester(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            String jsonChamado = null;

            try{
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, params[1]);
                Request request = new Request.Builder().url(params[0]).post(body).addHeader("content-type", "application/json")
                        .addHeader("cache-control", "no-cache")
                        .build();
                Response response = client.newCall(request).execute();
                jsonChamado = response.toString();
            } catch(IOException e){
                e.printStackTrace();
            }
            return(jsonChamado);
        }

        @Override
        protected void onPostExecute(String params) {
            depoisIncluirChamado(params);
        }
    }
    public void depoisIncluirChamado(String jsonChamado) {
        Toast.makeText(this, "Chamado criado com sucesso", Toast.LENGTH_SHORT).show();
    }

}
