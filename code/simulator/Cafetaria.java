package simulator;

import java.util.ArrayList;

public class Cafetaria extends Service {

	public Cafetaria(ArrayList<Tile> entrances, int maxAgents) {
		super("Cafetaria 22", "Eettent van het festival", entrances, maxAgents, 2.5f);
	}
}