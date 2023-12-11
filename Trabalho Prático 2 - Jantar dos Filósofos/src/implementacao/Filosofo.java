
package implementacao;


import java.util.Random;

public class Filosofo extends Thread {

	public int idThread, pratos;
	public boolean taComendo;
	public Mesa mesa;
	public long m1, m2, pensou, comeu, espera, esperaMax, esperaMed;
	
    
    public Filosofo (int idThread, Mesa mesa){   
        this.idThread = idThread;
        this.mesa = mesa;  
        this.taComendo = false;
        this.m1 = System.currentTimeMillis();
        this.pratos = 0;
        this.esperaMax = 0;
    }
    public Filosofo (Filosofo molde){   
        this.idThread = molde.idThread;
        this.taComendo = molde.taComendo;
        this.pratos = molde.pratos;
        this.esperaMax = molde.esperaMax;
        this.esperaMed = molde.esperaMed;
    }
    
    @Override
    public void run () {    	     	
    	Random rand = new Random();
    	this.m2 = this.m1;
    	
    	// Repetir para sempre
    	while (true) {    		
    		// Passa um tempo pensando
        	this.pensou = rand.nextLong(100, 500);
        	try {
        		sleep(this.pensou);
        	} catch (Exception e) {
				// TODO: handle exception
			}
        	// Pede permissão, ...
        	mesa.down();  	      
        	this.taComendo = true;
        	this.pratos++;
        	
        	// faz o que tem que fazer, ...
        	this.m1 = System.currentTimeMillis();
        	this.espera = (m1-m2);
        	if (espera > esperaMax) esperaMax = espera;
        	this.esperaMed = ((pratos-1)*this.esperaMed + espera) / pratos;
        	this.m2 = this.m1;
        	this.comeu = rand.nextLong(100, 500);
        	try {
        		sleep(this.comeu);
        	} catch (Exception e) {
				// TODO: handle exception
			}
        	System.out.println("Filosofo "+this.idThread+" esperou por "+espera+" ms, pensou por "+pensou+" ms, comeu por "+this.comeu+" ms.");
        	
        	// e libera a área de concorrência
        	this.taComendo = false;
        	mesa.up();
    	}
    }
}