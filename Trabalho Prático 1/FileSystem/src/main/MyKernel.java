package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import binary.Binario;
import fileItens.Arquivo;
import fileItens.Diretorio;
import hardware.HardDisk;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import operatingSystem.Kernel;
/**
 * Kernel desenvolvido pelo aluno. Outras classes criadas pelo aluno podem ser
 * utilizadas, como por exemplo: - Arvores; - Filas; - Pilhas; - etc...
 *
 * @author Bruno Ribeiro Batista
 */
public class MyKernel implements Kernel {
	
	// Informações da hd
	private int tamBloco = 512 * 8;
	
	private int atual;
	private ArrayList<String> vetComandos;
	private HardDisk hd;

    public MyKernel() {
    	Diretorio raiz = new Diretorio(0, "/");
    	this.hd = new HardDisk(1);
    	this.atual = 0;
    	this.vetComandos = new ArrayList<String>();
    	this.hd.inicializarMemoriaSecundaria();
    	desmontarDir(raiz);
    }
  
    // Concluídas 13/13 :^)
    public String mkdir(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: mkdir");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("mkdir "+parameters);
    	if (parameters.equals("/")) result = "mkdir: "+parameters+": Diretorio ja existe (Nenhum diretorio foi criado)";
    	else {
    		Diretorio aux;
    		int i = 1;
    		if (parameters.startsWith("../")) {
    			aux = montarDir(this.atual);
    			aux = montarDir(aux.getPai());
    		} 
    		else if (parameters.startsWith("./")) aux = montarDir(this.atual);
    		else if (!parameters.startsWith("/")) {
    			i = 0;
    			aux = montarDir(this.atual);
    		} else {
    			i = 1;
    			aux = montarDir(0);
    		}
    		    		
    		String[] caminho = parameters.split("/");
    		for (int j=i; j<caminho.length; j++)
    			if (caminho[j].contains(".") || caminho[j].contains(" ") || caminho[j].length() > 86) return "mkdir: Nome de diretório inválido. (Nada foi criado)";
    		
    		// Buscar e criar as pastas
    		for (; i<caminho.length; i++) {
    			Diretorio temp = aux;
    			for (int a : aux.getMapDir()) {
    				String nome = this.findNome(a);
    				if (nome.equals(caminho[i]+'\0')) {
    					aux = montarDir(a);
    					break;
    				} 
    			}

    			if (aux != temp) result = "mkdir: "+parameters+": Diretorio ja existe (Nenhum diretorio foi criado)";
    			else {
	    			if (caminho[i].equals("..")) {
	    				Diretorio pai = montarDir(aux.getPai());
	    				aux = pai;
	    			} else if (!caminho[i].equals(".")) {		
		    			// Não achou a pasta, criar uma nova
	    				int newEnd = this.findNextSpace();
	    				if (newEnd<0) return "mkdir: Espaco de HD insuficiente. (Não foi criado o diretório '"+caminho[i]+"', e consequentes)";
	    				Diretorio newDir = new Diretorio(aux.getEndereco(), caminho[i]);
	    				newDir.setEndereco(newEnd);
	    				aux.getMapDir().add(newEnd);
	    				desmontarDir(aux);
	    				desmontarDir(newDir);
	    				aux = newDir;
	    				result = "";
					}
    			}
    		}    		
    	}        	
        //fim da implementacao do aluno
        
    	return result;
    }
    public String ls(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: ls");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("ls "+parameters);
        boolean flgDetail = false;
        
        if (parameters.startsWith("-l")) {
        	flgDetail = true;
        	if (parameters.startsWith("-l ")) parameters = parameters.substring(3);
        	else parameters = parameters.substring(2);
        } else if (parameters.endsWith("-l")) {
        	flgDetail = true;
        	parameters = parameters.substring(0,parameters.indexOf(" -l"));
        }
        
        Diretorio aux = findDir(parameters);
        if (aux == null) return "ls: O diretório especificado não existe. (Nada foi mostrado)";
        
        if (flgDetail) {
        	Diretorio d;
        	for (int end : aux.getMapDir()) {
        		d = montarDir(end);
        		result += 
        			'd'+findPermit(String.valueOf(d.getPermissao()))+" "+
        			d.getCriacao()+" "+
        			d.getNome()+"\n";
        	}
        	Arquivo a;
        	for (int end : aux.getMapFiles()) {
        		a = montarArq(end);
        		result += 
	        		'-'+findPermit(String.valueOf(a.getPermissao()))+" "+
	    			a.getCriacao()+" "+
	    			a.getNome()+"\n";
        	}
        } else {
        	Diretorio d;
        	for (int end : aux.getMapDir()) {
        		d = montarDir(end);
        		result += d.getNome()+" ";        		
        	}
        	Arquivo a;
        	for (int end : aux.getMapFiles()) {
        		a = montarArq(end);
        		result += a.getNome()+" ";
        	}
        } 
        //fim da implementacao do aluno
        return result;
    }
    public String cd(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        String currentDir = "";
        System.out.println("Chamada de Sistema: cd");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("cd "+parameters);
        //indique o diretório atual. Por exemplo... /
        currentDir = operatingSystem.fileSystem.FileSytemSimulator.currentDir;
        
        //Caso mais básico, voltar a raíz
        if (parameters.equals("/")) {
        	operatingSystem.fileSystem.FileSytemSimulator.currentDir = "/";
        	this.atual = 0;
        	return result;
        }
        
        Diretorio aux = findDir(parameters);
        if (aux == null) return "cd: Diretório não foi encontrado.";
		this.atual = aux.getEndereco();

        //setando parte gráfica do diretorio atual	
		if (aux.getEndereco() == 0) currentDir = "/";
		else {
			String[] caminho = parameters.split("/");
			for (String s : caminho)
				if (s.equals("..")) {
					if (!currentDir.equals("/")) currentDir = currentDir.substring(0, currentDir.indexOf("/",caminho.length-1)+1);
				} else if (!s.equals(".") && !s.equals("")) currentDir += s+"/";
		}
        operatingSystem.fileSystem.FileSytemSimulator.currentDir = currentDir;

        //fim da implementacao do aluno
        return result;
    }    
    public String createfile(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: createfile");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("createfile "+parameters);
                
        String strCam = parameters.substring(0,parameters.indexOf(" "));
        String caminho = "";
        String nome;
        if (strCam.contains("/")) {
        	caminho = strCam.substring(0, strCam.lastIndexOf("/"));
        	nome = strCam.substring(strCam.lastIndexOf("/")+1);
        } else nome = strCam;
        Diretorio aux = findDir(caminho);
        if (aux == null) return "createfile: Diretório não encontrado. (Nenhum arquivo criado)";
                      
        String texto = parameters.substring(parameters.indexOf(" ")+1);
        
        for (int end : aux.getMapFiles()) if (findNome(end).equals(nome)) return "createfile: Arquivo já existe. Não foi posível criá-lo.";
		if ((nome.contains(" ") || nome.contains("/")) || !nome.contains(".") || nome.length()>86) return "createfile: Nome de arquivo inválido. (Nada foi criado)";
		
		String conteudo = texto.replaceAll("\\\\n", "\n");
		if (conteudo.length()>400) return "createfile: Conteúdo excessivo, limite de 400 caracteres. (Nada foi criado)";
		
		int newEnd = findNextSpace();
		if (newEnd<0) return "createfile: Sem espaço suficiente para criar arquivo. (Nada foi criado)";
		Arquivo novo = new Arquivo(aux.getEndereco(), nome, conteudo);
		novo.setEndereco(newEnd);
		
		aux.getMapFiles().add(novo.getEndereco());
        desmontarDir(aux);
        desmontarArq(novo);
        //fim da implementacao do aluno
        return result;
    }
    public String cat(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: cat");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("cat "+parameters);
        String caminho = "";
        String nome;
        if (parameters.contains("/")) {
        	caminho = parameters.substring(0,parameters.lastIndexOf("/"));
        	nome = parameters.substring(parameters.lastIndexOf("/")+1);
        } else {
        	nome = parameters;
        	caminho = "";
        }
        
        Diretorio aux = findDir(caminho);
		if (aux == null) return "cat: Diretorio do arquivo não existe. (Nada foi lido)";
			
		Arquivo alvo = null;
		for (int end : aux.getMapFiles()) {
			String s = findNome(end);
			if (s.equals(nome+'\0')) {
				alvo = montarArq(end);
				break;
			}
		}
		if (alvo != null) result = alvo.getConteudo();
		else result = "cat: Arquivo não existe. (Nada foi lido)";
        //fim da implementacao do aluno
        return result;
    }
    public String rmdir(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: rmdir");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("rmdir "+parameters);
        String caminho = "";
        String strAlvo;
        if (parameters.contains("/")) {
        	caminho = parameters.substring(0,parameters.lastIndexOf("/"));
        	strAlvo = parameters.substring(parameters.lastIndexOf("/")+1);
        } else strAlvo = parameters;
        Diretorio aux = findDir(caminho);
		if (aux == null) return "rmdir: Diretório "+parameters+"não existe. (Nada foi removido)";
		
		Diretorio dirAlvo = null;
		for (int end : aux.getMapDir()) 
			if (findNome(end).equals(strAlvo+'\0')) {
				dirAlvo = montarDir(end);
				break;
			}
		if (dirAlvo != null) {
			if (dirAlvo.getEndereco() == 0) return "rmdir: Não é possível remover o diretório raíz. (Nada foi removido)";
			if (dirAlvo.getMapDir().isEmpty() && dirAlvo.getMapFiles().isEmpty()) {
				aux.getMapDir().remove(aux.getMapDir().indexOf(dirAlvo.getEndereco()));
				dirAlvo.setEstado(0);
				desmontarDir(dirAlvo);
				desmontarDir(aux);
			} else result = "rmdir: Diretório "+parameters+" possui arquivos e/ou diretórios. (Nada foi removido)";
		} else result = "rmdir: Diretório "+parameters+" não existe. (Nada foi removido)";
		
        //fim da implementacao do aluno
        return result;
    }
    public String rm(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: rm");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("rm "+parameters);
        
        boolean isDir = false;
        if (parameters.startsWith("-R ") || parameters.startsWith("-r ")) {
        	isDir = true;
        	parameters = parameters.substring(3);
        }

        String caminho = "";
        String strAlvo;
        if (parameters.contains("/")) {
        	caminho = parameters.substring(0, parameters.lastIndexOf("/"));
        	strAlvo = parameters.substring(parameters.lastIndexOf("/")+1);
        } else strAlvo = parameters;
        Diretorio aux = findDir(caminho);
        if (aux == null) result = "rm: Diretório "+parameters+"não existe. (Nada foi removido)";

		if (isDir) {			
			Diretorio dirAlvo = null;
			for (int end : aux.getMapDir()) 
				if (findNome(end).equals(strAlvo+'\0')) {
					dirAlvo = montarDir(end);
					break;
				}
			if (dirAlvo != null) {
				if (dirAlvo.getEndereco() == 0) return "rm: Não é possível remover o diretório raíz. (Nada foi removido)";
				int index = aux.getMapDir().indexOf(dirAlvo.getEndereco());
				aux.getMapDir().remove(index);
				dirAlvo.setEstado(0);
				// Apagar todos os arquivos
				for (int end : dirAlvo.getMapFiles()) {
					Arquivo a = montarArq(end);
					a.setEstado(0);
					desmontarArq(a);
				}
				// Apagar todas as pastas
				for (int end : dirAlvo.getMapDir()) {
					String nome = findNome(end);
					nome = nome.substring(0,nome.length());
					this.rm("-R "+caminho+"/"+nome);
				}
				desmontarDir(dirAlvo);
				desmontarDir(aux);
			} else result = "rm: Diretório "+parameters+" não existe. (Nada foi removido)";
		} else {
			Arquivo arqAlvo = null;
			for (int end : aux.getMapFiles()) 
				if (findNome(end).equals(strAlvo+'\0')) {
					arqAlvo = montarArq(end);
					break;
				}
			if (arqAlvo != null) {
				int index = aux.getMapFiles().indexOf(arqAlvo.getEndereco());
				aux.getMapFiles().remove(index);
				arqAlvo.setEstado(0);
				desmontarArq(arqAlvo);
				desmontarDir(aux);
			} else result = "rm: Arquivo "+parameters+" não existe. (Nada foi removido)";
		}
        //fim da implementacao do aluno
        return result;
    }
    public String chmod(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: chmod");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("chmod "+parameters);
        boolean isDir = false;
        if (parameters.startsWith("-R ")) {
        	isDir = true;
        	parameters = parameters.substring(3);
        }

        String strPermit = parameters.substring(0, 3);
        int numPermit = Integer.parseInt(strPermit);
        parameters = parameters.substring(4);
        
		String caminho = "";
		String strAlvo;
		if (parameters.contains("/")) {
			caminho = parameters.substring(0,parameters.lastIndexOf("/"));
			strAlvo = parameters.substring(parameters.lastIndexOf("/")+1);
		} else strAlvo = parameters;
        Diretorio aux = findDir(caminho);
        if (aux == null) return "chmod: Diretório "+parameters+" não existe. (Nada foi modificado)";

		if (isDir) {
			Diretorio dirAlvo = null;
			for (int end : aux.getMapDir())
				if (findNome(end).equals(strAlvo+'\0')) {
					dirAlvo = montarDir(end);
					break;
				}
			if (dirAlvo != null) {				
				for (int end : dirAlvo.getMapDir()) {
					Diretorio d = montarDir(end);
					d.setPermissao(numPermit);
					desmontarDir(d);
				}
				for (int end: dirAlvo.getMapFiles()) {
					Arquivo a = montarArq(end);
					a.setPermissao(numPermit);
					desmontarArq(a);
				}
				dirAlvo.setPermissao(numPermit);
				desmontarDir(dirAlvo);
					
			} else result = "chmod: Diretório "+parameters+" não existe. (Nada foi modificado)";
		} else {
			Arquivo arqAlvo = null;
			for (int end : aux.getMapFiles())
				if (findNome(end).equals(strAlvo+'\0')) {
					arqAlvo = montarArq(end);
					break;
				}
			if (arqAlvo != null) {
				arqAlvo.setPermissao(numPermit);
				desmontarArq(arqAlvo);
			} else result = "chmod: Arquivo "+parameters+" não existe. (Nada foi modificado)";
		}
        //fim da implementacao do aluno
        return result;
    }
    public String mv(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: mv");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("mv "+parameters);
		String[] paramSplit = parameters.split(" ");
		
        String camOri = "";
        String tgtOri;
        if (paramSplit[0].contains("/")) {
        	camOri = paramSplit[0].substring(0, paramSplit[0].lastIndexOf("/"));
        	tgtOri = paramSplit[0].substring(paramSplit[0].lastIndexOf("/")+1);
        } else tgtOri = paramSplit[0];

        String camDst = "";
        String tgtDst;
        if (paramSplit[1].contains("/")) {
        	camDst = paramSplit[1].substring(0, paramSplit[1].lastIndexOf("/"));
        	tgtDst = paramSplit[1].substring(paramSplit[1].lastIndexOf("/")+1);
        } else tgtDst = paramSplit[1];
		
		Diretorio aux1 = findDir(camOri);
		if (aux1 == null) return "mv: Arquivo/Diretório não existe.";

		Diretorio aux2 = findDir(camDst);
		if (aux2 == null) return "mv: Arquivo/Diretório não existe.";
			
		// Pastas  iguais, renomear
		if (aux1.getEndereco() == aux2.getEndereco()) {
			if (tgtOri.contains(".") && tgtDst.contains(".")) {
				Arquivo a = null;
				for (int end : aux1.getMapFiles()) {
					String nome = findNome(end);
					if (nome.equals(tgtOri+'\0')) {
						a = montarArq(end);
						break;
					}
					if (nome.equals(tgtDst+'\0'))
						return "mv: Já existe um arquivo com esse nome. (Nada foi renomeado)";
				}
				
				if (a != null) {					
					a.setNome(tgtDst);
					desmontarArq(a);	
					
				} else return "mv: Arquivo/Diretório não existe. (Nada foi renomeado)";	
				
			} else if (!tgtOri.contains(".") && !tgtDst.contains(".")) {
				for (int end : aux1.getMapDir())
					if (findNome(end).equals(tgtDst+'\0'))
						return "mv: Já existe um arquivo com esse nome. (Nada foi renomeado)";
				
				Diretorio d = null;
				for (int end : aux1.getMapDir()) 
					if (findNome(end).equals(tgtOri+'\0')) {
						d = montarDir(end);
						break;
					}
				
				if (d != null) {					
					d.setNome(tgtDst);
					desmontarDir(d);	
					
				} else return "mv: Arquivo/Diretório não existe. (Nada foi renomeado)";	
				
			} else return "mv: Tipos diferentes de item. (Nada foi renomeado)";				
		
		// Pastas diferentes, mover
		} else {
			if (tgtOri.contains(".") && tgtDst.contains(".")) {
				if (aux2.getMapFiles().size() == 20)
					return "mv: Diretório destino não tem espaço suficiente. (Nada foi movido)";
				Arquivo a = null;
				int index = 0;
				for (int end : aux1.getMapFiles()) {
					String nome = findNome(end);
					if (nome.equals(tgtOri+'\0')) {
						a = montarArq(end);
						index = aux1.getMapFiles().indexOf(end);
						break;
					}
				}
				
				if (a != null) {
					a.setPai(aux2.getEndereco());
					aux1.getMapFiles().remove(index);
					aux2.getMapFiles().add(a.getEndereco());
					desmontarArq(a);	
					desmontarDir(aux1);
					desmontarDir(aux2);
					
				} else return "mv: Arquivo/Diretório não existe. (Nada foi renomeado)";	
				
			} else if (!tgtOri.contains(".") && !tgtDst.contains(".")) {
				if (aux2.getMapDir().size() == 20)
					return "mv: Diretório destino não tem espaço suficiente. (Nada foi movido)";
				Diretorio d = null;
				int index = 0;
				for (int end : aux1.getMapDir()) {
					String nome = findNome(end);
					if (nome.equals(tgtOri+'\0')) {
						d = montarDir(end);
						index = aux1.getMapDir().indexOf(end);
						break;
					}
					if (nome.equals(tgtDst+'\0'))
						return "mv: Já existe um arquivo com esse nome. (Nada foi renomeado)";
				}
				
				if (d != null) {					
					d.setPai(aux2.getEndereco());
					aux1.getMapDir().remove(index);
					aux2.getMapDir().add(d.getEndereco());
					desmontarDir(d);	
					desmontarDir(aux1);
					desmontarDir(aux2);	
					
				} else return "mv: Arquivo/Diretório não existe. (Nada foi renomeado)";	
				
			} else return "mv: Tipos diferentes de item. (Nada foi renomeado)";		
		}
        //fim da implementacao do aluno
        return result;
    }
    public String cp(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: cp");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("cp "+parameters);
        boolean isDir = false;
        if (parameters.startsWith("-R ")) {
        	isDir = true;
        	parameters = parameters.substring(3);
        }
        
		String[] paramSplit = parameters.split(" ");

        String camOri = "";
        String tgtOri;
        if (paramSplit[0].contains("/")) {
        	camOri = paramSplit[0].substring(0, paramSplit[0].lastIndexOf("/"));
        	tgtOri = paramSplit[0].substring(paramSplit[0].lastIndexOf("/")+1);
        } else tgtOri = paramSplit[0];

        String camDst = "";
        String tgtDst;
        if (paramSplit[1].contains("/")) {
        	camDst = paramSplit[1].substring(0, paramSplit[1].lastIndexOf("/"));
        	tgtDst = paramSplit[1].substring(paramSplit[1].lastIndexOf("/")+1);
        } else tgtDst = paramSplit[1];

        Diretorio aux1 = findDir(camOri);
        if (aux1 == null) return "cp: Diretório não encontrado. (Nada foi copiado)";
        
        Diretorio aux2 = findDir(camDst);
        if (aux2 == null) return "cp: Diretório não encontrado. (Nada foi copiado)";
		
		// Copiar sem renomear
		if (paramSplit[1].endsWith("/")) {
			if (isDir) {
				for (int end : aux2.getMapDir())
					if (findNome(end).equals(tgtDst+'\0'))
						return "cp: O diretório já existe. (Nada foi copiado)";
				
				Diretorio d = null;
				for (int end : aux1.getMapDir())
					if (findNome(end).equals(tgtOri+'\0')) {
						d = montarDir(end);
					}
						
				if (d != null) {
					ArrayList<Integer> livre = findAllFreeSpace();
					if (livre.size() == 0) return "cp: Não há espaço suficiente no HD para copiar o diretório. (Nada foi copiado)";
					Diretorio copia = copiar(d, aux2.getEndereco(), livre);
					if (copia == null) return "cp: Não há espaço suficiente no HD para copiar o diretório. (Nada foi copiado)";
					aux2.getMapDir().add(copia.getEndereco());
					desmontarDir(copia);
					desmontarDir(aux2);
				}
				
			} else {
				for (int end : aux2.getMapFiles())
					if (findNome(end).equals(tgtDst+'\0')) 
						return "cp: O arquivo já existe. (Nada foi copiado)";
				
				Arquivo molde = null;
				for (int end: aux1.getMapFiles())
					if (findNome(end).equals(tgtOri+'\0')) {
						molde = montarArq(end);
						break;
					}
				
				if (molde != null) {
					int newEnd = findNextSpace();
					if (newEnd < 0) return "cp: Espaço insuficiente no diretório destino. (Nada foi copiado)";
					Arquivo novo = new Arquivo(aux2.getEndereco(), molde);
					novo.setEndereco(newEnd);
					aux2.getMapFiles().add(newEnd);
					desmontarArq(novo);
					desmontarDir(aux2);
					
				} else return "cp: Arquivo não foi encontrado. (Nada foi copiado)";
			}
			
		// Copiar com novo nome
		} else {
			if (isDir) {
				for (int end : aux2.getMapFiles())
					if (findNome(end).equals(tgtDst+'\0')) return "cp: O diretório já existe. (Nada foi copiado)";
				
				Diretorio copiado = null;
				for (int end : aux1.getMapDir())
					if (findNome(end).equals(tgtOri+'\0')) {
						copiado = montarDir(end);
						break;
					}
				
				if (copiado != null) {
					ArrayList<Integer> livre = findAllFreeSpace();
					if (livre.size() == 0) return "cp: Não há espaço suficiente no HD para copiar o diretório. (Nada foi copiado)";
					Diretorio copia = copiar(copiado, aux2.getEndereco(), livre);
					if (copia == null) return "cp: Não há espaço suficiente no HD para copiar o diretório. (Nada foi copiado)";
					copia.setNome(tgtDst);
					aux2.getMapDir().add(copia.getEndereco());
					desmontarDir(copia);
					desmontarDir(aux2);
				}
				
			} else {
				for (int end : aux2.getMapFiles())
					if (findNome(end).equals(tgtDst+'\0')) return "cp: O arquivo já existe. (Nada foi copiado)";
				
				if ((tgtDst.contains(" ") || tgtDst.contains("/")) && !tgtDst.contains(".") && tgtDst.length()>86) return "mv: Nome de arquivo inválido. (Nada foi copiado)";
				
				Arquivo molde = null;
				for (int end : aux1.getMapFiles())
					if (findNome(end).equals(tgtOri+'\0')) {
						molde = montarArq(end);
						break;
					}
				
				if (molde != null) {
					int newEnd = findNextSpace();
					if (newEnd < 0) return "cp: Espaço insuficiente no diretório destino. (Nada foi copiado)";
					Arquivo novo = new Arquivo(aux2.getEndereco(), tgtDst, molde);
					novo.setEndereco(newEnd);
					aux2.getMapFiles().add(newEnd);
					desmontarArq(novo);
					desmontarDir(aux2);
					
				} else return "cp: Arquivo não foi encontrado. (Nada foi copiado)";
			}
		}

        //fim da implementacao do aluno
        return result;
    }
    public String info() {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: info");
        System.out.println("\tParametros: sem parametros");

        this.vetComandos.add("info");
        //nome do aluno
        String name = "Bruno Ribeiro Batista";
        //numero de matricula
        String registration = "202111020022";
        //versao do sistema de arquivos
        String version = "2.0";

        result += "Nome do Aluno:        " + name;
        result += "\nMatricula do Aluno:   " + registration;
        result += "\nVersao do Kernel:     " + version;

        return result;
    }
    public String dump(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: dump");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        try {
            PrintWriter arq = new PrintWriter(parameters, "UTF-8");
            for (String s : this.vetComandos)
            		arq.println(s);
            arq.close();
            result += "Dump criado com sucesso.";
            
        } catch (IOException e) {
        	result += "Erro ao criar dump.";
        	System.err.println(e);
        }
        this.vetComandos.add("dump "+parameters);
        //fim da implementacao do aluno
        return result;
    }
    public String batch(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: batch");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("batch "+parameters);
        try {
            File arq = new File(parameters);
            Scanner leit = new Scanner(arq);
            
            while (leit.hasNextLine())
            	executar(leit.nextLine());            
            leit.close();
            
            result += "Comandos foram executados.";
            	
		} catch (Exception e) {
			System.err.println(e);
			result += "Arquivo não existe.";
		}
        //fim da implementacao do aluno
        return result;
    }
          
    
    // FUNÇÕES UTITLITARIAS:
    //Encontrar uma pasta na HD, e devolve-la montada
    private Diretorio findDir(String parameters) {
    	if (parameters.isEmpty() || parameters.equals(" ")) return montarDir(this.atual);
    	
    	Diretorio aux;
    	int i = 1;
    	if (parameters.startsWith("../")) {
    		aux = montarDir(this.atual);
        	aux = montarDir(aux.getPai());
        } else if (parameters.startsWith("./")) {
        	aux = montarDir(this.atual);
        } else if (!parameters.startsWith("/")) {
			i = 0;
			aux = montarDir(this.atual);
		} else {
			i = 1;
			aux = montarDir(0);
		}
        
        String[] caminho = parameters.split("/");
		for (; i<caminho.length; i++) {
			if (caminho[i].equals("..")) {					
				aux = montarDir(aux.getPai());
			} else if (!caminho[i].equals(".")) {
				int endEscolhido = 0;
				String strEscolhido = "";
				for (int endPasta : aux.getMapDir()) {
					// 1o: Pegar o monte de bits do nome do filho
					String raw = "";
					for (int j=0; j<86*8; j++)
						if (hd.getBitDaPosicao(endPasta+(j+8))) raw += "1";
						else raw += "0";
					
					// 2o: converter o bruto em String
					String nome = "";
			    	for (int j=0;j<86;j++){
			    		String pedaco = raw.substring(8*j, (8*j)+8);
			    		if (!pedaco.equals("00000000")) nome += (char)Binario.binaryStringToInt(pedaco);
			    		else break;
			    	}
			    	
			    	// 3o: verificar se o nome bate
			    	if (caminho[i].equals(nome)) {
			    		endEscolhido = endPasta;
			    		strEscolhido = nome;
			    		break;			    	
			    	}
				}	
				if (!strEscolhido.equals("")) aux = montarDir(endEscolhido);
				else return null;
			}
		}
		
		return aux;
    }
    
    // Encontra o nome de um item salvo no endereço passado
    private String findNome(int end) {
    	
    	String raw = "";
    	for (int i=0; i<86*8; i++)
    		if (this.hd.getBitDaPosicao(end+8+i)) raw += '1';
    		else raw += '0';

    	String nome = "";
    	for (int i=0; i<86; i++){
    		int c = Binario.binaryStringToInt(raw.substring(i*8, (i*8)+8));
    		nome += (char) c;
    		if (c == 0) break;
    	}
    	
    	return nome;
    }
    
    //Executa automaticamente as funções do kernel de acordo com uma entrada de texto
    private String executar(String linha) {
    	String[] info = linha.split(" ",2);
    	switch (info[0]) {
			case "ls": return this.ls(info[1]);
			case "mkdir": return this.mkdir(info[1]);
			case "cd": return this.cd(info[1]);	
			case "createfile": return this.createfile(info[1]);
			case "cat": return this.cat(info[1]);
			case "info": return this.info();	
			case "rmdir": return this.rmdir(info[1]);
			case "rm": return this.rm(info[1]);
			case "chmod": return this.chmod(info[1]);	
			case "mv": return this.mv(info[1]);
			case "cp": return this.cp(info[1]);
			case "dump": return this.dump(info[1]);			
			
			default: return "";
		}
    }
    
    //"Funções gêmeas", pegam informações do HD e constroem estruturas legíveis
    private Diretorio montarDir(int endereco) {
    	String dirRaw = "";
    	for (int i=0; i<tamBloco; i++)
    		if (this.hd.getBitDaPosicao(endereco+i)) dirRaw += "1";
    		else dirRaw += "0";
    	
    	// 1a informação, Estado
    	int estado = Binario.binaryStringToInt(dirRaw.substring(0, 8));
    	dirRaw = dirRaw.substring(8);
    	
    	// 2a informação, Nome
    	String nome = "";
    	for (int i=0;i<86;i++){
    		String pedaco = dirRaw.substring(8*i, (8*i)+8);
    		if (!pedaco.equals("00000000"))
        		nome += (char)Binario.binaryStringToInt(pedaco);
    		else break;
    	}
    	dirRaw = dirRaw.substring(688);
    	
    	// 3a informação, Ponteiro Pai
    	int pai = Binario.binaryStringToInt(dirRaw.substring(0, 80));
    	dirRaw = dirRaw.substring(80);
    	
    	// 4a informação, Data de Criação
    	String data = "";
    	for (int i=0; i<12; i++)
    		data += Binario.binaryStringToInt(dirRaw.substring((8*i)+4, 8*(i+1)));
    	dirRaw = dirRaw.substring(96);
    	
    	// 5a informação, Permissão
    	int permit = 100*Binario.binaryStringToInt(dirRaw.substring(0,8));
    	permit += 10*Binario.binaryStringToInt(dirRaw.substring(8,16));
    	permit += Binario.binaryStringToInt(dirRaw.substring(16,24));
    	dirRaw = dirRaw.substring(24);
    	
    	// 6a informação, lista de pastas
    	ArrayList<Integer> arrayDir = new ArrayList<Integer>();
    	for (int i=0; i<20; i++) {
    		String pedaco = dirRaw.substring(80*i, (80*i)+80);
    		int endNew = Binario.binaryStringToInt(pedaco);
    		if (endNew != 0) arrayDir.add(endNew);
    	}
    	dirRaw = dirRaw.substring(1600);
    	
    	// 7a informação, lista de arquivos
    	ArrayList<Integer> arrayArq = new ArrayList<Integer>();
    	for (int i=0; i<20; i++) {
    		String pedaco = dirRaw.substring(80*i, (80*i)+80);
    		int endNew = Binario.binaryStringToInt(pedaco);
    		if (endNew != 0) arrayArq.add(endNew);
    	}    	
    	
    	return new Diretorio(estado, endereco, nome, pai, data, permit, arrayDir, arrayArq);
    }
    private Arquivo montarArq (int endereco) {
    	String dirRaw = "";
    	for (int i=0; i<tamBloco; i++)
    		if (this.hd.getBitDaPosicao(endereco+i)) dirRaw += "1";
    		else dirRaw += "0";
    	
    	// 1a informação, Estado
    	int estado = Binario.binaryStringToInt(dirRaw.substring(0, 8));
    	dirRaw = dirRaw.substring(8);
    	
    	// 2a informação, Nome
    	String nome = "";
    	for (int i=0;i<86;i++){
    		String pedaco = dirRaw.substring(8*i, (8*i)+8);
    		if (!pedaco.equals("00000000"))
        		nome += (char)Binario.binaryStringToInt(pedaco);
    		else break;
    	}
    	dirRaw = dirRaw.substring(688);
    	
    	// 3a informação, Ponteiro Pai
    	int pai = Binario.binaryStringToInt(dirRaw.substring(0, 80));
    	dirRaw = dirRaw.substring(80);
    	
    	// 4a informação, Data de Criação
    	String data = "";
    	for (int i=0; i<12; i++)
    		data += Binario.binaryStringToInt(dirRaw.substring((8*i)+4, 8*(i+1)));
    	dirRaw = dirRaw.substring(96);
    	
    	// 5a informação, Permissão
    	int permit = 100*Binario.binaryStringToInt(dirRaw.substring(0,8));
    	permit += 10*Binario.binaryStringToInt(dirRaw.substring(8,16));
    	permit += Binario.binaryStringToInt(dirRaw.substring(16,24));
    	dirRaw = dirRaw.substring(24);
    	
    	// 6a informação, conteudo
    	String conteudo = "";
    	for (int i=0;i<400;i++){
    		String pedaco = dirRaw.substring(8*i, (8*i)+8);
    		if (!pedaco.equals("00000000"))
        		conteudo += (char)Binario.binaryStringToInt(pedaco);
    		else break;
    	}
    	    	
    	return new Arquivo(estado, endereco, nome, pai, data, permit, conteudo);
    }

    // "Funções gêmeas", pegam estruturas prontas e escrevem no HD
    private void desmontarDir(Diretorio dir) {    	    	
    	// 1a parte: estado (fixo, estado sempre é 1 para dir's ativos)
    	String strBin = Binario.intToBinaryString(dir.getEstado(), 8);
    	
    	// 2a parte: nome da pasta
    	String aux = dir.getNome();
    	for (int i=0; i<86; i++) 
    		if (i >= aux.length()) strBin += "00000000";
    		else strBin += Binario.intToBinaryString((int) aux.charAt(i), 8);
    	
    	// 3a parte: ponteiro pai
    	strBin += Binario.intToBinaryString(dir.getPai(), 80);
    	
    	// 3a parte: data de criação
    	aux = dir.getCriacao();
    	for (char c : aux.toCharArray()) 
    		strBin += Binario.intToBinaryString((int) c, 8);
    	
    	// 4a parte: permissão
    	aux = String.valueOf(dir.getPermissao());
    	for (int i=0; i<3; i++) {
    		int a = Integer.parseInt(aux.substring(i, i+1));
    		strBin += Binario.intToBinaryString(a, 8);
    	}
    	
    	// 5a parte: pastas filhas
    	for (int i=0; i<20; i++) 
    		if (i >= dir.getMapDir().size()) strBin += Binario.intToBinaryString(0, 80);
    		else strBin += Binario.intToBinaryString(dir.getMapDir().get(i), 80);    	

    	// 6a parte: arquivos na pasta
    	for (int i=0; i<20; i++) 
    		if (i >= dir.getMapFiles().size()) strBin += Binario.intToBinaryString(0, 80);
    		else strBin += Binario.intToBinaryString(dir.getMapFiles().get(i), 80);
    	
    	// Final: escrever tudo na HD
    	for (int i=0; i<512*8; i++) {
    		boolean b = (strBin.charAt(i) == '1');
    		this.hd.setBitDaPosicao(b, dir.getEndereco()+i);    
    	}
    }
    private void desmontarArq(Arquivo arq) {    	    	
    	// 1a parte: estado (fixo, estado sempre é 1 para dir's ativos)
    	String strBin = Binario.intToBinaryString(arq.getEstado(), 8);
    	
    	// 2a parte: nome da pasta
    	String aux = arq.getNome();
    	for (int i=0; i<86; i++) 
    		if (i >= aux.length()) strBin += "00000000";
    		else strBin += Binario.intToBinaryString((int) aux.charAt(i), 8);
    	
    	// 3a parte: ponteiro pai
    	strBin += Binario.intToBinaryString(arq.getPai(), 80);
    	
    	// 3a parte: data de criação
    	aux = arq.getCriacao();
    	for (char c : aux.toCharArray()) 
    		strBin += Binario.intToBinaryString((int) c, 8);
    	
    	// 4a parte: permissão
    	aux = String.valueOf(arq.getPermissao());
    	for (int i=0; i<3; i++) {
    		int a = Integer.parseInt(aux.substring(i, i+1));
    		strBin += Binario.intToBinaryString(a, 8);
    	}
    	
    	// 5a parte: conteúdo
    	aux = arq.getConteudo();
    	for (int i=0; i<400; i++) 
    		if (i >= aux.length()) strBin += "00000000";
    		else strBin += Binario.intToBinaryString((int) aux.charAt(i), 8);
    	
    	// Final: escrever tudo na HD
    	for (int i=0; i<512*8; i++) {
    		boolean b = (strBin.charAt(i) == '1');
    		this.hd.setBitDaPosicao(b, arq.getEndereco()+i);    
    	}
    }
    
    // Encontra o próximo bloco disponível a partir do estado
    private int findNextSpace() {
    	int saida = -1;
    	
    	for (int i=0; i<this.hd.getNumBits(); i+=512*8)
    		if (!(this.hd.getBitDaPosicao(i+6) || this.hd.getBitDaPosicao(i+7))) return i;
    	
    	return saida;
    }
    
    // Conta quantos blocos estão disponíveis no HD pelo seu estado salvo
    private ArrayList<Integer> findAllFreeSpace() {
    	ArrayList<Integer> saida = new ArrayList<Integer>();
    	
    	for (int i=0; i<this.hd.getNumBits(); i+=512*8)
    		if (!(this.hd.getBitDaPosicao(i+6) || this.hd.getBitDaPosicao(i+7))) saida.add(i);
    	
    	return saida;
    }
    
    //"Auxiliar de 'findPermit()'" Transforma um número interiro em uma string de bits de tamanho determinado
    private static int[] intToBinary(int value, int size) {
        if (value > Math.pow(2, size) - 1) {
            return null;
        }
        int bin[] = new int[size];
        int i = 0;
        while (value > 0 && i < size) {
            int num = value % 2;
            value = value / 2;
            bin[i] = num;
            i++;
        }
        for (int j = 0; j <= size / 2; j++) {
            int temp = bin[j];
            bin[j] = bin[size - j - 1];
            bin[size - j - 1] = temp;
        }
        return bin;
    }
    
    //Pega os digitos da permissão (em String) e transforma na String da permissão
    private static String findPermit(String entrada) {
    	int nPerm1, nPerm2, nPerm3;
		nPerm1 = Integer.parseInt(entrada.charAt(0)+"");
		nPerm2 = Integer.parseInt(entrada.charAt(1)+"");
		nPerm3 = Integer.parseInt(entrada.charAt(2)+"");
		
		int[] bPerm1, bPerm2, bPerm3;
		bPerm1 = intToBinary(nPerm1, 3);
		bPerm2 = intToBinary(nPerm2, 3);
		bPerm3 = intToBinary(nPerm3, 3);
		
		String strPermit = "";
		if (bPerm1[0]==1) strPermit += "r";
		else strPermit += "-";
		if (bPerm1[1]==1) strPermit += "w";
		else strPermit += "-";
		if (bPerm1[2]==1) strPermit += "x";
		else strPermit += "-";
		if (bPerm2[0]==1) strPermit += "r";
		else strPermit += "-";
		if (bPerm2[1]==1) strPermit += "w";
		else strPermit += "-";
		if (bPerm2[2]==1) strPermit += "x";
		else strPermit += "-";
		if (bPerm3[0]==1) strPermit += "r";
		else strPermit += "-";
		if (bPerm3[1]==1) strPermit += "w";
		else strPermit += "-";
		if (bPerm3[2]==1) strPermit += "x";
		else strPermit += "-";
		
		return strPermit;
    }

    //"Função recursiva" Copia um diretório e todos os seus itens
	public Diretorio copiar(Diretorio molde, int pai, ArrayList<Integer> disponiveis) {
		// Vai acabar o espaço do HD?
		int vaiUsar = molde.getMapFiles().size() + molde.getMapDir().size()+1;
		if (disponiveis.size()-vaiUsar <= 0) return null;
		
		int paiEnd = disponiveis.remove(0);
		Diretorio novo = new Diretorio(pai, molde);
		novo.setEndereco(paiEnd);

		ArrayList<Arquivo> listArqs = new ArrayList<Arquivo>();
		for (int end : molde.getMapFiles()) {
			Arquivo original = montarArq(end);
			int newEnd = disponiveis.remove(0);
			Arquivo copia = new Arquivo(paiEnd, original);
			copia.setEndereco(newEnd);
			novo.getMapFiles().add(newEnd);
			listArqs.add(copia);
		}

		ArrayList<Diretorio> listDirs = new ArrayList<Diretorio>();
		for (int end : molde.getMapDir()) {
			Diretorio original = montarDir(end);
			int newEnd = disponiveis.remove(0);
			Diretorio copia = copiar(original, paiEnd, disponiveis);
			// Deu problema em uma pasta, nenhuma das anteriores pode ser escrita
			if (copia == null) return null;
			copia.setEndereco(newEnd);
			novo.getMapDir().add(newEnd);
			listDirs.add(copia);
		}
		
		// Se tudo acabou sem problema, pode escrever os itens
		for (Arquivo a : listArqs) desmontarArq(a);
		for (Diretorio d : listDirs) desmontarDir(d);
		
		return novo;
	}
}
