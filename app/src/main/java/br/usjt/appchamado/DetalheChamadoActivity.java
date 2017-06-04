package br.usjt.appchamado;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class DetalheChamadoActivity extends AppCompatActivity {
    private String TAG = DetalheChamadoActivity.class.getSimpleName();
    Chamado chamado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_chamado);
        setTitle("Detalhes do Chamado");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle args = getIntent().getBundleExtra("args");
        chamado = (Chamado) args.getSerializable("chamado");
        String prazo = (String) args.getString("prazo");
        Log.e(TAG, "response detalhe " + chamado);

        TextView chamado_solicitante = (TextView) findViewById(R.id.chamado_solicitante);
        TextView chamado_assunto = (TextView) findViewById(R.id.chamado_assunto);
        TextView chamado_fila = (TextView) findViewById(R.id.chamado_fila);
        TextView chamado_status = (TextView)  findViewById(R.id.chamado_status);
        TextView chamado_cadastro = (TextView) findViewById(R.id.chamado_cadastro);
        TextView chamado_limite = (TextView) findViewById(R.id.chamado_limite);
        TextView chamado_prazo = (TextView) findViewById(R.id.chamado_prazo);
        TextView chamado_inicioatendimento = (TextView) findViewById(R.id.chamado_inicioatendimento);
        TextView chamado_fimatendimento = (TextView) findViewById(R.id.chamado_fimatendimento);
        TextView chamado_descricao = (TextView) findViewById(R.id.chamado_descricao);
        TextView chamado_solucao = (TextView) findViewById(R.id.chamado_solucao);
        if (chamado.getSolucao().equals("")) {
            TextView textView5 = (TextView) findViewById(R.id.textView5);
            chamado_solucao.setVisibility(View.GONE);
            textView5.setVisibility(View.GONE);
        } else {
            chamado_solucao.setText(chamado.getSolucao());
        }

        String dtCadastro = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(chamado.getDtCadastro());
        String dtLimite = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(chamado.getDtLimite());
        String dtInicio = "";
        String dtFim = "";
        if (chamado.getDtInicioAtendimento() != null) {
            dtInicio = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(chamado.getDtInicioAtendimento());
        }
        if (chamado.getDtFimAtendimento() != null) {
            dtFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(chamado.getDtFimAtendimento());
        }

        chamado_solicitante.setText(chamado.getSolicitante().getNome());
        chamado_assunto.setText(chamado.getAssunto());
        chamado_fila.setText(chamado.getFila().getDescricao());
        chamado_cadastro.setText(dtCadastro);
        chamado_limite.setText(dtLimite);
        chamado_status.setText(chamado.getStatus().name());
        chamado_inicioatendimento.setText(dtInicio);
        chamado_fimatendimento.setText(dtFim);
        chamado_prazo.setText(prazo);
        chamado_descricao.setText(chamado.getDescricao());
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
}
