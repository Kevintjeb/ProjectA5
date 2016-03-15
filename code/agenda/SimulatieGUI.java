package agenda;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class SimulatieGUI extends JPanel
{
	Planner planner;
	ArrayList<File> maps = new ArrayList<File>();
	ArrayList<ImageIcon> images = new ArrayList<ImageIcon>();
	SimulatieGUI _this = this;
	
	public SimulatieGUI(Planner planner)
	{
		super(new BorderLayout());
		this.planner = planner;
		
		JPanel LabelPanel = new LabelPanel(planner);
		JPanel ChooseMapPanel = new ChooseMapPanel(planner);
		
		add(LabelPanel, BorderLayout.NORTH);
		add(ChooseMapPanel, BorderLayout.CENTER);
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
		
		int aantal = 0;
		
		public ChooseMapPanel(Planner planner)
		{
			super(null);
			setPreferredSize(new Dimension(1200,400));
			this.planner = planner;
			SpringLayout layout = new SpringLayout();
			setLayout(layout);
			
			File mapmap = new File("maps");
			fillArrayList(mapmap);
			fillImages(mapmap);
			
			
			
			for(File file : maps)
			{
				JButton b = new JButton(images.get(aantal));
				b.addActionListener(new ButtonListener(aantal));
				
				add(b);
				
				layout.putConstraint(SpringLayout.WEST, b, 300 + (aantal*150), SpringLayout.WEST, this);
				layout.putConstraint(SpringLayout.NORTH, b, 150, SpringLayout.NORTH, this);
				
				aantal++;
			}
		}
		
		public void fillImages(File file)
		{
			int teller = 1;
			if(file.exists())
			{
				if (file.isDirectory()) 
			    {
			      File[] files = file.listFiles(); // All files and subdirectories
			      for (int i = 0; files != null && i < files.length; i++) 
			      {
			    	  if(getFileExtension(files[i]).equals("jpg"))
			    	  {
			    		ImageIcon image = new ImageIcon("maps" + "\\" + teller + ".jpg");
			    		images.add(image);
			    		teller++;
			    	  }
			      }
			    }
			}
			else
			{
				System.out.println("Map bestaat niet");
			}
		}
		
		public void fillArrayList(File file)
		{
			if(file.exists())
			{
				if (file.isDirectory()) 
			    {
			      File[] files = file.listFiles(); // All files and subdirectories
			      for (int i = 0; files != null && i < files.length; i++) 
			      {
			    	  if(getFileExtension(files[i]).equals("json"))
			    	  {
			    		maps.add(files[i]);  
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
	}
	
	class ButtonListener implements ActionListener
	{
		int number;
		public ButtonListener(int number)
		{
			this.number = number;
		}

		@Override
		public void actionPerformed(ActionEvent arg0)
		{
			planner.tabbedPane.removeTabAt(2);
			planner.tabbedPane.addTab("Simulatie", new Simulator(maps.get(number), planner));
			planner.repaint();
	        planner.revalidate();
		}
	}
}
