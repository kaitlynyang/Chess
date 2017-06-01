/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package _123Chess;

/**
 *
 * @author kaitlyn.yang
 */
public class Engine {          
    BoardDB position;
    Piece p1King;
    Piece p2King;
    
    public Engine(BoardDB position){
        p1King = position.p1Pieces[8];
        p2King = position.p2Pieces[8];
        this.position = position;
    }
    
    public boolean safeMove(int player, int source,int destination){
        Move _move = new Move(source,destination);
        BoardDB _position = new BoardDB(position,_move);  
        Engine gs = new Engine(_position);   
        return !gs.isChecked(player);
    }
    
    public boolean isChecked(int player){
        Piece king = (player == Constants.PLAYER1)?p1King:p2King;
        if(king == null) return false;
        if(checkedByPawn(king)) return true;
        if(checkedByKnight(king)) return true;
        if(checkedByBishop(king)) return true;
        if(checkedByRook(king)) return true;
        if(checkedByQueen(king)) return true;
        if(desSquareAttackedByKing(king)) return true;       
        return false;
    }
    
    private boolean checkedByPawn(Piece king){
        boolean checked = false;   
        int location = king.location;
        if(king == p1King){
            int right_square = position.board[location-9];
            int left_square = position.board[location-11];
            if(right_square == Constants.ILLEGAL || left_square == Constants.ILLEGAL) return false;
            if(right_square<0 && position.p2Pieces[-right_square].value == Piece.PAWN)
                checked = true;
            if(left_square<0 && position.p2Pieces[-left_square].value == Piece.PAWN)
                checked = true;
        }else{
            int right_square = position.board[location+11];
            int left_square = position.board[location+9];
            if(right_square != Constants.ILLEGAL){
                if(right_square>0 && right_square != Constants.EMPTY && 
                        position.p1Pieces[right_square].value == Piece.PAWN)
                    checked = true;
            }
            if(left_square != Constants.ILLEGAL){
                if(left_square>0 && left_square != Constants.EMPTY && 
                        position.p1Pieces[left_square].value == Piece.PAWN)
                    checked = true;
            }
        }
        return checked;
    }
    private boolean checkedByKnight(Piece king){
        boolean checked = false;
        int location = king.location;
        int[] destinations = {location-21,location+21,location+19,location-19,
            location-12,location+12,location-8,location+8};
        for(int destination:destinations){
        	if (destination > 0 && destination < 99) {
	            int des_square = position.board[destination];
	            if(des_square != Constants.ILLEGAL) {
		            if(king == p1King){                
		                if(des_square<0 && position.p2Pieces[-des_square].value == Piece.KNIGHT){
		                    checked = true;
		                    break;
		                }
		            }else{
		                if(des_square>0 && des_square != Constants.EMPTY && 
		                        position.p1Pieces[des_square].value == Piece.KNIGHT){
		                    checked = true;
		                    break;
		                }
		            }
	            }
        	}
        }
        return checked;
    }
    private boolean desSquareAttackedByKing(Piece king){
        boolean checked = false;
        int location = king.location;
        int[] destinations = {location+1,location-1,location+10,location-10,
            location+11,location-11,location+9,location-9};
        for(int destination:destinations){
            int des_square = position.board[destination];
            if(des_square == Constants.ILLEGAL) continue;
            if(king == p1King){                
                if(des_square<0 && position.p2Pieces[-des_square].value == Piece.KING){
                    checked = true;
                    break;
                }
            }else{
                if(des_square>0 && des_square != Constants.EMPTY && 
                        position.p1Pieces[des_square].value == Piece.KING){
                    checked = true;
                    break;
                }
            }
        }
        return checked;
    }
    private boolean checkedByBishop(Piece king){
        boolean checked = false;
        int[] deltas = {11,-11,9,-9};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            while(true){
                int des_square = position.board[delta];
                if(des_square == Constants.ILLEGAL) {
                    checked = false;
                    break;
                }
                if(king == p1King){
                    if(des_square<0 && position.p2Pieces[-des_square].value == Piece.BISHOP){
                        checked = true;
                        break;
                    }else if(des_square != Constants.EMPTY) break;
                }else if(king == p2King){
                    if(des_square>0 && des_square != Constants.EMPTY && 
                            position.p1Pieces[des_square].value == Piece.BISHOP){
                        checked = true;
                        break;
                    }else if(des_square != Constants.EMPTY) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }    
    private boolean checkedByRook(Piece king){
        boolean checked = false;
        int[] deltas = {1,-1,10,-10};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            while(true){
                int des_square = position.board[delta];
                if(des_square == Constants.ILLEGAL) {
                    checked = false;
                    break;
                }
                if(king == p1King){
                    if(des_square<0 && position.p2Pieces[-des_square].value == Piece.ROOK){
                        checked = true;
                        break;
                    }else if(des_square != Constants.EMPTY) break;
                }else if(king == p2King){
                    if(des_square>0 && des_square != Constants.EMPTY && 
                            position.p1Pieces[des_square].value == Piece.ROOK){
                        checked = true;
                        break;
                    }else if(des_square != Constants.EMPTY) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }    
    private boolean checkedByQueen(Piece king){
        boolean checked = false;
        int[] deltas = {1,-1,10,-10,11,-11,9,-9};
        for(int i=0; i<deltas.length; i++){
            int delta = king.location+deltas[i];
            while(true){
                int des_square = position.board[delta];
                if(des_square == Constants.ILLEGAL) {
                    checked = false;
                    break;
                }
                if(king == p1King){
                    if(des_square<0 && position.p2Pieces[-des_square].value == Piece.QUEEN){
                        checked = true;
                        break;
                    }else if(des_square != Constants.EMPTY) break;
                }else if(king == p2King){
                    if(des_square>0 && des_square != Constants.EMPTY && 
                            position.p1Pieces[des_square].value == Piece.QUEEN){
                        checked = true;
                        break;
                    }else if(des_square != Constants.EMPTY) break;
                }
                delta += deltas[i];
            }
            if(checked) break;
        }
        return checked;
    }
}
