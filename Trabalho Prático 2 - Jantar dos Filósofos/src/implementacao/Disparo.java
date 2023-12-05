
package implementacao;

import java.io.IOException;

import exemplothreads.Semaforo;

public class Disparo extends Thread {
	
	private Filosofo[] filos;
	private Semaforo sem;
	
	public Disparo(Filosofo[] filos, Semaforo sem) {
		this.filos = filos;
		this.sem = sem;
	}

    public synchronized void largada() {    	
        synchronized (this) {
            try {
            	for (Filosofo filo : this.filos) filo.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public synchronized void parada() {    	
        synchronized (this) {
            try {
            	while (true) {
            		this.filos[0].wait();
            		this.filos[1].wait();
            		this.filos[2].wait();
            		this.filos[3].wait();
            		this.filos[4].wait();
            	}
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
	
}