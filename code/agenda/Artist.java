package agenda;
// @author Flobo, Menno
// @version 1.2

import java.io.File; // to check the image file path
import java.io.Serializable; // to safe the class


public class Artist implements Serializable
{
	private static final long serialVersionUID = 7285546341282599074L;
	private String name;
	private String imagePath; // a file path to the image
	private String genre;
	private String notes;
	
	private Agenda agenda;
	
	public Artist(String name, String imagePath, String genre, String notes, Agenda agenda) throws Exception
	{
		if (agenda == null)
			throw new Exception("De agenda kan niet null zijn");
		this.agenda = agenda;
		
		setName(name, false);
		//setImagePath(imagePath);
		setGenre(genre, false);
		setNotes(notes, false);
		
		checkForDuplicate();
	}
	
	void checkForDuplicate() throws Exception
	{
		if (agenda.getArtist().contains(this) == false)
			return;
		if( agenda.getArtist().get(agenda.getArtist().indexOf(this)) != this)
			throw new Exception("De artiest is al toegevoegt");
	}
	
	public String getName() 
	{
		return name;
	}
	
	public String getImagePath()
	{
		return imagePath;
	}
	
	public String getGenre()
	{
		return genre;
	}
	
	public String getNotes()
	{
		return notes;
	}
	
	public void setName(String name, boolean check) throws Exception
	{
		if (name == null) // checks if the name is not null if it its it displays an error
			throw new Exception("Artist::setName(name) : ERROR: name is null");
		else // if the name is valid it sets it
		{
			this.name = name;
		}
		if (check)
			checkForDuplicate();
			
	}
	
	public void setImagePath(String imagePath, boolean check)
	{
		// if the path is null it sets it because
		// an artist without an image is allowed
		if (imagePath == null) 
			this.imagePath = null;
		// if it isn't null it checks wether the path is valid 
		// if it isn't valid it displays an error
		else if(new File(imagePath).exists() == false)
		{
			System.out.println("Artist::setImagePath(imagePath) : ERROR: imagePath \"" + imagePath + "\" is not a valid path to a file");
		}
		// if it is valid it sets it
		else
		{
			this.imagePath = imagePath;
		}
	}
	
	public void setGenre(String genre, boolean check) throws Exception
	{
		if (genre != null)
			genre = genre.trim();
		// cheks if the genre is null and if it
		// is displays an error
		if (genre == null)
			throw new Exception("genre can't be a null refrence");
		// checks if the genre is "" and if it is dislays an error
		else if (genre.equals(""))
			throw new Exception("genre can't be \"\"");
		else
			this.genre = genre;
		if (check)
			checkForDuplicate();
		
	}
	
	public void setNotes(String notes, boolean check) throws Exception
	{
		if (notes != null)
			notes = notes.trim();
		
		// checks if the notes are null and if it is sets it to null
		if (notes == null)
			this.notes = null;

		else
			this.notes = notes;
		if (check)
			checkForDuplicate();
	}
	
	@Override
	public String toString()
	{
		return toString(false);
	}
	
	public String toString(boolean extencive)
	{
		if (extencive == false)
			return name;
		// it constructs a string from the data
		return "name \"" + name + "\", image \"" + imagePath +
				"\", genre \"" + genre + "\", notes \"" + notes + "\"";
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result + ((imagePath == null) ? 0 : imagePath.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
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
		Artist other = (Artist) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}

