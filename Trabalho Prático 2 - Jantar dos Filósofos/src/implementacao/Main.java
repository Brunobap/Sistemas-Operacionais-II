
package implementacao;


import exemplothreads.Semaforo;

public class Main {	
	public static void main(String[] args) {	
		
		// Passo 1: criar as 5 threads e o semáforo (com máximo de 1)
		Semaforo sem = new Semaforo(1);		
		Filosofo[] filos = new Filosofo[5];
		for (int i=0; i<5; i++) filos[i] = new Filosofo(i,sem);

		// Passo 2: ligar as threads e o contador
		Disparo disp = new Disparo(filos);
		disp.run();
	}
}