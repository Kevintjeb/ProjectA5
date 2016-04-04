package simulator;

import java.awt.Image;
import java.awt.geom.Point2D;

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

	private int aantalDrankjes, aantalSnacks;

	private double blaasInhoud = randomWaarde(70, 100);
	private double drankHandling = randomWaarde(10, 20);
	private double blaasToleratie = randomWaarde(20, 50);
	
	private double maagInhoud = randomWaarde(70, 100);
	private double snackHandling = randomWaarde(10, 20);
	private double maagToleratie = randomWaarde(20, 50);
	
	private double dorst = randomWaarde(70, 100);
	private double verloopDorst = randomWaarde(0.002, 0.005);
	private double dorstToleratie = randomWaarde(20, 40);
	
	private double honger = randomWaarde(70, 100);
	private double verloopHonger = randomWaarde(0.002, 0.005);
	private double hongerToleratie = randomWaarde(20, 40);

	private boolean toiletBezoek = false;
	private boolean grootToiletBezoek = false;
	private boolean drankBezoek = false;
	private boolean eetBezoek = false;

	public Visitor(Image image, Tile tile, Point2D point, float speed) {
		super(image, tile, point, speed);
	}

	@Override
	public void update() {
		move();
	}

	@Override
	void destenationReached() {
<<<<<<< HEAD
=======
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
	 * Zodra deze methode true teruggeeft, 
	 * dan moet de bezoeker pissen
	 */
	public boolean toiletBehoefte() {
		if (blaasInhoud <= blaasToleratie)
			toiletBezoek = true;
		else
			toiletBezoek = false;

		return toiletBezoek;
	}

	public double getBlaasCapaciteit() { return blaasInhoud; }
	public void setBlaasCapaciteit() { blaasInhoud = 100 - drankHandling * aantalDrankjes; }
	
	/*
	 * Zodra deze methode true teruggeeft,
	 * dan moet de bezoeker een grote boodschap doen
	 */
	public boolean groteBehoefte() {
		if (maagInhoud <= maagToleratie)
			grootToiletBezoek = true;
		else
			grootToiletBezoek = false;

		return grootToiletBezoek;
	}

	public double getMaagCapaciteit() { return maagInhoud; }
	public void setMaagCapaciteit() { maagInhoud = 100 - snackHandling * aantalSnacks; }

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

	public double getDorstPercentage() { return dorst; }
	public void setDorstPercentage() { dorst = dorst - verloopDorst; }
	
	/*
	 * Zodra deze methode true teruggeeft,
	 * dan moet de bezoeker iets gaan eten
	 */
	public boolean snackBehoefte() {
		if (honger <= hongerToleratie)
			eetBezoek = true;
		else
			eetBezoek = false;

		return eetBezoek;
	}

	public double getHongerPercentage() { return honger; }
	public void setHongerPercentage() { honger = honger - verloopHonger; }
	
	/*
	 * Genereer hiermee een random waarden tbv attributen
	 */
	public double randomWaarde(double min, double max) {
		double randomInt = min + (double)(Math.random() * ((max - min) + 1));
		return randomInt;
>>>>>>> 2db1cf386d91a9805f057eb41a218cb695f0f8d6
	}
}
