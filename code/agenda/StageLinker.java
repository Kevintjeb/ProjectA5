package agenda;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

class StageLinker extends JFrame
{
	JPanel linker;
	JButton saveButton = new JButton("Set link");
	JButton closeButton = new JButton("Close");
	private Agenda agenda;
	
	
	public StageLinker(Planner planner)
	{
		super("Stage Linking");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		linker = new JPanel(new BorderLayout());
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		
		this.agenda = planner.agenda;
		
		JComboBox<Object> comboBox = new JComboBox<>();
		for (Stage stage : agenda.getStages())
			comboBox.addItem(stage);
		
		JTextField idField = new JTextField();
		
		//Basic framework, it's done for now.
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//Fixed the stage specific set problem. Now for the json link
				int num = Integer.parseInt(idField.getText());
				Stage tempStage = (Stage) comboBox.getSelectedItem();
				tempStage.setID(num);
				System.out.println(tempStage.getID());
				JOptionPane.showMessageDialog(linker, "Link set");
			}
		});
		
		closeButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				JOptionPane.showMessageDialog(linker, "Closed linking gui");
				dispose();
			}
		});
		
		buttons.add(saveButton);
		buttons.add(closeButton);
		linker.add(buttons, BorderLayout.SOUTH);
		linker.add(comboBox, BorderLayout.NORTH);
		linker.add(idField, BorderLayout.CENTER);
		
		setContentPane(linker);
		setVisible(true);
		setResizable(false);
		setSize(400, 550);
		setLocationRelativeTo(null);
	}
}
