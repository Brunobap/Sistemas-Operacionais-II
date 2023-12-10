
package implementacao;


import exemplothreads.Semaforo;

public class Mesa extends Thread {
	
	private Filosofo[] filos;
	private Semaforo sem;
	
	public Mesa(Filosofo[] filos, Semaforo sem) {
		super("mesa");
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
    
    public synchronized Filosofo[] tirarFoto() {    	
        synchronized (this) {
            try {
            	Filosofo[] filos = new Filosofo[5];
            	for (int i=0; i<5; i++)	filos[i] = new Filosofo(this.filos[i]);
            	return filos;
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
	
}