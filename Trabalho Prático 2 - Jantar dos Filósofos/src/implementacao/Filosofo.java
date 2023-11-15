
package implementacao;


import java.util.Random;

import exemplothreads.Semaforo;

public class Filosofo extends Thread {

	private int idThread;
	private Semaforo semaforo;
	private long m1, m2, pensou, comeu;
    
    public Filosofo (int idThread, Semaforo sem){   
        this.idThread = idThread;
        this.semaforo = sem;    
        this.m1 = System.nanoTime();
    }
    
    @Override
    public void run () {    	     	
    	Random rand = new Random();
    	this.m2 = this.m1;
    	
    	// Repetir para sempre
    	while (true) {    		
        	// Pede permissão, ...
        	semaforo.down();  	        	
        	
        	// faz o que tem que fazer, ...
        	this.m1 = System.nanoTime();
        	this.pensou = (m1-m2)/1000000;
        	this.m2 = this.m1;
        	this.comeu = rand.nextLong(1000, 5000);
        	try {
        		sleep(this.comeu);
        	} catch (Exception e) {
				// TODO: handle exception
			}
        	System.out.println("Filosofo "+this.idThread+" pensou por "+this.pensou+" milisegundos.");
        	
        	// e libera a área de concorrência
        	semaforo.up();
    	}
    }
}