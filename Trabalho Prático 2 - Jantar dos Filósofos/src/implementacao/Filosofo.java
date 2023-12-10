
package implementacao;


import java.util.Random;

import exemplothreads.Semaforo;

public class Filosofo extends Thread {

	public int idThread, pratos;
	public boolean taComendo;
	public Semaforo semaforo;
	public long m1, m2, pensou, comeu, espera;
    
    public Filosofo (int idThread, Semaforo sem){   
        this.idThread = idThread;
        this.semaforo = sem;  
        this.taComendo = false;
        this.m1 = System.currentTimeMillis();
        this.pratos = 0;
    }
    public Filosofo (Filosofo molde){   
        this.idThread = molde.idThread;
        this.semaforo = molde.semaforo;  
        this.taComendo = molde.taComendo;
        this.m1 = molde.m1;
        this.m2 = molde.m2;
        this.pratos = molde.pratos;
        this.comeu = molde.comeu;
        this.espera = molde.espera;
    }
    
    @Override
    public void run () {    	     	
    	Random rand = new Random();
    	this.m2 = this.m1;
    	
    	// Repetir para sempre
    	while (true) {    		
    		// Passa um tempo pensando
        	this.comeu = rand.nextLong(100, 500);
        	try {
        		sleep(this.pensou);
        	} catch (Exception e) {
				// TODO: handle exception
			}
        	// Pede permissão, ...
        	semaforo.down();  	      
        	this.taComendo = true;
        	this.pratos++;
        	
        	// faz o que tem que fazer, ...
        	this.m1 = System.currentTimeMillis();
        	this.espera = (m1-m2);
        	this.m2 = this.m1;
        	this.pensou = rand.nextLong(100, 500);
        	try {
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