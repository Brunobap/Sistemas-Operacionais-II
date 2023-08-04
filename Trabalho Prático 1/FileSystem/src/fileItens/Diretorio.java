package fileItens;

import java.util.Date;
import java.util.HashMap;

public class Diretorio extends Item {
	
	private String nome;
	private Diretorio pai;
	
	private HashMap<String, Diretorio> mapDir;
	private HashMap<String, Arquivo> mapFiles;
	
	
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
	public HashMap<String, Diretorio> getMapDir() {
		return mapDir;
	}
	public void setMapDir(HashMap<String, Diretorio> mapDir) {
		this.mapDir = mapDir;
	}
	public HashMap<String, Arquivo> getMapFiles() {
		return mapFiles;
	}
	public void setMapFiles(HashMap<String, Arquivo> mapFiles) {
		this.mapFiles = mapFiles;
	}
	
	// ctor pra pasta raíz
	public Diretorio () {
		this.setPermissao("drwxrwxrwx");
		this.nome = "/";
		this.setCriacao(new Date());
		this.pai = this;
		this.mapDir = new HashMap<String, Diretorio>();
		this.mapFiles = new HashMap<String, Arquivo>();		
	}
	// ctor pras outras pastas
	public Diretorio (Diretorio pai, String nome) {
		this.nome = nome;
		this.pai = pai;
		this.setCriacao(new Date());
		this.setPermissao("drwx------");
		this.mapDir = new HashMap<String, Diretorio>();
		this.mapFiles = new HashMap<String, Arquivo>();
	}
}
