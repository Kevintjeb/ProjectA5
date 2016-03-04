
// @author Flobo, Menno

// to safe and load the agenda
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable; // to make the object safable
import java.util.ArrayList; // to use an ArrayList for storage
import java.util.Collections; // to use the Collections.Sort

public class Agenda implements Serializable {
	private static final long serialVersionUID = -1739310193034944061L;
	ArrayList<Artist> artists;
	ArrayList<Stage> stages;
	ArrayList<Performance> performances;

	// TODO create a constuct method to prevent code duplication between
	// constructors

	private void construct() {
		artists = new ArrayList<Artist>();
		stages = new ArrayList<Stage>();
		performances = new ArrayList<Performance>();
	}

	public Agenda() {
		construct();
	}

	// constructs an Agenda from a saved Agenda file
		public Agenda(String filePath) {
			// attemt to load to load the agenda
			Agenda temp = load(filePath);
			// if it was succesfully loaded 'copy' the agenda
			if (temp != null) {
				artists = temp.getArtist();
				stages = temp.getStages();
				performances = temp.getPerformances();
			}
			// otherwise construct an empty Agenda and display a warning
			else {
				System.out.println("Agenda::Agenda(filePath) : WARNING: agenda could not be loaded");
				construct();
			}
		}

	public ArrayList<Artist> getArtist() {
		return artists;
	}

	public ArrayList<Stage> getStages() {
		return stages;
	}

	public ArrayList<Performance> getPerformances() {
		return performances;
	}

	// returns all the preformances that the aritist preforms in
	public ArrayList<Performance> getArtistsPerformances(Artist artist) {
		ArrayList<Performance> performances = new ArrayList<Performance>();

		for (Performance performance : this.performances) // iliterates over all
															// performances
			if (performance.getArtists().contains(artist)) // if the artist
															// performs in the
															// performance
				performances.add(performance); // add the performance to the
												// performances

		return performances;
	}

	// returns all the stages that the artist preforms on
	public ArrayList<Stage> getArtistsStages(Artist artist) {
		ArrayList<Stage> stages = new ArrayList<Stage>();

		for (Performance performance : getArtistsPerformances(artist)) // iliterates
																		// over
																		// all
																		// the
																		// performaces
																		// that
																		// the
																		// artist
																		// performs
																		// in
			if (stages.contains(performance.getStage()) == false) // if stage
																	// hasn't
																	// already
																	// been
																	// added to
																	// stages
				stages.add(performance.getStage()); // add stage to stages

		return stages;
	}

	public ArrayList<Performance> getStagesPerformances(Stage stage) {
		ArrayList<Performance> performances = new ArrayList<Performance>();

		for (Performance performance : this.performances) // iliterates over all
															// the performances
			if (performance.getStage().getName().equals(stage.getName())) // if
																			// the
																			// stage
																			// is
																			// the
																			// stage
																			// of
																			// the
																			// performance
				performances.add(performance); // add the performance to
												// performances

		return performances;
	}

	public ArrayList<Artist> getStagesArtists(Stage stage) {
		ArrayList<Artist> artists = new ArrayList<Artist>();

		for (Performance performance : getStagesPerformances(stage)) // iliterates
																		// over
																		// all
																		// of
																		// the
																		// performances
																		// that
																		// happen
																		// on
																		// the
																		// stage
			for (Artist artist : performance.getArtists()) // iliterates over
															// all of the
															// performance's
															// artists
				if (artists.contains(artist) == false) // checks if the artist
														// is already in artists
					artists.add(artist); // if not it adds it

		return artists;
	}

	// if highTop is positive the higest item is the first in
	// the list if it is false it is the higest
	public void sortPerformances(PerformaceComparator.SortTypes sortType, PerformaceComparator.SortOrder sortOrder) {
		Collections.sort(performances, new PerformaceComparator(sortType, sortOrder));
	}

	public void save(String filePath) {
		save(this, filePath);
	}

	public static void save(Agenda agenda, String filePath) {
		// TODO more checks?

		try {
			FileOutputStream out = new FileOutputStream(filePath);
			ObjectOutputStream objOut = new ObjectOutputStream(out);

			objOut.writeObject(agenda);

			objOut.close();
		} catch (IOException e) {
			System.out.println("Agenda::safe(agenda, filePath) : ERROR: an exeption ocured while safing the file");
		}
	}

	public static Agenda load(String filePath) {
		// TODO more checks?

		try {
			FileInputStream in = new FileInputStream(filePath);
			ObjectInputStream objIn = new ObjectInputStream(in);
			Agenda agenda = (Agenda) objIn.readObject();

			objIn.close();
			return agenda;
		} catch (IOException e) {
			System.out.println("Agenda::load(filePath) : ERROR: an exception ocured when loading the file");
			return null;
		} catch (ClassNotFoundException e) {
			System.out.println("Agenda::load(filePath) : ERROR: Agenda wasn't in the safe file");
			return null;
		}
	}

	@Override
	public String toString() {
		String s = "";

		s += "stages\n";
		for (Stage stage : stages)
			s += "\t" + stage.toString() + "\n";
		s += "artists\n";
		for (Artist a : artists)
			s += "\t" + a.toString() + "\n";
		s += "performances\n";
		for (Performance p : performances)
			s += p.toString("\t") + "\n\n";

		return s;
	}
}
