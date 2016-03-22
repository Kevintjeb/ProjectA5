package simulator;

import java.awt.Image;
import java.awt.geom.Point2D;
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
		map.put(agenda.getStages().get(0), 5);
		
		World w = new World(agenda, map, "Endmap.json", "Tiled2.png");
		System.out.println("world was constructed");
		
		class Foo extends Agent
		{

			public Foo() {
				super(null, World.instance.getTileAt(0, 0), null, 0.0f);
			}

			@Override
			public void update() {
				System.out.println(World.instance.getWorldTime());
				
			}

			@Override
			void destenationReached() {
				// TODO Auto-generated method stub
				
			}
			
		}
		
		new Foo();
		
		w.update();
	}
}