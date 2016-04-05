package agenda;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

class StageLinker extends JFrame
{
	private JPanel linker;
	private JButton linkButton = new JButton("Save link");
	private JButton closeButton = new JButton("Close");
	private Agenda agenda;
	private Stage stage;
	protected HashMap map = new HashMap();
	
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

		JComboBox<Stage> selectStage = new JComboBox<Stage>();
		for (Stage stage : agenda.getStages())
			selectStage.addItem(stage);
		agendaStages.add(selectStage);
		
		JPanel jsonStages = new JPanel();
		JComboBox<Integer> selectLayers = new JComboBox<Integer>();
		for(int i = 6; i <= 27; i = i+3)
			selectLayers.addItem(i);
		jsonStages.add(selectLayers);
		
		linkButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				int key = (int)(selectLayers.getSelectedItem());
				Stage value = (Stage)(selectStage.getSelectedItem());
				map.put(key, value);
				System.out.println(map.entrySet());
				selectStage.removeItem(selectStage.getSelectedItem());
				selectLayers.removeItem(selectLayers.getSelectedItem());

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
	protected Map getLinkMap(){
		return map;
	}
}
