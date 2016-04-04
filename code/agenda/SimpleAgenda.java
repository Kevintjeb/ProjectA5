package agenda;


import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;

public class SimpleAgenda extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3147744315314108083L;
	JTable table;
	Planner planner;

	public SimpleAgenda(Planner planner) {
		super(new BorderLayout());
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
			| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
			}
		this.planner = planner;

		table = new JTable(new AgendaTable());
		add(table, BorderLayout.CENTER);
		add(table.getTableHeader(), BorderLayout.NORTH);
		table.getTableHeader().addMouseListener(new MouseAdapter()
		{
			PerformaceComparator.SortTypes previusSortType = null;
			PerformaceComparator.SortOrder previusSortOrder = null;
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				String name = table.getColumnName(table.columnAtPoint(e.getPoint()));
				
				PerformaceComparator.SortTypes sortType;
				PerformaceComparator.SortOrder sortOrder;
				switch(name)
				{
				case "Artiest":
					sortType = PerformaceComparator.SortTypes.ARTIST_NAME;
					break;
				case "Genre":
					sortType = PerformaceComparator.SortTypes.ARTIST_GENRE;
					break;
				case"Populariteit":
					sortType = PerformaceComparator.SortTypes.POPULARITY;
					break;
				case "Starttijd":
					sortType = PerformaceComparator.SortTypes.START_TIME;
					break;
				case "Eindtijd":
					sortType = PerformaceComparator.SortTypes.END_TIME;
					break;
				case "Podium":
					sortType = PerformaceComparator.SortTypes.STAGE;
					break;
				default:
					System.out.println("MouseAdapter::mouseClicked : ERROR: \"" + name + 
							"\" is not a supported collum name");
					return;
				}
				
				if (previusSortType == null)
					sortOrder = PerformaceComparator.SortOrder.HIGH_TOP;
				else if (previusSortType == sortType)
				{
					if (previusSortOrder == PerformaceComparator.SortOrder.HIGH_TOP)
						sortOrder = PerformaceComparator.SortOrder.LOW_TOP;
					else
						sortOrder = PerformaceComparator.SortOrder.HIGH_TOP;
				}
				else
					sortOrder = PerformaceComparator.SortOrder.HIGH_TOP;

				
				planner.agenda.sortPerformances(sortType, sortOrder);
				
				previusSortType = sortType;
				previusSortOrder = sortOrder;
				
				repaint();
			}
		});
	}

	class AgendaTable extends AbstractTableModel {
		/**
		 * 
		 */
		private static final long serialVersionUID = -8942879081454485945L;
		String[] columnNames = { "Artiest", "Genre", "Populariteit", "Starttijd", "Eindtijd", "Podium" };

		public int getColumnCount() {
			return 6;
		}

		public int getRowCount() {
			return planner.agenda.getPerformances().size();
		}

		public String getColumnName(int col) {
			return columnNames[col];
		}

		public Object getValueAt(int row, int col) {
			Performance performance = planner.agenda.getPerformances().get(row);
			Object answer = null;
			switch (col) {
			case 0:
				int teller = 1;
				
				if(performance.getArtists().size() > 1)
				{
					String namen = new String("");
					for(int i = 0; i < performance.getArtists().size(); i++)
					{   
						if(teller == performance.getArtists().size())
						{
							namen = namen + performance.getArtists().get(i).getName();
						}
						else
						{
							teller++;
							namen = namen + performance.getArtists().get(i).getName() + ", ";
						}
					}
						answer = namen;
				}
				else if (performance.getArtists().size() == 1)
				{
					answer = performance.getArtists().get(0).getName();
				}
				else
					answer = "-------";
				break;
			case 1:
				if (performance.getArtists().size() > 0)
					answer = performance.getArtists().get(0).getGenre();
				else
					answer = "-------";
				break;
			case 2:
				answer = performance.getPopularity();
				break;
			case 3:
				answer = performance.getStartTime();
				break;
			case 4:
				answer = performance.getEndTime();
				break;
			case 5:
				answer = performance.getStage().getName();
				break;
			}
			return answer;
		}
	}
}
