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

public class UsuarioListaAdapter extends ArrayAdapter<Usuario> {

    private Context context;
    private ArrayList<Usuario> lista;

    public UsuarioListaAdapter(Context context, ArrayList<Usuario> lista ) {
        super(context,0,lista);
        this.context = context;
        this.lista = lista;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.usuario,null,true);
        }

        Usuario usuarioPosicao = lista.get(position);

        TextView usuario_nome = (TextView) convertView.findViewById(R.id.usuario_nome);
        TextView usuario_celular = (TextView) convertView.findViewById(R.id.usuario_celular);
        TextView usuario_tipo = (TextView) convertView.findViewById(R.id.usuario_tipo);
        TextView usuario_email = (TextView) convertView.findViewById(R.id.usuario_email);

        usuario_nome.setText(usuarioPosicao.getNome());
        usuario_celular.setText(usuarioPosicao.getCelular());
        usuario_tipo.setText(usuarioPosicao.getTipoUsuario().toString());
        usuario_email.setText(usuarioPosicao.getEmail());
        return convertView;
    }

}
