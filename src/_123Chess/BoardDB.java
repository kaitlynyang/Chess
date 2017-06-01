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
    Move lastMove;
    public int[] board = new int[110];
    public Piece[] p1Pieces = new Piece[17];
    public Piece[] p2Pieces = new Piece[17];
    
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
        for(int i=1; i<p1Pieces.length; i++){
            if(position.p1Pieces[i] != null){
                this.p1Pieces[i] = position.p1Pieces[i].clone();
            }
            if(position.p2Pieces[i] != null){
                this.p2Pieces[i] = position.p2Pieces[i].clone();
            }
        }
        if(last_move != null) update(last_move);
    }  
    
    public void reset(boolean player1White){         
        p1Pieces[1] = new Piece(Piece.KNIGHT,82);
        p1Pieces[2] = new Piece(Piece.KNIGHT,87);
        p1Pieces[3] = new Piece(Piece.BISHOP,83);
        p1Pieces[4] = new Piece(Piece.BISHOP,86);
        p1Pieces[5] = new Piece(Piece.ROOK,81);
        p1Pieces[6] = new Piece(Piece.ROOK,88);
        p1Pieces[7] = new Piece(Piece.QUEEN,player1White?84:85);
        p1Pieces[8] = new Piece(Piece.KING,player1White?85:84);
        
        p2Pieces[1] = new Piece(Piece.KNIGHT,12);
        p2Pieces[2] = new Piece(Piece.KNIGHT,17);
        p2Pieces[3] = new Piece(Piece.BISHOP,13);
        p2Pieces[4] = new Piece(Piece.BISHOP,16);
        p2Pieces[5] = new Piece(Piece.ROOK,11);
        p2Pieces[6] = new Piece(Piece.ROOK,18);
        p2Pieces[7] = new Piece(Piece.QUEEN,player1White?14:15);
        p2Pieces[8] = new Piece(Piece.KING,player1White?15:14); 
        
        int j = 71;
        for(int i=9; i<p1Pieces.length; i++){
            p1Pieces[i] = new Piece(Piece.PAWN,j);
            p2Pieces[i] = new Piece(Piece.PAWN,j-50);
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
            for(int k=1; k<p1Pieces.length; k++){
                if(i==p1Pieces[k].location){
                    board[i] = k;
                }else if(i==p2Pieces[k].location){
                    board[i] = -k;
                }
            }
        }
    }    
    
    public void update(Move move){
        this.lastMove = move;   
        int source_index = board[move.from];
        int destination_index = board[move.to];  
        if(source_index>0){
            p1Pieces[source_index].hasMoved = true;
            p1Pieces[source_index].location = move.to;
            if(destination_index<0){                
                p2Pieces[-destination_index] = null;
            }            
        }else{
            p2Pieces[-source_index].hasMoved = true;
            p2Pieces[-source_index].location = move.to;
            if(destination_index>0 && destination_index != Constants.EMPTY){                
                p1Pieces[destination_index] = null;
            }            
        }
        board[move.from] = Constants.EMPTY;
        board[move.to] = source_index;
    }
}
