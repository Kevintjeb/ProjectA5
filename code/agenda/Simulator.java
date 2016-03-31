package agenda;

import java.awt.BorderLayout;
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
import java.io.File;
import java.io.IOException;
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

import simulator.World;

public class Simulator extends JPanel
{
	File json;
	Planner planner;
	Font font = new Font("SANS_SERIF", Font.PLAIN, 14);
	Font timeFont = new Font("SANS_SERIF", Font.BOLD, 36);
	
	Map<agenda.Stage, Integer> stageMap = new HashMap<>();
	
	World world;
	
	public Simulator(File json, Planner planner)//, Map<agenda.Stage, Integer> stageMap)
	{
		super(new BorderLayout());
		
		this.json = json;
		this.planner = planner;
		//this.stageMap = stageMap;
		
		world = new World(planner.agenda, stageMap, json.getAbsolutePath());
		
		add(new ButtonPanel(planner), BorderLayout.NORTH);
		add(new SimulatiePanel(planner), BorderLayout.CENTER);
		add(new TimePanel(planner), BorderLayout.SOUTH);
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
		JTextField speedInvoer = new JTextField("0.0", 2);
		JLabel visitors = new JLabel("Bezoekers:");
		JTextField visitorsField = new JTextField("0", 2);
		JLabel visitors2 = new JLabel(" aantal/min");
		
		double oldSpeed = 0.0;
		int oldVisitors = 0;
		
		public ButtonPanel(Planner planner)
		{
			super(null);
			setPreferredSize(new Dimension(1200, 80));
			SpringLayout layout = new SpringLayout();
			setLayout(layout);
			
			this.planner = planner;
			
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
			
			layout.putConstraint(SpringLayout.WEST, speed2, 25, SpringLayout.WEST, speedInvoer);
			layout.putConstraint(SpringLayout.NORTH, speed2, 25, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, time, 900, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, time, 15, SpringLayout.NORTH, this);
			
			File mapmap = new File("maps");
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
			    			  File chooseImage = new File("maps" + "\\" + teller + "-" + ".png");
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
			Rectangle2D power2D = new Rectangle2D.Double(1100 , 10, 50, 50);
			
			Area refresh = new Area(refresh2D);
			Area back = new Area(back2D);
			Area play = new Area(play2D);
			Area pause = new Area(pause2D);
			Area forward = new Area(forward2D);
			Area power = new Area(power2D);
			
			rechthoek.add(refresh2D);
			rechthoek.add(back2D);
			rechthoek.add(play2D);
			rechthoek.add(pause2D);
			rechthoek.add(forward2D);
			rechthoek.add(power2D);
			
			plaatjes.add(refresh);
			plaatjes.add(back);
			plaatjes.add(play);
			plaatjes.add(pause);
			plaatjes.add(forward);
			plaatjes.add(power);
			
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
								System.exit(0);
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
						System.out.println("wijzig snelheid");
					}
					if(newVisitors != oldVisitors)
					{
						System.out.println("wijzig bezoekers");
					}
					
					oldSpeed = speed;
					oldVisitors = newVisitors;
				}
			};
			
			Timer updateTime = new Timer(50, update);
			updateTime.start();
		}
		
		public void refreshSim()
		{
			
		}
		
		public void backSim()
		{
			
		}
		
		public void playSim()
		{
			
		}
		
		public void pauseSim()
		{
			
		}
		
		public void forwardSim()
		{
			
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
				TexturePaint tp = new TexturePaint(images.get(teller), image);
				g2.setPaint(tp);
				g2.fill(plaatjes.get(teller));
				teller++;
			}
		}
	}
	
	class SimulatiePanel extends JPanel implements MouseMotionListener, MouseListener
	{
		Planner planner;
		
		int x = 0;
		int y = 0;
		
		float scale = 1;
		private BufferedImage mapimage;
		AffineTransform transform = new AffineTransform();
		
		public SimulatiePanel(Planner planner)
		{
			super(null);
			setPreferredSize(new Dimension(1200, 500));
			this.planner = planner;
			
			addMouseMotionListener(this);
			addMouseListener(this);
			addMouseWheelListener(new zoomMap());
			//TODO simulatie 
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;

			AffineTransform oldtransform = g2d.getTransform();
			transform = new AffineTransform();
			transform.scale(scale, scale);
			transform.translate(x, y);

			g2d.setTransform(transform);

			g2d.drawImage(mapimage, new AffineTransform(), this);

			g2d.setTransform(oldtransform);
		}

		int oldX = -1;
		int oldY = -1;

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
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent e) {
			oldX = e.getX();
			oldY = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub

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
			this.planner = planner;
			
			JSlider slider = new JSlider(JSlider.HORIZONTAL, 9, 24, 9);

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
						
					}
					
					oldTime = slider.getValue();
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
