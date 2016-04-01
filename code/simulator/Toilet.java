

package simulator;

import java.util.ArrayList;

public class Toilet extends Service {

	public Toilet(ArrayList<Tile> entrances, int maxAgents) {
		super("Toilet", "Toilet voor festival.", entrances, maxAgents, 5.0f);
	}

}