package agenda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.Timer;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import simulator.World;

public class Simulator extends JPanel
{	
	File json;
	Planner planner;
	Font font = new Font("SANS_SERIF", Font.PLAIN, 14);
	Font timeFont = new Font("SANS_SERIF", Font.BOLD, 36);
	
	Map<agenda.Stage, Integer> stageMap = new HashMap<>();
	
	World world;
	
	Time tijd;
	
	int uren;
	int minuten;
	int updatetijd = 1000/30;
	
	ActionListener updateTime;
	
	Timer updateT;
	private AdvancedPlayer player;
	
	private int bezoekers;
	
	public Simulator(File json, Planner planner, Map<agenda.Stage, Integer> stageMap, int bezoekers)
	{
		super(new BorderLayout());
		
		this.json = json;
		this.planner = planner;
		this.stageMap = stageMap;
		this.bezoekers = bezoekers;
		world = new World(planner.agenda, stageMap, json, "tileSet\\Tiled2.png", false, false, bezoekers);
		
		ButtonPanel buttonPanel = new ButtonPanel(planner);
		
		add(buttonPanel, BorderLayout.NORTH);
		add(new SimulatiePanel(planner), BorderLayout.CENTER);
		add(new TimePanel(planner), BorderLayout.SOUTH);
		
		buttonPanel.playMusic("static_data\\music\\music.mp3");
	}
	
	class ButtonPanel extends JPanel
	{
		Planner planner;
		ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
		ArrayList<Area> plaatjes = new ArrayList<Area>();
		ArrayList<Rectangle2D> rechthoek = new ArrayList<Rectangle2D>();
		
		JLabel speed1 = new JLabel("Speed: ");
		JLabel speed2 = new JLabel(" min/sec");
		JLabel time = new JLabel("09:00");
		JTextField speedInvoer = new JTextField("0.0", 3);
		JLabel visitors = new JLabel("Bezoekers:");
		JTextField visitorsField = new JTextField("0", 2);
		JLabel visitors2 = new JLabel(" aantal/min");
		
		boolean plays = false;
		boolean musicOn = true;
		
		double oldSpeed = 0.0;
		int oldVisitors = 0;
		
		public ButtonPanel(Planner planner)
		{
			super(null);
			setPreferredSize(new Dimension(1200, 80));
			setBackground(Color.WHITE);
			SpringLayout layout = new SpringLayout();
			setLayout(layout);
			
			this.planner = planner;
			
			updateTime();
			
			add(speed1);
			add(speedInvoer);
			add(speed2);
			add(time);
			add(visitors);
			add(visitorsField);
			add(visitors2);
			
			speed1.setFont(font);
			speed2.setFont(font);
			time.setFont(timeFont);
			visitors.setFont(font);
			visitors2.setFont(font);
			
			layout.putConstraint(SpringLayout.WEST, visitors, 90, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, visitors, 25, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, visitorsField, 75, SpringLayout.WEST, visitors);
			layout.putConstraint(SpringLayout.NORTH, visitorsField, 25, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, visitors2, 23, SpringLayout.WEST, visitorsField);
			layout.putConstraint(SpringLayout.NORTH, visitors2, 25, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, speed1, 80, SpringLayout.WEST, visitors2);
			layout.putConstraint(SpringLayout.NORTH, speed1, 25, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, speedInvoer, 45, SpringLayout.WEST, speed1);
			layout.putConstraint(SpringLayout.NORTH, speedInvoer, 25, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, speed2, 30, SpringLayout.WEST, speedInvoer);
			layout.putConstraint(SpringLayout.NORTH, speed2, 25, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, time, 900, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, time, 15, SpringLayout.NORTH, this);
			
			File mapmap = new File("static_data/simulator_GUI/");
			fillArrayList(mapmap);
			fillPlaatjes();
			clicked();
		}
		
		public void fillArrayList(File file)
		{
			int teller = 1;
			
			if(file.exists())
			{
				if (file.isDirectory()) 
			    {
			      File[] files = file.listFiles(); // All files and subdirectories
			      for (int i = 0; files != null && i < files.length; i++) 
			      {
			    	  if(getFileExtension(files[i]).equals("png"))
			    	  {
			    		  try 
			    			{
			    			  File chooseImage = new File("static_data/simulator_GUI/" + teller + "-" + ".png");
			    			  BufferedImage image = ImageIO.read(chooseImage);
				    		  images.add(image);
				    		  teller++;
			    			} catch (IOException ex) 
			    			{
			    				ex.printStackTrace();
			    			}
			    		}
			      }
			    }
			}
			else
			{
				System.out.println("Map bestaat niet");
			}
		}
		
		public String getFileExtension(File file) 
		{
	        String fileName = file.getName();
	        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
	        return fileName.substring(fileName.lastIndexOf(".")+1);
	        else return "";
	    }
		
		public void fillPlaatjes()
		{
			Rectangle2D refresh2D = new Rectangle2D.Double(25 , 10, 50, 50);
			Rectangle2D back2D = new Rectangle2D.Double(400 , 10, 50, 50);
			Rectangle2D play2D = new Rectangle2D.Double(500 , 10, 50, 50);
			Rectangle2D pause2D = new Rectangle2D.Double(600 , 10, 50, 50);
			Rectangle2D forward2D = new Rectangle2D.Double(700 , 10, 50, 50);
			Rectangle2D music2D = new Rectangle2D.Double(1100 , 10, 50, 50);
			
			Area refresh = new Area(refresh2D);
			Area back = new Area(back2D);
			Area play = new Area(play2D);
			Area pause = new Area(pause2D);
			Area forward = new Area(forward2D);
			Area music = new Area(music2D);
			
			rechthoek.add(refresh2D);
			rechthoek.add(back2D);
			rechthoek.add(play2D);
			rechthoek.add(pause2D);
			rechthoek.add(forward2D);
			rechthoek.add(music2D);
			
			plaatjes.add(refresh);
			plaatjes.add(back);
			plaatjes.add(play);
			plaatjes.add(pause);
			plaatjes.add(forward);
			plaatjes.add(music);
			
		}
		
		public void clicked()
		{
			addMouseListener(new MouseAdapter()
			{
				public void mouseClicked(MouseEvent e) 
				{
					for(int i = 0; i < plaatjes.size(); i++)
					{
						if(plaatjes.get(i).contains(e.getPoint()))
						{
							switch (i)
							{
							case 0:
								musicOn = false;
								synchronized(this) {
						            if(player != null) {
						                player.stop();
						                player = null;
						            }
						        }
								refreshSim();
								break;
							case 1:
								backSim();
								break;
							case 2:
								playSim();
								break;
							case 3:
								pauseSim();
								break;
							case 4:
								forwardSim();
								break;
							case 5:
								if(musicOn){
									musicOn = false;
								synchronized(this) {
						            if(player != null) {
						                player.stop();
						                player = null;
						            }
						        }								
								}
								else{
									playMusic("static_data\\music\\music.mp3");
									musicOn = true;
								}
								repaint();
								break;
							default:
								break;
							}
						}
					}
				}
			});
			
			ActionListener update = new ActionListener()
			{
				int newVisitors = 0;
				double speed = 0.0;
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if(!speedInvoer.getText().equals(""))
					{
						speed = Double.parseDouble(speedInvoer.getText());
					}
					
					if(!visitorsField.getText().equals(""))
					{
						newVisitors = Integer.parseInt(visitorsField.getText());
					}
					
					if(speed != oldSpeed)
					{
						double realTimeToSimTime = (speed * 60)/1000;
						if(plays)
						{
							world.setRealTimeToSimTime(realTimeToSimTime);
						}
						/*
						 * min         /sec
						 * 60*sec	   /sec
						 * 60*sec/1000 / millisec
						 */
						//min*60/1000
					}
					if(newVisitors != oldVisitors)
					{
						int aantalBezoekers = (int) (newVisitors * speed); //aantal bezoeker per minuut die erbij komen. nodig is het aantal bezoeker per update dus moet gekeken worden hoeveel minuten er in een seconde om gaan
						if(plays)
						{
							//TODO doorgeven aantal bezoeker per minuut
						}
					}
					
					oldSpeed = speed;
					oldVisitors = newVisitors;
				}
			};
			
			Timer updateTimer = new Timer(50, update);
			updateTimer.start();
			
			
		}
		
		public void updateTime()
		{
			updateTime = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					tijd = world.getTime();
					minuten = tijd.getMinutes();
					uren = tijd.getHours();
					
					if(uren > 26) // wanneer alle bezoekers wegzijn stopt de simulatie of wanneer 3 uur nachts is bereikt
					{
						System.out.println("World : " + world + "bezoekers : " + bezoekers);
						pauseSim();
						world.close();
						world = new World(planner.agenda, stageMap, json, "tileSet\\Tiled2.png", false, false, bezoekers);
						tijd = world.getTime();
						minuten = tijd.getMinutes();
						uren = tijd.getHours();
					}
						
					if(uren > 23)
					{
						minuten = tijd.getMinutes();
						uren = (tijd.getHours() - 24);
					}
					
					String urenS;								
					if(uren < 10)
					{
						urenS = "0" + uren;
					}
					else
					{
						urenS = "" + uren;
					}
					
					String minutenS;
					if(minuten < 10)
					{
						minutenS = "0" + minuten;
					}
					else
					{
						minutenS = "" + minuten;
					}
					
					time.setText(urenS + ":" + minutenS);
				}
			};
			
			updateT = new Timer(updatetijd, updateTime);
		}
		
		public void refreshSim()
		{
			pauseSim();
			planner.tabbedPane.removeTabAt(2);
			planner.tabbedPane.addTab("Simulatie", new SimulatieGUI(planner));
			planner.tabbedPane.setSelectedIndex(2);
			planner.repaint();
			planner.revalidate();
		}
		
		public void backSim()
		{
			tijd = world.getTime();
			int h = 9;
			int m = tijd.getMinutes();
			
			if(tijd.getHours() != 9)
			{
				h = tijd.getHours() - 1;
			}
			world.setTime(h, m);
		}
		
		public void playSim()
		{
			world.setRealTimeToSimTime((Double.parseDouble(speedInvoer.getText())*60)/1000);
			
			//An explanation given for the used formule can be found in this class at line 269
			updateT.start();
			plays = true;
		}
		
		public void pauseSim()
		{
			world.setRealTimeToSimTime(0.0);
			updateT.stop();
			plays = false;
		}
		
		public void forwardSim()
		{
			world.setRealTimeToSimTime((90*60)/1000);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			
			Rectangle2D balk = new Rectangle2D.Double(0 , 0, 1175, 75);
			
			g2.draw(balk);
			
			int teller = 0;
			for(Rectangle2D image : rechthoek)
			{
				TexturePaint tp;
				if(teller == 5 && musicOn)
					tp = new TexturePaint(images.get(teller+1), image);
				else
					tp = new TexturePaint(images.get(teller), image);

				g2.setPaint(tp);
				g2.fill(plaatjes.get(teller));
				teller++;
			}
		}
		
		public void playMusic(String filename) {
			try {
				try {
					InputStream is = new BufferedInputStream(new FileInputStream(filename));
					player = new AdvancedPlayer(is, FactoryRegistry.systemRegistry().createAudioDevice());
				} catch (IOException e) {
					System.out.println("ERROR - Play music");
				} catch (JavaLayerException e) {
					System.out.println("ERROR - Play music");
				}
				Thread playerThread = new Thread() {
					public void run() {
						try {
							player.play(5000);
						} catch (JavaLayerException e) {
							System.out.println("ERROR - Play music");
						} finally {
							if(musicOn)
								playMusic("static_data\\music\\music.mp3");
						}
					}
				};
				playerThread.start();
			} catch (Exception ex) {
				System.out.println("ERROR - Play music");
			}

		}
	}
	
	class SimulatiePanel extends JPanel implements MouseMotionListener, MouseListener
	{
		Planner planner;
		
		int x = 0;
		int y = 0;
		
		int oldX = -1;
		int oldY = -1;
		
		float scale = 1;
		AffineTransform transform = new AffineTransform();
		
		
		
		public SimulatiePanel(Planner planner)
		{
			super(null);
			setPreferredSize(new Dimension(1200, 500));
			setBackground(Color.WHITE);
			this.planner = planner;
			setForeground(Color.BLACK);
			addMouseMotionListener(this);
			addMouseListener(this);
			addMouseWheelListener(new zoomMap());
			
			ActionListener simulate = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					repaint();
				}
			};
			
			Timer simulateTime = new Timer(1000/30, simulate);
			simulateTime.start(); 
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			AffineTransform oldtransform = g2d.getTransform();
			
			transform = new AffineTransform();
			transform.scale(scale, scale);
			transform.translate(x, y);

			world.inclusiveUpdate(g2d, transform, oldtransform);
		}

		public void mouseDragged(MouseEvent e) {

			x += (-1 * ((oldX - e.getX())) / scale);
			y += (-1 * ((oldY - e.getY())) / scale);
			oldX = e.getX();
			oldY = e.getY();
			repaint();
		}

		class zoomMap implements MouseWheelListener {
			float maxScale = 1.20f;
			float minScale = 0.50f;

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {

				double delta = -(0.05f * e.getPreciseWheelRotation()); 

				scale += delta;
				if (scale <= minScale) {
					scale = minScale;
				} else if (scale >= maxScale) {
					scale = maxScale;
				}
				repaint();
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			oldX = e.getX();
			oldY = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
		}
	}
	
	class TimePanel extends JPanel
	{
		Planner planner;
		JSlider timeSlider;
		
		int oldTime;
		
		public TimePanel(Planner planner)
		{
			super(null);
			SpringLayout layout = new SpringLayout();
			setLayout(layout);
			
			setPreferredSize(new Dimension(1200, 80));
			setBackground(Color.WHITE);
			this.planner = planner;
			
			JSlider slider = new JSlider(JSlider.HORIZONTAL, 9, 24, 9);
			
			slider.setBackground(Color.WHITE);
		    slider.setMinorTickSpacing(1);
		    slider.setMajorTickSpacing(1);
		    slider.setPaintTicks(true);
		    slider.setPaintLabels(true);
		    slider.setLabelTable(slider.createStandardLabels(1));
		    slider.setPreferredSize(new Dimension(1100,50));

		    add(slider);
		    
		    slider.setFont(font);
		    
		    ActionListener update = new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if(slider.getValue() != oldTime)
					{
						oldTime = slider.getValue();
						
						uren = oldTime;
						minuten = 0;
						if(uren != 24)
						{
							world.setTime(uren, minuten);
						}
					}
					
					if(uren < 9)
					{
						slider.setValue(24);
					}
					else
					{
						slider.setValue(uren);
					}
				}
				
			};
			
			Timer updateTime = new Timer(50, update);
			updateTime.start();
		    
		    layout.putConstraint(SpringLayout.WEST, slider, 25, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, slider, 25, SpringLayout.NORTH, this);
		}
		
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			
			Rectangle2D balk = new Rectangle2D.Double(0 , 0, 1175, 80);
			
			g2.draw(balk);
		}
	}

}
