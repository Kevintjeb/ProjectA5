package agenda;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class AddGUI extends JPanel
{
	Planner planner;

	JButton okeButton = new JButton("OK");
	JButton cancelButton = new JButton("Cancel");
	
	public AddGUI(Planner planner)
	{
		super(new BorderLayout());
		this.planner = planner;
		
		JTabbedPane tabbedPane = new JTabbedPane();
		add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane.addTab("Artiest", new AddArtist(true));
		tabbedPane.addTab("Podium", new AddStage(false));
		tabbedPane.addTab("Optreden", new AddPerformance(false));
		
		add(okeButton, BorderLayout.SOUTH);
		
		ChangeListener changeListner = new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent arg0) {
				JTabbedPane source = (JTabbedPane)arg0.getSource();
				
				switch (source.getTitleAt(source.getSelectedIndex()))
				{
				case "Artiest":
					AddArtist a = (AddArtist)source.getSelectedComponent();
					if (a != null)
						a.init(true);
					break;
				case "Podium":
					AddStage s = (AddStage)source.getSelectedComponent();
					if (s != null)
						s.init(true);
					break;
				case "Optreden":
					AddPerformance p = (AddPerformance)source.getSelectedComponent();
					if (p != null)
						p.init(true);
					break;
				default:
					//System.out.println("case is not supported");
				}
			}
			
		};
		
		tabbedPane.addChangeListener(changeListner);
	}
	
	
	private void clearOkeButton()
	{
		for(ActionListener a : okeButton.getActionListeners())
		{
			okeButton.removeActionListener(a);
		}
	}
	
	class AddArtist extends JPanel
	{
		JLabel nameLabel = new JLabel("Artiestnaam");
		JLabel genreLabel = new JLabel("Genre");
		JLabel notesLabel = new JLabel("Notes");
		JTextField nameField = new JTextField(10);
		JTextField genreField = new JTextField(10);
		JTextField notesField = new JTextField(10);
		AddArtist _this = this;
		
		public void init(boolean controlsOkButton)
		{
			nameField.setText("");
			genreField.setText("");
			notesField.setText("");
			
			if (controlsOkButton)
			{
				clearOkeButton();
				okeButton.addActionListener(new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						try
						{
							Artist artist = new Artist(nameField.getText(), null, genreField.getText(),notesField.getText(), planner.agenda);
							planner.agenda.getArtist().add(artist);
							JOptionPane.showMessageDialog(_this, "De artiest is toegevoegd");
							init(true);
						}
						catch(Exception exc)
						{
							JOptionPane.showMessageDialog(_this, exc.getMessage());
						}
					}
				});
			}
		}
		
		public AddArtist(boolean controlsOkButton)
		{
			super(null);
			SpringLayout artistLayout = new SpringLayout();
			
			setLayout(artistLayout);
					
			add(nameLabel);
			add(nameField);
			add(genreLabel);
			add(genreField);
			add(notesLabel);
			add(notesField);
			
			artistLayout.putConstraint(SpringLayout.WEST, nameLabel, 10, SpringLayout.WEST, this);
			artistLayout.putConstraint(SpringLayout.NORTH, nameLabel, 25, SpringLayout.NORTH, this);
			artistLayout.putConstraint(SpringLayout.NORTH, nameField, 25, SpringLayout.NORTH, this);
			artistLayout.putConstraint(SpringLayout.WEST, nameField, 75, SpringLayout.EAST, nameLabel);
			
			artistLayout.putConstraint(SpringLayout.WEST, genreLabel, 10, SpringLayout.WEST, this);
			artistLayout.putConstraint(SpringLayout.NORTH, genreLabel, 100, SpringLayout.NORTH, this);
			artistLayout.putConstraint(SpringLayout.NORTH, genreField, 100, SpringLayout.NORTH, this);
			artistLayout.putConstraint(SpringLayout.WEST, genreField, 75, SpringLayout.EAST, nameLabel);
			
			artistLayout.putConstraint(SpringLayout.WEST, notesLabel, 10, SpringLayout.WEST, this);
			artistLayout.putConstraint(SpringLayout.NORTH, notesLabel, 175, SpringLayout.NORTH, this);
			artistLayout.putConstraint(SpringLayout.NORTH, notesField, 175, SpringLayout.NORTH, this);
			artistLayout.putConstraint(SpringLayout.WEST, notesField, 75, SpringLayout.EAST, nameLabel);
			
			init(controlsOkButton);
		}
		
		
	}
	
	class AddStage extends JPanel
	{
		JLabel stageNameLabel = new JLabel("Stagenaam");
		JTextField stageNameField = new JTextField(10);
		AddStage _this = this;
		
		public void init(boolean controlsOkButton)
		{
			stageNameField.setText("");
			if (controlsOkButton)
			{
				clearOkeButton();
				okeButton.addActionListener(new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
						try
						{
						Stage stage = new Stage(stageNameField.getText(), planner.agenda);
						planner.agenda.getStages().add(stage);
						JOptionPane.showMessageDialog(_this, "Het podium is toegevoegd");
						init(true);
						}
						catch(Exception exc)
						{
							JOptionPane.showMessageDialog(_this, exc.getMessage());
						}
					}
				});
			}
		}
		
		public AddStage(boolean controlsOkButton)
		{
			super(null);
			SpringLayout stageLayout = new SpringLayout();
			
			setLayout(stageLayout);
					
			add(stageNameLabel);
			add(stageNameField);
			
			stageLayout.putConstraint(SpringLayout.WEST, stageNameLabel, 10, SpringLayout.WEST, this);
			stageLayout.putConstraint(SpringLayout.NORTH, stageNameLabel, 25, SpringLayout.NORTH, this);
			stageLayout.putConstraint(SpringLayout.NORTH, stageNameField, 25, SpringLayout.NORTH, this);
			stageLayout.putConstraint(SpringLayout.WEST, stageNameField, 75, SpringLayout.EAST, stageNameLabel);		
			
			init(controlsOkButton);
		}
	}
	
	class AddPerformance extends JPanel
	{
		JLabel chooseStage = new JLabel("Podium");
		JLabel chooseArtist = new JLabel("Artiest");
		JLabel startTimeLabel = new JLabel("Starttijd");
		JLabel endTimeLabel = new JLabel("Eindtijd");
		JLabel popularityLabel = new JLabel("Populariteit");
		JTextField startTimeField = new JTextField(10);
		JTextField endTimeField = new JTextField(10);
		JTextField popularityField = new JTextField(10);
		JComboBox<Stage> chooseStageBox = new JComboBox<>();
		JComboBox<Artist> chooseArtistBox = new JComboBox<>();
		AddPerformance _this = this;
		Stage selectedStage;
		Artist selectedArtist;
		
		public void init(boolean controlsOkButton)
		{
			chooseStageBox.removeAllItems();
			
			for(Stage stage : planner.agenda.getStages())
			{
				chooseStageBox.addItem(stage);
				chooseStageBox.addItemListener(new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent e)
					{
						selectedStage = (Stage) chooseStageBox.getSelectedItem();
					}
				});
			}
			
			
			if(planner.agenda.getStages().size() > 0)
			{
				selectedStage = planner.agenda.getStages().get(0);
			}
			
			chooseArtistBox.removeAllItems();
			
			for(Artist artist : planner.agenda.getArtist())
			{
				chooseArtistBox.addItem(artist);
				chooseArtistBox.addItemListener(new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent e)
					{
							selectedArtist = (Artist) chooseArtistBox.getSelectedItem();
					}
				});
			}
			
			
			if(planner.agenda.getArtist().size() > 0)
			{
				selectedArtist = planner.agenda.getArtist().get(0);
			}
			
			startTimeField.setText("");
			endTimeField.setText("");
			popularityField.setText("");
			
			if (controlsOkButton)
			{
				clearOkeButton();
				okeButton.addActionListener(new ActionListener() 
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{	
						try
						{
						Time selectedStartTime = Time.timeFromString(startTimeField.getText());
						Time selectedEndTime = Time.timeFromString(endTimeField.getText());
						int selectedPopularity = Integer.parseInt(popularityField.getText());
						
						//System.out.println("selected stage: " + selectedStage);
						Performance performance = new Performance(selectedStage, selectedArtist, selectedStartTime, selectedEndTime, selectedPopularity, planner.agenda);
						
						planner.agenda.getPerformances().add(performance);
						JOptionPane.showMessageDialog(_this, "Het optreden is toegevoegd");
						init(true);
						}
						catch(Exception exc)
						{
							JOptionPane.showMessageDialog(_this, exc.getMessage());
						}		
					}
				});
			}
		}
		
		public AddPerformance(boolean controlsOkButton)
		{
			super(null);
			SpringLayout performanceLayout = new SpringLayout();
			
			setLayout(performanceLayout);
					
			add(chooseStage);
			add(chooseStageBox);
			add(chooseArtist);
			add(chooseArtistBox);
			add(startTimeLabel);
			add(startTimeField);
			add(endTimeLabel);
			add(endTimeField);
			add(popularityLabel);
			add(popularityField);
			
			performanceLayout.putConstraint(SpringLayout.WEST, chooseStage, 10, SpringLayout.WEST, this);
			performanceLayout.putConstraint(SpringLayout.NORTH, chooseStage, 25, SpringLayout.NORTH, this);
			performanceLayout.putConstraint(SpringLayout.NORTH, chooseStageBox, 25, SpringLayout.NORTH, this);
			performanceLayout.putConstraint(SpringLayout.WEST, chooseStageBox, 75, SpringLayout.EAST, chooseStage);
			
			performanceLayout.putConstraint(SpringLayout.WEST, chooseArtist, 10, SpringLayout.WEST, this);
			performanceLayout.putConstraint(SpringLayout.NORTH, chooseArtist, 100, SpringLayout.NORTH, this);
			performanceLayout.putConstraint(SpringLayout.NORTH, chooseArtistBox, 100, SpringLayout.NORTH, this);
			performanceLayout.putConstraint(SpringLayout.WEST, chooseArtistBox, 75, SpringLayout.EAST, chooseStage);
			
			performanceLayout.putConstraint(SpringLayout.WEST, startTimeLabel, 10, SpringLayout.WEST, this);
			performanceLayout.putConstraint(SpringLayout.NORTH, startTimeLabel, 175, SpringLayout.NORTH, this);
			performanceLayout.putConstraint(SpringLayout.NORTH, startTimeField, 175, SpringLayout.NORTH, this);
			performanceLayout.putConstraint(SpringLayout.WEST, startTimeField, 75, SpringLayout.EAST, chooseStage);
			
			performanceLayout.putConstraint(SpringLayout.WEST, endTimeLabel, 10, SpringLayout.WEST, this);
			performanceLayout.putConstraint(SpringLayout.NORTH, endTimeLabel, 250, SpringLayout.NORTH, this);
			performanceLayout.putConstraint(SpringLayout.NORTH, endTimeField, 250, SpringLayout.NORTH, this);
			performanceLayout.putConstraint(SpringLayout.WEST, endTimeField, 75, SpringLayout.EAST, chooseStage);
			
			performanceLayout.putConstraint(SpringLayout.WEST, popularityLabel, 10, SpringLayout.WEST, this);
			performanceLayout.putConstraint(SpringLayout.NORTH, popularityLabel, 325, SpringLayout.NORTH, this);
			performanceLayout.putConstraint(SpringLayout.NORTH, popularityField, 325, SpringLayout.NORTH, this);
			performanceLayout.putConstraint(SpringLayout.WEST, popularityField, 75, SpringLayout.EAST, chooseStage);
			
			
			
			init(controlsOkButton);
		}
	}
}
