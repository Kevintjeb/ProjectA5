package agenda;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

class StageLinker extends JFrame
{
	JPanel linker;
	JButton saveButton = new JButton("Set link");
	JButton closeButton = new JButton("Close");
	private Agenda agenda;
	private Object selectedObject;
	
	
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
				//Now sets everything directly to stage without using the selected item. Need to amend this.
				selectedObject  = comboBox.getSelectedItem();
				int num = Integer.parseInt(idField.getText());
				Stage.IDCode = num;
				System.out.println(Stage.IDCode);
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
