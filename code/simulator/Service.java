package simulator;

import java.util.ArrayList;
import java.util.HashMap;

public class Service extends Building {
	private static HashMap<String, Integer> serviceToTypeIDMap = new HashMap<String, Integer>();
	private static int getTypeID(String serviceName)
	{
		if (serviceToTypeIDMap.containsKey(serviceName) == false)
			serviceToTypeIDMap.put(serviceName, generateNewTypeID());
		return serviceToTypeIDMap.get(serviceName);
	}
	
	private final float visitCost;
	
	public Service(String name, String description,
			ArrayList<Tile> entrances, int maxAgents, float visitCost) {
		super(getTypeID(name), name, description, entrances, null, maxAgents);
		
		this.visitCost = visitCost;
	}
	
	public int visit(Agent agent)
	{
		int result = super.visit(agent);
		if (result < 0)
			return result;
		// TODO check if the agent is a visitor and if it is check if the agent has enough money
		return 0;
	}

}
