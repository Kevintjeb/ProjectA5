/**
 * 
 */
package simulator;

import java.util.ArrayList;

class Stage extends Building implements Updateable{
	private agenda.Stage stage;
	private ArrayList<Tile> danceFloor;
	private ArrayList<agenda.Performance> performances;
	private agenda.Performance currentPerformance = null;
	
	Stage(String description, ArrayList<Tile> entrances,
			ArrayList<Tile> exits, int maxAgents, agenda.Stage stage, ArrayList<Tile> danceFloor) {
		super(generateNewTypeID(), stage.getName(), description, entrances, exits, maxAgents);
		
		this.stage = stage;
		this.danceFloor = danceFloor;
		System.out.println(World.instance + " instance");
		System.out.println(this + "this");
		performances = World.instance.agenda.getStagesPerformances(stage);
	}
	

	@Override
	public void close() {
		World.instance.unregesterUpdatable(this);
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
	public void update() {
		final int NO_PERFORMANCE = -1;
		int lastPerformance = NO_PERFORMANCE, currentPerformance = NO_PERFORMANCE;
		
		agenda.Time currentTime = new agenda.Time(World.instance.getWorldTime());
		agenda.Time preciusTime = new agenda.Time(World.instance.getWorldTime() - World.instance.getDeltaTime());
		
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
			if (currentPerformance == NO_PERFORMANCE)
			{
				message = "the performance \"" + performances.get(currentPerformance).toString() + 
						"\" started at stage \"" + stage.toString() + "\"";
			}
			
			System.out.println(message);
		}
	}

}
