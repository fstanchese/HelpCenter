package br.usjt.appchamado;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.text.SimpleDateFormat;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AtendeChamadoActivity extends AppCompatActivity {
    private String TAG = AtendeChamadoActivity.class.getSimpleName();
    private Chamado chamado;
    private Bundle args;
    private Spinner finalizaSpinner;
    private EditText chamado_solucao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atende_chamado);

        setTitle("Atender Chamado");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        args = getIntent().getBundleExtra("args");
        chamado = (Chamado) args.getSerializable("chamado");
        String prazo = (String) args.getString("prazo");
        finalizaSpinner = (Spinner) findViewById(R.id.finaliza_spinner);

        TextView chamado_id = (TextView) findViewById(R.id.chamado_id);
        TextView chamado_solicitante = (TextView) findViewById(R.id.chamado_solicitante);
        TextView chamado_fila = (TextView) findViewById(R.id.chamado_fila);
        TextView chamado_status = (TextView)  findViewById(R.id.chamado_status);
        TextView chamado_cadastro = (TextView) findViewById(R.id.chamado_cadastro);
        TextView chamado_limite = (TextView) findViewById(R.id.chamado_limite);
        TextView chamado_prazo = (TextView) findViewById(R.id.chamado_prazo);
        TextView chamado_inicioatendimento = (TextView) findViewById(R.id.chamado_inicioatendimento);
        chamado_solucao = (EditText) findViewById(R.id.chamado_solucao);

        String dtCadastro = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(chamado.getDtCadastro());
        String dtLimite = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(chamado.getDtLimite());
        String dtInicio = "";
        if (chamado.getDtInicioAtendimento() != null) {
            dtInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(chamado.getDtInicioAtendimento());
        }

        String id = chamado.getId()+" ";

        chamado_id.setText(id);
        chamado_solicitante.setText(chamado.getSolicitante().getNome());
        chamado_fila.setText(chamado.getFila().getDescricao());
        chamado_cadastro.setText(dtCadastro);
        chamado_limite.setText(dtLimite);
        chamado_status.setText(chamado.getStatus().name());
        chamado_inicioatendimento.setText(dtInicio);
        chamado_prazo.setText(prazo);
        chamado_solucao.setText(chamado.getSolucao());

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this,R.array.Finaliza,android.R.layout.simple_spinner_item);
        finalizaSpinner.setAdapter(arrayAdapter);
    }

    public void atenderChamado(View view) {
        String finaliza = (String) finalizaSpinner.getSelectedItem();
        String solucao = chamado_solucao.getText().toString();

        Chamado chamadoAtende = new Chamado(chamado.getId(),solucao,finaliza);

        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(chamadoAtende);
        jsonElement.getAsJsonObject().addProperty("finaliza", finaliza);
        String jsonChamado = gson.toJson(jsonElement);

        Log.e(TAG, "response chamadoAtende " + jsonChamado);

        Boolean lDesc = Validator.validateNotNull(chamado_solucao, "Solução não pode ser vazio");
        if (lDesc) {
            ChamadoRequester chamadoRequester = new ChamadoRequester(this);
            chamadoRequester.execute("http://107.170.41.209:8080/chamado/rest/v1/chamado/atende", jsonChamado);
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
                Request request = new Request.Builder().url(params[0]).put(body).addHeader("content-type", "application/json")
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
        Log.e(TAG, "response detalhe " + jsonChamado);
        Toast.makeText(this, "Chamado atendido com sucesso", Toast.LENGTH_SHORT).show();
    }

}
