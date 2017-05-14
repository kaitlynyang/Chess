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
	    //HistoryBoardPane history_pane;
	    JPanel east_pane;
	    Resource resource = new Resource();
	    Map<Integer,Image> images = new HashMap<Integer,Image>();
	    Map<Integer,Icon> icon_images = new HashMap<Integer,Icon>();
	    Move move = new Move();
	    boolean piece_selected;
	    boolean is_white;
	    int state;
	    //MoveSearcher move_searcher;
	    Game game;    
	    JLabel new_game,quit,about,history,first,prev,next,last;    
	    JPanel main_pane = new JPanel(new BorderLayout());
	    //PreferencesPane play_options;
	    boolean castling;
	    //PromotionPane promotion_pane;
	    List<Position> history_positions = new ArrayList<Position>();
	    int history_count;
	    Color bg_color = Color.decode("#efd39c");	
	 
	    
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
	        	//images.put(GameData.BOARD_IMAGE,ImageIO.read(new File("src/res/images/chessboard.png")));
	            images.put(GameData.BOARD_IMAGE,ImageIO.read(new File(resource.getResourceString("chessboard"))));
	            //images.put(GameData.BOARD_IMAGE2,ImageIO.read(resource.getResource("history_board")));
	            //images.put(GameData.GLOW,ImageIO.read(resource.getResource("glow")));
	            //images.put(GameData.GLOW2,ImageIO.read(resource.getResource("glow2")));            
	            //images.put(GameData.HISTORY_TITLE,ImageIO.read(resource.getResource("history_title")));
	            //images.put(GameData.MYCHESSMATE,ImageIO.read(resource.getResource("mychessmate")));
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
            setPreferredSize(new Dimension(450,495));
            setBackground(bg_color);
            addMouseListener(this);
        }
        @Override
        public void paintComponent(Graphics g){
            if(position.board == null) return;
            super.paintComponent(g);  
            //g.drawImage(images.get(GameData.MYCHESSMATE),20,36,this);
            Image scaledImage = images.get(GameData.BOARD_IMAGE).getScaledInstance(board_pane.getWidth()-20,board_pane.getHeight()-65,Image.SCALE_SMOOTH);
            g.drawImage(scaledImage,15,60,this);       
            for (int i = 0; i < position.board.length-11; i++) {
                if (position.board[i] == GameData.ILLEGAL) continue;                                                                
                int x = i%10;
                int y = (i-x)/10;
                
                if (piece_selected && i == move.source_location) {                
                    g.drawImage(images.get(GameData.GLOW), x * 45, y * 45,this);                    
                }else if(!piece_selected && move.destination == i && 
                        (position.board[i]==GameData.EMPTY || position.board[i]<0)){
                    g.drawImage(images.get(GameData.GLOW2), x * 45, y * 45, this);                                        
                }
                
                if (position.board[i] == GameData.EMPTY) continue;
                
                if(state == GameData.ANIMATING && i==move.source_location) continue;
                if (position.board[i] > 0) {          
                    int piece = position.human_pieces[position.board[i]].value;
                    g.drawImage(images.get(piece),x*45,y*45,this);
                }else{
                    int piece = position.computer_pieces[-position.board[i]].value;
                    g.drawImage(images.get(-piece),x*45,y*45,this);
                }               
            }  
            if(state == GameData.ANIMATING){
                g.drawImage(animating_image,movingX,movingY,this);
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if(state != GameData.HUMAN_MOVE) return;
            int location = boardValue(e.getY())*10+boardValue(e.getX());              
            if(position.board[location] == GameData.ILLEGAL) return;
            if((!piece_selected || position.board[location]>0) && position.board[location] != GameData.EMPTY){
                if(position.board[location]>0){
                    piece_selected = true;
                    move.source_location = location;
                }
            }else if(piece_selected && validMove(location)){
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
    
    public boolean validMove(int destination){        
        int source = move.source_location;
        int destination_square = position.board[destination];
        if(destination_square == GameData.ILLEGAL) return false;
        if(!game.safeMove(GameData.HUMAN,source,destination)) return false;
        boolean valid = false;
        int piece_value = position.human_pieces[position.board[source]].value;                        
        switch(piece_value){
            case Piece.PAWN:
                if(destination == source-10 && destination_square == GameData.EMPTY) valid = true;
                if(destination == source-20 && position.board[source-10] == GameData.EMPTY &&
                        destination_square == GameData.EMPTY && source>80) valid = true;
                if(destination == source-9 && destination_square<0) valid = true;
                if(destination == source-11 && destination_square<0) valid = true;
                break;
            case Piece.KNIGHT:
            case Piece.KING:
                //if(piece_value == Piece.KING) valid = checkCastling(destination);
                int[] destinations = null;
                if(piece_value == Piece.KNIGHT) destinations = new int[]{source-21,source+21,source+19,source-19,                    
                    source-12,source+12,source-8,source+8};
                else destinations = new int[]{source+1,source-1,source+10,source-10,
                    source+11,source-11,source+9,source-9};
                for(int i=0; i<destinations.length; i++){
                    if(destinations[i] == destination){
                        if(destination_square == GameData.EMPTY || destination_square<0){
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
                        if(destination_square != GameData.EMPTY){
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
//      if (!this.east_pane.isVisible())
//      {
//        this.east_pane.setVisible(true);
//        pack();
//        setLocationRelativeTo(null);
//      }
      this.is_white = true;//this.play_options.white_button.isSelected();
      this.move.source_location = -1;
      this.move.destination = -1;
      this.position = new Position();
      this.position.initialize(this.is_white);
      this.game = new Game(this.position);
      loadPieceImages();
      //this.promotion_pane.setIcons(this.is_white);
      this.board_pane.repaint();
      if (this.is_white) {
        this.state = 1005;
      } else {
        this.state = 1006;
      }
      this.castling = false;
      this.history_positions.clear();
      this.history_count = 0;
      //newHistoryPosition();
      //this.move_searcher.level = this.play_options.levelSlider.getValue();
      //play();
    }
}
