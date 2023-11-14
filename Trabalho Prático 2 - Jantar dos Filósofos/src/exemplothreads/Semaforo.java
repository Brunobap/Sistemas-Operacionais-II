/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exemplothreads;

/**
 *
 * @author Douglas
 */
public class Semaforo {

    private int num;

    public Semaforo(int numMaxThreads) {
        this.num = numMaxThreads;
    }

    public synchronized void down() {
        synchronized (this) {
            try {
                while (num == 0) {
                    this.wait();
                }
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

}
