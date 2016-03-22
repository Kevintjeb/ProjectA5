package simulator;

import java.util.HashMap;

import agenda.Artist;
import agenda.Performance;
import agenda.Stage;

// just for testing
public class __Main {
	public static void main(String[] args) throws Exception {
		agenda.Agenda agenda = new agenda.Agenda();
		agenda.getArtist().add(new Artist("PAULTIE", null, "SLAGER", "ZEHR GUT", agenda));
		agenda.getStages().add(new Stage("MAIN STAGE", agenda));
		agenda.getPerformances().add(new Performance(agenda.getStages().get(0), agenda.getArtist().get(0),
				new agenda.Time(16, 00), new agenda.Time(19, 00), 123, agenda));

		HashMap<agenda.Stage, Integer> map = new HashMap<>();

		World w = new World(null, map, "Endmap.json", "filemap.png");

	}
}