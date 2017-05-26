/**
 * Main class of the 123Chess app.
 */
package _123Chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

//import JFrame;
import _123Chess.Game;
import _123Chess.Constants;
import _123Chess.Move;
import _123Chess.Piece;
import _123Chess.BoardDB;
import _123Chess.Resource;
import _123Chess.Constants;

/**
 * @author kaitlyn.yang
 *
 */
public class OneTwoThreeChess extends javax.swing.JFrame implements MouseListener {

	 	BoardDB position;        
	    ChessBoardPane boardPane;  
	    Resource resource = new Resource();
	    Map<Integer,Image> images = new HashMap<Integer,Image>();
	    Map<Integer,Icon> iconImages = new HashMap<Integer,Icon>();
	    Move move = new Move();
	    boolean pieceSelected;
	    boolean isWhite;
	    int state;
	    int activePlayer = Constants.PLAYER1_MOVE;
	    Game game;    
	    JPanel mainPane = new JPanel(new BorderLayout());
	    boolean castling;
	    Color bgColor = Color.decode("#e3edd5");	
	 
	    
	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable(){
	            @Override
	            public void run(){
	                try{
	                    boolean nimbusFound = false;
	                        for(UIManager.LookAndFeelInfo info: UIManager.getInstalledLookAndFeels()){
	                            if(info.getName().equals("Nimbus")){
	                                UIManager.setLookAndFeel(info.getClassName());
	                                nimbusFound = true;
	                                break;
	                            }
	                        }
	                        if(!nimbusFound){
	                            int option = JOptionPane.showConfirmDialog(null,
	                                    "Nimbus Look And Feel not found\n"+
	                                    "Do you want to proceed?",
	                                    "Warning",JOptionPane.YES_NO_OPTION,
	                                    JOptionPane.WARNING_MESSAGE);
	                            if(option == JOptionPane.NO_OPTION){
	                                System.exit(0);
	                            }
	                        }
	                    OneTwoThreeChess app = new OneTwoThreeChess();
	                   // mcg.pack();
	                    app.setLocationRelativeTo(null);
	                    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	                    app.setResizable(false);
	                    app.setVisible(true); 
	                }catch(Exception e){
	                    JOptionPane.showMessageDialog(null, e.getStackTrace());
	                    e.printStackTrace();
	                }
	            }
	        });
	    }	
	    
	    public OneTwoThreeChess(){
	        //super("MyChessmate "+Constants.VERSION);                                  
	        setContentPane(mainPane);                
	        position = new BoardDB();
	        //promotion_pane = new PromotionPane(this);
	        
	        //loadMenuIcons();
	        loadBoardImages();
	        
	        boardPane = new ChessBoardPane();  
	        
	        //main_pane.add(createMenuPane(),BorderLayout.WEST);
	        mainPane.add(boardPane,BorderLayout.CENTER);  
	        mainPane.setBackground(bgColor);      
	        //createEastPane();
	        
	        pack();
	        Dimension size = getSize();
	        size.height = 523;
	        setSize(size);
	        
	        this.newGame();
	        addWindowListener(new WindowAdapter(){
	            public void windowClosing(WindowEvent e){
	                //quit();
	            }
	        });
	    }  
	    
	    public void loadBoardImages(){
	        try{ 
	            images.put(Constants.BOARD_IMAGE,ImageIO.read(new File(resource.getResourceString("chessboard"))));
	            images.put(Constants.SELECTED,ImageIO.read(new File(resource.getResourceString("Selected"))));
	            images.put(Constants.MOVED,ImageIO.read(new File(resource.getResourceString("Moved"))));
	        }catch(IOException ex){
	            ex.printStackTrace();
	        }        
	    }
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
    public class ChessBoardPane extends JPanel implements MouseListener{     
        Image animatingImage;
        int movingX,movingY,desX,desY,deltaX,deltaY;
        public ChessBoardPane(){
            setPreferredSize(new Dimension(450, 495));
            setBackground(bgColor);
            addMouseListener(this);
        }
        @Override
        public void paintComponent(Graphics g){
            if(position.board == null) return;
            super.paintComponent(g);  
            Image scaledImage = images.get(Constants.BOARD_IMAGE).getScaledInstance(boardPane.getWidth()-20,boardPane.getHeight()-60,Image.SCALE_SMOOTH);
            g.drawImage(scaledImage,8,55,this);    
            
            for (int i = 0; i < position.board.length-11; i++) {
                if (position.board[i] != Constants.ILLEGAL) {                                                                
	                int x = i % 10;
	                int y = (i - x) / 10;
	
	            	//Paint special cell
	                if (pieceSelected && i == move.from) {                
	                    g.drawImage(images.get(Constants.SELECTED), x * 45, y * 45,this);                    
	                }else if(!pieceSelected && move.to == i && 
	                        (position.board[i]==Constants.EMPTY || position.board[i]<0)){
	                    g.drawImage(images.get(Constants.MOVED), x * 45, y * 45, this);                                        
	                }
	
	                if (position.board[i] != Constants.EMPTY) {
		                if (position.board[i] > 0) {          
		                    int piece = position.player1_pieces[position.board[i]].value;
		                    g.drawImage(images.get(piece), x*45, y*45, this);
		                }else{
		                    int piece = position.player2_pieces[-position.board[i]].value;
		                    g.drawImage(images.get(-piece), x*45, y*45, this);
		                }   
	                }
                }
            } 
            if(state == Constants.ANIMATING){
                g.drawImage(animatingImage, movingX, movingY, this);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int location = boardValue(e.getY())*10+boardValue(e.getX());              
            if(position.board[location] == Constants.ILLEGAL) return;
            if(!pieceSelected && position.board[location] != Constants.EMPTY &&
            		((activePlayer == Constants.PLAYER1_MOVE && position.board[location]>0) 
            				|| (activePlayer == Constants.PLAYER2_MOVE && position.board[location]<0))) {
                    pieceSelected = true;
                    move.from = location;
            }else if(pieceSelected) {
                pieceSelected = false;
                move.to = location;     
                state = Constants.PREPARE_ANIMATION;
            }
            repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) { }

        @Override
        public void mouseReleased(MouseEvent e) { }

        @Override
        public void mouseEntered(MouseEvent e) { }

        @Override
        public void mouseExited(MouseEvent e) { }
    }
    
    public int boardValue(int value){
        return value/45;
    }
    
    
    public void loadPieceImages()
    {
      char[] resource_keys = { 'p', 'n', 'b', 'r', 'q', 'k' };
      int[] images_keys = { 100, 320, 325, 500, 900, 1000000 };
      try
      {
        for (int i = 0; i < resource_keys.length; i++)
        {
          this.images.put(Integer.valueOf(images_keys[i]), ImageIO.read(new File(this.resource.getResourceString((this.isWhite ? "w" : "b") + resource_keys[i]))));
          this.images.put(Integer.valueOf(-images_keys[i]), ImageIO.read(new File(this.resource.getResourceString((this.isWhite ? "b" : "w") + resource_keys[i]))));
        }
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
    }

    public void newGame()
    {
      this.isWhite = true;//this.play_options.white_button.isSelected();
      this.move.from = -1;
      this.move.to = -1;
      this.position = new BoardDB();
      this.position.initialize(this.isWhite);
      this.game = new Game(this.position);
      loadPieceImages();
      this.boardPane.repaint();
      if (this.isWhite) {
        this.state = Constants.PLAYER1_MOVE;
      } else {
        this.state = Constants.PLAYER2_MOVE;
      }
      this.castling = false;
      play();
    }
    
    public void play(){
        Thread t = new Thread(){
            public void run(){
                while(true){           
                    switch(state){
                        case Constants.PLAYER1_MOVE:    
                            break;
                        case Constants.PLAYER2_MOVE:             
                            break;
                        case Constants.PREPARE_ANIMATION:
                            prepareAnimation();
                            break;
                        case Constants.ANIMATING:
                            animate();
                            break;                        
                        case Constants.GAME_ENDED: return;
                    }
                    try{                        
                        Thread.sleep(3);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
    
    public void prepareAnimation(){
        int animating_image_key = 0;
        if(position.board[move.from]>0){
            animating_image_key = position.player1_pieces[position.board[move.from]].value;
        }else {
            animating_image_key = -position.player2_pieces[-position.board[move.from]].value;
        }        
        boardPane.animatingImage = images.get(animating_image_key);
        int x = move.from%10;        
        int y = (move.from-x)/10;
        boardPane.desX = move.to%10;
        boardPane.desY = (move.to-boardPane.desX)/10;
        int dX = boardPane.desX-x;
        int dY = boardPane.desY-y;           
        boardPane.movingX = x*45;
        boardPane.movingY = y*45;
        if(Math.abs(dX)>Math.abs(dY)){
            if(dY == 0){
                boardPane.deltaX = (dX>0)?1:-1;
                boardPane.deltaY = 0;
            }else{
                boardPane.deltaX = (dX>0)?Math.abs(dX/dY):-(Math.abs(dX/dY));
                boardPane.deltaY = (dY>0)?1:-1;
            }
        }else{
            if(dX == 0){
                boardPane.deltaY = (dY>0)?1:-1;
                boardPane.deltaX = 0;
            }else{
                boardPane.deltaX = (dX>0)?1:-1;
                boardPane.deltaY = (dY>0)?Math.abs(dY/dX):-(Math.abs(dY/dX));
            }
        }          
        state = Constants.ANIMATING;
    }
    public void animate(){
        if (boardPane.movingX == boardPane.desX * 45 && boardPane.movingY == boardPane.desY * 45) {                                           
            boardPane.repaint();            
            int source_square = position.board[move.from];            
//            if(source_square>0){                
//            }else {
//                if(move.to > 90 && move.to<98 
//                        && position.player2_pieces[-source_square].value == Piece.PAWN)
//                    //promoteComputerPawn();
//            }                        
            position.update(move); 
            togglePlayer();
            if(source_square>0){
                if(castling){   
                    //prepareCastlingAnimation();
                      state = Constants.PREPARE_ANIMATION;
                }else if(move.to > 20 && move.to < 29 && 
                        position.player1_pieces[source_square].value == Piece.PAWN){
                    //promoteHumanPawn();                    
                }
            }else{
//                if (gameEnded(Constants.PLAYER1)) {
//                    state = Constants.GAME_ENDED;
//                    return;
//                }
            }
//            if(!castling && state != Constants.PROMOTING) 
//                newHistoryPosition();
//            if(castling) castling = false;
        }
        boardPane.movingX += boardPane.deltaX;
        boardPane.movingY += boardPane.deltaY;
        boardPane.repaint();
        //togglePlayer();
    }

	private void togglePlayer() {
		if (activePlayer == Constants.PLAYER1_MOVE) {
			state = Constants.PLAYER2_MOVE;
			activePlayer = Constants.PLAYER2_MOVE;
		}
		else {
			state = Constants.PLAYER1_MOVE;
			activePlayer = Constants.PLAYER1_MOVE;
		}		
	}
    
    public boolean validMove(int destination){        
        int source = move.from;
        int destination_square = position.board[destination];
        if(destination_square == Constants.ILLEGAL) return false;
        //if(!game.safeMove(Constants.PLAYER1, source, to)) return false;
        boolean valid = false;
        int piece_value = position.player1_pieces[position.board[source]].value;                        
        switch(piece_value){
            case Piece.PAWN:
                if(destination == source-10 && destination_square == Constants.EMPTY) valid = true;
                if(destination == source-20 && position.board[source-10] == Constants.EMPTY &&
                        destination_square == Constants.EMPTY && source>80) valid = true;
                if(destination == source-9 && destination_square<0) valid = true;
                if(destination == source-11 && destination_square<0) valid = true;
                break;
            case Piece.KNIGHT:
            case Piece.KING:
                //if(piece_value == Piece.KING) valid = checkCastling(to);
                int[] destinations = null;
                if(piece_value == Piece.KNIGHT) destinations = new int[]{source-21,source+21,source+19,source-19,                    
                    source-12,source+12,source-8,source+8};
                else destinations = new int[]{source+1,source-1,source+10,source-10,
                    source+11,source-11,source+9,source-9};
                for(int i=0; i<destinations.length; i++){
                    if(destinations[i] == destination){
                        if(destination_square == Constants.EMPTY || destination_square<0){
                            valid = true;
                            break;
                        }
                    }
                }                
                break;
            case Piece.BISHOP:
            case Piece.ROOK:
            case Piece.QUEEN:
                int[] deltas = null;
                if(piece_value == Piece.BISHOP) deltas = new int[]{11,-11,9,-9};
                if(piece_value == Piece.ROOK) deltas = new int[]{1,-1,10,-10};
                if(piece_value == Piece.QUEEN) deltas = new int[]{1,-1,10,-10,11,-11,9,-9};
                for (int i = 0; i < deltas.length; i++) {
                    int des = source + deltas[i]; 
                    valid = true;
                    while (destination != des) { 
                        destination_square = position.board[des];  
                        if(destination_square != Constants.EMPTY){
                            valid = false;
                            break;
                        }                        
                        des += deltas[i];
                    }
                    if(valid) break;
                }
                break;
        }        
        return valid;
    }
    
}
