
package semaforo;

import implementacao.Filosofo;
import implementacao.Mesa;

public class Semaforo extends Mesa {
	
	public Semaforo(Filosofo[] filos) {
		super(filos);
		this.num = 0;
	}

	@Override
    public synchronized void down(int id) {
        synchronized (this) {
            try {
                while (num == 0) this.wait();
                num--;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    public synchronized void up() {
        synchronized (this) {
            this.num++;
            notifyAll();
        }
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