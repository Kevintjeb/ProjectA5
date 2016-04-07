package simulator;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

abstract class Agent implements Updateable, Drawable {
	private Point2D currentPosition, nextPosition;
	private final Image image;
	private Tile currentTile, nextTile;
	private int destination;
	private float speed;
	private static int currentTypeID = 0;
	private int destinationOld = 0;
	private int mod = 0;
	public static final int NO_DESTINATION = -1;

	static int generateNewTypeID() {
		return currentTypeID++;
	}
	public Agent(Image image, Tile tile, Point2D point, float speed) {
		this.image = image;
		this.currentTile = tile;
		this.nextTile = tile;
		mod = World.instance.getSizeBuildingID();
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
		if (destination >= mod)
			this.destination = (destination + 1) % mod;
		else
			this.destination = destination;
		if (state)
			state = false;
		// else
		// System.out.println("destination " + destination);
	}

	void move() {
		// System.out.println("current location: " + currentPosition);
		// if the nextTile is the currentTile we have reached our position
		// NOTE: this is because Tile.getDirection will give us the tile itself
		// if it is the destination

		// if the next time is null we have reset our destination so we need to
		// get our nextTile from the currentTile
		if (nextTile == null) {
			// System.out.println("next tile is set");
			nextTile = currentTile.getDirection(destination);
			try {
				nextPosition = new Point2D.Double(nextTile.X, -nextTile.Y);
			} catch (Exception e) {
				return;
			}
		}

		float remainingMoveDistance = World.instance.getDeltaTime() * speed;
		while (remainingMoveDistance > 0) {
			// we don't need to go anywhere so we can just return
			if (destination == NO_DESTINATION) {
				return;
			}

			// if the next time is null we have reset our destination so we need
			// to
			// get our nextTile from the currentTile
			if (nextTile == null) {
				// System.out.println("next tile is set");
				nextTile = currentTile.getDirection(destination);
				try {
					nextPosition = new Point2D.Double(nextTile.X, -nextTile.Y);
				} catch (Exception e) {
					return;
				}
			}

			if (nextTile == currentTile) // we are at our destination
			{
				destinationOld = destination;
				destination = NO_DESTINATION;
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
//		boolean isCollision = false;
//		Agent v = this;
//		for (Agent v2 : World.instance.getVisitors()) {
//			if (v == v2) {
//				continue;
//			}
//			Vec2f A = new Vec2f((float)v.currentPosition.getX(), (float)v.currentPosition.getY());
//			Vec2f B = new Vec2f((float)v2.currentPosition.getX(), (float)v2.currentPosition.getY());
//			Vec2f C = new Vec2f(B.x-A.x, B.y-A.y);
//			final float r = 0.5f;
//			final float cm = C.distance(new Vec2f()); // manitude of c
//			if (cm > r*2)
//				continue;
//			float mod = ((r*2-cm)/2);
//			Vec2f M = new Vec2f(C.x/cm*mod, C.y/cm*mod);
//			Vec2f A2 = new Vec2f((float)(A.x-M.x-M.y*0.5),(float)( A.y-M.y-M.x*0.5));
//			Vec2f B2 = new Vec2f((float)(A.x+M.x+M.y*0.5),(float)( A.y+M.y+M.x*0.5));
//			
//			v.currentPosition = new Point2D.Double(A2.x, A2.y);
//			if (A.distance(A2) > 1)
//			{
//				Tile tile = World.instance.getTileAt((int)A2.x, (int)A2.y);
//				if (tile.getDirection(v.destination) != null) // it is a tile with pathfinding
//				{
//					v.currentTile = tile;
//					v.nextTile = null; // v's move will fix this
//				}
//			}
//
//			v2.currentPosition = new Point2D.Double(B2.x, B2.y);
//			if (B.distance(B2) > 1) {
//				Tile tile = World.instance.getTileAt((int) B2.x, (int) B2.y);
//				if (tile.getDirection(v2.destination) != null) // it is a tile
//																// with
//																// pathfinding
	}

	public void setCurrentPosition(Point2D destination)
	{
		this.currentPosition = destination; 
	}
	
	public Point2D getCurrentPosition()
	{
		return this.currentPosition;
	}
	
	public int getDestinationOld()
	{
		return this.destinationOld;
	}
//	public void destenationReached(String destination) {
//		int teller = 0;
//		Tile[] tileArray = dance(destination);
//		while (teller < 200) {
//			nextPosition = new Point2D.Double(tileArray[teller % 3].X, tileArray[teller % 3].Y);
//			teller++;
//		}
//		// setDestination(i2++ % MOD);
//		// System.out.println("VISITOR destination reached");
//	}

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
