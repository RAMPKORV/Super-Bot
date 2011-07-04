
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author patrik
 */
class ReactionSender {
    Reaction mR;
    Bot mB;
    private static Thread currentThread;
    public ReactionSender(final Bot b, final Reaction r) {
        mR = r;
        mB = b;
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(currentThread != null && currentThread.isAlive()) {
                    try {
                        currentThread.join(30000);
                    } catch (InterruptedException ex) {
                        return;
                    } 
                }
                currentThread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(r.secondsToThink*1000);
                        } catch (InterruptedException ex) {
                            return;
                        } finally {
                            mB.sendMessage(r.response);
                        }
                    }
                });
                currentThread.start();
            }
        }).start();
        
    }
    
}
