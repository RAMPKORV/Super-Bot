
import com.knowledgebooks.nlp.fasttag.FastTag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import org.jibble.pircbot.PircBot;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author patrik
 */
public class AinorBot extends Bot {
    
    private Random mRandom = new Random();
    private JMegaHal mHal = new JMegaHal();
    
    @Override
    int getDiscussionLength() {
        return 300;
    }

    @Override
    String getSubject(List<Sentence> sentences) {
        if(sentences.size() == 1) {
            return getSubject(sentences.get(0));
        } else {
            Map<String,Integer> subjectCounter = new HashMap<String,Integer>();
            for(Sentence s : sentences) {
                String subject = getSubject(s);
                if(!subjectCounter.containsKey(subject)) {
                    subjectCounter.put(subject, 1);
                } else {
                    subjectCounter.put(subject, subjectCounter.get(subject));
                }
            }
            String subjectMax = null;
            int numMax = 0;
            Set<String> subjects = subjectCounter.keySet();
            for(String subject : subjects) {
                if(subjectCounter.get(subject) > numMax) {
                    numMax = subjectCounter.get(subject);
                    subjectMax = subject;
                }
            }
            return subjectMax;
        }   
    }
    
    String getSubject(Sentence s) {
        String message = s.getText().toLowerCase();
        List<String> words = com.knowledgebooks.nlp.util.Tokenizer.wordsToList(message);
        List<String> tags = (new FastTag()).tag(words);
        List<String> goodwords = new ArrayList<String>();

        for (int i = 0; i < words.size(); i++) {
            if (tags.get(i).contains("NN") && !words.get(i).equals(mNick)) {
                goodwords.add(words.get(i));
            }
        }
        String w = null;
        if (goodwords.size() > 0) {
            for (int i = 0; i < 10; i++) {
                w = goodwords.get(mRandom.nextInt(goodwords.size()));
                if (w.length() >= 4) {
                    break;
                }
            }
        } else {
            for (int i = 0; i < 10; i++) {
                w = words.get(mRandom.nextInt(words.size()));
                if (w.length() >= 4) {
                    break;
                }
            }
        }
        return w;
    }

    @Override
    void addSentence(Sentence s) {
        mHal.add(s.getText());
    }

    @Override
    void addSubject(String subject) {
        // Do nothing yet
    }

    @Override
    String createNewSentence(String to) {
        String ret = "";
        if(mRandom.nextInt(100) > 50) {
            ret += to + ": ";
        }
        return ret + mHal.getSentence();
    }

    @Override
    float getInterestLevel(Sentence s) {
        float score = 0;
        String txt = s.getText().toLowerCase();
        score += txt.length()/2;
        if(txt.contains(mNick.toLowerCase())) {
            score += 30;
            if(txt.startsWith(mNick.toLowerCase())) {
                score += 35;
            }
        }
        score = score / 100f;
        if(score > 1) {
            score = 1;
        }     
        System.out.println("Score was " + score);
        return score;
    }

    @Override
    Reaction getReaction(Sentence s) {
        String txt = s.getText();
        return new Reaction(txt.length()/6, txt);
    }

    @Override
    float getInterestFloor() {
        return 0.64f;
    }

    @Override
    Sentence getSentence(String sender, String subject) {
        String ret = "";
        if(mRandom.nextInt(100) > 50) {
            ret += sender + ": ";
        }
        Sentence retSen = new Sentence(ret + mHal.getSentence(subject));
        return retSen;
    }

    @Override
    void sendMessage(String message) {
        this.sendMessage(mChannel, message);
    }
    
}
