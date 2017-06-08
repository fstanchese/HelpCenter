package br.usjt.appchamado;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ChamadoListaAdapter extends ArrayAdapter<Chamado> {

    private Context context;
    private ArrayList<Chamado> lista;

    public ChamadoListaAdapter(Context context, ArrayList<Chamado> lista ) {
        super(context,0,lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.chamado,null,true);
        }

        Chamado chamadoPosicao = lista.get(position);

        TextView chamado_id = (TextView) convertView.findViewById(R.id.chamado_id);
        TextView chamado_solicitante = (TextView) convertView.findViewById(R.id.chamado_solicitante);
        TextView chamado_assunto = (TextView) convertView.findViewById(R.id.chamado_assunto);
        TextView chamado_fila = (TextView) convertView.findViewById(R.id.chamado_fila);
        TextView chamado_status = (TextView)  convertView.findViewById(R.id.chamado_status);
        TextView chamado_cadastro = (TextView) convertView.findViewById(R.id.chamado_cadastro);
        TextView chamado_limite = (TextView) convertView.findViewById(R.id.chamado_limite);
        TextView chamado_prazo = (TextView) convertView.findViewById(R.id.chamado_prazo);

        String dtCadastro = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(chamadoPosicao.getDtCadastro());
        String dtLimite = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(chamadoPosicao.getDtLimite());

        String id = chamadoPosicao.getId()+" ";

        chamado_id.setText(id);
        chamado_solicitante.setText(chamadoPosicao.getSolicitante().getNome());
        chamado_assunto.setText(chamadoPosicao.getAssunto());
        chamado_fila.setText(chamadoPosicao.getFila().getDescricao());
        chamado_cadastro.setText(dtCadastro);
        chamado_limite.setText(dtLimite);
        chamado_status.setText(chamadoPosicao.getStatus().name());

        chamado_prazo.setText(chamadoPosicao.getPrazo());
        return convertView;
    }

}
