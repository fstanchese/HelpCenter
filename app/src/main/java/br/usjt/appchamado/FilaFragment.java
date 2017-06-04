package br.usjt.appchamado;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FilaFragment extends Fragment {
    private ArrayList<Fila> lista = null;
    private Context context;
    View v;

    public FilaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fila, container, false);

        FilaRequester filaRequester = new FilaRequester(getActivity());
        filaRequester.execute("http://107.170.41.209:8080/chamado/rest/v1/filas");

        return v;
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
        lista = new ArrayList<Fila>();

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
                lista.add(fila);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (lista != null) {
            FilaListaAdapter listaAdapterFila = new FilaListaAdapter(getContext(),lista);

            ListView listView = (ListView) v.findViewById(R.id.listarFilas);
            listView.setAdapter(listaAdapterFila);
        }
    }
}
