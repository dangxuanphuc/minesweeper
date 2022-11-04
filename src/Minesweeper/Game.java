package Minesweeper;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.BevelBorder;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Random;
import java.awt.event.InputEvent;

public class Game extends JFrame {

	private JPanel contentPane;
	private JTextField txtMine, txtTime;
	private JPanel panel2, panel1;
	private JButton btnIcon;

	private JRadioButtonMenuItem btnIntermediate,btnBeginner, btnExpert, btnCustom;
	private JMenuItem mntmNewGame, mntmOverView, mntmExit, mntmAbout;
	private ButtonGroup group;
	private JScrollPane aboutPane, helpPane;
	private JMenu mnGame, mnHelp, mnOptions;
	private JMenuItem mntmTotalMines, mntmSolveGame;
	
	private ImageIcon[] icon = new ImageIcon[20];
	private int rows, cols, mines, minesLeft;
	private JButton[][] buttons;
	private int width, height;
		
	private boolean check = true; // Kiem tra xem da click vao min hay chua
	private boolean startTime = false;
	private boolean isWon = false;
	private int[][] box;  //0: chua mo, 1: da mo
	private MouseHandler handler;
	private int[][]countMine; // -1: bom, 0: o trang, ....
    private StopTimer time;
    private int savedLevel = 1, savedRow, savedCol, savedMine;
	private Sound sound = new Sound();

	/**
	 * Create the frame.
	 */
	public Game() {
		setBackground(Color.LIGHT_GRAY);
		setTitle("Minesweeper");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(200, 100);
		
		setIcon();
		setMenu();
		setPanel(savedLevel, 0, 0, 0);
		
		time = new StopTimer();
		
		btnIcon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				reset();
				panel1.removeAll();
				setPanel(savedLevel, savedRow, savedCol, savedMine);
			}
		});
	}

	public void setIcon() {
        for (int i = 0; i <= 8; i++) {
        	icon[i] = new ImageIcon(getClass().getResource("/imgs/"+i+".png"));
        }
        icon[9] = new ImageIcon(getClass().getResource("/imgs/icon1.png"));
        icon[10] = new ImageIcon(getClass().getResource("/imgs/icon2.png"));
        icon[11] = new ImageIcon(getClass().getResource("/imgs/icon3.png"));
        icon[12] = new ImageIcon(getClass().getResource("/imgs/icon4.png"));
        icon[13] = new ImageIcon(getClass().getResource("/imgs/icon5.png"));
        icon[14] = new ImageIcon(getClass().getResource("/imgs/mine.png"));
        icon[15] = new ImageIcon(getClass().getResource("/imgs/mine1.png"));
        icon[16] = new ImageIcon(getClass().getResource("/imgs/flag.png"));
        icon[17] = new ImageIcon(getClass().getResource("/imgs/unsure.png"));
        icon[18] = new ImageIcon(getClass().getResource("/imgs/square.png"));
        icon[19] = new ImageIcon(getClass().getResource("/imgs/not_flag.png"));
    }
	
	public void setPanel(int level, int setRow, int setCol, int setMine) {
		panel1 = new JPanel();
		btnIcon = new JButton();
		txtMine = new JTextField();
		txtTime = new JTextField();
		panel2 = new JPanel();
		
		if (level == 1) {
			width = 180;
	        height = 263;
	        rows = 9;
	        cols = 9;
	        mines = 10;
	        panel1.setBounds(10, 5, 148, 37);
			txtMine.setBounds(8, 6, 47, 26);
			btnIcon.setBounds(61, 6, 26, 26);
			txtTime.setBounds(93, 6, 47, 26);
			panel2.setBounds(10, 47, 148, 148);
        } else if (level == 2) {
        	width = 293;
            height = 380;
            rows = 16;
            cols = 16;   	
            mines = 40;
        	panel1.setBounds(10, 7, 260, 37);
    		txtMine.setBounds(10, 6, 47, 26);
    		btnIcon.setBounds(118, 6, 26, 26);
    		txtTime.setBounds(205, 6, 47, 26);
    		panel2.setBounds(10, 50, 260, 260);
        } else if (level == 3) {
        	width = 517;
            height = 375;
            rows = 16;
            cols = 30;
            mines = 99;
            panel1.setBounds(10, 7, 485, 37);
            txtMine.setBounds(10, 6, 47, 26);
            btnIcon.setBounds(233, 6, 26, 26);
            txtTime.setBounds(430, 6, 47, 26);
            panel2.setBounds(10, 50, 485, 260);
        } else if (level == 4) {
        	rows = setRow;
        	cols = setCol;
        	mines = setMine;
            width = cols * 16 + 30;
            height = rows * 16 + 110;
            panel1.setBounds(10, 5, cols * 16 + 5, 37);
    		txtMine.setBounds(8, 6, 47, 26);
    		btnIcon.setBounds((cols * 16 + 5)/2 - 13, 6, 26, 26);
    		txtTime.setBounds((cols * 16 + 5) - 8 - 47, 6, 47, 26);
    		panel2.setBounds(10, 47, cols * 16 + 5, rows * 16 + 5);
        }
		setSize(width, height);
		savedLevel = level;
		savedRow = rows;
		savedCol = cols;
		
		minesLeft = mines;
		buttons = new JButton[rows][cols];
		box = new int[rows][cols];
		countMine = new int[rows][cols];
		handler = new MouseHandler();
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.LIGHT_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		panel1.setBackground(Color.LIGHT_GRAY);
		panel1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, Color.GRAY, Color.GRAY));
		contentPane.add(panel1);
		panel1.setLayout(null);
		
		btnIcon.setIcon(icon[9]);
		btnIcon.setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(128, 128, 128)));
		panel1.add(btnIcon);
		
		txtMine.setBackground(Color.BLACK);
		txtMine.setForeground(Color.RED);
		txtMine.setFont(new Font("Dialog", Font.BOLD, 25));
		txtMine.setEditable(false);
		txtMine.setText("0"+mines);
		panel1.add(txtMine);
		txtMine.setColumns(10);
		
		txtTime.setBackground(Color.BLACK);
		txtTime.setFont(new Font("Dialog", Font.BOLD, 25));
		txtTime.setForeground(Color.RED);
		txtTime.setEditable(false);
		txtTime.setHorizontalAlignment(SwingConstants.RIGHT);
		txtTime.setText("000");
		panel1.add(txtTime);
		txtTime.setColumns(10);
		
		panel2.setBackground(Color.LIGHT_GRAY);
		panel2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, Color.GRAY, Color.GRAY));
		panel2.setLayout(new GridLayout(rows, cols));
		contentPane.add(panel2);
	
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				buttons[i][j] = new JButton();
				buttons[i][j].setIcon(icon[18]);
				buttons[i][j].setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.WHITE, Color.GRAY, Color.GRAY));
				buttons[i][j].addMouseListener(handler);
				panel2.add(buttons[i][j]);
			}
		}
		
		reset();
		
		panel2.revalidate();
		panel2.repaint();
	}
	
	public void setMenu() {
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.LIGHT_GRAY);
		setJMenuBar(menuBar);
		
		try {
			JEditorPane aboutContent = new JEditorPane(getClass().getResource("/imgs/about.html"));
			aboutContent.setEditable(false);
			aboutPane = new JScrollPane(aboutContent);
			
			JEditorPane helpContent = new JEditorPane(getClass().getResource("/imgs/help.html"));
			helpContent.setEditable(false);
			helpPane = new JScrollPane(helpContent);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		mnGame = new JMenu("Game");
		mnGame.setMnemonic('G');
		mnGame.setForeground(Color.BLACK);
		menuBar.add(mnGame);
		
		mntmNewGame = new JMenuItem("New Game");
		mntmNewGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel1.removeAll();
				setPanel(savedLevel, savedRow, savedCol, savedMine);
			}
		});
		mntmNewGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0));
		mntmNewGame.setForeground(Color.BLACK);
		mnGame.add(mntmNewGame);
		
		btnBeginner = new JRadioButtonMenuItem("Beginner");
		btnBeginner.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPanel(1, 0, 0, 0);
				btnBeginner.setSelected(true);
			}
		});
		btnBeginner.setSelected(true);
		btnBeginner.setForeground(Color.BLACK);
		mnGame.add(btnBeginner);
		
		btnIntermediate = new JRadioButtonMenuItem("Intermediate");
		btnIntermediate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPanel(2, 0, 0, 0);
				btnIntermediate.setSelected(true);
			}
		});
		btnIntermediate.setForeground(Color.BLACK);
		mnGame.add(btnIntermediate);
		
		btnExpert = new JRadioButtonMenuItem("Expert");
		btnExpert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPanel(3, 0, 0, 0);
				btnExpert.setSelected(true);
			}
		});
		btnExpert.setForeground(Color.BLACK);
		mnGame.add(btnExpert);
		
		btnCustom = new JRadioButtonMenuItem("Custom");
		btnCustom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int boxRow = Integer.parseInt(JOptionPane.showInputDialog("Select a number of row"));
				int boxCol = Integer.parseInt(JOptionPane.showInputDialog("Select a number of column"));
				int minesNum = Integer.parseInt(JOptionPane.showInputDialog("Select a number of mines, less than " + (boxRow - 1) * (boxCol - 1)));
				savedMine = minesNum;
				if(minesNum > 0 && minesNum <= 9)
					txtMine.setText("00"+minesNum);
				
				setPanel(4, boxRow, boxCol, minesNum);
				btnCustom.setSelected(true);
			}
		});
		btnCustom.setForeground(Color.BLACK);
		mnGame.add(btnCustom);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_MASK));
		mntmExit.setForeground(Color.BLACK);
		mnGame.add(mntmExit);
		
		group = new ButtonGroup();
		group.add(btnBeginner);
		group.add(btnIntermediate);
		group.add(btnExpert);
		group.add(btnCustom);
		
		mnOptions = new JMenu("Options");
		mnOptions.setMnemonic('O');
		mnOptions.setForeground(Color.BLACK);
		menuBar.add(mnOptions);
		
		mntmTotalMines = new JMenuItem("Total Mines");
		mntmTotalMines.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int mine = Integer.parseInt(JOptionPane.showInputDialog("Select a number of mines less than " + (savedRow - 1) * (savedCol - 1)));
				if(mine > (savedRow - 1) * (savedCol - 1)){
					mine = (savedRow - 1) * (savedCol - 1);
				}
				savedMine = mine;
				setPanel(4, savedRow, savedCol, mine);
				if(mine <= 9)
					txtMine.setText("00"+mine);
				else if(mine > 9 && mine <= 99)
					txtMine.setText("0"+mine);
				else if(mine > 99)
					txtMine.setText(""+mine);
			}
		});
		mntmTotalMines.setForeground(Color.BLACK);
		mnOptions.add(mntmTotalMines);
		
		mntmSolveGame = new JMenuItem("Solve Game");
		mntmSolveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!isWon){
					for (int i = 0; i < rows; i++) {
						for (int j = 0; j < cols; j++) {
							if(countMine[i][j] == -1 && buttons[i][j].getIcon() != icon[16])
								buttons[i][j].setIcon(icon[19]);
							txtMine.setText("000");
							btnIcon.setIcon(icon[10]);
							buttons[i][j].removeMouseListener(handler);
							time.stop();
						}
					}
				}
			}
		});
		mntmSolveGame.setForeground(Color.BLACK);
		mnOptions.add(mntmSolveGame);
		
		mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('H');
		mnHelp.setForeground(Color.BLACK);
		menuBar.add(mnHelp);
		
		mntmOverView = new JMenuItem("Over View");
		mntmOverView.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
		mntmOverView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, helpPane, "How to play and more", JOptionPane.PLAIN_MESSAGE, null);
			}
		});
		mntmOverView.setForeground(Color.BLACK);
		mnHelp.add(mntmOverView);
		
		mntmAbout = new JMenuItem("About");
		mntmAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, aboutPane, "About", JOptionPane.PLAIN_MESSAGE, null);
			}
		});
		mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK | InputEvent.ALT_MASK));
		mntmAbout.setForeground(Color.BLACK);
		mnHelp.add(mntmAbout);
	}
	
	public void reset(){
		check = true;
		startTime = false;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				box[i][j] = 0;
			}
		}
	}

	class StopTimer extends JFrame implements Runnable{
		long startTime;
		long timer = 0;
		boolean isRunning = false;
		Thread thread;
		Runnable displayUpdate = new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				displayElapsedTime(timer);
				timer++;
			}
		};
		
		public void stop(){
			long elapsed = timer;
			isRunning = false;
			try {
				thread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			displayElapsedTime(timer);
			timer = 0;
		}
		
		public void displayElapsedTime(long elapsedTime){
			if (elapsedTime >= 0 && elapsedTime <= 9) {
	            txtTime.setText("00" + elapsedTime);
	        } else if (elapsedTime > 9 && elapsedTime <= 99) {
	        	txtTime.setText("0" + elapsedTime);
	        } else if (elapsedTime > 99 && elapsedTime <= 999) {
	        	txtTime.setText("" + elapsedTime);
	        } else if(elapsedTime == 999)
	        	return;
		}
		
		public void Start(){
			startTime = System.currentTimeMillis();
			isRunning = true;
			thread = new Thread(this);
			thread.start();
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				while(isRunning){
					SwingUtilities.invokeAndWait(displayUpdate);
					Thread.sleep(1000);
				}
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class MouseHandler extends MouseAdapter{
		@Override
		public void mouseClicked(MouseEvent e) {
			//TODO Auto-generated method stub
			super.mouseClicked(e);
			if(check == true){
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						buttons[i][j].setIcon(icon[18]);
					}
				}
				setMine();  
				check = false;
			}
			showValue(e);
            hasWon();

            if (startTime == false) {
                time.Start();
                startTime = true;
            }
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			super.mousePressed(e);
			btnIcon.setIcon(icon[11]);
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			super.mouseReleased(e);
			btnIcon.setIcon(icon[9]);
		}
	}
	
	public void hasWon(){
		int a = 0;
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if(box[i][j] == 0)
					a = 1;
			}
		}
		if(a == 0){
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					buttons[i][j].removeMouseListener(handler);
				}
			}
			time.stop();
			btnIcon.setIcon(icon[12]);
			txtMine.setText("000");
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					if(buttons[i][j].getIcon() == icon[18]){
						buttons[i][j].setIcon(icon[16]);
						sound.tada();
						isWon = true;
					}
					if(buttons[i][j].getIcon() == icon[16])
						sound.tada();
				}
			}
		}
	}
	
	public void showValue(MouseEvent e){
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				
				if(e.getSource() == buttons[i][j]){
					if(e.isMetaDown() == false){
						
						if(countMine[i][j] == -1){ 
							for (int k = 0; k < rows; k++) {
								for (int k2 = 0; k2 < cols; k2++) {
									
									if(countMine[k][k2] == -1){
										if(buttons[k][k2].getIcon() != icon[16]){
											buttons[k][k2].setIcon(icon[14]);
											btnIcon.setIcon(icon[10]);
											buttons[k][k2].setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(128, 128, 128)));
											sound.boom();
										}
										buttons[k][k2].removeMouseListener(handler);
										
									}else if(buttons[k][k2].getIcon() == icon[16]){
										buttons[k][k2].setIcon(icon[15]);
										buttons[k][k2].setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(128, 128, 128)));
									}
									buttons[k][k2].removeMouseListener(handler);
								}
							}
							time.stop();
							btnIcon.setIcon(icon[10]);
						} else if(countMine[i][j] == 0){
							setValues(i, j);
							sound.beep();
						} else {
							if(buttons[i][j].getIcon() != icon[16]){
								buttons[i][j].setIcon(icon[countMine[i][j]]);
								buttons[i][j].setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(128, 128, 128)));
								sound.beep();
							} else sound.beep();
							box[i][j] = 1;
							break;
						}
					} else {
							if(buttons[i][j].getIcon() == icon[18]){
								minesLeft--;
								buttons[i][j].setIcon(icon[16]);
								sound.beep2();
							} else if(buttons[i][j].getIcon() == icon[16]){
								minesLeft++;
								buttons[i][j].setIcon(icon[17]);
								sound.beep2();
							} else if(buttons[i][j].getIcon() == icon[17]){
								buttons[i][j].setIcon(icon[18]);
								sound.beep2();
							}
							if(minesLeft < 0)
								txtMine.setText("" + minesLeft);
							else if(minesLeft >= 0 && minesLeft <= 9)
                            	txtMine.setText("00" + minesLeft);
                            else if(minesLeft > 9 && minesLeft <= 99)
                            	txtMine.setText("0" + minesLeft);
                            else if(minesLeft > 99)
                            	txtMine.setText("" + minesLeft);
					}
				}
			}
		}
	}
	
	public void setMine(){
		Random rd = new Random();
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {                                         
				countMine[i][j] = 0;
			}
		}
		
		for (int i = 0; i < mines; i++) {
			int x = rd.nextInt(rows);
			int y = rd.nextInt(cols);
			
			countMine[x][y] = -1;
			box[x][y] = 1;
		}
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if(countMine[i][j] != -1){
					int count = 0;
					for (int k = i-1; k < i+2; k++) {
						for (int k2 = j-1; k2 < j+2; k2++) {
							if(k >= 0 && k < rows && k2 >= 0 && k2 < cols && countMine[k][k2] == -1){
								count++;
							}
							countMine[i][j] = count;
						}
					}
				}
			}
		}
	}
	
	public void setValues(int i, int j){
		box[i][j] = 1;
		buttons[i][j].setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(128, 128, 128)));
		if(buttons[i][j].getIcon() != icon[16]){
			buttons[i][j].setIcon(icon[countMine[i][j]]);
		}
		for (int k = i-1; k < i+2; k++) {
			for (int k2 = j-1; k2 < j+2; k2++) {
				if(k >= 0 && k < rows && k2 >= 0 && k2 < cols && box[k][k2] == 0){
					if(buttons[i][j].getIcon() != icon[16]){
						if(countMine[k][k2] == 0)
							setValues(k, k2);
						else {
							buttons[k][k2].setIcon(icon[countMine[k][k2]]);
							box[k][k2] = 1;
							buttons[k][k2].setBorder(new MatteBorder(0, 0, 0, 0, (Color) new Color(128, 128, 128)));
						}
					}
				}
			}
		}
	}
}
