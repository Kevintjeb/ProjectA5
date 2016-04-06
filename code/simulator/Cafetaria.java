
package simulator;

import java.util.ArrayList;

public class Cafetaria extends Service {

	public Cafetaria(String naam ,ArrayList<Tile> entrances, int maxAgents) {
		super(naam, "Eettent van het festival", entrances, maxAgents, 2.5f);
	}
}