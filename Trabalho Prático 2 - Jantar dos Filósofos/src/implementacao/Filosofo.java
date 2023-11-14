
package implementacao;

import exemplothreads.Semaforo;

public class Filosofo extends Thread {

	private int idThread;
	private Semaforo semaforo;
	//TODO: marcadores de tempo, tempo que comeu e tempo que pensou
    
    public Filosofo (int idThread, Semaforo sem){   
        this.idThread = idThread;
        this.semaforo = sem;
    }
    
    @Override
    public void run () {
        int count = 0;
        while (true) {
            System.out.println("thread " + idThread + " - " + count++);
        }
    }
}