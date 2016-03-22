package agenda;


import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Planner extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2809776790156312314L;
	JPanel contentPane;
	JMenuBar menuBar;
	Agenda agenda = new Agenda();
	Planner _this = this;
	JTabbedPane tabbedPane = new JTabbedPane();

	public static void main(String[] args) {
		new Planner();
		
	}

	public Planner() {
		super("Festival Planner");
		//agenda = Agenda.debugSetup();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		contentPane = new JPanel(new GridLayout());
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu file = new JMenu("File");
		menuBar.add(file);
		
		JMenu credits = new JMenu("Credits");
		menuBar.add(credits);
			

		// Kevin --
		JMenuItem save = new JMenuItem("Save");
		JMenuItem load = new JMenuItem("load");
		JMenuItem edit = new JMenuItem("Edit");
		JMenuItem credit = new JMenuItem("Credits");
		JMenuItem linker = new JMenuItem("Stage linking");

		file.add(save);
		file.add(load);
		file.add(edit);
		file.add(linker);
		credits.add(credit);

		save.addActionListener(new ActionListener() {
			
			FileNameExtensionFilter filter;
			File file;
			BufferedWriter saving;
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser saver = new JFileChooser();
				saver.setCurrentDirectory(new File(System.getProperty("user.home")));
				saver.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
				try{
					filter = new FileNameExtensionFilter(".agenda", "agenda");
				}
				catch(IllegalArgumentException i){
					i.printStackTrace();
				}
				
				saver.setFileFilter(filter);
				//System.out.println(chooser.getFileFilter());
				int verification = saver.showSaveDialog(_this);
				
				if(verification == JFileChooser.APPROVE_OPTION){
					String fileName = saver.getSelectedFile().getName();
					if(!fileName.endsWith(".agenda")){
						fileName += ".agenda";
					}
					
					try{
						FileWriter writer = new FileWriter(fileName);
						Agenda.save(agenda, saver.getSelectedFile().getAbsolutePath() + ".agenda");
					
						writer.close();
					}
					catch(IOException y){
						y.printStackTrace();
					}
				}
				else if(verification == JFileChooser.CANCEL_OPTION){
					JOptionPane.showMessageDialog(null, "The saving has been cancelled.");
				}
				
				//System.out.print(file.exists());
			}
		});

		load.addActionListener(new ActionListener() {
			
			FileNameExtensionFilter filter;
			File file;
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				
			try{
					filter = new FileNameExtensionFilter(".agenda", "agenda");
				}
				catch(IllegalArgumentException i){
					i.printStackTrace();
				}
				
				chooser.setFileFilter(filter);
				//System.out.println(chooser.getFileFilter());
				int verification = chooser.showOpenDialog(_this);
				
				if(verification == JFileChooser.APPROVE_OPTION){
					file = chooser.getSelectedFile();
				}
				else if(verification == JFileChooser.CANCEL_OPTION){
					JOptionPane.showMessageDialog(null, "The loading has been cancelled.");
				}
				
				//System.out.print(file.exists());
				agenda = new Agenda(file.getPath());
				
				//agenda = new Agenda("agenda");
				repaint();

			}
		});

		edit.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				new EditorFrame(_this);
			}
		});
		
		linker.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				new StageLinker(_this);
			}
		});
		
		credit.addActionListener(new ActionListener() 
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "\u00a9 Groep TI1.3A5" + "\n" + "Technische Informatica" + "\n" + "Avans Hogeschool Breda", "Credits", JOptionPane.INFORMATION_MESSAGE);
			}
		});

		

		// Kevin --

		
		
		tabbedPane.addTab("Simple", new SimpleAgenda(this));

		TimeLine tl = new TimeLine(this);
		JScrollPane pane = new JScrollPane(tl);
		tabbedPane.addTab("2D", pane);
		
<<<<<<< HEAD
		//tabbedPane.addTab("Simulatie", newSimulatieGUI(this));
=======
		tabbedPane.addTab("Simulatie", new SimulatieGUI(this));
		
>>>>>>> refs/remotes/origin/master
		contentPane.add(tabbedPane);
		
		setContentPane(contentPane);
		
		setVisible(true);
		setMinimumSize(new Dimension(600, 600));
		setSize(1200, 700);
		setLocationRelativeTo(null);
	}
}
