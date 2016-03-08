// @author Flobo, Menno
// @version 1.2

import java.io.Serializable; // to safe the class

public class Stage implements Serializable 
{
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Stage other = (Stage) obj;
		if (name == null)
		{
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	private static final long serialVersionUID = -1359262160412641958L;
	private String name;
	private Agenda agenda;
	
	public Stage(String name, Agenda agenda) throws Exception
	{
		if (agenda == null)
			throw new Exception("De agenda is null");
		this.agenda = agenda;
		
		setName(name);
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name) throws Exception
	{
		if (name != null)
			name = name.trim();
		
		// the name of the stage can't be null so if it is 
		// it displays an error
		if (name == null)
			throw new Exception("name can't be a empty refrence");
		// the name of the stage also can't be "" so if it is it 
		// displays an error
		else if (name.equals(""))
			throw new Exception("name can't be a empty refrence");
		// if the name is valid it is set
		this.name = name;
		if (agenda.getStages().contains(this) && agenda.getStages().get(agenda.getStages().indexOf(this)) != this)
			throw new Exception("stage has already been added to the agenda");
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
		
		// it return the data
		return "name \"" + name + "\"";
	}
}