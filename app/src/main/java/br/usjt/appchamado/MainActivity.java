package br.usjt.appchamado;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    EditText editLogin,editSenha;
    Button btnLogar;
    CheckBox ckLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        editLogin = (EditText)findViewById(R.id.editLogin);
        editSenha = (EditText)findViewById(R.id.editSenha);
        btnLogar = (Button)findViewById(R.id.btnLogar);
    }

    public void logar( View view) {
        LoginRequester requester = new LoginRequester(this);
        String login = editLogin.getText().toString();
        String senha = editSenha.getText().toString();

        if (login.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Login ou senha vazio", Toast.LENGTH_SHORT).show();
        } else {
            requester.execute("http://107.170.41.209:8080/chamado/rest/v1/logar?login=" + login+"&senha="+senha);
        }
    }

    public class LoginRequester extends AsyncTask<String, String, String> {
        private ProgressDialog progress;
        private Context context;

        public LoginRequester(Context context) {
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
            String jsonUsuario = null;

            try{
                publishProgress("Carregando...");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(params[0]).build();
                Response response = client.newCall(request).execute();
                jsonUsuario = response.body().string();

            }
            catch(IOException e){}

            return(jsonUsuario);
        }

        @Override
        protected void onPostExecute(String params) {
            progress.dismiss();
            depoisLoginRequester(params);
        }
    }

    public void depoisLoginRequester(String jsonUsuario) {

        Log.e(TAG, "Response from url:" + jsonUsuario +":");
        if (!jsonUsuario.isEmpty()) {
            try {
                JSONObject reader = new JSONObject(jsonUsuario);
                Long idLogin            = reader.getLong("id");
                String nome             = reader.getString("nome");
                String celular          = reader.getString("celular");
                String email            = reader.getString("email");
                TipoUsuario tipoUsuario = TipoUsuario.valueOf(reader.getString("tipoUsuario"));
                String login            = reader.getString("login");
                Integer ativo           = reader.getInt("ativo");

                String JSONObject = reader.getString("sla");
                SLA sla = null;

                if(JSONObject != "null" && !JSONObject.isEmpty()) {
                    JSONObject reader2 = new JSONObject(JSONObject);

                    Long idSLA          = reader2.getLong("id");
                    String descricao    = reader2.getString("descricao");
                    Integer slatempo    = reader2.getInt("slaTempo");

                    sla = new SLA(idSLA, descricao, slatempo);
                } else {
                    sla = new SLA(1L, "Nível Crítico",1);
                }
                if (ativo==1) {
                    Usuario usuario = new Usuario(idLogin, nome, celular, email, tipoUsuario, login, sla);

                    Bundle args = new Bundle();

                    args.putSerializable("usuario", usuario);

                    Intent it = new Intent(this, MenuActivity.class);
                    it.putExtra("args", args);

                    startActivity(it);
                } else {
                    Toast.makeText(this, "Usuário está inativado, entre em contato com o adm", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "Login ou senha invalido", Toast.LENGTH_SHORT).show();
        }
    }

    protected void onPause() {
        super.onPause();
        finish();
    }
}
