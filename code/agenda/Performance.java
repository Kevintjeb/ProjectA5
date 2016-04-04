package agenda;

// @author Flobo, Menno
// @version 1.2

import java.io.Serializable; // to safe the class
import java.util.ArrayList; // to store data in ArrayLists


public class Performance implements Serializable
{
	
	private static final long serialVersionUID = -1863213156305878969L;
	private Stage stage;
	private ArrayList<Artist> artists;
	private Time startTime, endTime;
	private int popularity;
	
	private Agenda agenda;
	
	// the normal constuctor
	public Performance(Stage stage, ArrayList<Artist> artists, 
			Time startTime, Time endTime,
			int popularity, Agenda agenda) throws Exception
	{
		if (agenda != null)
			this.agenda = agenda;
		
		setStage(stage, false);
		setArtists(artists, false);
		setStartTime(startTime, false);
		setEndTime(endTime, false);
		setPopularity(popularity);
		
		check();
		
	}
	
	void check() throws Exception
	{
		if (agenda == null)
			throw new Exception("agenda can't be null");
		if (startTime.toMinutes() >= endTime.toMinutes())
			throw new Exception("the end time has to be later than the start time");
		for (Performance p : agenda.getStagesPerformances(stage))
			if (Time.overlaps(this.startTime, this.endTime, p.startTime, p.endTime) == true)
				if (p != this)
				throw new Exception("this performance overlaps the performance :" + p);
		if (agenda.performances.contains(this) && agenda.getPerformances().get(agenda.getPerformances().indexOf(this)) != this)
			throw new Exception("Performance is already in the agenda");
		for (Artist artist : getArtists())
			for (Performance performance : agenda.getArtistsPerformances(artist))
				if (performance != this)
					if (Time.overlaps(startTime, endTime, performance.getStartTime(), performance.getEndTime()))
						throw new Exception("the artist \"" + artist + "\" is performing at the performance \"" + 
								performance + "\" during this performance");
	}
	
	// a constructor that allows you to pass just one artist
	public Performance(Stage stage, Artist artist, 
			Time startTime, Time endTime,
			int popularity, Agenda agenda) throws Exception
	{
		// the 'normal' constuctor get's called and the artist is put into an ArrayList
		this(stage, new ArrayList<Artist>() {
			private static final long serialVersionUID = 4032607537170122106L; // for serilization TODO fix spelling
		{add(artist);}}, startTime, endTime, popularity, agenda);
	}
	
	public void setStage(Stage stage, boolean check) throws Exception
	{
	
		// the stage can't be null so when it is 
		// an error will be displayed
		if (stage == null)
			throw new Exception("stage can't be a null refrence");
		// if the stage is valid it is set
		else
			this.stage = stage;	
		if (check)
			check();
	}
	
	public void setArtists(ArrayList<Artist> artists, boolean check) throws Exception
	{
		// the ArrayList of artist can't be null 
		// so when it is it will display an error
		if (artists == null)
			throw new Exception("artists can't be null");
		// if it is valid it gets set
		else
			this.artists = artists;
		if (check)
			check();
	}
	
	
	public void setStartTime(Time startTime, boolean check) throws Exception
	{
		// the startTime can't be null so if it is
		// it displays an error
		if (startTime == null)
		{
			throw new Exception("start time can't be a null refrence");
		}
		// start time gets set
		this.startTime = startTime;
		if (check)
			check();
	}
	
	public void setEndTime(Time endTime, boolean check) throws Exception
	{
		// the endTime can't be null so if it is
		// it displays an error
		if (endTime == null)
		{
			throw new Exception("end time can't be a null refrence");
		}
		// end time gets set
		Time tempTime = endTime;
		this.endTime = endTime;
		if (check)
		{
			try
			{
			check();
			}
			catch(Exception e)
			{
				this.endTime = tempTime;
				throw e;
			}
		}
		
	}
	
	public void setPopularity(int popularity)
	{
		this.popularity = popularity;
	}
	
	public Stage getStage()
	{
		return stage;
	}
	
	public ArrayList<Artist> getArtists()
	{
		return artists;
	}
	
	public Time getStartTime()
	{
		return startTime;
	}
	
	public Time getEndTime()
	{
		return endTime;
	}
	
	public int getPopularity()
	{
		return popularity;
	}
	
	@Override
	public String toString()
	{
		return toString("");
	}
	public String toString(String prefix)
	{
		String s = stage.toString();
		s+= ", " + startTime.toString();
		s+= " - " + endTime.toString();
		for (int i = 0; i < artists.size(); i++)
			s+= ", " + artists.get(i).toString();
		
		return s;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artists == null) ? 0 : artists.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + popularity;
		result = prime * result + ((stage == null) ? 0 : stage.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Performance other = (Performance) obj;
		if (artists == null)
		{
			if (other.artists != null)
				return false;
		} else if (!artists.equals(other.artists))
			return false;
		if (endTime == null)
		{
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (popularity != other.popularity)
			return false;
		if (stage == null)
		{
			if (other.stage != null)
				return false;
		} else if (!stage.equals(other.stage))
			return false;
		if (startTime == null)
		{
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}
}

