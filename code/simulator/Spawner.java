package simulator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class Spawner {

	private int visitors = 0;
	private float spawnTime = 30f;
	private float timeLeft = 0f;
	private int numberVisitors = 0;
	private World world;
	private Timer t;
	private Tile[] tiles = {World.instance.getTileAt(8, 99), World.instance.getTileAt(9, 99), World.instance.getTileAt(10, 99)};

	public Spawner(int visitors, World w) {
		this.visitors = visitors;
		this.world = w;
		t = new Timer(500, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		
	}


	public void close() {
		// TODO Auto-generated method stub

	}


	public void update() {
		// TODO Auto-generated method stub
		if (numberVisitors < visitors) {
			timeLeft -= World.instance.getDeltaTime();
			while (timeLeft <= 0) {
				numberVisitors++;
				new Visitor(tiles[(int) (Math.random()*3)], 0.01f);
				timeLeft = 0;
				timeLeft += spawnTime;
			}
		}
	}

	public void stopTimer()
	{
		t.stop();
	}
	
	public void continueTimer()
	{
		t.start();
	}
	
	public Timer getTimer()
	{
		return t;
	}
	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}

	public int getvisitors() {
		return visitors;
	}

}
