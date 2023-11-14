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
public class ExemploThreads {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Runnable thread1 = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (true){
                    System.out.println("thread 1 - " + count++);
                }
            }
        };
        
        Runnable thread2 = new Runnable() {
            @Override
            public void run() {
                int count = 0;
                while (true){
                    System.out.println("thread 2 - " + count++);
                }
            }
        };
        
        Thread t1 = new Thread(thread1);
        Thread t2 = new Thread(thread2);
        
        t1.start();
        t2.start();
        
        ExemploThread ex1 = new ExemploThread(3);
        ex1.start();
        ExemploThread ex2 = new ExemploThread(4);
        ex2.start();
        
    }
    
}
