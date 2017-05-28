package _123Chess;

/**
 *
 * @author kaitlyn.yang
 */
public class Piece {
    public final static int PAWN = 100;
    public final static int KNIGHT = 300;
    public final static int BISHOP = 350;
    public final static int ROOK = 500;
    public final static int QUEEN = 900;
    public final static int KING = 1000;
    
    public int value;
    int location;
    boolean has_moved;
    
    public Piece(int value,int location){
        this(value,location,false);
    }
    public Piece(int value,int location,boolean hasMoved){
        this.value = value;
        this.location = location;
        this.has_moved = hasMoved;
    }
    @Override
    public Piece clone(){
        return new Piece(value,location,has_moved);
    }
}
