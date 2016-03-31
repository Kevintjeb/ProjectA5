package agenda;

import java.io.Serializable;


public class Time implements Serializable
{
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + hours;
		result = prime * result + minutes;
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
		Time other = (Time) obj;
		if (hours != other.hours)
			return false;
		if (minutes != other.minutes)
			return false;
		return true;
	}

	private static final long serialVersionUID = -1693443174753793715L;

	private int hours;
	private int minutes;
	
	public Time(int h, int m)
	{
		setHours(h);
		setMinutes(m);
	}
	
	public Time(int m)
	{
		this(m/60, m%60);
	}
	
	public void setHours(int hours)
	{
		if (hours < 0 || hours > 23)
			System.out.println("Time::setHours(hours) : ERROR: hours is out of range");
		else
			this.hours = hours;
	}
	
	public void setMinutes(int minutes)
	{
		if (minutes < 0 || minutes > 59)
			System.out.println("Time::setMinutes(minutes) : ERROR: minutes is out of range");
		else
			this.minutes = minutes;
	}
	
	public int getHours()
	{
		return hours;
	}
	
	public int getMinutes()
	{
		return minutes;
	}
	
	public static Time timeFromString(String s) throws NumberFormatException 
	{
		int h = Integer.parseInt(s.substring(0, 2));
		int m = Integer.parseInt(s.substring(3, 5));
		
		return new Time(h, m);
	}
	
	public static boolean overlaps(Time aStart, Time aEnd, Time bStart, Time bEnd)
	{
		if (aStart.toMinutes() == bStart.toMinutes())
			return true;
		if (aEnd.toMinutes() == bEnd.toMinutes())
			return true;
		
		if (bStart.toMinutes() < aStart.toMinutes())
			if (bEnd.toMinutes() >= aStart.toMinutes())
				return true;
		if (bStart.toMinutes() > aStart.toMinutes() && bStart.toMinutes() < aEnd.toMinutes())
			return true;
		return false;
	}
	
	public static boolean contains(Time start, Time end, Time time)
	{
		if (time.toMinutes() < start.toMinutes() || time.toMinutes() > end.toMinutes())
			return false;
		return true;
	}
	
	public int toMinutes()
	{
		return getHours()*60+getMinutes();
	}
	
	@Override
	public String toString()
	{
		return toTwoDigits(hours) + ":" + toTwoDigits(minutes);
	}
	
	private String toTwoDigits(int n)
	{
		String r = "";
		if (n < 10)
			r += "0";
		r += "" + n;
		return r;
	}
}

