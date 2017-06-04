package br.usjt.appchamado;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ChamadoFragment extends Fragment {
    private String TAG = FilaFragment.class.getSimpleName();
    private ArrayList<Chamado> lista = null;
    private Spinner status;
    private Usuario usuario;
    private Button btnBuscar;
    private View v;

    public ChamadoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.fragment_chamado, container, false);

        Bundle args = getArguments();
        usuario = (Usuario) args.getSerializable("usuario");

        status = (Spinner) v.findViewById(R.id.status);
        btnBuscar = (Button) v.findViewById(R.id.btnBuscar);
        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemSelecionado = (String) status.getSelectedItem();

                ChamadoRequester chamadoRequester = new ChamadoRequester(getActivity());
                chamadoRequester.execute("http://107.170.41.209:8080/chamado/rest/v1/chamados?login="+usuario.getLogin()+"&status="+itemSelecionado);

            }
        });

        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getContext(),R.array.Status,android.R.layout.simple_spinner_item);
        status.setAdapter(arrayAdapter);

        ChamadoRequester chamadoRequester = new ChamadoRequester(getActivity());
        chamadoRequester.execute("http://107.170.41.209:8080/chamado/rest/v1/chamados?login="+usuario.getLogin()+"&status=ABERTO");

        return v;
    }

    public class ChamadoRequester extends AsyncTask<String, String, String> {
        private ProgressDialog progress;
        private Context context;

        public ChamadoRequester(Context context){
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
            String login = null;

            try{
                progress.setMessage("Carregando...");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(params[0]).build();
                Response response = client.newCall(request).execute(); //error aqui
                login = response.body().string();

            }
            catch(IOException e){}

            return(login);
        }

        @Override
        protected void onPostExecute(String params) {
            progress.dismiss();
            depoisChamadoRequester(params);
        }
    }

    public void depoisChamadoRequester(String chamados) {
        lista = new ArrayList<Chamado>();

        try {
            JSONArray root = new JSONArray(chamados);
            JSONObject item = null;
            for (int i = 0; i < root.length(); i++) {
                item = (JSONObject) root.get(i);

                Long idChamado = item.getLong("id");
                Usuario solicitante = null;

                String JSONObject = item.getString("solicitante");

                if(JSONObject != "null" && !JSONObject.isEmpty()) {

                    Log.e(TAG, "Response from url:" + JSONObject +":");
                    JSONObject reader = new JSONObject(JSONObject);

                    Long idGerente = reader.getLong("id");
                    String nome = reader.getString("nome");
                    String celular = reader.getString("celular");
                    String email = reader.getString("email");
                    String login = reader.getString("login");
                    TipoUsuario tipoUsuario = TipoUsuario.valueOf(reader.getString("tipoUsuario"));
                    SLA sla = null;
                    solicitante = new Usuario(idGerente, nome, celular, email, tipoUsuario, login, sla);
                }

                JSONObject = item.getString("fila");
                Fila fila = null;

                if(JSONObject != "null" && !JSONObject.isEmpty()) {
                    JSONObject reader = new JSONObject(JSONObject);
                    Long idFila = reader.getLong("id");
                    String descricao = reader.getString("descricao");
                    fila = new Fila(idFila,descricao);
                }

                Long lCadastro = item.getLong("dtCadastro");
                Long lLimite = item.getLong("dtLimite");
                Date dtCadastro = new Date(lCadastro);
                Date dtLimite = new Date(lLimite);


                Date dtInicioAtendimento = null;
                if (!"null".equals(item.getString("dtInicioAtendimento"))) {
                    Long lInicioAtendimeno = item.getLong("dtInicioAtendimento");
                    dtInicioAtendimento = new Date(lInicioAtendimeno);
                }
                Date dtFimAtendimento = null;

                if (!"null".equals(item.getString("dtFimAtendimento"))) {
                    Long lFimAtendimento = item.getLong("dtInicioAtendimento");
                    dtFimAtendimento = new Date(lFimAtendimento);
                }

                String prazo = item.getString("prazo");
                String assunto = item.getString("assunto");
                String descricao = item.getString("descricao");
                String solucao = item.getString("solucao");

                if ("null".equals(prazo))
                {
                    prazo = "FINALIZADO";
                }
                if ("null".equals(descricao))
                {
                    descricao = "";
                }
                if ("null".equals(solucao))
                {
                    solucao = "";
                }

                Status status = Status.valueOf(item.getString("status"));

                Chamado chamado = new Chamado(idChamado, solicitante, fila, dtCadastro, dtLimite, prazo, dtInicioAtendimento, dtFimAtendimento, assunto, status, descricao, solucao );
                lista.add(chamado);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (lista != null) {
            ChamadoListaAdapter listaAdapterChamado = new ChamadoListaAdapter(getContext(),lista);

            ListView listView = (ListView) v.findViewById(R.id.listarChamados);
            listView.setAdapter(listaAdapterChamado);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long l) {
                    Intent intent = new Intent(getActivity(),DetalheChamadoActivity.class);

                    Log.e(TAG, "response listview " + lista.get(posicao));
                    Bundle args = new Bundle();
                    args.putSerializable("chamado", lista.get(posicao));
                    args.putString("prazo", lista.get(posicao).getPrazo());
                    intent.putExtra("args", args);

                    startActivity(intent);
                }
            });
        }
    }
}
