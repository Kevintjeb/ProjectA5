package simulator;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import agenda.Artist;
import agenda.Performance;

/* 
 * Deze klasse kan later toegevoegd worden aan de Visitor klasse
 * Alle waarden betreffende verloop zijn per seconde
 * 
 * TODO: Genre voorkeuren nader bepalen
 * 			>> Wanneer voorkeur voor genre speelt, daar naartoe
 * 			>> Wanneer voorkeur niet speelt, naar optreden met de hoogste populariteit
 * 			>> Wanneer populairste vol, volgende in de lijst
 * TODO: Waarden voor gedrag natuurlijker maken
 * 
 * -- Thijs
 */

/* 
  * Deze klasse kan later toegevoegd worden aan de Visitor klasse
  * Alle waarden betreffende verloop zijn per seconde
  * 
  * TODO: Random genrevoorkeur meegeven, kan door middel 
  * 	  van performances uit te lezen
  * TODO: Implementeren, kan wanneer Pathfinding werkt
  * TODO: Zakgeld toevoegen
  * TODO: Static array maken met agent images erin, geheugen friendly alles
  * Optional TODO: Omdraainen waarden van oplopend naar aflopend
  * 
  * -- Thijs
  */

public class Visitor extends Agent {

	static int i = 0;
	static int i2 = 0;
	private int mod = 0;
	private int aantalDrankjes, aantalSnacks;

	private String genreVoorkeur;

	private double blaasInhoud = randomWaardeDouble(7000, 10000);
	private double drankHandling = randomWaardeDouble(0.5, 1.5);
	private double blaasToleratie = randomWaardeDouble(200, 500);

	private double maagInhoud = randomWaardeDouble(7000, 10000);
	private double snackHandling = randomWaardeDouble(0.5, 1.5);
	private double maagToleratie = randomWaardeDouble(200, 500);

	private double dorst = randomWaardeDouble(7000, 10000);
	private double verloopDorst = randomWaardeDouble(0.5, 1.5);
	private double dorstToleratie = randomWaardeDouble(200, 400);

	private double honger = randomWaardeDouble(7000, 10000);
	private double verloopHonger = randomWaardeDouble(0.5, 1.5);
	private double hongerToleratie = randomWaardeDouble(200, 400);

	private boolean toiletBezoek = false;
	private boolean grootToiletBezoek = false;
	private boolean drankBezoek = false;
	private boolean eetBezoek = false;

	private int destination;
	private int teller = 0;
	private Point2D nextDancePosition;
	private Point2D currentDancePosition;
	private float remainingDanceDistance;
	private boolean toDance = false;
	private int voorkeur = 0;
	private boolean reached = false;
	private boolean voorkeurchecked = false;
	private int podiumsize = 0;
	private static ArrayList<Image> images = new ArrayList<>();

	public Visitor(Tile tile, float speed) {
		super(getImage(), tile, new Point2D.Double(tile.X, -tile.Y), speed);
		mod = World.instance.getSizeBuildingID();
		int dest = i++ % mod;

		setDestination(dest);
		destination = dest;
		// System.out.println(World.instance.getBuildings().get(dest).toString());
		setGenreVoorkeur();
	}

	@Override
	public void update() {
		bezoekPerformance();
		bezoekFaciliteit();
		danceMethod();
		toiletBehoefte();
		setBlaasCapaciteit();
		groteBehoefte();
		setMaagCapaciteit();
		drankBehoefte();
		setDorstPercentage();
		snackBehoefte();
		setHongerPercentage();
		if (toDance) {
			teller++;
		}
		move();
	}

	public Tile[] dance(String destenation) {
		if (destenation.toLowerCase().endsWith("stage")) {
			Stage stage = null;
			for (Building b : World.instance.getBuildings()) {
				if (b.name.equals(destenation)) {
					stage = (Stage) b;
				}
			}

			ArrayList<Tile> dancefloor = new ArrayList<>();
			dancefloor.addAll(stage.getDanceFloor());

			Tile punt1 = dancefloor.get((int) (Math.random() * dancefloor.size() - 1));
			Tile punt2 = dancefloor.get((int) (Math.random() * dancefloor.size() - 1));
			Tile punt3 = dancefloor.get((int) (Math.random() * dancefloor.size() - 1));

			Tile[] tileArray = { punt1, punt2, punt3 };
			return tileArray;
		} else {
			return null;
		}
	}

	@Override
	void destenationReached() {

		// setDestination(i2++ % MOD);

		if (World.instance.getBuildings().get(this.getDestinationOld()).toString().toLowerCase().endsWith("stage")) {
			toDance = true;
		} else {
			// System.out.println(World.instance.getBuildings().get(this.getDestinationOld()).toString());
			toDance = false;
			int dest = i2++ % mod;
			destination = dest;
			setDestination(dest);
			if (!reached)
				reached = true;
		}

		// System.out.println("Destination reached! destination : " +
		// destination + " stage ja nee? "
		// +
		// World.instance.getBuildings().get(this.getDestinationOld()).toString().toLowerCase().endsWith("stage")
		// + " --- building : " +
		// World.instance.getBuildings().get(this.getDestinationOld()).toString()
		// + " --- dancing : " + toDance);

	}

	public void danceMethod() {

		if (toDance) {
			if (teller < 100) {
				// STAAT STIL ATM.
				// int tellertje = 0;
				// Tile[] tiles =
				// dance(World.instance.getBuildings().get(this.getDestinationOld()).toString());
				// remainingDanceDistance = 0;
				// if (nextDancePosition.distance(getCurrentPosition()) <=
				// remainingDanceDistance) {
				// remainingDanceDistance -=
				// nextDancePosition.distance(getCurrentPosition());
				// setCurrentPosition(nextDancePosition);
				// nextDancePosition = new Point2D.Double(tiles[tellertje].X,
				// -tiles[tellertje].Y);
				// tellertje++;
				// } else {
				//
				// float tempX = (float) (nextDancePosition.getX() -
				// getCurrentPosition().getX()),
				// tempY = (float) (nextDancePosition.getY() -
				// getCurrentPosition().getY());
				//
				// float magnitude = (float) Math.sqrt(tempX * tempX + tempY *
				// tempY);
				// tempX /= magnitude;
				// tempY /= magnitude;
				//
				// // now we have a unit vector
				//
				// tempX *= remainingDanceDistance;
				// tempY *= remainingDanceDistance;
				//
				// remainingDanceDistance = 0;
				//
				// setCurrentPosition(new
				// Point2D.Double(getCurrentPosition().getX() + tempX ,
				// getCurrentPosition().getY() + tempY));
				// }

			} else {
				toDance = false;
				if (!toDance) {// System.out.println("dance voorbij");
					teller = 0;
					int dest = i++ % mod;
					setDestination(dest);
					// System.out.println("dest set " + dest);
					destination = dest;
				}
			}
		}
	}

	public static Image getImage() {
		if (images.isEmpty()) {
			File[] lijst = new File("static_data/sprites/").listFiles();
			for (File f : lijst) {
				try {
					BufferedImage temp = (BufferedImage) ImageIO.read(f);
					images.add(temp.getScaledInstance(32, 32, BufferedImage.SCALE_FAST));
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
		int getal = (int) (1 + Math.random() * 16);
		return images.get(getal);
	}

	
	public void bezoekPerformance() {
		
		ArrayList<String> podias = new ArrayList<>();
		if (World.instance.getActualPerformances(World.instance.getTime()) != null) {
			podias.addAll(World.instance.getActualPerformances(World.instance.getTime()));
			if (!podias.isEmpty()) {
				System.out.println("nu zijn er performances op stage :\n " + podias.toString());
				//TODO CHECK WELKE HEEFT VOORKEUR.
				if(podiumsize != podias.size())
				{
					voorkeurchecked = false;
				}
				if(podias.size() > 0 && !voorkeurchecked)
				{
					voorkeur = (int) (Math.random()*podias.size());
					voorkeurchecked = true;
					podiumsize = podias.size();
				}
				
				
				setDestination(World.instance.getPathID(podias.get(voorkeur)));
			}
			else
			{
				//TODO exit location
			}

		}
		
	}


	public void bezoekFaciliteit() {
		if (toiletBezoek == true || grootToiletBezoek == true) {
			if (toiletBezoek == true) {
				setDestination(World.instance.getPathID("Toilet"));
				if (reached) {
					aantalDrankjes = 0;
					blaasInhoud = 5000;
					toiletBezoek = false;
				}
			}
			if (grootToiletBezoek == true) {
				setDestination(World.instance.getPathID("Toilet"));
				if (reached) {
					aantalSnacks = 0;
					aantalDrankjes = 0;
					maagInhoud = 5000;
					blaasInhoud = 5000;
					grootToiletBezoek = false;
				}
			}
		} else {
			if (drankBezoek == true) {
				setDestination(World.instance.getPathID("Cafetaria"));
				if (reached) {
					aantalDrankjes++;
					dorst = 5000;
					drankBezoek = false;
				}
			}

			if (eetBezoek == true) {
				setDestination(World.instance.getPathID("Cafetaria"));
				if (reached) {
					aantalSnacks++;
					honger = 5000;
					eetBezoek = false;
				}

			}
		}
		reached = false;
	}

	/*
	 * Zodra deze methode true teruggeeft, dan moet de bezoeker pissen
	 */
	public boolean toiletBehoefte() {
		if (blaasInhoud <= blaasToleratie)
			toiletBezoek = true;
		else
			toiletBezoek = false;

		return toiletBezoek;
	}

	public double getBlaasCapaciteit() {
		return blaasInhoud;
	}

	public void setBlaasCapaciteit() {
		blaasInhoud = blaasInhoud - drankHandling * aantalDrankjes;
	}

	/*
	 * Zodra deze methode true teruggeeft, dan moet de bezoeker een grote
	 * boodschap doen
	 */
	public boolean groteBehoefte() {
		if (maagInhoud <= maagToleratie)
			grootToiletBezoek = true;
		else
			grootToiletBezoek = false;

		return grootToiletBezoek;
	}

	public double getMaagCapaciteit() {
		return maagInhoud;
	}

	public void setMaagCapaciteit() {
		maagInhoud = maagInhoud - snackHandling * aantalSnacks;
	}

	/*
	 * Zodra deze methode true teruggeeft, dan moet de bezoeker het op een
	 * zuipen zetten
	 */
	public boolean drankBehoefte() {
		if (dorst <= dorstToleratie)
			drankBezoek = true;
		else
			drankBezoek = false;

		return drankBezoek;
	}

	public double getDorstPercentage() {
		return dorst;
	}

	public void setDorstPercentage() {
		dorst = dorst - verloopDorst;
	}

	/*
	 * Zodra deze methode true teruggeeft, dan moet de bezoeker iets gaan eten
	 */
	public boolean snackBehoefte() {
		if (honger <= hongerToleratie)
			eetBezoek = true;
		else
			eetBezoek = false;

		return eetBezoek;
	}

	public double getHongerPercentage() {
		return honger;
	}

	public void setHongerPercentage() {
		honger = honger - verloopHonger;
	}

	public String getGenreVoorkeur() {
		return genreVoorkeur;
	}

	public void setGenreVoorkeur() {
		ArrayList<String> genres = new ArrayList<String>() {
			{
				add("Rock");
				add("Schlager");
				add("Pop");
				add("Funk");
				add("Metal");
				add("Techno");
				add("Hiphop");
				add("R&B");
				add("Klassiek");
				add("Volksmuziek");
				add("Jazz");
				add("Soul");
				add("Religieus");
				add("Disco");
				add("Blues");
			}
		};
		genreVoorkeur = genres.get(randomWaardeInt(0, 14));
	}

	/*
	 * Genereer hiermee een random waarden tbv attributen
	 */
	public int randomWaardeInt(int min, int max) {
		int randomInt = min + (int) (Math.random() * ((max - min) + 1));
		return randomInt;
	}

	public double randomWaardeDouble(double min, double max) {
		double randomDouble = min + (double) (Math.random() * ((max - min) + 1));
		return randomDouble;
	}

}
