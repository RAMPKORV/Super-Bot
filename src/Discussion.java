
import java.util.ArrayList;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author patrik
 */
class Discussion {
    
    public Discussion(String s, List<Sentence> l, int ttl, long ts) {
        subject = s;
        log = l;
        timeToLive = ttl;
        timeStarted = ts;
    }
    
    public Discussion() {
    }
    
    public String subject;
    public List<Sentence> log = new ArrayList<Sentence>();
    public int timeToLive;
    public long timeStarted;
    
    public boolean isAlive() {
        return ((System.currentTimeMillis() - timeStarted) / 1000) < timeToLive;
    }
    
}
