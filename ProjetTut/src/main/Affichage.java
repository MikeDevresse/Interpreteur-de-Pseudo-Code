package main;

public class Affichage {
	private String[] code;
	
	public Affichage(String[] code) {
		this.code = code;
	}
	
	public void afficher(String donnees, String console) {
		String affichage = "";
		
		int cpt=0;
		int cptTExec=0;
		
		String[] lignesExec = donnees.split("\n");
		
		affichage += String.format("-----------%31s-----------\n", " ");
		affichage += String.format("| CODE    |%31s| DONNEES |\n", " ");
		affichage += "---------------------------------------------------------------------------------\n";
		
		for(String str : code) {
			str = str.replace("\t", "  ");
			cpt++;
			affichage += String.format("|%2d %-38s|", cpt, str);
			if(cptTExec < lignesExec.length)
				affichage += String.format("%-37s|", lignesExec[cptTExec]);
			else
				affichage += String.format("%-37s|", " ");
			cptTExec++;
			affichage += "\n";
		}
		
		affichage += "---------------------------------------------------------------------------------\n\n";
		affichage += "-----------\n";
		affichage += "| CONSOLE |\n";
		affichage += "---------------------------------------------------------------------------------\n";
		
		for(String str : console.split("\n")) {
			affichage += String.format("|%80s|", str);
		}
		
		System.out.println(affichage);
	}
}
