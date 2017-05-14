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
    public int source_location;
    public int destination;
    public Move(){
        source_location = -1;
        destination = -1;
    }
    public Move(int source_location, int destination){
        this.source_location = source_location;
        this.destination = destination;
    }
}
