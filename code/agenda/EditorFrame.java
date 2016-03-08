import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class EditorFrame extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 976383459480903200L;
	JPanel editorContentPane;
	Planner planner;
	JButton closeButton = new JButton("Close");
	JTabbedPane editorTabbedPane;
	
	public EditorFrame(Planner planner)
	{
		super("EditorFrame");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.planner = planner;
		
		editorContentPane = new JPanel(new BorderLayout());
		
		editorTabbedPane = new JTabbedPane();
		
		editorTabbedPane.addTab("Edit", new EditGUI(planner));
		editorTabbedPane.addTab("Add", new AddGUI(planner));
		editorTabbedPane.addTab("Remove", new RemoveOption(planner));
		
		editorContentPane.add(editorTabbedPane, BorderLayout.CENTER);
		editorContentPane.add(closeButton, BorderLayout.SOUTH);
		
		
		closeButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				dispose();
			}
		});
		
		setContentPane(editorContentPane);
		setVisible(true);
		setResizable(false);
		setSize(400, 550);
		setLocationRelativeTo(null);
	}

	public EditorFrame(Planner planner2, Performance performance)
	{
		this(planner2);
		editorTabbedPane.setSelectedIndex(2);
		
	}

}
