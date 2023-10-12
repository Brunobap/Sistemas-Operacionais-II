package fileItens;

import java.util.Date;
import java.util.HashMap;

public class Item {
	
	private int endereco;
	
	private int estado;
	private String nome;
	private int pai;
	private String criacao;
	private int permissao;
	
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
	public String getCriacao() {
		return criacao;
	}
	public void setCriacao(String criacao) {
		this.criacao = criacao;
	}	
	public int getPermissao() {
		return permissao;
	}
	public void setPermissao(int permissao) {
		this.permissao = permissao;
	}	
	
		
	public Item(String nome, int pai) {
		this.nome = nome;
		Date agora = new Date();
		this.criacao = 
				agora.getYear()+
				(agora.getMonth()+1)+
				(agora.getDate()+1)+
				(agora.getHours()+1)+
				(agora.getMinutes()+1)+"";
		this.pai = pai;
	}
	
	public Item(int estado, int endereco, String nome, int pai, String data, int permit) {
		this.estado = estado;
		this.endereco = endereco;
		this.nome = nome;
		this.pai = pai;
		this.criacao = data;
		this.permissao = permit;
	}
}
