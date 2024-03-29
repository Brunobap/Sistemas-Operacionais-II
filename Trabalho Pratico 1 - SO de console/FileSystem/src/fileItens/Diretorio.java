package fileItens;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Diretorio extends Item {
	
	private ArrayList<Integer> mapDir;
	private ArrayList<Integer> mapFiles;
	
	
	public ArrayList<Integer> getMapDir() {
		return mapDir;
	}
	public void setMapDir(ArrayList<Integer> mapDir) {
		this.mapDir = mapDir;
	}
	public ArrayList<Integer> getMapFiles() {
		return mapFiles;
	}
	public void setMapFiles(ArrayList<Integer> mapFiles) {
		this.mapFiles = mapFiles;
	}
		
	// ctor para pastas novas
	public Diretorio (int pai, String nome) {
		super(nome, pai);
		this.setPai(pai);
		this.setEstado(1);
		this.setPermissao(777);
		this.mapDir = new ArrayList<Integer>();
		this.mapFiles = new ArrayList<Integer>();
	}
	
	public Diretorio (int pai, Diretorio molde) {
		super(molde.getNome(), pai);
		this.setEstado(1);
		this.setPermissao(molde.getPermissao());
		this.mapDir = new ArrayList<Integer>();
		this.mapFiles = new ArrayList<Integer>();
	}
	
	// ctor para pastas já salvas
	public Diretorio (int estado, int endereco, String nome, int pai, String data, int permit, ArrayList<Integer> listDir, ArrayList<Integer> listArq) {
		super(estado, endereco, nome, pai, data, permit);
		this.mapDir = listDir;
		this.mapFiles = listArq;
	}
	
}
