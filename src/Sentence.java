/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author patrik
 */
public class Sentence {
    private String txt;
    private Sentence() {}
    public Sentence(String txt) {
        this.txt = txt;
    }
    public String getText() {
        return txt;
    }
}
