
package simulator;

import java.util.ArrayList;

public class Toilet extends Service {

	public Toilet(String naam, ArrayList<Tile> entrances, int maxAgents) {
		super(naam, "Toilet voor het festival.", entrances, maxAgents, 5.0f);

	}

}