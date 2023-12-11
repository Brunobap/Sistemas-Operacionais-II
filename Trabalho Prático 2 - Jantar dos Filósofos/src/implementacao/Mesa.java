
package implementacao;

public class Mesa extends Thread {
	
	public int num;	
	private Filosofo[] filos;
	
	public Mesa(Filosofo[] filos) {
		this.filos = filos;
		this.num = 0;
	}

    public synchronized void down() {
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