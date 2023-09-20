package fileItens;

import java.util.Date;
import java.util.HashMap;

public class Diretorio extends Item {
	
	private HashMap<String, Diretorio> mapDir;
	private HashMap<String, Arquivo> mapFiles;
	
	
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
	
	public Diretorio copiar(int pai) {
		Diretorio novo = new Diretorio(pai,this);
		
		for (Arquivo a : this.getMapFiles().values()) {
			Arquivo addF = new Arquivo(novo,a);
			novo.getMapFiles().put(addF.getNome(), addF);
		}
		for (Diretorio d : this.getMapDir().values()) {
			Diretorio addD = d.copiar(novo.getEndereco());
			novo.getMapDir().put(addD.getNome(), addD);
		}
		
		return novo;
	}
	
	// ctor pras outras pastas
	public Diretorio (int pai, String nome) {
		super(nome, pai);
		this.setPai(pai);
		this.setCriacao(new Date());
		this.setPermissao(777);
		this.mapDir = new HashMap<String, Diretorio>();
		this.mapFiles = new HashMap<String, Arquivo>();
	}
	
	public Diretorio (int pai, Diretorio molde) {
		super(molde.getNome(), pai);
		this.setPai(this.getEndereco());
		this.setCriacao(new Date());
		this.setPermissao(molde.getPermissao());
		this.mapDir = new HashMap<String, Diretorio>();
		this.mapFiles = new HashMap<String, Arquivo>();
	}
	
}
