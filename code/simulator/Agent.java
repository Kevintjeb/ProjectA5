package simulator;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Point2D;

abstract class Agent implements Updateable, Drawable {
	private Point2D currentPosition, nextPosition;
	private final Image image;
	private Tile currentTile, nextTile;
	private int destenation;
	private float speed;
	private float rotation;
	
	public static final int NO_DESTENATION = -1;
	
	public Agent(Image image, Tile tile, Point2D point, float speed)
	{
		this.image = image;
		this.currentTile = tile;
		this.nextTile = tile;
		this.destenation = NO_DESTENATION;
		this.speed = speed;
		this.rotation = 0.0f;
		
		World.instance.regesterDrawable(this);
		World.instance.regesterUpdateable(this);
	}
	
	public void close()
	{
		World.instance.unregesterDrawable(this);
		World.instance.unregesterUpdatable(this);
	}
	
	abstract void destenationReached();
	
	void setDestination(int destination)
	{
		nextTile = null; // the next tile gets set to null so move() will recalculate the path
		this.destenation = destination;
	}
	
	void move()
	{
		// we don't need to go anywhere so we can just return
		if (destenation == NO_DESTENATION)
			return;
		
		// if the next time is null we have reset our destination so we need to
		// get our nextTile from the currentTile
		if (nextTile == null)
			nextTile = currentTile.getDirection(destenation);
		
		// if the nextTile is the currentTile we have reached our position
		// NOTE: this is because Tile.getDirection will give us the tile itself if it is the destination
		if (nextTile == currentTile) // we are at our destination
			destenationReached();
		
		// if we can reach our nextPosition this move we should move there and look for our next tile
		else if (currentPosition.distance(nextPosition) <= speed)
		{
			currentPosition = nextPosition; 
			currentTile = nextTile;
			nextTile = nextTile.getDirection(destenation); // we get the next tile from our currentTile
			// the nextPosition gets generated
			// TODO make the generated point be semi random so not all agents will move throu the same 
			//      points
			nextPosition = new Point2D.Float(nextTile.X*Tile.getTileSize(), nextTile.Y*Tile.getTileSize());
		}
		
		// we move closer to the next position
		else
		{
			// TODO implement rotation
			// if (need to rotate) rotate()
			// if we don't need to rotate we move towards the nextPoint
			{
				float distance = (float) currentPosition.distance(nextPosition);
				float modifier = speed*World.instance.getDeltaTime();
				currentPosition = new Point2D.Float(
						(float)Math.cos((nextPosition.getX()-currentPosition.getX())/distance)*modifier,
						(float)Math.sin((nextPosition.getY()-currentPosition.getY())/distance)*modifier
						);
			}
			
		}
	}
	
	public void draw(Graphics2D graphics)
	{
		// TODO implement the rotation
		graphics.drawImage(image, (int)(currentPosition.getX()+image.getWidth(null)/2),
				(int)(currentPosition.getY()+image.getHeight(null)/2), null);
	}
}
