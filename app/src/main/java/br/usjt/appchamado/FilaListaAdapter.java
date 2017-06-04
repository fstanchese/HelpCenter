package br.usjt.appchamado;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FilaListaAdapter extends ArrayAdapter<Fila> {

    private Context context;
    private ArrayList<Fila> lista;

    public FilaListaAdapter(Context context, ArrayList<Fila> lista ) {
        super(context,0,lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fila,null,true);
        }

        Fila filaPosicao = lista.get(position);

        TextView fila_descricao = (TextView) convertView.findViewById(R.id.fila_descricao);
        TextView fila_gerente = (TextView) convertView.findViewById(R.id.fila_gerente);
        TextView fila_solucionador = (TextView) convertView.findViewById(R.id.fila_solucionador);

        fila_descricao.setText(filaPosicao.getDescricao());
        if (filaPosicao.getGerente() != null)
            fila_gerente.setText(filaPosicao.getGerente().getNome());
        fila_solucionador.setText(filaPosicao.getNomeSolucionador());

        return convertView;
    }

}
