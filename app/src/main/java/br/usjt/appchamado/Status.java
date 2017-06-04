package br.usjt.appchamado;

public enum Status {
	ABERTO("Aberto"), FECHADO("Fechado"), EMATENDIMENTO("Em Atendimento"),
	PENDENTE("Pendente"), CANCELADO("Cancelado"), ATRASADO("Atrasado");

    private String tipo;

    private Status(String tipo) {
        this.tipo = tipo;
    }

    public String getStatus() {
        return tipo;
    }

}
