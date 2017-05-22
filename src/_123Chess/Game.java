/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package _123Chess;

/**
 *
 * @author kaitlyn.yang
 */
public class Game {          
    BoardDB position;
    Piece player1_king;
    Piece player2_king;
    
    public Game(BoardDB position){
        player1_king = position.player1_pieces[8];
        player2_king = position.player2_pieces[8];
        this.position = position;
    }
}
