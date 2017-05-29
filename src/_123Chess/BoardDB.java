/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package _123Chess;

/**
 *
 * @author kaitlyn.yang
 */
public class BoardDB {
    Move last_move;
    public int[] board = new int[110];
    public Piece[] player1_pieces = new Piece[17];
    public Piece[] player2_pieces = new Piece[17];
    
    public BoardDB(){
        for(int i=0; i<board.length; i++){
            board[i] = Constants.EMPTY;
        }
    }
    public BoardDB(BoardDB position){
        this(position,null);
    }
    public BoardDB(BoardDB position, Move last_move){
        System.arraycopy(position.board, 0, this.board, 0, board.length);
        for(int i=1; i<player1_pieces.length; i++){
            if(position.player1_pieces[i] != null){
                this.player1_pieces[i] = position.player1_pieces[i].clone();
            }
            if(position.player2_pieces[i] != null){
                this.player2_pieces[i] = position.player2_pieces[i].clone();
            }
        }
        if(last_move != null) update(last_move);
    }  
    
    public void reset(boolean player1White){         
        player1_pieces[1] = new Piece(Piece.KNIGHT,82);
        player1_pieces[2] = new Piece(Piece.KNIGHT,87);
        player1_pieces[3] = new Piece(Piece.BISHOP,83);
        player1_pieces[4] = new Piece(Piece.BISHOP,86);
        player1_pieces[5] = new Piece(Piece.ROOK,81);
        player1_pieces[6] = new Piece(Piece.ROOK,88);
        player1_pieces[7] = new Piece(Piece.QUEEN,player1White?84:85);
        player1_pieces[8] = new Piece(Piece.KING,player1White?85:84);
        
        player2_pieces[1] = new Piece(Piece.KNIGHT,12);
        player2_pieces[2] = new Piece(Piece.KNIGHT,17);
        player2_pieces[3] = new Piece(Piece.BISHOP,13);
        player2_pieces[4] = new Piece(Piece.BISHOP,16);
        player2_pieces[5] = new Piece(Piece.ROOK,11);
        player2_pieces[6] = new Piece(Piece.ROOK,18);
        player2_pieces[7] = new Piece(Piece.QUEEN,player1White?14:15);
        player2_pieces[8] = new Piece(Piece.KING,player1White?15:14); 
        
        int j = 71;
        for(int i=9; i<player1_pieces.length; i++){
            player1_pieces[i] = new Piece(Piece.PAWN,j);
            player2_pieces[i] = new Piece(Piece.PAWN,j-50);
            j++;
        }                      
        board = new int[]{
            Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,
            Constants.ILLEGAL,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.ILLEGAL,
            Constants.ILLEGAL,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.ILLEGAL,
            Constants.ILLEGAL,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.ILLEGAL,
            Constants.ILLEGAL,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.ILLEGAL,
            Constants.ILLEGAL,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.ILLEGAL,
            Constants.ILLEGAL,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.ILLEGAL,
            Constants.ILLEGAL,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.ILLEGAL,
            Constants.ILLEGAL,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.EMPTY,Constants.ILLEGAL,
            Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,
            Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL
        };        
//        Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,Constants.ILLEGAL,
        for(int i=0; i<board.length; i++){                        
            for(int k=1; k<player1_pieces.length; k++){
                if(i==player1_pieces[k].location){
                    board[i] = k;
                }else if(i==player2_pieces[k].location){
                    board[i] = -k;
                }
            }
        }
    }    
    
    public void update(Move move){
        this.last_move = move;   
        int source_index = board[move.from];
        int destination_index = board[move.to];  
        if(source_index>0){
            player1_pieces[source_index].has_moved = true;
            player1_pieces[source_index].location = move.to;
            if(destination_index<0){                
                player2_pieces[-destination_index] = null;
            }            
        }else{
            player2_pieces[-source_index].has_moved = true;
            player2_pieces[-source_index].location = move.to;
            if(destination_index>0 && destination_index != Constants.EMPTY){                
                player1_pieces[destination_index] = null;
            }            
        }
        board[move.from] = Constants.EMPTY;
        board[move.to] = source_index;
    }
}
