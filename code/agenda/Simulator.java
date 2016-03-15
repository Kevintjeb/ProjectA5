package agenda;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.TexturePaint;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class Simulator extends JPanel
{
	File json;
	Planner planner;
	public Simulator(File json, Planner planner)
	{
		super(new BorderLayout());
		
		this.json = json;
		this.planner = planner;
		
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
		JLabel speed1 = new JLabel("Speed ");
		JLabel speed2 = new JLabel(" min/sec");
		JLabel time = new JLabel("Time 00:00");
		JTextField speedInvoer = new JTextField(2);
		
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
			
			layout.putConstraint(SpringLayout.WEST, speed1, 200, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, speed1, 25, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, speedInvoer, 40, SpringLayout.WEST, speed1);
			layout.putConstraint(SpringLayout.NORTH, speedInvoer, 25, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, speed2, 30, SpringLayout.WEST, speedInvoer);
			layout.putConstraint(SpringLayout.NORTH, speed2, 25, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, time, 900, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, time, 25, SpringLayout.NORTH, this);
			
			File mapmap = new File("maps");
			fillArrayList(mapmap);
			fillPlaatjes();
			
			
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
	
	class SimulatiePanel extends JPanel
	{
		Planner planner;
		
		public SimulatiePanel(Planner planner)
		{
			super(null);
			setPreferredSize(new Dimension(1200, 500));
			this.planner = planner;
			
			//TODO simulatie 
		}
	}
	
	class TimePanel extends JPanel
	{
		Planner planner;
		JSlider timeSlider;
		
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
