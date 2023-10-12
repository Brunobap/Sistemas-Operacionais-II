package fileItens;

import java.util.ArrayList;

public class Arquivo extends Item {

	private String conteudo;
	
	public String getConteudo() {
		return conteudo;
	}
	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}
	

	public Arquivo(int pai, String nome, String conteudo) { 
		super(nome, pai);
		this.setConteudo(conteudo);
		this.setPermissao(777);
	}
	
	public Arquivo(int pai, Arquivo molde) {
		super(molde.getNome(), pai);
		this.setConteudo(molde.getConteudo());
		this.setPermissao(molde.getPermissao());
	}
	
	public Arquivo(int pai, String nome, Arquivo molde) {
		super(nome, pai);
		this.setConteudo(molde.getConteudo());
		this.setPermissao(molde.getPermissao());
	}
	
	// ctor para arquivos já salvas
	public Arquivo (int estado, int endereco, String nome, int pai, String data, int permit, String conteudo) {
		super(estado, endereco, nome, pai, data, permit);
		this.conteudo = conteudo;
	}
}
