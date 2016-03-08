package agenda;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

class StageLinker extends JFrame
{
	JPanel linker;
	JButton saveButton = new JButton("Save links");
	JButton closeButton = new JButton("Close");
	
	public StageLinker()
	{
		super("Stage Linking");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		linker = new JPanel(new BorderLayout());
		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());
		
		saveButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				dispose();
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
		
		setContentPane(linker);
		setVisible(true);
		setResizable(false);
		setSize(400, 550);
		setLocationRelativeTo(null);
	}
}
