
package implementacao;

import exemplothreads.Semaforo;

public class Main {	
	public static void main(String[] args) {
		// Passo 1: criar as 5 threads e o semáforo (com máximo de 1)
		Semaforo sem = new Semaforo(1);
		Filosofo f0 = new Filosofo(0,sem);
		Filosofo f1 = new Filosofo(1,sem);
		Filosofo f2 = new Filosofo(2,sem);
		Filosofo f3 = new Filosofo(3,sem);
		Filosofo f4 = new Filosofo(4,sem);
		
		// Passo 2: ligar as threats
		f0.run();
		f1.run();
		f2.run();
		f3.run();
		f4.run();
		
	}
}