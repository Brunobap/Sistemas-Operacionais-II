
package monitor;

import implementacao.Filosofo;
import implementacao.Mesa;

public class Monitor extends Mesa {
	
	
	public Monitor(Filosofo[] filos) {
		super(filos);
		this.num = -1;
	}

	@Override
    public synchronized void down(int id) {
        synchronized (this) {
            try {
                while (num != id) this.wait();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
	@Override
    public synchronized void up() {
        synchronized (this) {
            if (this.num < this.filos.length-1) this.num++;
            else this.num = 0;
            notifyAll();
        }
    }	
}