
package implementacao;


import java.util.Random;

import exemplothreads.Semaforo;

public class Filosofo extends Thread {

	private int idThread;
	private boolean taComendo;
	private Semaforo semaforo;
	private long m1, m2, pensou, comeu, espera;
    
    public Filosofo (int idThread, Semaforo sem){   
        this.idThread = idThread;
        this.semaforo = sem;  
        this.taComendo = false;
        this.m1 = System.currentTimeMillis();
    }
    
    @Override
    public void run () {    	     	
    	Random rand = new Random();
    	this.m2 = this.m1;
    	
    	// Repetir para sempre
    	while (true) {    		
        	// Pede permissão, ...
        	semaforo.down();  	      
        	this.taComendo = true;
        	
        	// faz o que tem que fazer, ...
        	this.m1 = System.currentTimeMillis();
        	this.espera = (m1-m2);
        	this.m2 = this.m1;
        	this.comeu = rand.nextLong(100, 500);
        	this.pensou = rand.nextLong(100, 500);
        	try {
        		sleep(this.pensou);
        		sleep(this.comeu);
        	} catch (Exception e) {
				// TODO: handle exception
			}
        	System.out.println("Filosofo "+this.idThread+" esperou por "+espera+" ms, pensou por "+pensou+" ms, comeu por "+this.comeu+" ms.");
        	
        	// e libera a área de concorrência
        	this.taComendo = false;
        	semaforo.up();
    	}
    }
}