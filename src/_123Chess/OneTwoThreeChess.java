/**
 * Main class of the 123Chess app.
 */
package _123Chess;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import _123Chess.Engine;
import _123Chess.Constants;
import _123Chess.Move;
import _123Chess.Piece;
import _123Chess.BoardDB;
import _123Chess.Resource;

/**
 * @author kaitlyn.yang
 *
 */
public class OneTwoThreeChess extends javax.swing.JFrame {

	 	BoardDB position;        
	    BoardPane boardPane;  
	    Resource resource = new Resource();
	    Map<Integer,Image> images = new HashMap<Integer,Image>();
	    Map<Integer,Icon> iconImages = new HashMap<Integer,Icon>();
	    Move move = new Move();
	    boolean pieceSelected;
	    boolean isWhite;
	    int state;
	    int activePlayer = Constants.PLAYER1;
	    Engine engine;    
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
	        super("123 Chess "+Constants.VERSION);                                  
	        setContentPane(mainPane);                
	        position = new BoardDB();
	        JMenuBar menuBar = new JMenuBar();
	        setJMenuBar(menuBar);
	        menuBar.add(createGameMenu());
	        
	        loadBoardImages();
	        
	        boardPane = new BoardPane();  
	        
	        mainPane.add(boardPane,BorderLayout.CENTER);  
	        mainPane.setBackground(bgColor);      
	        
	        pack();
	        Dimension size = getSize();
	        size.height = 505;
	        setSize(size);
	        
	        this.newGame();
	        addWindowListener(new WindowAdapter(){
	            public void windowClosing(WindowEvent e){
	                quit();
	            }
	        });
	    } 
	    
	    public JMenu createGameMenu() {
	    	JMenu menu = new JMenu("Game");
	    	menu.add(createNewGameItem());
	    	menu.add(createQuitGameItem());
	    	return menu;
	    }
	    
	    private JMenuItem createNewGameItem() {
			JMenuItem item = new JMenuItem("New Game");
			class MenuItemListener implements ActionListener
			{

				@Override
				public void actionPerformed(ActionEvent e) {
					newGame();					
				}				
			}
			ActionListener listener = new MenuItemListener();
			item.addActionListener(listener);
			return item;
		}

		private JMenuItem createQuitGameItem() {
			JMenuItem item = new JMenuItem("Quit");
			class MenuItemListener implements ActionListener
			{

				@Override
				public void actionPerformed(ActionEvent e) {
					quit();					
				}				
			}
			ActionListener listener = new MenuItemListener();
			item.addActionListener(listener);
			return item;
		}
		
		public void quit(){
	        int option = JOptionPane.showConfirmDialog(null,"Are you sure you want to quit?", 
	                    "123 Chess", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
	        if(option == JOptionPane.YES_OPTION)
	            System.exit(0);
	        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	    }
		
		public void loadBoardImages(){
	        try{ 
	            images.put(Constants.BOARDIMAGE,ImageIO.read(new File(resource.getResourceString("chessboard"))));
	            images.put(Constants.SELECTED,ImageIO.read(new File(resource.getResourceString("Selected"))));
	            images.put(Constants.MOVED,ImageIO.read(new File(resource.getResourceString("Moved"))));
	        }catch(IOException ex){
	            ex.printStackTrace();
	        }        
	    }
	

    public class BoardPane extends JPanel implements MouseListener{     
        Image movingImage;
        int movingX,movingY,desX,desY,deltaX,deltaY;
        public BoardPane(){
            setPreferredSize(new Dimension(450, 495));
            setBackground(bgColor);
            addMouseListener(this);
        }
        @Override
        public void paintComponent(Graphics g){
            if(position.board == null) return;
            super.paintComponent(g);  
            Image scaledImage = images.get(Constants.BOARDIMAGE).getScaledInstance(442,448,Image.SCALE_SMOOTH);
            g.drawImage(scaledImage,8,1,this);    
            
            for (int i = 0; i < position.board.length-11; i++) {
                if (position.board[i] != Constants.ILLEGAL) {                                                                
	                int x = i % 10;
	                int y = (i - x) / 10;
	
	            	//Paint special cell
	                if (pieceSelected && i == move.from) {                
	                    g.drawImage(images.get(Constants.SELECTED), x * 45, y * 45,this);                    
	                }else if(!pieceSelected && move.to == i && 
	                        (position.board[i]==Constants.EMPTY || 
	                        ((activePlayer == Constants.PLAYER1 && position.board[i]<0) || 
	                        		(activePlayer == Constants.PLAYER2 && position.board[i]>0)))){
	                    g.drawImage(images.get(Constants.MOVED), x * 45, y * 45, this);                                        
	                }
	
	                if (position.board[i] != Constants.EMPTY) {
		                if (position.board[i] > 0) {          
		                    int piece = position.p1Pieces[position.board[i]].value;
		                    g.drawImage(images.get(piece), x*45, y*45, this);
		                }else{
		                    int piece = position.p2Pieces[-position.board[i]].value;
		                    g.drawImage(images.get(-piece), x*45, y*45, this);
		                }   
	                }
                }
            } 
            if(state == Constants.MOVING){
                g.drawImage(movingImage, this.movingX, this.movingY, this);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int location = boardValue(e.getY())*10+boardValue(e.getX());              
            if(position.board[location] == Constants.ILLEGAL) return;
            if(!pieceSelected && position.board[location] != Constants.EMPTY &&
            		((activePlayer == Constants.PLAYER1 && position.board[location]>0) 
            				|| (activePlayer == Constants.PLAYER2 && position.board[location]<0))) {
                    pieceSelected = true;
                    move.from = location;
            }
            else if( pieceSelected && position.board[location] != Constants.EMPTY &&
            		((activePlayer == Constants.PLAYER1 && position.board[location]>0) 
            				|| (activePlayer == Constants.PLAYER2 && position.board[location]<0))) {
                    move.from = location;
            }
            else if(pieceSelected && ((activePlayer == Constants.PLAYER1 && validPlayer1Move(location))
            		|| (activePlayer == Constants.PLAYER2 && validPlayer2Move(location)))) {
                pieceSelected = false;
                move.to = location;     
                state = Constants.PREPAREMOVE;
            }
            else
            	return;
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
      int[] images_keys = { Piece.PAWN, Piece.KNIGHT, Piece.BISHOP, Piece.ROOK, Piece.QUEEN, Piece.KING };
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
      this.isWhite = true;
      this.move.from = -1;
      this.move.to = -1;
      this.position = new BoardDB();
      this.position.reset(this.isWhite);
      this.engine = new Engine(this.position);
      loadPieceImages();
      this.boardPane.repaint();
      if (this.isWhite) {
        this.state = Constants.P1MOVE;
      } else {
        this.state = Constants.P2MOVE;
      }
      this.castling = false;
      play();
    }
    
    public void play(){
        Thread t = new Thread(){
            public void run(){
                while(true){           
                    switch(state){
                        case Constants.P1MOVE:    
                            break;
                        case Constants.P2MOVE:             
                            break;
                        case Constants.PREPAREMOVE:
                            prepareMove();
                            break;
                        case Constants.MOVING:
                            moveOneStep();
                            break;                        
                        case Constants.GAMEENDED: return;
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
    
    public void prepareMove(){
        int animating_image_key = 0;
        if(position.board[move.from]>0){
            animating_image_key = position.p1Pieces[position.board[move.from]].value;
        }else {
            animating_image_key = -position.p2Pieces[-position.board[move.from]].value;
        }        
        boardPane.movingImage = images.get(animating_image_key);
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
        state = Constants.MOVING;
    }
    public void moveOneStep(){
        if (boardPane.movingX == boardPane.desX * 45 && boardPane.movingY == boardPane.desY * 45) {                                           
            boardPane.repaint();            
            int source_square = position.board[move.from];            
            position.update(move); 
            if (!castling) togglePlayer();
            
            if(source_square>0){
                if(castling){   
                    prepareCastlingMove();
                    state = Constants.PREPAREMOVE;
                }
                else if(move.to > 20 && move.to < 29 && 
                        position.p1Pieces[source_square].value == Piece.PAWN){
                }
            }
            
            if(castling) castling = false;
        }
        boardPane.movingX += boardPane.deltaX;
        boardPane.movingY += boardPane.deltaY;
        boardPane.repaint();
    }

    public void prepareCastlingMove(){
        if(move.to == 87){
            move.from = 88;
            move.to = 86;
        }
        else if(move.to == 83){
            move.from = 81;
            move.to = 84;
        }
        else if(move.to == 17){
            move.from = 18;
            move.to = 16;
        }
        else if(move.to == 13){
            move.from = 11;
            move.to = 14;
        }
    }

	private void togglePlayer() {
		if (activePlayer == Constants.PLAYER1) {
			state = Constants.P2MOVE;
			activePlayer = Constants.PLAYER2;
		}
		else {
			state = Constants.P1MOVE;
			activePlayer = Constants.PLAYER1;
		}		
	}
    
    public boolean validPlayer1Move(int destination){        
        int source = move.from;
        int dSquare = position.board[destination];
        if(dSquare == Constants.ILLEGAL) return false;
        if(!engine.safeMove(Constants.PLAYER1, source, destination)) return false;
        boolean valid = false;
        int pValue = position.p1Pieces[position.board[source]].value;                        
        switch(pValue){
            case Piece.PAWN:
                if(destination == source-10 && dSquare == Constants.EMPTY) valid = true;
                if(destination == source-20 && position.board[source-10] == Constants.EMPTY &&
                        dSquare == Constants.EMPTY && source>70) valid = true;
                if(destination == source-9 && dSquare<0) valid = true;
                if(destination == source-11 && dSquare<0) valid = true;
                break;
            case Piece.KNIGHT:
            case Piece.KING:
            	if(pValue == Piece.KING) valid = isPlayer1Castling(destination);
                int[] destinations = null;
                if(pValue == Piece.KNIGHT) destinations = new int[]{source-21,source+21,source+19,source-19,                    
                    source-12,source+12,source-8,source+8};
                else destinations = new int[]{source+1,source-1,source+10,source-10,
                    source+11,source-11,source+9,source-9};
                for(int i=0; i<destinations.length; i++){
                    if(destinations[i] == destination){
                        if(dSquare == Constants.EMPTY || dSquare<0){
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
                if(pValue == Piece.BISHOP) deltas = new int[]{11,-11,9,-9};
                if(pValue == Piece.ROOK) deltas = new int[]{1,-1,10,-10};
                if(pValue == Piece.QUEEN) deltas = new int[]{1,-1,10,-10,11,-11,9,-9};
                for (int i = 0; i < deltas.length; i++) {
                    int des = source + deltas[i]; 
                    valid = true;
                    while (destination != des) { 
                        dSquare = position.board[des];  
                        if(dSquare != Constants.EMPTY){
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
    
    public boolean validPlayer2Move(int destination){        
        int source = move.from;
        int dSquare = position.board[destination];
        if(dSquare == Constants.ILLEGAL) return false;
        if(!engine.safeMove(Constants.PLAYER2, source, destination)) return false;
        boolean valid = false;
        int pValue = position.p2Pieces[-position.board[source]].value;                        
        switch(pValue){
            case Piece.PAWN:
                if(destination == source+10 && dSquare == Constants.EMPTY) valid = true;
                if(destination == source+20 && position.board[source+10] == Constants.EMPTY &&
                        dSquare == Constants.EMPTY && source<30) valid = true;
                if(destination == source+9 && dSquare>0) valid = true;
                if(destination == source+11 && dSquare>0) valid = true;
                break;
            case Piece.KNIGHT:
            case Piece.KING:
                if(pValue == Piece.KING) valid = isPlayer2Castling(destination);
                int[] destinations = null;
                if(pValue == Piece.KNIGHT) destinations = new int[]{source-21,source+21,source+19,source-19,                    
                    source-12,source+12,source-8,source+8};
                else destinations = new int[]{source+1,source-1,source+10,source-10,
                    source+11,source-11,source+9,source-9};
                for(int i=0; i<destinations.length; i++){
                    if(destinations[i] == destination){
                        if(dSquare == Constants.EMPTY || dSquare>0){
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
                if(pValue == Piece.BISHOP) deltas = new int[]{11,-11,9,-9};
                if(pValue == Piece.ROOK) deltas = new int[]{1,-1,10,-10};
                if(pValue == Piece.QUEEN) deltas = new int[]{1,-1,10,-10,11,-11,9,-9};
                for (int i = 0; i < deltas.length; i++) {
                    int des = source + deltas[i]; 
                    valid = true;
                    while (destination != des) { 
                        dSquare = position.board[des];  
                        if(dSquare != Constants.EMPTY){
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
    
    public boolean isPlayer1Castling(int destination){        
        Piece king = position.p1Pieces[8];
        Piece rightRook = position.p1Pieces[6];
        Piece leftRook = position.p1Pieces[5];
        
        if(king.hasMoved) return false;              
        int source = move.from;
        
        if(rightRook == null && leftRook == null) return false;
        if(rightRook != null && rightRook.hasMoved && 
                leftRook != null && leftRook.hasMoved) return false;
            
        if(source != 85) return false;            
        if(destination != 87 && destination != 83) return false;
        if(destination == 87){
            if(position.board[86] != Constants.EMPTY) return false;
            if(position.board[87] != Constants.EMPTY) return false;
            if(!engine.safeMove(Constants.PLAYER1,source,86)) return false;
            if(!engine.safeMove(Constants.PLAYER1,source,87)) return false;
        }
        else if(destination == 83){
            if(position.board[84] != Constants.EMPTY) return false;
            if(position.board[83] != Constants.EMPTY) return false;
            if(!engine.safeMove(Constants.PLAYER1,source,84)) return false;
            if(!engine.safeMove(Constants.PLAYER1,source,83)) return false;
        }
        return castling=true;
    }

    public boolean isPlayer2Castling(int destination){        
        Piece king = position.p2Pieces[8];
        Piece rightRook = position.p2Pieces[6];
        Piece leftRook = position.p2Pieces[5];
        
        if(king.hasMoved) return false;              
        int source = move.from;
        
        if(rightRook == null && leftRook == null) return false;
        if(rightRook != null && rightRook.hasMoved && 
                leftRook != null && leftRook.hasMoved) return false;
            
        if(source != 15) return false;            
        if(destination != 17 && destination != 13) return false;
        if(destination == 17){
            if(position.board[16] != Constants.EMPTY) return false;
            if(position.board[17] != Constants.EMPTY) return false;
            if(!engine.safeMove(Constants.PLAYER2,source,16)) return false;
            if(!engine.safeMove(Constants.PLAYER2,source,17)) return false;
        }
        else if(destination == 13){
            if(position.board[14] != Constants.EMPTY) return false;
            if(position.board[13] != Constants.EMPTY) return false;
            if(!engine.safeMove(Constants.PLAYER2,source,14)) return false;
            if(!engine.safeMove(Constants.PLAYER2,source,13)) return false;
        }
        return castling=true;
    }
    
}
