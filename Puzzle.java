/************************************************
 * Put two images inside your class path        *
 * with the name programming.jpg and blank.png. *
 * We'll more improve the code soon.            *
 ************************************************/
package com.easyprogramming;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class Puzzle extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5707218739787388455L;
	protected Container c;
	protected JLabel statusLabel;
	protected JPanel topPanel;
	protected JButton loadButton;
	protected JTextField loadField;
	
	int blank = 16;
	final JLabel[] labels = new JLabel[17];
	public BufferedImage image;
	public Puzzle(){
		super("Easy programming");
		c = this.getContentPane();
		c.setLayout(new BorderLayout());
	
		
		topPanel = new JPanel(new BorderLayout());
			JPanel midPanel = new JPanel(new FlowLayout());
			loadButton = new JButton("Load");
			loadField = new JTextField(12);
			midPanel.add(loadField);
			midPanel.add(loadButton);
			loadButton.addActionListener(this);
		    topPanel.add(midPanel, BorderLayout.CENTER);
	   c.add(getStatusBar(), BorderLayout.SOUTH);
		statusLabel.setText("Status: Initializing...");    
		initialize("programming.jpg");
		c.add(topPanel, BorderLayout.NORTH);
		setDefaultCloseOperation(Puzzle.EXIT_ON_CLOSE);
		setSize(600, 800);
		setVisible(true);
		setResizable(false);
		pack();
	}
	
	private Component getStatusBar() {
		// TODO Auto-generated method stub
		JPanel statusBar = new JPanel();
		statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		statusLabel = new JLabel("Status: ");
		statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 11));
		statusBar.setLayout(new FlowLayout(FlowLayout.LEFT));
		statusBar.add(statusLabel);
		statusBar.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
		return statusBar;
	}

	public void initialize(String path_to_image){
		image = null;
		
		try{
			File file = new File(path_to_image); // I have bear.jpg in my working directory
			FileInputStream fis = new FileInputStream(file);
			image = ImageIO.read(fis);
			fis.close();
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
		topPanel.add( getPreviewPane(), BorderLayout.WEST);
		c.add( getSplitBoard(), BorderLayout.CENTER);
		statusLabel.setText("Status: Ready to play..!");
	}
	private Component getSplitBoard() {
		// TODO Auto-generated method stub
		
		 int rows = 4; //You should decide the values for rows and cols variables
	        int cols = 4;
	        int chunks = rows * cols;

	        int chunkWidth = image.getWidth() / cols; // determines the chunk width and height
	        int chunkHeight = image.getHeight() / rows;
	        int count = 0;
	        BufferedImage imgs[] = new BufferedImage[chunks]; //Image array to hold image chunks
	        
	        for (int x = 0; x < rows; x++) {
	            for (int y = 0; y < cols; y++) {
	                //Initialize the image array with image chunks
	                imgs[count] = new BufferedImage(chunkWidth, chunkHeight, image.getType());

	                // draws the image chunk
	               Graphics2D gr = imgs[count++].createGraphics();
	                gr.drawImage(image, 0, 0, chunkWidth, chunkHeight, chunkWidth * y, chunkHeight * x, chunkWidth * y + chunkWidth, chunkHeight * x + chunkHeight, null);
	                gr.dispose();
	            }
	        }
	        
	        
	       JPanel imageBoard = new JPanel(new GridLayout(5, 4));
			//imageBoard.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
	        imageBoard.setBorder ( new TitledBorder ( new EtchedBorder (), "Image Completion puzzle" ) );
	        //writing mini images into image files
	        
	      //int[] random_index = {3, 15, 0, 2, 5, 1, 4, 8, 6, 9, 7, 11, 14, 12, 13, 10 };
	        statusLabel.setText("Status: preparing random image board...");
	        int[] random_index = generateRandomNumbers(imgs.length);
	   
	        //labels = new JLabel[imgs.length];
	      
	        for (int i = 0; i < imgs.length; i++) {
	        	//final ImageIcon icn = new ImageIcon(imgs[ random_index[i] ]);
	        	labels[i] = new JLabel(new ImageIcon(imgs[ random_index[i] ]));
	        	labels[i].setBorder(BorderFactory.createLineBorder(Color.WHITE));
	        	imageBoard.add(labels[i]);
	        	final int pos = i;
	        	labels[i].addMouseListener(new MouseAdapter() {
	                @Override
	                public void mouseClicked(MouseEvent e) {
	                	if(canMove(pos)){
	                		Icon temp = labels[pos].getIcon();
	                		labels[pos].setIcon(labels[blank].getIcon());
	                		labels[blank].setIcon(temp);
	                		blank = pos;
	                		statusLabel.setText("moved!");
	                	}else{
	                		statusLabel.setText("Cannot move!");
	                	}
	                }
	            });
	            //ImageIO.write(imgs[i], "jpg", new File("img" + i + ".jpg"));
	        }
	        
	        labels[16] = new JLabel(new ImageIcon("blank.png"));
	        
	        labels[16].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                	if(canMove(16)){
                		Icon temp = labels[16].getIcon();
                		labels[16].setIcon(labels[blank].getIcon());
                		labels[blank].setIcon(temp);
                		blank = 16;
                		statusLabel.setText("moved!");
                	}else{
                		statusLabel.setText("Cannot move!");
                	}
                }
            });
	        
	        imageBoard.add( labels[16] );
	        
		return imageBoard;
	}

	protected boolean canMove(int pos) {
		// TODO Auto-generated method stub
		String[] directions = getDirectionsFor(pos).split(",");
		for(String strlabel : directions){
			int label = Integer.parseInt(strlabel);
			if(label == blank)
				return true;
		}
		return false;
	}

	private String getDirectionsFor(int pos) {
		// TODO Auto-generated method stub
		String direction = "25,26,27,28";
		
		switch(pos){
			case 0:
				direction = "1,4";
			break;
			case 1:
				direction = "0,5,2";
			break;
			case 2:
				direction = "1,3,6";
			break;
			case 3:
				direction = "2,7";
			break;
			case 4:
				direction = "0,5,8";
			break;
			case 5:
				direction = "1,4,6,9";
			break;
			case 6:
				direction = "2,5,7,10";
			break;
			case 7:
				direction = "3,6,11";
			break;
			case 8:
				direction = "4,9,12";
			break;
			case 9:
				direction = "5,8,10,13";
			break;
			case 10:
				direction = "6,9,11,14";
			break;
			case 11:
				direction = "7,10,15";
			break;
			case 12:
				direction = "8,9,13,16";
			break;
			case 13:
				direction = "9,12,14,16";
			break;
			case 14:
				direction = "10,13,15";
			break;
			case 15:
				direction = "11,14,15";
			break;
			case 16:
				direction = "12,24";
			break;
		}
		return direction;
	}

	private int[] generateRandomNumbers(int max) {
		// TODO Auto-generated method stub
		Random random = new Random();
		int[] rand_array = new int[max];
		int cntr = 0;
		
		//fill array with a number not coming in  random numbers range.
		for( int i=0; i<max; i++ )
			rand_array[i] = 1111111111;
		
		while( cntr < max ){
			int rand_index = random.nextInt(max);

			if(!( in_array(rand_array, rand_index ))){
				rand_array[cntr] = rand_index;
				System.out.println(cntr+" : "+rand_index);
				cntr++;
			}
		}
		return rand_array;
	}

	private boolean in_array(int[] rand_array, int rand_index) {
		// TODO Auto-generated method stub
		for( int index : rand_array ){
			if( index == rand_index )
				return true;
		}
		
		return false;
	}

	private Component getPreviewPane() {
		// TODO Auto-generated method stub
		ImageIcon img = new ImageIcon(image.getScaledInstance(100, 140, Image.SCALE_DEFAULT));
		JLabel preview = new JLabel(img);
		
		return preview;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Puzzle();
		
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		// TODO Auto-generated method stub
		//if(evt.getSource() == loadButton)
			//initialize(loadField.getText());
		JOptionPane.showMessageDialog(null, "This feature will be added soon.");
	}

}
