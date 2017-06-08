package br.usjt.appchamado;

import java.io.Serializable;
import java.util.Date;

public class Chamado implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Fila fila;
	private Usuario solicitante;
	private Date dtCadastro;
	private Date dtAlteracao;
	private Date dtLimite;
	private Date dtInicioAtendimento;
	private Date dtFimAtendimento;
	private Status status;
	private String assunto;
	private String descricao;
	private String solucao;
	private SLA sla;
	private Integer ativo;
	
	transient String prazo;
	transient String finaliza;

	public Chamado(Long id, Usuario solicitante, Fila fila, Date dtCadastro, Date dtLimite, String prazo,
				   Date dtInicioAtendimento, Date dtFimAtendimento, String assunto, Status status, String descricao, String solucao ) {
		this.id = id;
		this.solicitante = solicitante;
		this.fila = fila;
		this.dtCadastro = dtCadastro;
		this.dtLimite = dtLimite;
		this.prazo = prazo;
		this.dtInicioAtendimento = dtInicioAtendimento;
		this.dtFimAtendimento = dtFimAtendimento;
		this.assunto = assunto;
		this.status = status;
		this.descricao = descricao;
		this.solucao = solucao;
	}

	public Chamado(Usuario solicitante, Fila fila, SLA sla, String assunto, String descricao) {
		this.solicitante = solicitante;
		this.fila = fila;
		this.sla = sla;
		this.assunto = assunto;
		this.descricao = descricao;
	}

	public Chamado(Long id, String solucao, String finaliza) {
		this.id = id;
		this.solucao = solucao;
		this.finaliza = finaliza;
	}

	public void setPrazo(String prazo) {
		this.prazo = prazo;
	}
	public String getPrazo() {
		return prazo;
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Fila getFila() {
		return fila;
	}

	public void setFila(Fila fila) {
		this.fila = fila;
	}

	public Usuario getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(Usuario solicitante) {
		this.solicitante = solicitante;
	}

	public Date getDtCadastro() {
		return dtCadastro;
	}

	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}

	public Date getDtLimite() {
		return dtLimite;
	}

	public void setDtLimite(Date dtLimite) {
		this.dtLimite = dtLimite;
	}

	public Date getDtInicioAtendimento() {
		return dtInicioAtendimento;
	}

	public void setDtInicioAtendimento(Date dtInicioAtendimento) {
		this.dtInicioAtendimento = dtInicioAtendimento;
	}

	public Date getDtFimAtendimento() {
		return dtFimAtendimento;
	}

	public void setDtFimAtendimento(Date dtFimAtendimento) {
		this.dtFimAtendimento = dtFimAtendimento;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getSolucao() {
		return solucao;
	}

	public void setSolucao(String solucao) {
		this.solucao = solucao;
	}

	public SLA getSla() {
		return sla;
	}

	public void setSla(SLA sla) {
		this.sla = sla;
	}

	public Integer getAtivo() {
		return ativo;
	}

	public void setAtivo(Integer ativo) {
		this.ativo = ativo;
	}

	public Date getDtAlteracao() {
		return dtAlteracao;
	}
	
	public void setDtAlteracao(Date dtAlteracao) {
		this.dtAlteracao = dtAlteracao;
	}
	
	public String getFinaliza() {
		return finaliza;
	}
	
	public void setFinaliza(String finaliza) {
		this.finaliza = finaliza;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chamado other = (Chamado) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Chamado{" +
				"id=" + id +
				", fila=" + fila +
				", solicitante=" + solicitante +
				", dtCadastro=" + dtCadastro +
				", dtAlteracao=" + dtAlteracao +
				", dtLimite=" + dtLimite +
				", dtInicioAtendimento=" + dtInicioAtendimento +
				", dtFimAtendimento=" + dtFimAtendimento +
				", status=" + status +
				", assunto='" + assunto + '\'' +
				", descricao='" + descricao + '\'' +
				", solucao='" + solucao + '\'' +
				", sla=" + sla +
				", ativo=" + ativo +
				", prazo='" + prazo + '\'' +
				", finaliza='" + finaliza + '\'' +
				'}';
	}
}
