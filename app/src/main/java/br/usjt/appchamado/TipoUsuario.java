package br.usjt.appchamado;

public enum TipoUsuario {
    ADMINISTRADOR("ADMINISTRADOR"), SOLUCIONADOR("SOLUCIONADOR"), SOLICITANTE("SOLICITANTE");

    private String tipo;

    TipoUsuario(String tipo) {
        this.tipo = tipo;
    }

    public String getTipoUsuario() {
        return tipo;
    }

}
