package agenda;

import java.util.Comparator;

public class PerformaceComparator implements Comparator<Performance>
{
	public enum SortTypes
	{
		ARTIST_NAME, ARTIST_GENRE, STAGE, START_TIME, END_TIME, POPULARITY, 
	}
	
	public enum SortOrder
	{
		LOW_TOP, HIGH_TOP
	}
	
	private SortTypes sortType;
	private int modifier;
	
	public PerformaceComparator(SortTypes sortType, SortOrder sortOrder)
	{
		this.sortType = sortType;
		modifier = (sortOrder == SortOrder.HIGH_TOP) ? -1 : 1;
	}
	
	@Override
	public int compare(Performance arg0, Performance arg1) {
		int result = 0;
		
		switch (sortType)
		{
		case ARTIST_NAME:
			// TODO fix for multiple artists
			result = arg0.getArtists().get(0).getName().compareToIgnoreCase(arg1.getArtists().get(0).getName());
			break;
		case ARTIST_GENRE:
			result = arg0.getArtists().get(0).getGenre().compareToIgnoreCase(arg1.getArtists().get(0).getGenre());
			break;
		case STAGE:
			result = arg0.getStage().getName().compareToIgnoreCase(arg1.getStage().getName());
			break;
		case START_TIME:
			result = arg0.getStartTime().getHours() - arg1.getStartTime().getHours();
			if (result == 0)
				result = arg0.getStartTime().getMinutes() - arg1.getStartTime().getMinutes();
			break;
		case END_TIME:
			result = arg0.getEndTime().getHours() - arg1.getEndTime().getHours();
			if (result == 0)
				result = arg0.getEndTime().getMinutes() - arg1.getEndTime().getMinutes();
			break;
		case POPULARITY:
			result = arg0.getPopularity() - arg1.getPopularity();
			break;
		default:
			//System.out.println("PerformaceComparator::compare : ERROR: default state should never be reached");
			result = 0;
		}
		 return result*modifier;
	}
	
}

