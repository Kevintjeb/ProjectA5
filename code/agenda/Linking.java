package agenda;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

public class Linking extends JPanel
{
	Planner planner;
	Font font = new Font("SANS_SERIF", Font.PLAIN, 14);
	HashMap<Stage, Integer> map = new HashMap<Stage, Integer>();
		
	ArrayList<Rectangle2D> stages = new ArrayList<Rectangle2D>();
	ArrayList<Rectangle2D> numbers = new ArrayList<Rectangle2D>();
		
	Rectangle2D rectSelected;
	Rectangle2D rectNumSelected;
		
	Stage selectedStage;
	int selectedNumber;
		
	Rectangle2D applyButton;
	Rectangle2D diposeButton;
		
	boolean drawLine = false;
	boolean hasBeenClicked = false;
	
	File json;
		
		
		
	public Linking(Planner planner, File json)
	{
		super(null);
		this.planner = planner;
		this.json = json;
		setPreferredSize(new Dimension(400, 550));
		clicked();
	}
		
	public void paintComponent(Graphics g)
	{
		int y = 10;
		int y2 = 10;
		
		if(!stages.isEmpty())
		{
			stages.clear();
		}
		if(!numbers.isEmpty())
		{
			numbers.clear();
		}
		
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D) g;
		
		for(Stage stage : planner.agenda.getStages())
		{
			if(hasBeenClicked){
				g2d.setColor(new Color(25, 160, 95, 192));
				Rectangle2D rect = new Rectangle2D.Double(10, y, 100, 50);
				g2d.fill(rect);
			
				stages.add(rect);
				
				int stringLen = (int) g2d.getFontMetrics().getStringBounds(stage.getName(), g2d).getWidth();
				int stringHei = (int) g2d.getFontMetrics().getStringBounds(stage.getName(), g2d).getHeight();
			
				int xPos = (int) 100 / 2 - stringLen / 2;
				int yPos = (int) 50 / 2 + stringHei / 4;				
					
				g2d.setColor(Color.BLACK);
				g2d.drawString(stage.getName(), xPos + 10, (yPos + y));
			
				y += 60;
			}
			else{
				g2d.setColor(new Color(25, 160, 95, 128));
				Rectangle2D rect = new Rectangle2D.Double(10, y, 100, 50);
				g2d.fill(rect);
				
				stages.add(rect);
				
				int stringLen = (int) g2d.getFontMetrics().getStringBounds(stage.getName(), g2d).getWidth();
				int stringHei = (int) g2d.getFontMetrics().getStringBounds(stage.getName(), g2d).getHeight();
				
				int xPos = (int) 100 / 2 - stringLen / 2;
				int yPos = (int) 50 / 2 + stringHei / 4;
				
				g2d.setColor(Color.BLACK);
				g2d.drawString(stage.getName(), xPos + 10, (yPos + y));
				
				y += 60;
			}
		}
			
		for(int i = 6; i <= 27; i = i+3)
		{
			if(hasBeenClicked){
				g2d.setColor(new Color(225, 230, 150, 192));
				Rectangle2D rect = new Rectangle2D.Double(290, y2, 100, 50);
				g2d.fill(rect);
				
				numbers.add(rect);
			
				int stringLen = (int) g2d.getFontMetrics().getStringBounds(""+i, g2d).getWidth();
				int stringHei = (int) g2d.getFontMetrics().getStringBounds(""+i, g2d).getHeight();
				
				int xPos = (int) 100 / 2 - stringLen / 2;
				int yPos = (int) 50 / 2 + stringHei / 4;
			
				g2d.setColor(Color.BLACK);
				g2d.drawString(""+i, xPos + 290, (yPos + y2));
			
				y2 += 60;
			}
			else{
				g2d.setColor(new Color(225, 230, 150, 128));
				Rectangle2D rect = new Rectangle2D.Double(290, y2, 100, 50);
				g2d.fill(rect);
			
				numbers.add(rect);
				
				int stringLen = (int) g2d.getFontMetrics().getStringBounds(""+i, g2d).getWidth();
				int stringHei = (int) g2d.getFontMetrics().getStringBounds(""+i, g2d).getHeight();
			
				int xPos = (int) 100 / 2 - stringLen / 2;
				int yPos = (int) 50 / 2 + stringHei / 4;
			
				g2d.setColor(Color.BLACK);
				g2d.drawString(""+i, xPos + 290, (yPos + y2));
			
				y2 += 60;
			}
		}
		
		g2d.setColor(new Color(121, 121, 121));
		applyButton = new Rectangle2D.Double(80, 500, 110, 50);
		g2d.fill(applyButton);
		
		int stringLen = (int) g2d.getFontMetrics().getStringBounds("APPLY", g2d).getWidth();
		int stringHei = (int) g2d.getFontMetrics().getStringBounds("APPLY", g2d).getHeight();
		
		int xPos = (int) 75 / 2 - stringLen / 2;
		int yPos = (int) 50 / 2 + stringHei / 4;
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("APPLY", xPos + 100, (yPos + 500));
		//--------------------------------------------------------------------------
		g2d.setColor(new Color(121, 121, 121));
		diposeButton = new Rectangle2D.Double(210, 500, 110, 50);
		g2d.fill(diposeButton);
		
		int stringLenPotty = (int) g2d.getFontMetrics().getStringBounds("Close linker", g2d).getWidth();
		int stringHeiPotty = (int) g2d.getFontMetrics().getStringBounds("Close linker", g2d).getHeight();
		
		int xPosPotty = (int) 75 / 2 - stringLenPotty / 2;
		int yPosPotty = (int) 50 / 2 + stringHeiPotty / 4;
		
		g2d.setColor(Color.WHITE);
		g2d.drawString("Close linker", xPosPotty + 227, (yPosPotty + 500));
		
		if(drawLine)
		{
			int lx = (int) rectSelected.getX() + 100;
			int ly = (int) rectSelected.getY() + 25;
			int rx = (int) rectNumSelected.getX();
			int ry = (int) rectNumSelected.getY() + 25;
			
			g2d.setColor(Color.BLACK);
			g2d.setStroke(new BasicStroke(5));
			g2d.drawLine(lx, ly, rx, ry);
			drawLine = false;
			
		}
	}
	
	public void clicked()
	{
		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				for(int i =0; i<stages.size(); i++)
				{
					if(stages.get(i).contains(e.getPoint()))
					{
						selectedStage = planner.agenda.getStages().get(i);
						rectSelected = stages.get(i);
						toggleHasBeenClicked();
						repaint();
						toggleHasBeenClicked();
					}
				}
				
				for(int i=0; i<numbers.size(); i++)
				{
					if(numbers.get(i).contains(e.getPoint()))
					{
						selectedNumber = 6 + (i*3);
						rectNumSelected = numbers.get(i);
						toggleHasBeenClicked();
						repaint();
						toggleHasBeenClicked();
					}
				}
			}
		});
		
		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if(applyButton.contains(e.getPoint()))
				{
					map.put(selectedStage, selectedNumber);
					System.out.println(map.entrySet());
					
					drawLine = true;
					repaint();
				}
			}
		});
		
		addMouseListener(new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				if(diposeButton.contains(e.getPoint()))
				{
					planner.tabbedPane.removeTabAt(2);
					planner.tabbedPane.addTab("Simulatie", new Simulator(json, planner, map));//, linking.getLinkMap()));
					planner.repaint();
			        planner.revalidate();
				}
			}
		});
	}
		
	private void toggleHasBeenClicked(){
		hasBeenClicked = !hasBeenClicked;
	}
	
	public HashMap<Stage, Integer> getLinkMap()
	{
		return map;
	}
}
