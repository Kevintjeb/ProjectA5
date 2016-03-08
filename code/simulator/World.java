package simulator;

public class World {
	public static World instance;
	
	int getDeltaTime()
	{
		// TODO fix this
		return 0;
	}
	
	void regesterDrawable(Drawable d) {}
	void unregesterDrawable(Drawable d) {}
	
	void regesterUpdateable(Updateable u) {}
	void unregesterUpdatable(Updateable u) {}
}
