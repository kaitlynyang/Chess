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
import _123Chess.GameData;
import _123Chess.Move;
//import _123Chess.MoveSearcher;
import _123Chess.Piece;
import _123Chess.Position;
import _123Chess.Resource;

/**
 * @author kaitlyn.yang
 *
 */
public class OneTwoThreeChess extends javax.swing.JFrame implements MouseListener {

	 	Position position;        
	    ChessBoardPane board_pane;  
	    Resource resource = new Resource();
	    Map<Integer,Image> images = new HashMap<Integer,Image>();
	    Map<Integer,Icon> icon_images = new HashMap<Integer,Icon>();
	    Move move = new Move();
	    boolean piece_selected;
	    boolean is_white;
	    int state;
	    Game game;    
	    JLabel new_game,quit,about,history,first,prev,next,last;    
	    JPanel main_pane = new JPanel(new BorderLayout());
	    boolean castling;
	    List<Position> history_positions = new ArrayList<Position>();
	    int history_count;
	    Color bg_color = Color.decode("#e3edd5");	
	 
	    
	    /**
	     * @param args the command line arguments
	     */
	    public static void main(String[] args) {
	        // TODO code application logic here
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
	        //super("MyChessmate "+GameData.VERSION);                                  
	        setContentPane(main_pane);                
	        position = new Position();
	        //promotion_pane = new PromotionPane(this);
	        
	        //loadMenuIcons();
	        loadBoardImages();
	        
	        board_pane = new ChessBoardPane();  
	        
	        //main_pane.add(createMenuPane(),BorderLayout.WEST);
	        main_pane.add(board_pane,BorderLayout.CENTER);  
	        main_pane.setBackground(bg_color);      
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
	            images.put(GameData.BOARD_IMAGE,ImageIO.read(new File(resource.getResourceString("chessboard"))));
	        }catch(IOException ex){
	            ex.printStackTrace();
	        }        
	    }
	 /* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
    public class ChessBoardPane extends JPanel implements MouseListener{     
        Image animating_image;
        int movingX,movingY,desX,desY,deltaX,deltaY;
        public ChessBoardPane(){
            setPreferredSize(new Dimension(450, 495));
            setBackground(bg_color);
            addMouseListener(this);
        }
        @Override
        public void paintComponent(Graphics g){
            if(position.board == null) return;
            super.paintComponent(g);  
            Image scaledImage = images.get(GameData.BOARD_IMAGE).getScaledInstance(board_pane.getWidth()-20,board_pane.getHeight()-65,Image.SCALE_SMOOTH);
            g.drawImage(scaledImage,10,55,this);       
            for (int i = 0; i < position.board.length-11; i++) {
                if (position.board[i] == GameData.ILLEGAL) continue;                                                                
                int x = i % 10;
                int y = (i - x) / 10;
                
                if (position.board[i] == GameData.EMPTY) continue;
                
                if (position.board[i] > 0) {          
                    int piece = position.player1_pieces[position.board[i]].value;
                    g.drawImage(images.get(piece), x*45, y*45, this);
                }else{
                    int piece = position.player2_pieces[-position.board[i]].value;
                    g.drawImage(images.get(-piece), x*45, y*45, this);
                }               
            } 
            if(state == GameData.ANIMATING){
                g.drawImage(animating_image, movingX, movingY, this);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            //if(state != GameData.PLAYER1_MOVE) return;
            int location = boardValue(e.getY())*10+boardValue(e.getX());              
            if(position.board[location] == GameData.ILLEGAL) return;
            if((!piece_selected || position.board[location]>0) && position.board[location] != GameData.EMPTY){
//                if(position.board[location]>0){
                    piece_selected = true;
                    move.source_location = location;
//                }
            }else if(piece_selected){
                piece_selected = false;
                move.destination = location;     
                state = GameData.PREPARE_ANIMATION;
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
          this.images.put(Integer.valueOf(images_keys[i]), ImageIO.read(new File(this.resource.getResourceString((this.is_white ? "w" : "b") + resource_keys[i]))));
          this.images.put(Integer.valueOf(-images_keys[i]), ImageIO.read(new File(this.resource.getResourceString((this.is_white ? "b" : "w") + resource_keys[i]))));
          this.images.put(Integer.valueOf(images_keys[i] + 10), ImageIO.read(new File(this.resource.getResourceString((this.is_white ? "w" : "b") + resource_keys[i] + '2'))));
          this.images.put(Integer.valueOf(-images_keys[i] + 10), ImageIO.read(new File(this.resource.getResourceString((this.is_white ? "b" : "w") + resource_keys[i] + '2'))));
        }
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
    }

    public void newGame()
    {
      this.is_white = true;//this.play_options.white_button.isSelected();
      this.move.source_location = -1;
      this.move.destination = -1;
      this.position = new Position();
      this.position.initialize(this.is_white);
      this.game = new Game(this.position);
      loadPieceImages();
      this.board_pane.repaint();
      if (this.is_white) {
        this.state = GameData.PLAYER1_MOVE;
      } else {
        this.state = GameData.PLAYER2_MOVE;
      }
      this.castling = false;
      play();
    }
    
    public void play(){
        Thread t = new Thread(){
            public void run(){
                while(true){           
                    switch(state){
                        case GameData.PLAYER1_MOVE:    
                            break;
                        case GameData.PLAYER2_MOVE:             
                            break;
                        case GameData.PREPARE_ANIMATION:
                            prepareAnimation();
                            break;
                        case GameData.ANIMATING:
                            animate();
                            break;                        
                        case GameData.GAME_ENDED: return;
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
        if(position.board[move.source_location]>0){
            animating_image_key = position.player1_pieces[position.board[move.source_location]].value;
        }else {
            animating_image_key = -position.player2_pieces[-position.board[move.source_location]].value;
        }        
        board_pane.animating_image = images.get(animating_image_key);
        int x = move.source_location%10;        
        int y = (move.source_location-x)/10;
        board_pane.desX = move.destination%10;
        board_pane.desY = (move.destination-board_pane.desX)/10;
        int dX = board_pane.desX-x;
        int dY = board_pane.desY-y;           
        board_pane.movingX = x*45;
        board_pane.movingY = y*45;
        if(Math.abs(dX)>Math.abs(dY)){
            if(dY == 0){
                board_pane.deltaX = (dX>0)?1:-1;
                board_pane.deltaY = 0;
            }else{
                board_pane.deltaX = (dX>0)?Math.abs(dX/dY):-(Math.abs(dX/dY));
                board_pane.deltaY = (dY>0)?1:-1;
            }
        }else{
            if(dX == 0){
                board_pane.deltaY = (dY>0)?1:-1;
                board_pane.deltaX = 0;
            }else{
                board_pane.deltaX = (dX>0)?1:-1;
                board_pane.deltaY = (dY>0)?Math.abs(dY/dX):-(Math.abs(dY/dX));
            }
        }          
        state = GameData.ANIMATING;
    }
    public void animate(){
        if (board_pane.movingX == board_pane.desX * 45 && board_pane.movingY == board_pane.desY * 45) {                                           
            board_pane.repaint();            
            int source_square = position.board[move.source_location];            
            if(source_square>0){                
                state = GameData.PLAYER2_MOVE;                                               
            }else {
                if(move.destination > 90 && move.destination<98 
                        && position.player2_pieces[-source_square].value == Piece.PAWN)
                    //promoteComputerPawn();
                state = GameData.PLAYER1_MOVE;
            }                        
            position.update(move);       
            if(source_square>0){
                if(castling){   
                    //prepareCastlingAnimation();
                      state = GameData.PREPARE_ANIMATION;
                }else if(move.destination > 20 && move.destination < 29 && 
                        position.player1_pieces[source_square].value == Piece.PAWN){
                    //promoteHumanPawn();                    
                }
            }else{
//                if (gameEnded(GameData.PLAYER1)) {
//                    state = GameData.GAME_ENDED;
//                    return;
//                }
            }
//            if(!castling && state != GameData.PROMOTING) 
//                newHistoryPosition();
//            if(castling) castling = false;
        }
        board_pane.movingX += board_pane.deltaX;
        board_pane.movingY += board_pane.deltaY;
        board_pane.repaint();
    }
}
