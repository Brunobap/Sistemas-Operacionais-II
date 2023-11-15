
package implementacao;

public class Disparo extends Thread {
	
	private Filosofo[] filos;
	
	public Disparo(Filosofo[] filos) {
		this.filos = filos;
	}
	
	@Override
    public void run () {    	
		filos[0].start();   	
		filos[1].start();   	
		filos[2].start();   	
		filos[3].start();   	
		filos[4].start();
    }
}