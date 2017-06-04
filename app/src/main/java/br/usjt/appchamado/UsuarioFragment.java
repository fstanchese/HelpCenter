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

public class UsuarioFragment extends Fragment {
    private String TAG = FilaFragment.class.getSimpleName();
    private ArrayList<Usuario> lista = null;
    private Context context;
    View v;

    public UsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.fragment_usuario, container, false);
        UsuarioRequester usuarioRequester = new UsuarioRequester(getActivity());
        usuarioRequester.execute("http://107.170.41.209:8080/chamado/rest/v1/usuarios");

        return v;
    }

    public class UsuarioRequester extends AsyncTask<String, String, String> {
        private ProgressDialog progress;
        private Context context;;

        public UsuarioRequester(Context context) {
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
                publishProgress("Carregando...");
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
            depoisUsuarioRequester(params);
        }
    }

    public void depoisUsuarioRequester(String usuarios) {
        lista = new ArrayList<Usuario>();
        Log.e(TAG, "Response from url:" + usuarios +":");

        try {
            JSONArray root = new JSONArray(usuarios);
            JSONObject item = null;
            for (int i = 0; i < root.length(); i++) {
                item = (JSONObject) root.get(i);

                Long idUsuario = item.getLong("id");
                String nome = item.getString("nome");
                String celular = item.getString("celular");
                String email = item.getString("email");
                String login = item.getString("login");
                TipoUsuario tipoUsuario = TipoUsuario.valueOf(item.getString("tipoUsuario"));
                SLA sla = null;
                Usuario usuario = new Usuario(idUsuario,nome,celular,email,tipoUsuario, login, sla);
                lista.add(usuario);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (lista != null) {
            UsuarioListaAdapter listaAdapterUsuario = new UsuarioListaAdapter(getContext(),lista);

            ListView listView = (ListView) v.findViewById(R.id.listarUsuarios);
            listView.setAdapter(listaAdapterUsuario);
        }
    }
}
