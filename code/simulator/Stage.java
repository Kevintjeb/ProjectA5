/**
 * 
 */
package simulator;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Iterator;

class Stage extends Building implements Updateable, Drawable{
	private agenda.Stage stage;
	private ArrayList<Tile> danceFloor;
	private ArrayList<agenda.Performance> performances;
	private agenda.Performance currentPerformance = null;
	ArrayList<Particle> firework = new ArrayList<>();
	Iterator<Particle> itr = firework.iterator();
	Iterator<agenda.Performance> itrPerformance;
	
	int alpha = 50;
	int verdwijnt = 1;
	
	Stage(String description, ArrayList<Tile> entrances,
			ArrayList<Tile> exits, int maxAgents, agenda.Stage stage, ArrayList<Tile> danceFloor) {
		super(generateNewTypeID(), stage.getName(), description, entrances, exits, maxAgents);
		
		this.stage = stage;
		this.danceFloor = danceFloor;
		World.instance.regesterUpdateable(this);
		World.instance.regesterDrawable(this);
		performances = World.instance.agenda.getStagesPerformances(stage);
		itrPerformance = performances.iterator();
	}
	
	public agenda.Performance getPerformance()
	{
		itrPerformance = performances.iterator();
		while(itrPerformance.hasNext())
		{
			agenda.Performance p = itrPerformance.next();
			if(agenda.Time.contains(p.getStartTime(), p.getEndTime(), World.instance.getTime()))
			{
				currentPerformance = p;
				break;
			}
			else
			{
				currentPerformance = null;
			}
		}
		
		return currentPerformance;
	}

	@Override
	public void close() {
		World.instance.unregesterUpdatable(this);
		World.instance.unregesterDrawable(this);
	}
	
	agenda.Performance getCurrentPerformance()
	{
		return currentPerformance;
	}

	@Override
	int visit(Agent agent) {
		int result = super.visit(agent);
		if (result < 0)
			return result;
		return 60*10; // 10 minutes
	}

	@Override
	public void update() 
	{
		if(getPerformance() != null)
		{
			double x;
			double y;
			
			if(Math.random() > 0.5)
			{
				x = danceFloor.get(0).X * 32; 
				y = danceFloor.get(0).Y * 32; 
			}
			else
			{
				x = danceFloor.get(danceFloor.size()-1).X * 32;
				y = danceFloor.get(0).Y * 32; 
			}
			
			if(World.instance.getTime().getHours() >= 20 || World.instance.getTime().getHours() <= 6)
			{
				if(alpha < 255)
				{
					alpha++;
					verdwijnt++;
				}
			}
			
			for(int i = 0; i < 25; i++)
			{
				double bool = Math.random();
				double newX;
				double newY;
				
				if(bool < 0.25)
				{
					newX = Math.random() * 5;
					newY = Math.random() * 5;
				}
				else if(bool > 0.25 && bool < 0.5)
				{
					newX = Math.random() * -5;
					newY = Math.random() * -5;
				}
				else if(bool > 0.5 && bool < 0.75)
				{
					newX = Math.random() * -5;
					newY = Math.random() * 5;
				}
				else
				{
					newX = Math.random() * 5;
					newY = Math.random() * -5;
				}
				
				firework.add(new Particle(x, y, newX, newY, alpha));
			}
		}
		
		itr = firework.iterator();
		while(itr.hasNext())
		{
			Particle p = itr.next();
			if(p.getAlpha() < verdwijnt)
			{
				itr.remove();
			}
		}
		
		
		

		
		/*final int NO_PERFORMANCE =t -1;
		int lastPerformance = NO_PERFORMANCE, currentPerformance = NO_PERFORMANCE;
		
		System.out.println(new agenda.Time(World.instance.getWorldTime()/60));
		
		agenda.Time currentTime = new agenda.Time(World.instance.getWorldTime()/60);
		agenda.Time preciusTime = new agenda.Time((World.instance.getWorldTime() - World.instance.getDeltaTime())/60);
		
		//System.out.println("Current : " + currentTime + "precious " + preciusTime);
		for (int i = 0; i < performances.size(); i++)
		{
			agenda.Time start = performances.get(i).getStartTime(), end = performances.get(i).getEndTime();
			
			if (agenda.Time.contains(start, end, currentTime) == true)
				currentPerformance = i;
			if (agenda.Time.contains(start, end, preciusTime) == true)
				lastPerformance = i;
		}
		
		if (currentPerformance == NO_PERFORMANCE)
			this.currentPerformance = null;
		else
			this.currentPerformance = performances.get(currentPerformance);
		
		// TODO remove in final version?
		if (lastPerformance != currentPerformance)
		{
			String message = "";
			
			if (lastPerformance != NO_PERFORMANCE)
			{
				message += "the performance \"" + performances.get(lastPerformance) + "\" on stage \"" +
						stage + "\" ended";
			}
			if (currentPerformance != NO_PERFORMANCE)
			{
				message = "the performance \"" + performances.get(currentPerformance).toString() + 
						"\" started at stage \"" + stage.toString() + "\"";
			}
			
			System.out.println(message);
			int a = 0;
		}*/
	}
	public void draw(Graphics2D graphics, AffineTransform t)
	{
		itr = firework.iterator();
		while(itr.hasNext())
		{
			Particle p = itr.next();
			
			graphics.setColor(p.update());
			Area a = new Area(p.updateDeeltje());
			graphics.setTransform(t);
			graphics.fill(a);
			
		}
	}

	public ArrayList<Tile> getDanceFloor() {
		return danceFloor;
	}

}
