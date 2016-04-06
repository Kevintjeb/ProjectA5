package simulator;

import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

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

public class Visitor extends Agent {
	static int i = 0;
	static int i2 = 0;
	private static final int MOD = 8;
	private int aantalDrankjes, aantalSnacks;

	private String genreVoorkeur;

	private double blaasInhoud = randomWaardeDouble(70, 100);
	private double drankHandling = randomWaardeDouble(10, 20);
	private double blaasToleratie = randomWaardeDouble(20, 50);

	private double maagInhoud = randomWaardeDouble(70, 100);
	private double snackHandling = randomWaardeDouble(10, 20);
	private double maagToleratie = randomWaardeDouble(20, 50);

	private double dorst = randomWaardeDouble(700, 1000);
	private double verloopDorst = randomWaardeDouble(0.5, 1.5);
	private double dorstToleratie = randomWaardeDouble(200, 400);

	private double honger = randomWaardeDouble(700, 1000);
	private double verloopHonger = randomWaardeDouble(0.5, 1.5);
	private double hongerToleratie = randomWaardeDouble(200, 400);

	private boolean toiletBezoek = false;
	private boolean grootToiletBezoek = false;
	private boolean drankBezoek = false;
	private boolean eetBezoek = false;

	private boolean reached = false;

	private static ArrayList<Image> images = new ArrayList<>();

	public Visitor(Tile tile, float speed) {
		super(getImage(), tile, new Point2D.Double(tile.X, -tile.Y), speed);
		setGenreVoorkeur();
		setDestination(i++ % MOD);
	}

	@Override
	public void update() {
		bezoekPerformance();
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
		System.out.println(reached + " reached <----");
		System.out.println(honger); // TROLOLOLOLOLOLOL
	}

	@Override
	void destenationReached() {
		setDestination(i2++ % MOD);
		System.out.println("reached");
		if (!reached)
			reached = true;
		// System.out.println("VISITOR destination reached");
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
	
	// TODO: afmaken
	public void bezoekPerformance() {
		ArrayList<String> genres = new ArrayList<String>();
		//genres.add(World.instance.agenda.getPerformances().get(0).getArtists().get(0).getGenre().(genreVoorkeur));
		if (World.instance.agenda.getPerformances().get(0).getArtists().get(0).getGenre().contains(genreVoorkeur)) {
			setDestination(
					World.instance.getPathID(World.instance.agenda.getPerformances().get(0).getStage().getName()));
		}
		else {
			setDestination(World.instance.agenda.getPerformances());
		}
	}

	public void bezoekFaciliteit() {
		if (toiletBezoek == true || grootToiletBezoek == true) {
			if (toiletBezoek == true) {
				setDestination(World.instance.getPathID("Toilet"));
				if (reached) {
					aantalDrankjes = 0;
					blaasInhoud = 100;
					toiletBezoek = false;
				}
			}
			if (grootToiletBezoek == true) {
				setDestination(World.instance.getPathID("Toilet"));
				if (reached) {
					aantalSnacks = 0;
					aantalDrankjes = 0;
					maagInhoud = 100;
					blaasInhoud = 100;
					grootToiletBezoek = false;
				}
			}
		} else {
			if (drankBezoek == true) {
				setDestination(World.instance.getPathID("Cafetaria"));
				if (reached) {
					aantalDrankjes++;
					dorst = 1000;
					drankBezoek = false;
					state = false;
				}
			}

			if (eetBezoek == true) {
				setDestination(World.instance.getPathID("Cafetaria"));
				if (reached) {
					aantalSnacks++;
					honger = 1000;
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