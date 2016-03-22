package agenda;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Linking extends JFrame
{
	Planner planner;
	Font font = new Font("SANS_SERIF", Font.PLAIN, 14);
	public Linking(Planner planner)
	{
		super("Linking");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.planner = planner;
		
		JPanel pane = new LinkingPanel(planner);
		pane.setFont(font);
		
		setBackground(Color.WHITE);
		setContentPane(pane);
		setVisible(true);
		setResizable(false);
		setSize(400, 550);
		setLocationRelativeTo(null);
	}
	
	class LinkingPanel extends JPanel
	{
		
		ArrayList<Rectangle2D> stages = new ArrayList<Rectangle2D>();
		ArrayList<Rectangle2D> numbers = new ArrayList<Rectangle2D>();
		
		Rectangle2D rectSelected;
		Rectangle2D rectNumSelected;
		
		Stage selectedStage;
		int selectedNumber;
		
		Rectangle2D applyButton;
		
		boolean drawLine = false;
		
		HashMap<Stage, Integer> map = new HashMap<Stage, Integer>();
		
		Planner planner;
		
		LinkingPanel L_this = this;
		
		public LinkingPanel(Planner planner)
		{
			super(null);
			this.planner = planner;
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
			
			for(int i = 6; i <= 27; i = i+3)
			{
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
			
			g2d.setColor(new Color(121, 121, 121));
			applyButton = new Rectangle2D.Double(150, 450, 75, 50);
			g2d.fill(applyButton);
			
			int stringLen = (int) g2d.getFontMetrics().getStringBounds("APPLY", g2d).getWidth();
			int stringHei = (int) g2d.getFontMetrics().getStringBounds("APPLY", g2d).getHeight();
			
			int xPos = (int) 75 / 2 - stringLen / 2;
			int yPos = (int) 50 / 2 + stringHei / 4;
			
			g2d.setColor(Color.WHITE);
			g2d.drawString("APPLY", xPos + 150, (yPos + 450));
			
			if(drawLine)
			{
				int lx = (int) rectSelected.getX() + 100;
				int ly = (int) rectSelected.getY() + 25;
				int rx = (int) rectNumSelected.getX();
				int ry = (int) rectNumSelected.getY() + 25;
				
				g2d.setColor(Color.BLACK);
				g2d.setStroke(new BasicStroke(5));
				g2d.drawLine(lx, ly, rx, ry);
				
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
						}
					}
					
					for(int i=0; i<numbers.size(); i++)
					{
						if(numbers.get(i).contains(e.getPoint()))
						{
							selectedNumber = 6 + (i*3);
							rectNumSelected = numbers.get(i);
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
						L_this.repaint();
					}
				}
			});
		}
		
		public Map getLinkMap()
		{
			return map;
		}
	}
}
