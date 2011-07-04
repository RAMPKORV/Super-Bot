
import java.util.ArrayList;
import java.util.List;
import org.jibble.pircbot.PircBot;


public abstract class Bot extends PircBot {
    protected String mServer = "irc.esper.net";
    protected String mChannel = "#monsquaz";
    protected String mNick = "ROTADERP";
    protected Discussion currentDiscussion = null;
    
    public Bot() {
        int i = 0;
        do {
            this.setName(mNick + (i > 0 ? i : ""));
            try {
                connect(mServer);
            } catch (Exception ex) {
                i++;
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException ex1) {
                    return;
                }
            }
        } while (this.isConnected() == false);
        joinChannel(mChannel);
    }
    
    @Override
    protected void onMessage(String channel, String sender, String login, String hostname, String message) {
        handleSentence(sender, new Sentence(message.toLowerCase()));
    }

    
    public final void handleSentence(String sender, Sentence s) {
        String subject = null;
        String[] sList = s.getText().split("[\\.\\?\\!\\(\\)\\:]");
        List<Sentence> dummy = new ArrayList<Sentence>();
        boolean sent = false;
        for(String sl : sList) {
            
            if(sl.startsWith(mNick.toLowerCase())) {
                sl = sl.substring(mNick.length()+1).trim();
            } else { }
            Sentence newSentence = new Sentence(sl + ".");
            addSentence(newSentence);
            dummy.add(newSentence);
            if(!sent && (getInterestLevel(s) > getInterestFloor())) { System.out.println("WILL RESPOND");
                sent = true;
                if(currentDiscussion == null || !currentDiscussion.isAlive()) {         
                    currentDiscussion = new Discussion(getSubject(dummy), dummy, getDiscussionLength(), System.currentTimeMillis());
                }
                subject = getSubject(currentDiscussion.log);
                Reaction r = getReaction(getSentence(sender, subject));
                ReactionSender reactionSender = new ReactionSender(this, r);
            }
        }
        subject = getSubject(dummy);        
        addSubject(subject);
        currentDiscussion.log.addAll(dummy);
        System.out.println("Subject: " + subject);
    }
    
    //abstract Discussion getDiscussion();
    abstract int getDiscussionLength();
    abstract String getSubject(List<Sentence> sentences); 
    abstract void addSentence(Sentence s);     
    abstract void addSubject(String subject);
    abstract String createNewSentence(String to);
    abstract float getInterestLevel(Sentence s);
    abstract Reaction getReaction(Sentence s);
    abstract float getInterestFloor();
    abstract Sentence getSentence(String sender, String subject);
    abstract void sendMessage(String message);
    
}
