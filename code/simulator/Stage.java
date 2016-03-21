/**
 * 
 */
package simulator;

import java.util.ArrayList;

/**
 * @author flori
 *
 */
public class Stage extends Building implements Updateable{
	private agenda.Stage stage;
	private Tile[][] danceFloor;
	private ArrayList<agenda.Performance> performances;
	
	Stage(String name, String description, Tile[] entrances,
			Tile[] exits, int maxAgents, agenda.Stage stage, Tile[][] danceFloor) {
		super(generateNewTypeID(), name, description, entrances, exits, maxAgents);
		
		this.stage = stage;
		this.danceFloor = danceFloor;
		World.instance.regesterUpdateable(this);
		performances = World.instance.agenda.getStagesPerformances(stage);
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
		
		int currentTime = World.instance.getWorldTime(), previusTime = currentTime - World.instance.getDeltaTime();
		
		for (int i = 0; i < performances.size(); i++)
		{
			
		}
	}

}
