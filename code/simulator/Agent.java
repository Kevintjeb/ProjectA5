package simulator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

abstract class Agent implements Updateable, Drawable {
	private Point2D currentPosition, nextPosition;
	private final BufferedImage image;
	private Tile currentTile, nextTile;
	private int destenation;
	private float speed;
	private float rotation;
	private float imageScale;

	public static final int NO_DESTENATION = -1;

	public Agent(BufferedImage image, Tile tile, Point2D point, float speed) {
		this.image = image;
		this.currentTile = tile;
		this.nextTile = tile;
		this.destenation = NO_DESTENATION;
		this.speed = speed;
		this.currentPosition = point;

		{
			float scaleX = 32 / image.getWidth(null);
			float scaleY = 32 / image.getHeight(null);
			imageScale = (scaleX < scaleY) ? scaleX : scaleY;
		}

		World.instance.regesterDrawable(this);
		World.instance.regesterUpdateable(this);
	}

	public void close() {
		World.instance.unregesterDrawable(this);
		World.instance.unregesterUpdatable(this);
	}

	abstract void destenationReached();

	void setDestination(int destination) {
		nextTile = null; // the next tile gets set to null so move() will
							// recalculate the path
		this.destenation = destination;
	}

	void move() {
		// System.out.println("current location: " + currentPosition);

		// we don't need to go anywhere so we can just return
		if (destenation == NO_DESTENATION) {
			System.out.println("no destination ");
			return;
		}
		// if the next time is null we have reset our destination so we need to
		// get our nextTile from the currentTile
		if (nextTile == null) {
			System.out.println("next tile is set");
			nextTile = currentTile.getDirection(destenation);
			nextPosition = new Point2D.Double(nextTile.X, -nextTile.Y);
		}
		// if the nextTile is the currentTile we have reached our position
		// NOTE: this is because Tile.getDirection will give us the tile itself
		// if it is the destination
		if (nextTile == currentTile) // we are at our destination
		{
			System.out.println("destenation reached");

			destenationReached();
		}
		// if we can reach our nextPosition this move we should move there and
		// look for our next tile
		else if (currentPosition.distance(nextPosition) <= speed) {
			System.out.println("kleiner dan speed");
			currentPosition = nextPosition;
			currentTile = nextTile;
			nextTile = nextTile.getDirection(destenation); // we get the next
															// tile from our
															// currentTile
			// the nextPosition gets generated
			// TODO make the generated point be semi random so not all agents
			// will move throu the same
			// points
			nextPosition = new Point2D.Float(nextTile.X * 32, nextTile.Y * 32);
		}

		// we move closer to the next position
		else {
			// TODO implement rotation
			// if (need to rotate) rotate()
			// if we don't need to rotate we move towards the nextPoint
			float remainingMoveDistance = World.instance.getDeltaTime() * speed;
			while (remainingMoveDistance > 0) {
				if (nextTile == currentTile) // we are at our destination
				{
					System.out.println("destination reached");
					destenationReached();
					destenation = NO_DESTENATION;
					return;
				}

				if (nextPosition.distance(currentPosition) <= remainingMoveDistance) {
					System.out.println("we can move to the next tile");
					remainingMoveDistance -= nextPosition.distance(currentPosition);
					currentPosition = nextPosition;
					currentTile = nextTile;
					nextTile = currentTile.getDirection(destenation);
					nextPosition = new Point2D.Double(nextTile.X, -nextTile.Y);
					System.out.println("next tile = (" + nextTile.X + ", " + nextTile.Y + ")");
				} else {
					System.out.println("we move towards the next tile");
					float tempX = (float) (nextPosition.getX() - currentPosition.getX()),
							tempY = (float) (nextPosition.getY() - currentPosition.getY());

					float magnitude = (float) Math.sqrt(tempX * tempX + tempY * tempY);
					tempX /= magnitude;
					tempY /= magnitude;

					// now we have a unit vector

					tempX *= remainingMoveDistance;
					tempY *= remainingMoveDistance;
					System.out.println("move vector (" + tempX + "  " + tempY + ")");
					remainingMoveDistance = 0;

					currentPosition.setLocation(currentPosition.getX() + tempX, currentPosition.getY() + tempY);
				}
			}
		}
	}

	@Override
	public void draw(Graphics2D graphics, AffineTransform t) {
		AffineTransform tempTrans = new AffineTransform(t);
		tempTrans.scale(imageScale, imageScale);

		double theta = Math.atan2(nextPosition.getY() - currentPosition.getY(),
				nextPosition.getX() - currentPosition.getX());

		graphics.drawRect(500, 1000, 40, 40);
		graphics.rotate(theta, (int) ((currentPosition.getX() - image.getWidth(null) / 2)),
				(int) ((currentPosition.getY() - image.getHeight(null) / 2)));
		graphics.drawRect(500, 500, 40, 40);
		graphics.drawImage(image, ((int) (currentPosition.getX() - image.getWidth(null) / 2)) * 32,
				(int) ((currentPosition.getY() - image.getHeight(null) / 2)) * -32, null);
	}
}
