
package implementacao;

import exemplothreads.Semaforo;

public class Main {	
	public static void main(String[] args) {	
		// Passo 0: criar o grupo das threads
		//ThreadPoolExecutor mesa = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
		
		// Passo 1: criar as 5 threads e o semáforo (com máximo de 1)
		Semaforo sem = new Semaforo(1);		
		Filosofo[] filos = new Filosofo[5];
		for (int i=0; i<5; i++) filos[i] = new Filosofo(i,sem);
		
		// Passo 2: ligar as threads e o contador
		Mesa mesa = new Mesa(filos,sem);
		mesa.largada();	
		
		// Passo 3: parar todas as threads ao mesmo tempo
		Filosofo[] foto;
		try {
			Thread.currentThread();
			Thread.sleep(1000);			
			
			//mesa.shutdownNow();
			foto = mesa.tirarFoto();
			sem.num = -1;
		} catch (Exception e) {
			foto = filos;
		}
		
		// Passo 4: mostrar as suas informações
		for (Filosofo filo : foto)
			if (filo.taComendo) System.out.println(filo.idThread+" está comendo");
			else  System.out.println(filo.idThread+" não está comendo");
		sem.up();
	}
}