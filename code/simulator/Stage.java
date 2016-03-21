/**
 * 
 */
package simulator;

/**
 * @author flori
 *
 */
public class Stage extends Building implements Updateable{
	private agenda.Stage stage;
	private Tile[][] danceFloor;
	
	Stage(String name, String description, Tile[] entrances,
			Tile[] exits, int maxAgents, agenda.Stage stage, Tile[][] danceFloor) {
		super(generateNewTypeID(), name, description, entrances, exits, maxAgents);
		
		this.stage = stage;
		this.danceFloor = danceFloor;
		World.instance.regesterUpdateable(this);
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
		
	}

}
