package exemplothreads;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Douglas
 */
public class ExemploThread extends Thread{
    
    private int idThread;
    
    ExemploThread (int idThread){   
        this.idThread = idThread;
    }
    
    @Override
    public void run () {
        int count = 0;
        while (true) {
            System.out.println("thread " + idThread + " - " + count++);
        }
    }
    
}
