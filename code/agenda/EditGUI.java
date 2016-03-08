package agenda;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;


public class EditGUI extends JPanel
{
	private Agenda agenda;
	private Object selectedObject;
	private JPanel content;
	private JButton button;
	
	private JLabel[] labels;
	private Component[] components;
	private Object[] selectedObjects;
	
	private final JPanel _this = this;
	
	public EditGUI(Planner planner)
	{
		super(null);
		BorderLayout border = new BorderLayout();
		SpringLayout spring = new SpringLayout();
		
				
		setLayout(border);
		labels = new JLabel[5];
		components = new Component[5];
		selectedObjects = new Object[2];
		button = new JButton("Apply");
		
		this.agenda = planner.agenda;
		content = new JPanel();
		add(content, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
		
		JComboBox<Object> comboBox = new JComboBox<>();
		add(comboBox, BorderLayout.NORTH);
		
		for (Artist artist : agenda.getArtist())
			comboBox.addItem(artist);
		for (Stage stage : agenda.getStages())
			comboBox.addItem(stage);
		for (Performance performance : agenda.getPerformances())
			comboBox.addItem(performance);
		
		comboBox.addItemListener(new ItemListener()
		{

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				selectedObject = comboBox.getSelectedItem();
				updateContent();
			}
		}
		);
	}
	
	int i = 0;
	
	private void clearButton()
	{
		for (ActionListener actionListner : button.getActionListeners())
			button.removeActionListener(actionListner);
	}
	
	private void updateContent()
	{
		if (selectedObject == null)
			return;
		remove(content);
		remove(button);
		int n = 0;
		
		clearButton();
		
		if (selectedObject instanceof Artist)
		{
			Artist artist = (Artist)selectedObject;
			n = 3;
			labels[0] = new JLabel("Artiestnaam");
			labels[1] = new JLabel("Genre");
			labels[2] = new JLabel("Notes");
			
			JTextField naam = new JTextField();
			naam.setText(artist.getName());
			components[0] = naam;
			
			JTextField genre = new JTextField();
			genre.setText(artist.getGenre());
			components[1] = genre;
			
			JTextField notes = new JTextField();
			notes.setText(artist.getNotes());
			components[2] = notes;
			
			button.addActionListener(new ActionListener()
			{

				@Override
				public void actionPerformed(ActionEvent arg0) {
					try
					{
					artist.setName(naam.getText(), true);
					artist.setGenre(genre.getText(), true);
					artist.setNotes(notes.getText(), true);
					JOptionPane.showMessageDialog(_this, "De artiest is bijgewerkt");
					}
					catch(Exception e)
					{
						JOptionPane.showMessageDialog(_this, e.getMessage());
						e.printStackTrace();
					}
				}
				
			});
		}
		else if (selectedObject instanceof Stage)
		{
			Stage stage = (Stage)selectedObject;
			n = 1;
			labels[0] = new JLabel("Stagenaam");
			components[0] = new JTextField();
			((JTextField)components[0]).setText(stage.getName());
			
			button.addActionListener(new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e) {
					try
					{
						stage.setName(((JTextField)components[0]).getText());
						JOptionPane.showMessageDialog(_this, "Het podium is bijgewerkt");
					}
					
					catch(Exception e1)
					{
						JOptionPane.showMessageDialog(_this, e1.getMessage());
					}			
				}
				
			});
		}
		else if (selectedObject instanceof Performance)
		{
			Performance performance = (Performance)selectedObject;
			n = 5;
			labels[0] = new JLabel("Podium");
			labels[1] = new JLabel("Artiest");
			labels[2] = new JLabel("Starttijd");
			labels[3] = new JLabel("Eindtijd");
			labels[4] = new JLabel("Popularitijd");
			
			JComboBox<Stage> stageBox = new JComboBox<>();
			for (Stage stage : agenda.getStages())
				stageBox.addItem(stage);
			stageBox.setSelectedItem(performance.getStage());
			selectedObjects[0] = performance.getStage();
			stageBox.addItemListener(new ItemListener()
			{
				@Override
				public void itemStateChanged(ItemEvent arg0) {
					selectedObjects[0] = stageBox.getSelectedItem();
				}		
			});
			components[0] = stageBox;
			
			JComboBox<Artist> artistBox = new JComboBox<>();
			for (Artist artist : agenda.getArtist())
				artistBox.addItem(artist);
			artistBox.setSelectedItem(performance.getArtists().get(0));
			selectedObjects[1] = performance.getArtists().get(0);
			artistBox.addItemListener(new ItemListener()
			{
				@Override
				public void itemStateChanged(ItemEvent e) {
					selectedObjects[1] = artistBox.getSelectedItem();
				}				
			});
			components[1] = artistBox;
			
			JTextField startTijd = new JTextField();
			startTijd.setText(performance.getStartTime().toString());
			components[2] = startTijd;
			
			JTextField eindTijd = new JTextField();
			eindTijd.setText(performance.getEndTime().toString());
			components[3] = eindTijd;
			
			JTextField popularitijd = new JTextField();
			popularitijd.setText(performance.getPopularity() + "");
			components[4] = popularitijd;
			
			button.addActionListener(new ActionListener()
			{
				@SuppressWarnings("serial")
				@Override
				public void actionPerformed(ActionEvent e) {
					try
					{
						performance.setStage((Stage)selectedObjects[0], true);
						performance.setArtists(new ArrayList<Artist>() {{add((Artist)selectedObjects[1]);}}, true);
						performance.setStartTime(Time.timeFromString(startTijd.getText()), true);
						performance.setEndTime(Time.timeFromString(eindTijd.getText()), true);
						performance.setPopularity(Integer.parseInt(popularitijd.getText()));
						
						JOptionPane.showMessageDialog(_this, "Optreden is bijgewerkt");
						
					}
					catch (Exception e1)
					{
						JOptionPane.showMessageDialog(_this, e1.getMessage());
					}
				}
			});
		}
		
		if (n != 0)
		{
			content = new JPanel(new GridLayout(n, 2));
			for (int i = 0; i < n; i++)
			{
				if (labels[i] != null)
					content.add(labels[i]);
				else 
					content.add(new JLabel());
				
				if ( components[i] != null) 
					content.add(components[i]);
				else 
					content.add(new JButton("null"));
			}
		}
		
		add(content, BorderLayout.CENTER);
		add(button, BorderLayout.SOUTH);
		content.revalidate();
		content.invalidate();
		content.repaint();
	}
}
