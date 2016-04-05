package simulator;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

/* 
 * Deze klasse kan later toegevoegd worden aan de Visitor klasse
 * Alle waarden betreffende verloop zijn per seconde
 * 
 * TODO: Genre voorkeuren nader bepalen
 * 			>> Wanneer voorkeur voor genre speelt, daar naartoe
 * 			>> Wanneer voorkeur niet speelt, naar optreden met de hoogste populariteit
 * 			>> Wanneer populairste vol, volgende in de lijst
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

	private int aantalDrankjes, aantalSnacks;

	private double blaasInhoud = randomWaardeDouble(70, 100);
	private double drankHandling = randomWaardeDouble(10, 20);
	private double blaasToleratie = randomWaardeDouble(20, 50);

	private double maagInhoud = randomWaardeDouble(70, 100);
	private double snackHandling = randomWaardeDouble(10, 20);
	private double maagToleratie = randomWaardeDouble(20, 50);

	private double dorst = randomWaardeDouble(70, 100);
	private double verloopDorst = randomWaardeDouble(0.002, 0.005);
	private double dorstToleratie = randomWaardeDouble(20, 40);

	private double honger = randomWaardeDouble(70, 100);
	private double verloopHonger = randomWaardeDouble(0.002, 0.005);
	private double hongerToleratie = randomWaardeDouble(20, 40);

	private boolean toiletBezoek = false;
	private boolean grootToiletBezoek = false;
	private boolean drankBezoek = false;
	private boolean eetBezoek = false;
	
	private static ArrayList<Image> images = new ArrayList<>();

	public Visitor(Tile tile, float speed) {
		super(getImage(), tile, new Point2D.Double(tile.X, -tile.Y), speed);
		setDestination(i++ % 8);
	}

	@Override
	public void update() {
		bezoekFaciliteit();
		toiletBehoefte();
		setBlaasCapaciteit();
		groteBehoefte();
		setMaagCapaciteit();
		drankBehoefte();
		setDorstPercentage();
		snackBehoefte();
		setHongerPercentage();
		move();
		//System.out.println("DERP"); //TROLOLOLOLOLOLOL
	}

	@Override
	void destenationReached() {
		setDestination(i2++%8);
		//System.out.println("VISITOR destination reached");
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
		int getal = (int) (1 + Math.random()*16);
		return images.get(getal);
	}
	
	public void bezoekFaciliteit() {
		if (toiletBezoek == true) {
			// Wandel naar dichtsbijzijnde toilet
			// Interactie met toiletgebouw, if-looppie interactie
			aantalDrankjes = 0;
			blaasInhoud = 100;
			toiletBezoek = false;
		}
		if (grootToiletBezoek == true) {
			// Wandel naar dichtsbijzijnde toilet
			// Interactie met toiletgebouw, if-looppie interactie
			aantalSnacks = 0;
			aantalDrankjes = 0;
			maagInhoud = 100;
			blaasInhoud = 100;
			grootToiletBezoek = false;
		}
		if (drankBezoek == true) {
			// Wandel naar dichtsbijzijnde drankkraam
			// Interactie met drankkraam, if-looppie interactie
			aantalDrankjes++;
			dorst = 100;
			drankBezoek = false;
		}
		if (eetBezoek == true) {
			// Wandel naar dichtsbijzijnde eetkraam
			// Interactie met eetkraam, if-looppie interactie
			aantalSnacks++;
			honger = 100;
			eetBezoek = false;
		} else {
			// Doe helemaal niks jonguh
		}
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
		blaasInhoud = 100 - drankHandling * aantalDrankjes;
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
		maagInhoud = 100 - snackHandling * aantalSnacks;
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