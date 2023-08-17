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

    // TODO: 9 de 13 funções completas
    
    // *** C O M P L E T A S ***
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
				result = "createfile: Diretório não existe. Não foi possível criar arquivo.";
				return result;
			}
		}
		
		if (aux.getMapFiles().containsKey(caminho[i])) 
			result = "createfile: Arquivo já existe. Não foi posível criá-lo.";
		else {
			String infoFile[] = caminho[i].split(" ", 2);
			String conteudo = infoFile[1].replaceAll("\\\\n", "\n");
			Arquivo novo = new Arquivo(aux, infoFile[0], conteudo);
			
			aux.getMapFiles().put(infoFile[0], novo);
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
			} else if (caminho[i].equals("..")) { 
				aux = aux.getPai();
			} else if (!caminho[i].equals(".")) return "cat: Arquivo não existe.";
		}
			
		String infoFile[] = caminho[i].split(" ", 2);
		if (aux.getMapFiles().containsKey(infoFile[0])) result = aux.getMapFiles().get(infoFile[0]).getConteudo();
		else result = "cat: Arquivo não existe.";
        //fim da implementacao do aluno
        return result;
    }
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
    public String rmdir(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: rmdir");
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
				result = "rmdir: Diretório "+parameters+"não existe. (Nada foi removido)";
				return result;
			}
		}
		
		if (aux.getMapDir().containsKey(caminho[i])) {
			Diretorio target = aux.getMapDir().get(caminho[i]);
			if (target.getMapDir().isEmpty() && target.getMapFiles().isEmpty()) 
				aux.getMapDir().remove(target.getNome());
			else result = "rmdir: Diretório "+parameters+" possui arquivos e/ou diretórios. (Nada foi removido)";
		} else result = "rmdir: Diretório "+parameters+"não existe. (Nada foi removido)";
		
        //fim da implementacao do aluno
        return result;
    }
    public String rm(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: rm");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        boolean isDir = false;
        if (parameters.startsWith("-R ")) {
        	isDir = true;
        	parameters = parameters.substring(3);
        }
        
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
				result = "rm: Diretório "+parameters+"não existe. (Nada foi removido)";
				return result;
			}
		}
		if (isDir) {
			if (aux.getMapDir().containsKey(caminho[i])) {
				Diretorio target = aux.getMapDir().get(caminho[i]);
				aux.getMapDir().remove(target.getNome());
			} else result = "rm: Diretório "+parameters+" não existe. (Nada foi removido)";
		} else {
			if (aux.getMapFiles().containsKey(caminho[i])) {
				Arquivo target = aux.getMapFiles().get(caminho[i]);
				aux.getMapFiles().remove(target.getNome());
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
        boolean isDir = false;
        if (parameters.startsWith("-R ")) {
        	isDir = true;
        	parameters = parameters.substring(3);
        }
        
        Diretorio aux;
		int i = 1;
		if (parameters.startsWith("../")) aux = this.atual.getPai();
		else if (parameters.startsWith("./")) aux = this.atual;
		else {
			if (!parameters.startsWith("/")) i = 0;
			aux = this.raiz;
		}
		
		String[] paramRedux = parameters.split(" ", 2);
		String[] caminho = paramRedux[1].split("/");
		for (; i<(caminho.length-1); i++) {
			if (aux.getMapDir().containsKey(caminho[i])) {
				aux = aux.getMapDir().get(caminho[i]);
			} else {
				result = "chmod: Diretório "+parameters+" não existe. (Nada foi modificado)";
				return result;
			}
		}
		if (isDir) {
			if (aux.getMapDir().containsKey(caminho[i])) {
				Diretorio target = aux.getMapDir().get(caminho[i]);				
				String strPermit = findPermit(paramRedux[0]);
				
				for (Diretorio d: target.getMapDir().values())
					d.setPermissao("d"+strPermit);
				for (Arquivo a: target.getMapFiles().values())
					a.setPermissao("-"+strPermit);
					
			} else result = "chmod: Diretório "+parameters+" não existe. (Nada foi modificado)";
		} else {
			if (aux.getMapFiles().containsKey(caminho[i])) {
				Arquivo target = aux.getMapFiles().get(caminho[i]);				
				String strPermit = findPermit(paramRedux[0]);
				target.setPermissao("-"+strPermit);
			} else result = "chmod: Arquivo "+parameters+" não existe. (Nada foi modificado)";
		}
        //fim da implementacao do aluno
        return result;
    }

    
    // * * * P E N D E N T E S * * *    
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
    
    public static int[] intToBinary(int value, int size) {
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
    
    public static String findPermit(String entrada) {
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
