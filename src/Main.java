import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class Main {
	final JFileChooser fc = new JFileChooser();

	public static void main(String[] args) {
		// loader le fichier csv
		File scrumDump = loadCSVFile();
		// le charger dans une list
		List<String[]> scrumDumpList = loadListCSV(scrumDump);
		// le parser dans un modèle
		TaigaCSVModel taigaModel = new TaigaCSVModel(scrumDumpList);
		//printSingleList(taigaModel.getUsers());
		//printSingleList(taigaModel.getSprints());
		//printList(taigaModel.getInfoForEachSprint(""));
		File scrumFolder = getDumpFolder();
		writeScrumDumps(scrumFolder, taigaModel);
	}

	private static void writeScrumDumps(File scrumFolder, TaigaCSVModel taigaModel) {
		try {
			CSVWriter writer = new CSVWriter(new FileWriter(scrumFolder.getAbsolutePath() + "\\all.csv"));
			writer.writeNext(taigaModel.getHeader());
			for(String[] line : taigaModel.getInfoForEachSprint(null)) {
				writer.writeNext(line);
			}
			writer.close();
			
			for(String member : taigaModel.getMembers()) {
				CSVWriter writer1 = new CSVWriter(new FileWriter(scrumFolder.getAbsolutePath() + "\\"+ member +".csv"));
				writer1.writeNext(taigaModel.getHeader());
				for(String[] line : taigaModel.getInfoForEachSprint(member)) {
					writer1.writeNext(line);
				}
				writer1.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static File getDumpFolder() {
		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setCurrentDirectory(new File("C:\\Users\\User\\Google Drive\\Projet E16\\SCRUM\\Analyse heures\\dump"));
		File scrumFolder = null;
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			scrumFolder = fc.getSelectedFile();
			System.out.println("writing to : " + scrumFolder.getAbsolutePath() + ".");
		} else {
			System.out.println("Open command cancelled by user.");
			System.exit(1);
		}

		if (!scrumFolder.exists()) {
			System.out.println("Le fichier n'existe pas");
			System.exit(0);
		}
		return scrumFolder;
	}

	private static File loadCSVFile() {
		JOptionPane.showMessageDialog(null, "ATTENTION il faut enlever toutes les descriptions des tâches pour que ça fonctionne");
		final JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV", "csv");
		fc.setFileFilter(filter);
		fc.setCurrentDirectory(new File("C:\\Users\\User\\Google Drive\\Projet E16\\SCRUM\\Analyse heures"));
		File scrumDump = null;
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			scrumDump = fc.getSelectedFile();
			// This is where a real application would open the file.
			System.out.println("Opening: " + scrumDump.getAbsolutePath() + ".");
		} else {
			System.out.println("Open command cancelled by user.");
			System.exit(1);
		}

		if (!scrumDump.exists()) {
			System.out.println("Le fichier n'existe pas");
			System.exit(0);
		}
		return scrumDump;
	}

	private static List<String[]> loadListCSV(File scrumDump) {
		CSVReader reader = null;
		List<String[]> myEntries = null;
		try {
			reader = new CSVReader(new InputStreamReader(new FileInputStream(scrumDump.getAbsolutePath()), "UTF-8"),
					',', '\n', 1);
			myEntries = reader.readAll();

		} catch (IOException e) {
			e.printStackTrace();
		}
		return myEntries;
	}
	private static void printList(List<String[]> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j = 0; j < list.get(i).length; j++) {
				System.out.print(list.get(i)[j] + ", ");
			}
			System.out.println();
		}
	}
	
	private static void printSingleList(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
				System.out.print(list.get(i) + ", ");
		}
		System.out.println();
	}
}
