package main;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import fileItens.Arquivo;
import fileItens.Diretorio;
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
	
	private Diretorio atual;
	private Diretorio raiz;
	private ArrayList<String> vetComandos;

    public MyKernel() {
    	this.raiz = new Diretorio(null, "/");
    	this.raiz.setPai(raiz);
    	this.atual = this.raiz;
    	this.vetComandos = new ArrayList<String>();
    }

    // TODO: 13 de 13 funções completas :)
    
    // *** C O M P L E T A S ***
    public String ls(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: ls");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        this.vetComandos.add("ls "+parameters);
        if (parameters.equals("")) {
        	for (Diretorio d : this.atual.getMapDir().values())
        		result += d.getNome()+" ";
        	for (Arquivo a : this.atual.getMapFiles().values())
        		result += a.getNome()+" ";
        } else if (parameters.equals("-l")) {
        	for (Diretorio d : this.atual.getMapDir().values())
        		result += 
        			d.getPermissao()+" "+
        			checkMonth(d.getCriacao().getMonth()+1)+" "+
        			(d.getCriacao().getDate()+" ")+
        			(d.getCriacao().getHours()+":"+d.getCriacao().getMinutes())+" "+
        			d.getNome()+"\n";
        	for (Arquivo a : this.atual.getMapFiles().values())
        		result += 
        			a.getPermissao()+" "+
        			checkMonth(a.getCriacao().getMonth()+1)+" "+
        			(a.getCriacao().getDate()+" ")+
        			(a.getCriacao().getHours()+":"+a.getCriacao().getMinutes())+" "+
        			a.getNome()+"\n";
        	
        } else {        	
        	String paramRedux = parameters;
        	if (parameters.startsWith("-l ")) paramRedux = parameters.substring(3,parameters.length());

        	Diretorio aux = this.encontrar(paramRedux);
        	if (aux == null) return "ls: Diretório não foi encontrado.";
        	
    		if (parameters.startsWith("-l ")) {
	    		for (Diretorio d : aux.getMapDir().values())
	        		result += 
	        			d.getPermissao()+" "+
	        			checkMonth(d.getCriacao().getMonth()+1)+" "+
	        			(d.getCriacao().getDate()+" ")+
	        			(d.getCriacao().getHours()+":"+d.getCriacao().getMinutes())+" "+
	        			d.getNome()+"\n";
	        	for (Arquivo a : aux.getMapFiles().values())
	        		result += 
	    				a.getPermissao()+" "+
	    				checkMonth(a.getCriacao().getMonth()+1)+" "+
	    				(a.getCriacao().getDate()+" ")+
	    				(a.getCriacao().getHours()+":"+a.getCriacao().getMinutes())+" "+
	    				a.getNome()+"\n";
    		} else {
    			for (Diretorio d : aux.getMapDir().values())
	        		result += d.getNome()+" ";
	        	for (Arquivo a : aux.getMapFiles().values())
	        		result += a.getNome()+" ";
    		}
        }
        //fim da implementacao do aluno
        return result;
    }
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
    		if (parameters.startsWith("../")) aux = this.atual.getPai();
    		else if (parameters.startsWith("./")) aux = this.atual;
    		else {
    			if (!parameters.startsWith("/")) i = 0;
    			aux = this.raiz;
    		}
    		    		
    		String[] caminho = parameters.split("/");
    		if (caminho[caminho.length-1].contains(".") || caminho[caminho.length-1].contains(" ")) return "mkdir: Nome de diretório inválido. (Nada foi criado)";
    		for (; i<caminho.length; i++) {
    			if (aux.getMapDir().containsKey(caminho[i])) {
    				aux = aux.getMapDir().get(caminho[i]);
    				result = "mkdir: "+parameters+": Diretorio ja existe (Nenhum diretorio foi criado)";
    			} else if (caminho[i].equals("..")) {					
					aux = aux.getPai();
				} else if (!caminho[i].equals(".")) {
    				Diretorio novo = new Diretorio(aux, caminho[i]);
    				aux.getMapDir().put(caminho[i], novo);
    				aux = novo;
    				result = "";
    			}
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
        
        Diretorio aux = encontrar(parameters);
        if (aux == null) return "cd: Diretório não foi encontrado.";
		this.atual = aux;

        //setando parte gráfica do diretorio atual
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
        } else nome = caminho;
        Diretorio aux = encontrar(caminho);
        if (aux == null) return "createfile: Diretório não encontrado. (Nenhum arquivo criado)";
                      
        String texto = parameters.substring(parameters.indexOf(" ")+1);
        
		if (aux.getMapFiles().containsKey(nome)) 
			result = "createfile: Arquivo já existe. Não foi posível criá-lo.";
		else {
			if ((nome.contains(" ") || nome.contains("/")) && !nome.contains(".")) return "createfile: Nome de arquivo inválido. (Nada foi criado)";
			String conteudo = texto.replaceAll("\\\\n", "\n");
			Arquivo novo = new Arquivo(aux, nome, conteudo);
			
			aux.getMapFiles().put(nome, novo);
		}
        
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
        } else nome = parameters;
        
        Diretorio aux = encontrar(caminho);
		if (aux == null) return "cat: Diretorio do arquivo não existe. (Nada foi lido)";
			
		if (aux.getMapFiles().containsKey(nome)) result = aux.getMapFiles().get(nome).getConteudo();
		else result = "cat: Arquivo não existe. (Nada foi lido)";
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
        String version = "1.0";

        result += "Nome do Aluno:        " + name;
        result += "\nMatricula do Aluno:   " + registration;
        result += "\nVersao do Kernel:     " + version;

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
        Diretorio aux = encontrar(caminho);
		if (aux == null) return "rmdir: Diretório "+parameters+"não existe. (Nada foi removido)";
		
		if (aux.getMapDir().containsKey(strAlvo)) {
			Diretorio dirAlvo = aux.getMapDir().get(strAlvo);
			if (dirAlvo.getMapDir().isEmpty() && dirAlvo.getMapFiles().isEmpty()) 
				aux.getMapDir().remove(dirAlvo.getNome());
			else result = "rmdir: Diretório "+parameters+" possui arquivos e/ou diretórios. (Nada foi removido)";
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
        if (parameters.startsWith("-R ")) {
        	isDir = true;
        	parameters = parameters.substring(3);
        }

        String caminho = "";
        String strAlvo;
        if (parameters.contains("/")) {
        	caminho = parameters.substring(0, parameters.lastIndexOf("/"));
        	strAlvo = parameters.substring(parameters.lastIndexOf("/")+1);
        } else strAlvo = parameters;
        Diretorio aux = encontrar(caminho);
        if (aux == null) result = "rm: Diretório "+parameters+"não existe. (Nada foi removido)";

		if (isDir) {
			if (aux.getMapDir().containsKey(strAlvo)) {
				Diretorio dirAlvo = aux.getMapDir().get(strAlvo);
				aux.getMapDir().remove(dirAlvo.getNome());
			} else result = "rm: Diretório "+parameters+" não existe. (Nada foi removido)";
		} else {
			if (aux.getMapFiles().containsKey(strAlvo)) {
				Arquivo dirAlvo = aux.getMapFiles().get(strAlvo);
				aux.getMapFiles().remove(dirAlvo.getNome());
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

        String numPermit = parameters.substring(0, 3);
        String strPermit = findPermit(numPermit);
        parameters = parameters.substring(4);
        
		String caminho = "";
		String strAlvo;
		if (parameters.contains("/")) {
			caminho = parameters.substring(0,parameters.lastIndexOf("/"));
			strAlvo = parameters.substring(parameters.lastIndexOf("/")+1);
		} else strAlvo = parameters;
        Diretorio aux = encontrar(caminho);
        if (aux == null) return "chmod: Diretório "+parameters+" não existe. (Nada foi modificado)";

		if (isDir) {
			if (aux.getMapDir().containsKey(strAlvo)) {
				Diretorio dirAlvo = aux.getMapDir().get(strAlvo);	
				
				for (Diretorio d: dirAlvo.getMapDir().values())
					d.setPermissao("d"+strPermit);
				for (Arquivo a: dirAlvo.getMapFiles().values())
					a.setPermissao("-"+strPermit);
					
			} else result = "chmod: Diretório "+parameters+" não existe. (Nada foi modificado)";
		} else {
			if (aux.getMapFiles().containsKey(strAlvo)) {
				Arquivo target = aux.getMapFiles().get(strAlvo);
				target.setPermissao("-"+strPermit);
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
		
		Diretorio aux1 = encontrar(camOri);
		if (aux1 == null) return "mv: Arquivo/Diretório não existe.";

		Diretorio aux2 = encontrar(camDst);
		if (aux2 == null) return "mv: Arquivo/Diretório não existe.";
			
		// Pastas  iguais, renomear
		if (aux1 == aux2) {
			if (aux1.getMapFiles().containsKey(tgtOri)) {
				if (aux1.getMapFiles().containsKey(tgtDst)) return "mv: Já existe um arquivo com esse nome. (Nada foi renomeado)";
				Arquivo a = aux1.getMapFiles().remove(tgtOri);
				a.setNome(tgtDst);
				aux1.getMapFiles().put(tgtDst, a);
				
			} else if (aux1.getMapDir().containsKey(tgtOri)) {
				if (aux1.getMapDir().containsKey(tgtDst)) return "mv: Já existe um diretório com esse nome. (Nada foi renomeado)";
				Diretorio d = aux1.getMapDir().remove(tgtOri);
				d.setNome(tgtDst);
				aux1.getMapDir().put(tgtDst, d);
				
			} else return "mv: Arquivo/Diretório não existe. (Nada foi renomeado)";				
		
		// Pastas diferentes, mover
		} else {
			if (aux1.getMapFiles().containsKey(tgtOri)) {
				Arquivo a = aux1.getMapFiles().remove(tgtOri);
				aux2.getMapFiles().put(a.getNome(), a);
				
			} else if (aux1.getMapDir().containsKey(tgtOri)) {
				Diretorio d = aux1.getMapDir().remove(tgtOri);
				aux2.getMapDir().put(d.getNome(), d);
				
			} else return "mv: Arquivo/Diretório não existe. (Nada foi movido)";	
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

        Diretorio aux1 = encontrar(camOri);
        if (aux1 == null) return "cp: Diretório não encontrado. (Nada foi copiado)";
        
        Diretorio aux2 = encontrar(camDst);
        if (aux2 == null) return "cp: Diretório não encontrado. (Nada foi copiado)";
		
		// Copiar sem renomear
		if (paramSplit[1].endsWith("/")) {
			if (isDir) {
				if (aux2.getMapDir().containsKey(tgtDst)) return "cp: O diretório já existe. (Nada foi copiado)";
				if (aux1.getMapDir().containsKey(tgtOri)) {
					Diretorio copiado = aux1.getMapDir().get(tgtOri);
					Diretorio copia = copiado.copiar(aux2);
					aux2.getMapDir().put(copia.getNome(), copia);
				}
				
			} else {
				if (aux2.getMapFiles().containsKey(tgtDst)) return "cp: O arquivo já existe. (Nada foi copiado)";
				if (aux1.getMapFiles().containsKey(tgtOri)) {
					Arquivo molde = aux1.getMapFiles().get(tgtOri);
					Arquivo novo = new Arquivo(aux2, molde);
					aux2.getMapFiles().put(novo.getNome(), novo);
					
				} else return "cp: Arquivo não foi encontrado. (Nada foi copiado)";
			}
			
		// Copiar com novo nome
		} else {
			if (isDir) {
				if (aux2.getMapDir().containsKey(tgtDst)) return "cp: O diretório já existe. (Nada foi copiado)";
				if (aux1.getMapDir().containsKey(tgtOri)) {
					Diretorio copiado = aux1.getMapDir().get(tgtOri);
					Diretorio copia = copiado.copiar(aux2);
					copia.setNome(tgtDst);
					aux2.getMapDir().put(copia.getNome(), copia);
				}
				
			} else {
				if (aux2.getMapFiles().containsKey(tgtDst)) return "cp: O arquivo já existe. (Nada foi copiado)";
				if ((tgtDst.contains(" ") || tgtDst.contains("/")) && !tgtDst.contains(".")) return "mv: Nome de arquivo inválido. (Nada foi copiado)";
				if (aux1.getMapFiles().containsKey(tgtOri)) {
					Arquivo molde = aux1.getMapFiles().get(tgtOri);
					Arquivo novo = new Arquivo(aux2, tgtDst, molde);
					aux2.getMapFiles().put(novo.getNome(), novo);
					
				} else return "cp: Arquivo não foi encontrado. (Nada foi copiado)";
			}
		}

        //fim da implementacao do aluno
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
          
    
    // FUNÇÕES UTITLITARIAS
    private Diretorio encontrar(String parameters) {
    	if (parameters.equals("")) return this.atual;
    	
    	Diretorio aux;
    	int i = 1;
    	if (parameters.startsWith("../")) {
        	aux = this.atual.getPai();
        } else if (parameters.startsWith("./")) {
        	aux = this.atual;
        } else {
			if (!parameters.startsWith("/")) i = 0;
			aux = this.raiz;
		}
        
        String[] caminho = parameters.split("/");
		for (; i<caminho.length; i++) {
			if (aux.getMapDir().containsKey(caminho[i])) {
				aux = aux.getMapDir().get(caminho[i]);
			} else if (caminho[i].equals("..")) {					
				aux = aux.getPai();
			} else if (!caminho[i].equals(".")) return null;
		}
		
		return aux;
    }
    
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
    
    private static String checkMonth(int month) {
    	switch (month) {
			case 1:  return "Jan";
			case 2: return "Feb";
			case 3: return "Mar";
			case 4: return "Apr";
			case 5: return "May";
			case 6: return "Jun";
			case 7: return "Jul";
			case 8: return "Aug";
			case 9: return "Sep";
			case 10: return "Oct";
			case 11: return "Nov";
			case 12: return "Dec";
			
			default: return "-";
    	}
	}
    
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
}
