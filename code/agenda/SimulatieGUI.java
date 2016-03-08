package agenda;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class SimulatieGUI extends JPanel
{
	Planner planner;
	
	public SimulatieGUI(Planner planner)
	{
		super(new BorderLayout());
		this.planner = planner;
		
		JPanel LabelPanel = new LabelPanel(planner);
		JPanel ChooseMapPanel = new ChooseMapPanel(planner);
		JPanel ButtonPanel = new ButtonPanel(planner);
		
		add(LabelPanel, BorderLayout.NORTH);
		add(ChooseMapPanel, BorderLayout.CENTER);
		add(ButtonPanel, BorderLayout.SOUTH);
	}
	class LabelPanel extends JPanel
	{
		Planner planner;
		JLabel chooseMap = new JLabel("Kies een festivalmap");
		
		public LabelPanel(Planner planner)
		{
			super(null);
			this.planner = planner;
			setPreferredSize(new Dimension(1200,150));
			SpringLayout layout = new SpringLayout();
			setLayout(layout);
			
			add(chooseMap);
			
			layout.putConstraint(SpringLayout.WEST, chooseMap, 490, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, chooseMap, 75, SpringLayout.NORTH, this);
		}
	}
	
	class ChooseMapPanel extends JPanel
	{
		Planner planner;
		
		ImageIcon image1 = new ImageIcon("");//TODO image
		ImageIcon image2 = new ImageIcon("");//TODO image
		ImageIcon image3 = new ImageIcon("");//TODO image
		ImageIcon image4 = new ImageIcon("");//TODO image
		ImageIcon image5 = new ImageIcon("");//TODO image
		JButton map1= new JButton(image1);
		JButton map2= new JButton(image2);
		JButton map3= new JButton(image3);
		JButton map4= new JButton(image4);
		JButton map5= new JButton(image5);
		
		
		public ChooseMapPanel(Planner planner)
		{
			super(null);
			setPreferredSize(new Dimension(1200,400));
			this.planner = planner;
			SpringLayout layout = new SpringLayout();
			setLayout(layout);
			
			add(map1);
			add(map2);
			add(map3);
			add(map4);
			add(map5);
			
			layout.putConstraint(SpringLayout.WEST, map1, 450, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, map1, 175, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, map2, 50, SpringLayout.WEST, map1);
			layout.putConstraint(SpringLayout.NORTH, map2, 175, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, map3, 50, SpringLayout.WEST, map2);
			layout.putConstraint(SpringLayout.NORTH, map3, 175, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, map4, 50, SpringLayout.WEST, map3);
			layout.putConstraint(SpringLayout.NORTH, map4, 175, SpringLayout.NORTH, this);
			
			layout.putConstraint(SpringLayout.WEST, map5, 50, SpringLayout.WEST, map4);
			layout.putConstraint(SpringLayout.NORTH, map5, 175, SpringLayout.NORTH, this);
		}
	}
	
	class ButtonPanel extends JPanel
	{
		Planner planner;
		JButton okButton = new JButton("OK");
		
		public ButtonPanel(Planner planner)
		{
			super(null);
			this.planner = planner;
			setPreferredSize(new Dimension(1200,150));
			SpringLayout layout = new SpringLayout();
			setLayout(layout);
			
			add(okButton);
			
			layout.putConstraint(SpringLayout.WEST, okButton, 550, SpringLayout.WEST, this);
			layout.putConstraint(SpringLayout.NORTH, okButton, 75, SpringLayout.NORTH, this);
		}
	}

}
