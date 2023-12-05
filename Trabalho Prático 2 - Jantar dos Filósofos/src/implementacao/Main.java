
package implementacao;


import exemplothreads.Semaforo;

public class Main {	
	public static void main(String[] args) {	
		
		// Passo 1: criar as 5 threads e o semáforo (com máximo de 1)
		Semaforo sem = new Semaforo(1);		
		Filosofo[] filos = new Filosofo[5];
		for (int i=0; i<5; i++) filos[i] = new Filosofo(i,sem);

		ThreadGroup mesa = filos[0].getThreadGroup();
		Thread princ = Thread.currentThread();
		princ.setPriority(Thread.MAX_PRIORITY);
		
		// Passo 2: ligar as threads e o contador
		Disparo disp = new Disparo(filos,sem);
		disp.start();		
		
		// Passo 3: parar todas as threads ao mesmo tempo
		for (int i=0; i<Integer.MAX_VALUE; i++) ;
		mesa.suspend();
		
		// Passo 4: mostrar as suas informações
		for (Filosofo filo : filos)
			if (filo.isComendo()) System.out.println(filo.getId()+" está comendo");
			else  System.out.println(filo.getId()+" não está comendo");
		mesa.resume();
	}
}