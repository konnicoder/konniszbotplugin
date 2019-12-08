/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package konni.konniskot;



/**
 *
 * @author Dennis
 */
public abstract class Task extends Thread {
    
    protected final BlockingAI ai;
    protected final int aiTaskId;
    
    public Task(int interval) {
        this.ai = new BlockingAI();
        aiTaskId = Main.self.scheduleSyncRepeatingTask(Main.instance, ai, interval);
    }
    
    protected void unregister() {
        Main.self.cancelTask(aiTaskId);
    }    
}
