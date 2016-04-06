package simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;

abstract class Agent implements Updateable, Drawable {
	private Point2D currentPosition, nextPosition;
	private final Image image;
	private Tile currentTile, nextTile;
	private int destination;
	private float speed;
	private static int currentTypeID = 0;

	private static final int MOD = 8;
	public static final int NO_DESTINATION = -1;

	static int generateNewTypeID() {
		return currentTypeID++;
	}

	public Agent(Image image, Tile tile, Point2D point, float speed) {
		this.image = image;
		this.currentTile = tile;
		this.nextTile = tile;
		this.destination = NO_DESTINATION;
		this.speed = speed;
		this.currentPosition = point;
		World.instance.regesterDrawable(this);
		World.instance.regesterUpdateable(this);
	}

	public void close() {
		World.instance.unregesterDrawable(this);
		World.instance.unregesterUpdatable(this);
	}

	 abstract void destenationReached();

	boolean state = true;

	void setDestination(int destination) {
		nextTile = null; // the next tile gets set to null so move() will
							// recalculate the path
		if (destination >= MOD)
			this.destination = (destination + 1) % MOD;
		else
			this.destination = destination;
		if (state)
			state = false;
		// else
		// System.out.println("destination " + destination);
	}

	void move() {
		// System.out.println("current location: " + currentPosition);

		// we don't need to go anywhere so we can just return
		if (destination == NO_DESTINATION) {
			System.out.println("no destination");
			return;
		}

		// if the next time is null we have reset our destination so we need to
		// get our nextTile from the currentTile
		if (nextTile == null) {
			// System.out.println("next tile is set");
			nextTile = currentTile.getDirection(destination);
			try {
				nextPosition = new Point2D.Double(nextTile.X, -nextTile.Y);
			} catch (Exception e) {
				Graphics2D g2 = World.instance.mapImage.createGraphics();
				g2.setColor(Color.BLACK);
				g2.fillRect(currentTile.X * 32, currentTile.Y * 32, 32, 32);
				return;
			}
		}
		// if the nextTile is the currentTile we have reached our position
		// NOTE: this is because Tile.getDirection will give us the tile itself
		// if it is the destination

		float remainingMoveDistance = World.instance.getDeltaTime() * speed;
		while (remainingMoveDistance > 0) {
			if (nextTile == currentTile) // we are at our destination
			{
				int destinationOld = destination;
				destination = NO_DESTINATION;
				// System.out.println(World.instance.getBuildings().get(destinationOld).toString());
				//destenationReached(World.instance.getBuildings().get(destinationOld).toString());
				destenationReached();
				return;
			}

			if (nextPosition.distance(currentPosition) <= remainingMoveDistance) {
				remainingMoveDistance -= nextPosition.distance(currentPosition);
				currentPosition = nextPosition;
				currentTile = nextTile;
				nextTile = currentTile.getDirection(destination);
				nextPosition = new Point2D.Double(nextTile.X, -nextTile.Y);

			} else {

				float tempX = (float) (nextPosition.getX() - currentPosition.getX()),
						tempY = (float) (nextPosition.getY() - currentPosition.getY());

				float magnitude = (float) Math.sqrt(tempX * tempX + tempY * tempY);
				tempX /= magnitude;
				tempY /= magnitude;

				// now we have a unit vector

				tempX *= remainingMoveDistance;
				tempY *= remainingMoveDistance;

				remainingMoveDistance = 0;

				currentPosition.setLocation(currentPosition.getX() + tempX, currentPosition.getY() + tempY);
			}
		}

		// collision : TODO collision! RIP?
		boolean isCollision = false;
		for (Agent v : World.instance.getVisitors()) {
			for (Agent v2 : World.instance.getVisitors()) {
				if (v == v2) {
					continue;
				}
				if (v.currentPosition.distance(v2.currentPosition) < 1) {

					v.nextPosition = new Point2D.Double(v.currentPosition.getX(), v.currentPosition.getY());
					v2.nextPosition = new Point2D.Double(v2.currentPosition.getX() + .25,
							v2.currentPosition.getY() - .25);

				}
			}
		}
	}

	public void destenationReached(String destination) {
		int teller = 0;
		Tile[] tileArray = dance(destination);
		while (teller < 200) {
			nextPosition = new Point2D.Double(tileArray[teller % 3].X, tileArray[teller % 3].Y);
			teller++;
		}
		// setDestination(i2++ % MOD);
		// System.out.println("VISITOR destination reached");
	}

	public Tile[] dance(String destenation) {
		if (destenation.toLowerCase().endsWith("stage")) {
			int locatie = World.instance.getPathID(destenation);
			Stage g = (Stage) World.instance.getBuildings().get(locatie);
			ArrayList<Tile> dancefloor = new ArrayList<>();
			dancefloor.addAll(g.getDanceFloor());

			Tile punt1 = dancefloor.get((int) (Math.random() * dancefloor.size() - 1));
			Tile punt2 = dancefloor.get((int) (Math.random() * dancefloor.size() - 1));
			Tile punt3 = dancefloor.get((int) (Math.random() * dancefloor.size() - 1));

			Tile[] tileArray = { punt1, punt2, punt3 };
			// System.out.println("geluk! stage : " + g);
			return tileArray;
		} else {
			return null;
		}
	}

	@Override
	public void draw(Graphics2D graphics, AffineTransform t) {
		double dy = nextPosition.getY() - currentPosition.getY();
		double dx = nextPosition.getX() - currentPosition.getX();
		double theta = Math.atan2(dy, dx);

		AffineTransform tx = new AffineTransform();
		tx.translate(currentPosition.getX() * 32 + 16, currentPosition.getY() * -32 + 16);
		tx.rotate(-theta, 16, 16);
		graphics.drawImage(image, tx, null);
	}
}
