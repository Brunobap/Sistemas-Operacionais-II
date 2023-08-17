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
	
	// ctor pras outras pastas
	public Diretorio (Diretorio pai, String nome) {
		super(nome, pai);
		if (this.getPai() == null) this.setPai(this);
		this.setCriacao(new Date());
		this.setPermissao("drwxrwxrwx");
		this.mapDir = new HashMap<String, Diretorio>();
		this.mapFiles = new HashMap<String, Arquivo>();
	}
}
