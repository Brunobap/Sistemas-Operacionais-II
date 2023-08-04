package fileItens;

import java.util.Date;
import java.util.HashMap;

public class Item {
	
	private String nome;
	private Diretorio pai;
	private Date criacao;
	private String permissao;
	private String caminho;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Diretorio getPai() {
		return pai;
	}
	public void setPai(Diretorio pai) {
		this.pai = pai;
	}	
	public Date getCriacao() {
		return criacao;
	}
	public void setCriacao(Date criacao) {
		this.criacao = criacao;
	}	
	public String getPermissao() {
		return permissao;
	}
	public void setPermissao(String permissao) {
		this.permissao = permissao;
	}	
	public String getCaminho() {
		return caminho;
	}
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}
	
}
