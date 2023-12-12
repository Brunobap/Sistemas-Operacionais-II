
package implementacao;

public abstract class Mesa extends Thread {
	
	public int num;	
	public Filosofo[] filos;
	
	public Mesa(Filosofo[] filos) {
		this.filos = filos;
		this.num = 0;
	}

	// Funções "abstratas"
    public synchronized void down(int id) { }
    public synchronized void up() { }

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