import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class RemoveOption extends JPanel
{
	private static final long serialVersionUID = 1L;
	Planner planner;
	Artist selectedArtist;
	Performance selectedPerformance;
	Stage selectedStage;
			
	public RemoveOption(Planner planner) 
	{
		setLayout(null);
			
		JComboBox<Artist> comboBox = new JComboBox<Artist>();	
		comboBox.setBounds(90, 45, 286, 32);
		add(comboBox);	
		
		JComboBox<Performance> comboBox1 = new JComboBox<Performance>(); //TODO
		comboBox1.setBounds(90, 174, 286, 32);
		add(comboBox1);
		
		if(planner.agenda.getArtist().size() > 0)
		{
			selectedArtist = planner.agenda.getArtist().get(0);
		}
		
		for (Artist artist : planner.agenda.getArtist())
		{
			comboBox.addItem(artist);
			comboBox.addItemListener(new ItemListener()
			{

				@Override
				public void itemStateChanged(ItemEvent arg0) {
				selectedArtist = (Artist) comboBox.getSelectedItem();	
			}
					
		}										
		);};		
					
		JButton deleteArtist = new JButton("Delete Artist");
		deleteArtist.setToolTipText("Deletes the selected Artist");
		deleteArtist.setBounds(90, 98, 286, 35);
		add(deleteArtist);
			
		    deleteArtist.addActionListener(new ActionListener() 
		    {
		        public void actionPerformed(ActionEvent e) {
		        	if(selectedArtist == null)
		        		return;
		        	
                	ArrayList<Performance> performances = planner.agenda.getArtistsPerformances(selectedArtist);
                	
                	String endMessage;
                	
                    if (performances.size() != 0)
                    {
                    	if (JOptionPane.showConfirmDialog(null, "Verwijder alle optredens van deze artiest?") == JOptionPane.YES_OPTION)
                    	{
                    		endMessage = "Artist and performances deleted";
                    		planner.agenda.getPerformances().removeAll(performances);
                    	}
                    	else
                    	{
                    		endMessage = "Performances not Deleted";
                    		for (Performance performance : performances)
                    			performance.getArtists().remove(selectedArtist);
                    	}
                    }
                    else
                    	endMessage = "Artist deleted";
                    
                    planner.agenda.getArtist().remove(selectedArtist);
                  
                    
                    JOptionPane.showMessageDialog(null, endMessage);
                    comboBox.removeAllItems();
                    for (int t = 0; t < planner.agenda.getArtist().size(); t++) 
                    {
                        Artist p = planner.agenda.getArtist().get(t);
                        comboBox.addItem(p);
                    }
                    
                    comboBox1.removeAllItems();
                    for (int t = 0; t < planner.agenda.getPerformances().size(); t++) 
                    {
                    	
                    	Performance p = planner.agenda.getPerformances().get(t);
                        comboBox1.addItem(p);
                    }
                }
		        
		    });
			
		
		
		if(planner.agenda.getPerformances().size() > 0)
		{
			selectedPerformance = planner.agenda.getPerformances().get(0);
		}
		
		for (Performance performance : planner.agenda.getPerformances())
		{
			comboBox1.addItem(performance);
			comboBox1.addItemListener(new ItemListener()
			{

				@Override
				public void itemStateChanged(ItemEvent arg0) {
				selectedPerformance = (Performance) comboBox1.getSelectedItem();
				
			}
					
		}										
		);};
				
		JButton deletePerformance = new JButton("Delete Performance"); //TODO
		deletePerformance.setToolTipText("Deletes the selected Performance");			
		deletePerformance.setBounds(90, 227, 286, 35);
		add(deletePerformance);
		
		deletePerformance.addActionListener(new ActionListener() 
	    {
	        public void actionPerformed(ActionEvent e) {
	        	for (int p = 0; p < planner.agenda.getPerformances().size(); p++) 
	            {
            		if (planner.agenda.getPerformances().get(p) == selectedPerformance)
            		{
            			planner.agenda.getPerformances().remove(p);
            		}
	            }
	        	
	        	JOptionPane.showMessageDialog(null, "Performance Deleted");
                comboBox1.removeAllItems();
                for (int t = 0; t < planner.agenda.getPerformances().size(); t++) 
                {
                	
                	Performance p = planner.agenda.getPerformances().get(t);
                    comboBox1.addItem(p);
                }
	        }
	    });
		
		JComboBox<Stage> comboBox2 = new JComboBox<Stage>();			
		comboBox2.setBounds(90, 306, 286, 32);
		add(comboBox2);
		
		if(planner.agenda.getStages().size() > 0)
		{
			selectedStage = planner.agenda.getStages().get(0);
		}
	
		for (Stage stage : planner.agenda.getStages())
		{
			comboBox2.addItem(stage);
			comboBox2.addItemListener(new ItemListener()
			{

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					selectedStage = (Stage) comboBox2.getSelectedItem();					
			}					
		}										
		);};	
			
		JButton deleteStage = new JButton("Delete Stage");
		deleteStage.setToolTipText("Deletes the selected Stage");
		deleteStage.setBounds(90, 359, 286, 35);
		add(deleteStage);
		
	    deleteStage.addActionListener(new ActionListener() 
	    {
	        public void actionPerformed(ActionEvent e) {
	            for (int i = 0; i < planner.agenda.getStages().size(); i++) 
	            {
	                if (planner.agenda.getStages().get(i).getName() == selectedStage.getName()) 
	                {
	                	planner.agenda.getPerformances().removeAll(planner.agenda.getStagesPerformances(selectedStage));
	                    planner.agenda.getStages().remove(i);
	                    JOptionPane.showMessageDialog(null, "Stage Deleted");
	                    comboBox2.removeAllItems();
	                    for (int t = 0; t < planner.agenda.getStages().size(); t++) 
	                    {
	                        Stage p = planner.agenda.getStages().get(t);
	                        comboBox2.addItem(p);
	                    }
	                }
	            }
	        }
	    });
			
	}
}



