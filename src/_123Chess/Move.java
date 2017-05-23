/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package _123Chess;

/**
 *
 * @author kaitlyn.yang
 */
public class Move {
    public int from;
    public int to;
    
    public Move(){
        from = -1;
        to = -1;
    }
    public Move(int f, int t){
        this.from = f;
        this.to = t;
    }
}
