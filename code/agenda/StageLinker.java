package agenda;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import json.TileLayer;

class StageLinker extends JFrame
{
	private JPanel linker;
	private JButton linkButton = new JButton("Save links");
	private JButton closeButton = new JButton("Close");
	private Agenda agenda;
	private Stage stage;
	/*private Color blockColor = new Color(255, 255, 255, 128);
	private Color textColor = new Color(0, 0, 0);
	private Color lineColor = new Color(0, 0, 0);
	private Color backgroundColor = new Color(255, 0, 0, 255);
*/
	
	public StageLinker(Agenda agendaInput)
	{
		super("Stage Linking");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.agenda = agendaInput;
		
		linker = new JPanel(new BorderLayout());
		
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		
		//JPanel stagePainter = new JPanel();
		
		repaint();
		
		JPanel agendaStages = new JPanel();
		JComboBox<Stage> selectAgendaStage = new JComboBox<Stage>();
		for (Stage stage : agenda.getStages())
			selectAgendaStage.addItem(stage);
		agendaStages.add(selectAgendaStage);
		
		JPanel jsonStages = new JPanel();
		JComboBox<String> selectLayers = new JComboBox<String>();
		ArrayList<String> stages = new ArrayList<String>();
		for(int i = 0; i < 8; i++){
			stages.add("Stage" + (i + 1));
		}
		System.out.println(stages);
		for(String s : stages)
			selectLayers.addItem(s);
		jsonStages.add(selectLayers);
		
		linkButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				stage = (Stage)selectAgendaStage.getSelectedItem();
				System.out.println(stage);
				stage.setID((int)(Math.random()*100));
				System.out.println(stage.getID());
				//dispose();
			}
		});
		
		closeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		
		buttons.add(linkButton);
		buttons.add(closeButton);
		linker.add(buttons, BorderLayout.SOUTH);
		//linker.add(stagePainter, BorderLayout.WEST);
		linker.add(agendaStages, BorderLayout.WEST);
		linker.add(jsonStages, BorderLayout.EAST);
		
		setContentPane(linker);
		setVisible(true);
		setResizable(false);
		setSize(400, 550);
		setLocationRelativeTo(null);
	}
	
	/*public void paintComponent(Graphics g){
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D)g;
		for(int i = 0; i < agenda.stages.size(); i++){
			g2d.setColor(lineColor);
			g2d.drawRect(20, 10 + i*10, 50, 20);
			g2d.setColor(blockColor);
			g2d.fillRect(20, 10 + i*10, 50, 20);		
		}
	}*/
}