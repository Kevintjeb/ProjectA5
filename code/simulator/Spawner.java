package simulator;

public class Spawner implements Updateable {

	private int visitors = 0;
	private float spawnTime = 30f;
	private float timeLeft = 0f;
	private int numberVisitors = 0;
	private Tile[] tiles = {World.instance.getTileAt(8, 99), World.instance.getTileAt(9, 99), World.instance.getTileAt(10, 99)};

	public Spawner(int visitors) {
		this.visitors = visitors;
		World.instance.regesterUpdateable(this);
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		if (numberVisitors < visitors) {
			timeLeft -= World.instance.getDeltaTime();
			while (timeLeft <= 0) {
				numberVisitors++;
				new Visitor(tiles[(int) (Math.random() * 2)], 0.01f);
				timeLeft = 0;
				timeLeft += spawnTime;
			}
		}
	}

	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}

	public int getvisitors() {
		return visitors;
	}

}
