package simulator;

// just for testing
public class __Main {
	public static void main(String[] args)
	{
		World w = new World();
		
		class Foo implements Updateable
		{

			public Foo()
			{
				World.instance.regesterUpdateable(this);
			}
			
			@Override
			public void update() {
				System.out.println(World.instance.getDeltaTime());
				System.out.println(World.instance.getWorldTime());
				System.out.println(new agenda.Time(World.instance.getWorldTime()/60)+"\n");
			}

			@Override
			public void close() {
				World.instance.unregesterUpdatable(this);
			}
			
		}
		
		
		new Foo();
		
		while (true)
		{
			w.update();
			
			try
			{
				Thread.sleep(1000/60);
			}
			catch (Exception e)
			{
				
			}
		}
	}
}
