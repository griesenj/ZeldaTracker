import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

/**
 * @author Jon Griesen (griesenj@mail.gvsu.edu)
 */

@SuppressWarnings("serial")
public class View extends JFrame {
	
	private Model model;
	private Controller controller;
	
	// Classifies JLabels by number of unique collection states.
	private ArrayList<JLabel> twoStatesArray = new ArrayList<JLabel>();
	private ArrayList<JLabel> threeStatesArray = new ArrayList<JLabel>();
	private ArrayList<JLabel> fourStatesArray = new ArrayList<JLabel>();
	
	// Associates JLabels with item names for purposes of manipulating model data via controller.
	private LinkedHashMap<JLabel, String> labelNames = new LinkedHashMap<JLabel, String>();
	
	public View(Model model, Controller controller) {
		this.model = model;
		this.controller = controller;
		
		// Populate model w/ initial data from collectables.txt
		controller.populateMap();
		
		// Create frame and application window preferences
		JFrame zeldaFrame = new JFrame("Zelda: Majora's Mask - Item Tracker");
		zeldaFrame.setSize(425, 625);
		zeldaFrame.setResizable(false);
		
		try {
			BufferedImage icon = ImageIO.read(getClass().getResource("/img/_triforcelogo.png"));
			zeldaFrame.setIconImage(icon);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		// Create Layout Manager
		zeldaFrame.setLayout(new BorderLayout());
		Container contentPane = zeldaFrame.getContentPane();
		
		// Create Menu Bar
		JMenuBar zeldaMenuBar = new JMenuBar();
		JMenu zeldaOptionsMenu = new JMenu("Options");
		zeldaMenuBar.add(zeldaOptionsMenu);
		
		JMenuItem menuItem1 = new JMenuItem("Save Current Progress");
		menuItem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.writeFile();
			}
		});
		
		JMenuItem menuItem2 = new JMenuItem("Reset Item Tracker");
		menuItem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.resetMap();
				labelNames.entrySet().forEach(entry -> {			
					setImage(entry.getKey(), entry.getValue() + " " + model.getValue(entry.getValue()));
				});				
			}
		});
		
		zeldaOptionsMenu.add(menuItem1);
		zeldaOptionsMenu.add(menuItem2);
		zeldaFrame.setJMenuBar(zeldaMenuBar);

		// Create Panel #1 : Masks Panel
		JPanel masksPanel = new JPanel();
		Dimension masksSize = getPreferredSize();
				
		masksPanel.setPreferredSize(masksSize);
		masksSize.height = 220;
		masksPanel.setLayout(new GridLayout(4, 6));
		masksPanel.setBackground(Color.BLACK);
		createJLabels(masksPanel, 0, 24);
		
		// Create Panel #2 : Items Panel
		JPanel itemsPanel = new JPanel();
		Dimension itemsSize = getPreferredSize();

		itemsPanel.setPreferredSize(itemsSize);
		itemsSize.height = 220;
		itemsPanel.setLayout(new GridLayout(4, 6));
		itemsPanel.setBackground(Color.BLACK);	
		createJLabels(itemsPanel, 24, 48);
		
		// Create Panel #3 : Songs Panel
		JPanel songsPanel = new JPanel();
		Dimension songsSize = getPreferredSize();

		songsPanel.setPreferredSize(songsSize);
		songsSize.width = 175;
		songsPanel.setLayout(new GridLayout(2, 5));
		songsPanel.setBackground(Color.BLACK);
		createJLabels(songsPanel, 48, 58);
		
		// Create Panel #4 : Remains Panel
		JPanel remainsPanel = new JPanel();
		Dimension remainsSize = getPreferredSize();

		remainsPanel.setPreferredSize(remainsSize);
		remainsPanel.setLayout(new GridLayout(2, 2));
		remainsPanel.setBackground(Color.BLACK);
		createJLabels(remainsPanel, 58, 62);
		
		// Create Panel #5 : Upgrades Panel
		JPanel upgradesPanel = new JPanel();
		Dimension upgradesSize = getPreferredSize();
		
		upgradesPanel.setPreferredSize(upgradesSize);
		upgradesSize.width = 145;
		upgradesPanel.setLayout(new GridLayout(2, 3));
		upgradesPanel.setBackground(Color.BLACK);
		createJLabels(upgradesPanel, 62, 68);
		
		// Add panels to overall layout manager
		contentPane.add(masksPanel, BorderLayout.NORTH);
		contentPane.add(itemsPanel, BorderLayout.SOUTH);
		contentPane.add(remainsPanel, BorderLayout.CENTER);
		contentPane.add(upgradesPanel, BorderLayout.EAST);
		contentPane.add(songsPanel, BorderLayout.WEST);
		
		zeldaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
		zeldaFrame.setVisible(true);
	}
		
	/**
	 * Creates JLabels for the specified panel with corresponding ImageIcons and MouseListeners.
	 */
	public void createJLabels(JPanel panel, int lowerBound, int upperBound) {	
		for (int i = lowerBound; i < upperBound; i++) {
			JLabel label = new JLabel();
			String item = model.getKeyStrings().get(i);			
			setImage(label, item + " " + model.getValue(model.getKeyStrings().get(i)));
			
			labelNames.put(label, item);
			if (item.equals("Sword") | item.equals("Bomb Bag") | item.equals("Quiver")) {
				fourStatesArray.add(label);				
			} else if (item.equals("Shield") | item.equals("Wallet")) {
				threeStatesArray.add(label);
			} else {
				twoStatesArray.add(label);
			}
			addMouseListener(label);
			
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setVerticalAlignment(JLabel.CENTER);
			panel.add(label);
		}
	}
	
	/**
	 * Sets ImageIcons for JLabel based on specified filename.
	 */
	public void setImage(JLabel label, String filename) {
		try {
			BufferedImage icon = ImageIO.read(getClass().getResource("/img/" + filename + ".png"));
			label.setIcon(new ImageIcon(icon));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds MouseListeners to each JLabel. When label detects click, view uses controller to update
	 * item collection state. Model is consulted and JLabel ImageIcon is updated to reflect change. 
	 */
	public void addMouseListener(JLabel label) {
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (twoStatesArray.contains(label)) {
					controller.incrementValueTwoStates(labelNames.get(label));
					String filename = labelNames.get(label) + " " + model.getValue(labelNames.get(label));
					setImage(label, filename);					
				} else if (threeStatesArray.contains(label)) {
					controller.incrementValueThreeStates(labelNames.get(label));
					String filename = labelNames.get(label) + " " + model.getValue(labelNames.get(label));
					setImage(label, filename);						
				} else if (fourStatesArray.contains(label)) {
					controller.incrementValueFourStates(labelNames.get(label));
					String filename = labelNames.get(label) + " " + model.getValue(labelNames.get(label));
					setImage(label, filename);
				}
			}
		});
	}
	
}

