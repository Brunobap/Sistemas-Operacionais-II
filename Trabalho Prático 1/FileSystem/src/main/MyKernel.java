package main;

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
    	this.raiz = new Diretorio();
    	this.raiz.setPai(raiz);
    	this.atual = this.raiz;
    }

    public String ls(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: ls");
        System.out.println("\tParametros: " + parameters);

        //inicio da implementacao do aluno
        if (parameters.equals("")) {
        	int i = 1;
        	for (Diretorio d : this.atual.getMapDir().values()) {
        		result += d.getNome()+" ";
        		if (i%3 == 0) result += "\n";
        		i++;
        	}
        } else if (parameters.equals("-l")) {
        	for (Diretorio d : this.atual.getMapDir().values())
        		result += d.getPermissao()+" ";
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
    			}
    			else {
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
				currentDir += caminho[i]+"/";
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
        //fim da implementacao do aluno
        return result;
    }

    public String cat(String parameters) {
        //variavel result deverah conter o que vai ser impresso na tela apos comando do usuário
        String result = "";
        System.out.println("Chamada de Sistema: cat");
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

}
