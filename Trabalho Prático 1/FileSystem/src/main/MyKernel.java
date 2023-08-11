package main;

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

    public MyKernel() {
    	this.raiz = new Diretorio(null, "/");
    	this.raiz.setPai(raiz);
    	this.atual = this.raiz;
    }

    // *** C O M P L E T A ***
    public String ls(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: ls");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
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
        	Diretorio aux;
        	int i = 1;
        	
        	String paramRedux = parameters;
        	if (parameters.startsWith("-l ")) paramRedux = parameters.substring(3,parameters.length());
        	
            if (paramRedux.startsWith("../")) {
            	aux = this.atual.getPai();
            } else if (paramRedux.startsWith("./")) {
            	aux = this.atual;
            } else {
    			if (!paramRedux.startsWith("/")) i = 0;
    			aux = this.raiz;
    		}
            
            String[] caminho = paramRedux.split("/");
    		for (; i<caminho.length; i++) {
    			if (aux.getMapDir().containsKey(caminho[i])) {
    				aux = aux.getMapDir().get(caminho[i]);
    			} else if (caminho[i].equals("..")) {					
					aux = aux.getPai();
				} else if (!caminho[i].equals(".")) return "ls: Diretório não existe.";
    		}
    		
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

    // *** C O M P L E T A ***
    public String mkdir(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: mkdir");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
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

    // *** C O M P L E T A ***
    public String cd(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        String currentDir = "";
        System.out.println("Chamada de Sistema: cd");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        //indique o diretório atual. Por exemplo... /
        currentDir = operatingSystem.fileSystem.FileSytemSimulator.currentDir;
        
        Diretorio aux;
        int i = 1;
        if (parameters.startsWith("../")) {
        	aux = this.atual.getPai();
        	currentDir.substring(0, this.atual.getNome().length());
        } else if (parameters.startsWith("./")) {
        	aux = this.atual;
        } else {
			if (!parameters.startsWith("/")) i = 0;
			aux = this.raiz;
			currentDir = "/";
		}
        
        String[] caminho = parameters.split("/");
		for (; i<caminho.length; i++) {
			if (aux.getMapDir().containsKey(caminho[i])) {
				aux = aux.getMapDir().get(caminho[i]);
				if (!currentDir.equals("/")) currentDir += "/";
				currentDir += caminho[i];
			} else {
				if (caminho[i].equals("..")) {					
					if (aux != this.raiz) {
						currentDir = currentDir.substring(0, currentDir.length()-(aux.getNome().length()+1));
						aux = aux.getPai();
					}
				} else if (!caminho[i].equals("."))	return parameters+": Diretório não existe.";
			}
		}
		this.atual = aux;

        //setando parte gráfica do diretorio atual
        operatingSystem.fileSystem.FileSytemSimulator.currentDir = currentDir;

        //fim da implementacao do aluno
        return result;
    }

    public String rmdir(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: rmdir");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        //fim da implementacao do aluno
        return result;
    }

    public String cp(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: cp");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        //fim da implementacao do aluno
        return result;
    }

    public String mv(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: mv");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        //fim da implementacao do aluno
        return result;
    }

    public String rm(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: rm");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        //fim da implementacao do aluno
        return result;
    }

    public String chmod(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: chmod");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        //fim da implementacao do aluno
        return result;
    }

    public String createfile(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: createfile");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        Diretorio aux;
		int i = 1;
		if (parameters.startsWith("../")) aux = this.atual.getPai();
		else if (parameters.startsWith("./")) aux = this.atual;
		else {
			if (!parameters.startsWith("/")) i = 0;
			aux = this.raiz;
		}
		
		String[] caminho = parameters.split("/");
		for (; i<(caminho.length-1); i++) {
			if (aux.getMapDir().containsKey(caminho[i])) {
				aux = aux.getMapDir().get(caminho[i]);
			}
			else {
				Diretorio novo = new Diretorio(aux, caminho[i]);
				aux.getMapDir().put(caminho[i], novo);
				aux = novo;
			}
		}
			
		/*
		 * 
		 * public static String removeCarriageReturn(String query) {
        //        query.replaceAll(regex, replacement)
        String result = query.replaceAll("[\r\n]", " ");
        return result;
    }
		 */
		String infoFile[] = caminho[caminho.length-1].split(" ", 2);
		String conteudo = infoFile[1].replaceAll("(\\n)", "\r\n");
		Arquivo novo = new Arquivo(aux, infoFile[0], conteudo);
		
		aux.getMapFiles().put(infoFile[0], novo);
        
        //fim da implementacao do aluno
        return result;
    }

    public String cat(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: cat");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        Diretorio aux;
		int i = 1;
		if (parameters.startsWith("../")) aux = this.atual.getPai();
		else if (parameters.startsWith("./")) aux = this.atual;
		else {
			if (!parameters.startsWith("/")) i = 0;
			aux = this.raiz;
		}
		
		String[] caminho = parameters.split("/");
		for (; i<(caminho.length-1); i++) {
			if (aux.getMapDir().containsKey(caminho[i])) {
				aux = aux.getMapDir().get(caminho[i]);
			}
			else return "cat: Arquivo não existe.";
		}
			
		String infoFile[] = caminho[caminho.length-1].split(" ", 2);
		if (aux.getMapFiles().containsKey(infoFile[0])) result = aux.getMapFiles().get(infoFile[0]).getConteudo();
		else result = "cat: Arquivo não existe.";
        //fim da implementacao do aluno
        return result;
    }

    public String batch(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: batch");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        //fim da implementacao do aluno
        return result;
    }

    public String dump(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: dump");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        //fim da implementacao do aluno
        return result;
    }

    // COMPLETA
    public String info() {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: info");
        System.out.println("\tParametros: sem parametros");

        //nome do aluno
        String name = "Bruno Ribeiro Batista";
        //numero de matricula
        String registration = "202111020022";
        //versao do sistema de arquivos
        String version = "0.1";

        result += "Nome do Aluno:        " + name;
        result += "\nMatricula do Aluno:   " + registration;
        result += "\nVersao do Kernel:     " + version;

        return result;
    }

    
    // FUNÇÕES UTITLITARIAS
    public static String checkMonth(int month) {
		if (month == 1) return "Jan";
		else if (month == 2) return "Feb";
		else if (month == 3) return "Mar";
		else if (month == 4) return "Apr";
		else if (month == 5) return "May";
		else if (month == 6) return "Jun";
		else if (month == 7) return "Jul";
		else if (month == 8) return "Aug";
		else if (month == 9) return "Sep";
		else if (month == 10) return "Oct";
		else if (month == 11) return "Nov";
		else if (month == 12) return "Dec";
		else return "-";
	}
}
