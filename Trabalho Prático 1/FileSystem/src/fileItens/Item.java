package fileItens;

import java.util.Date;
import java.util.HashMap;

public class Item {
	
	private int endereco;
	
	private int estado;
	private String nome;
	private int pai;
	private Date criacao;
	private int permissao;
	private String caminho;
	
	public int getEndereco() {
		return endereco;
	}
	public void setEndereco(int endereco) {
		this.endereco = endereco;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public int getPai() {
		return pai;
	}
	public void setPai(int pai) {
		this.pai = pai;
	}	
	public Date getCriacao() {
		return criacao;
	}
	public void setCriacao(Date criacao) {
		this.criacao = criacao;
	}	
	public int getPermissao() {
		return permissao;
	}
	public void setPermissao(int permissao) {
		this.permissao = permissao;
	}	
	public String getCaminho() {
		return caminho;
	}
	public void setCaminho(String caminho) {
		this.caminho = caminho;
	}	
		
	public Item(String nome, int pai) {
		this.nome = nome;
		this.criacao = new Date();
		this.pai = pai;
	}
}
