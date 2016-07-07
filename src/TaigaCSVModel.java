import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TaigaCSVModel {

	private ArrayList<String> members = new ArrayList<String>();
	private ArrayList<String> sprints = new ArrayList<String>();
	private List<String[]> data;

	public TaigaCSVModel(List<String[]> list) {
		data = list;
		for (String[] t : data) {
			if (t.length < 26)
				continue;
			// remplir les users
			String currentUser = t[getIndexOf("assigned_to")];
			if (!members.contains(currentUser) && !currentUser.equals(""))
				members.add(currentUser);
			// remplir les sprints
			String currentSprint = t[getIndexOf("sprint")];
			if (!sprints.contains(currentSprint) && !currentSprint.equals(""))
				sprints.add(currentSprint);
		}

	}

	/**
	 * 
	 * @return sprint, Nombre de tâche dans le sprint, Nombre de tâches
	 *         effectués, Temps prevu, Temps reel
	 */
	public List<String[]> getInfoForEachSprint(String member) {
		boolean executeForAllMembers = false;
		if(member == null || member.isEmpty())
			executeForAllMembers = true;
		ArrayList<String[]> retList = new ArrayList<String[]>();
		for (String sprint : sprints) {
			double tempsPrevu = 0;
			double tempsReel = 0;
			double taskNumber = 0;
			double taskCompletedNumber = 0;
			
			for (String[] line : data) {
				if (line.length < 26)
					continue;
				if (line[getIndexOf("sprint")].equals(sprint) && (line[getIndexOf("assigned_to")].equals(member) || executeForAllMembers)) {
					try {
						taskNumber++;
						if (line[getIndexOf("is_closed")].equals("TRUE")) {
							taskCompletedNumber++;
						}

						// calculer temps prévu de cette ligne de donnée
						String tpString = line[getIndexOf("Temps prevu")];
						double tp = 0;
						if (!tpString.isEmpty())
							tp = Double.parseDouble(tpString);
						tempsPrevu += tp;

						// calculer temps réel de cette ligne de donnée
						String trString = line[getIndexOf("Temps reel")];
						double tr = 0;
						if (!trString.isEmpty())
							tr = Double.parseDouble(trString);
						tempsReel += tr;

					} catch (Exception e) {
						System.out.println("error at" + line[0] + line[1]);
					}
				}
			}
			
			String[] retLine = { sprint, String.valueOf(taskNumber), String.valueOf(taskCompletedNumber),
					String.valueOf(tempsPrevu), String.valueOf(tempsReel) };
			retList.add(retLine);
		}
		return retList;
	}
	

	private int getIndexOf(String param) {
		if (param.equals("subject"))
			return 1;
		if (param.equals("sprint"))
			return 4;
		if (param.equals("assigned_to"))
			return 9;
		if (param.equals("is_closed"))
			return 13;
		if (param.equals("Temps prevu"))
			return 24;
		if (param.equals("Temps reel"))
			return 25;
		System.out.println("Cannot find the value in the array : returning subject");
		return 1;
	}

	public ArrayList<String> getMembers() {
		return members;
	}

	public ArrayList<String> getSprints() {
		return sprints;
	}

	public String[] getHeader() {
		// TODO Auto-generated method stub
		String [] a = {"Sprint", "nbr de taches", "nbr de taches completes", "temps prevu", "temps reel"};
		return a;
	}

}
