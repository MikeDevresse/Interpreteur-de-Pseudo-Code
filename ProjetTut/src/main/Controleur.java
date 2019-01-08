package main;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ihmCui.Affichage;
import ihmGui.GUI;
import pseudoCode.Algorithme;
import pseudoCode.AlgorithmeException;
import pseudoCode.Donnee;
import pseudoCode.Programme;
import pseudoCode.Tableau;

/**
 * La classe controleur permet d'exécuter le programme et d'interagir avec l'IHM
 */
public class Controleur {

	public static boolean DEBUG = false;
	private static Controleur ctrl;

	private String derniereCommande;
	private String configFile;

	private Programme prog;
	private LectureFichier lecture;
	private Scanner sc;

	private int ligneAAttendre = -1;
	private int ligneRestantes = -1;
	private int revenir = -1;

	private double tempDefaut;

	private ArrayList<Integer> etapes;
	private ArrayList<Integer> anciennesEtapes;
	private ArrayList<Integer> breakpoints;
	private ArrayList<String> varsALire;
	private ArrayList<String> varsLu;
	private HashMap<Integer, Double> temps;
	private HashMap<Integer, String> comms;

	private Affichage aff;
	private GUI gui;

	private boolean peutContinuer;
	private boolean attendBreakpoint;
	private boolean modeGui;
	private boolean marcheAuto;

	public static Controleur getControleur() {
		if (Controleur.ctrl == null)
			return new Controleur("", "", false);
		else
			return Controleur.ctrl;
	}

	/**
	 * Constructeur du controleur.
	 */
	private Controleur(String fichier, String configFile, boolean modeGui) {

		this.varsALire = new ArrayList<String>();
		this.varsLu = new ArrayList<String>();
		this.breakpoints = new ArrayList<Integer>();
		this.etapes = new ArrayList<Integer>();

		this.temps = new HashMap<Integer, Double>();
		this.comms = new HashMap<Integer, String>();

		this.configFile = configFile;
		this.modeGui = modeGui;

		Controleur.ctrl = this;
		this.sc = new Scanner(System.in);
		this.lecture = new LectureFichier(fichier);

		// si aucun fichier de configuration n'est renseigné
		if (!this.configFile.equals("")) {
			try {
				lancerConfig();
			} catch (Exception e) {
				// fichier de configuration invalide
				System.out
						.println("/!\\ ERREUR : le fichier de configuration est invalide. Exécution en mode normal...");
			}
		}

		// création du programme
		try {
			this.prog = new Programme(lecture.getTexteParLigne());
		} catch (AlgorithmeException e) {
			e.printStackTrace();
		}

		// Récupération des variables à tracer
		getVariableATracer();

		// création de l'IHM
		if (this.modeGui)
			this.gui = new GUI(lecture.getTexteParLigne(), prog);
		else
			this.aff = new Affichage(lecture.getTexteParLigne(), prog);

		// exécution de l'interprétation
		while (!prog.getMain().estTerminer()) {
			try {
				prog.getCurrent().ligneSuivante();
			} catch (AlgorithmeException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * Permet à l'IHM GUI d'envoyer une commande au controleur
	 * 
	 * @param s commande
	 */
	public void envoyerCommande(String s) {
		this.derniereCommande = s;
		this.peutContinuer = true;
	}

	/**
	 * Lit et interprète le fichier de configuration
	 * 
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void lancerConfig() throws NumberFormatException, IOException {
		String s = "";
		BufferedReader reader = new BufferedReader(new FileReader(this.configFile));

		while ((s = reader.readLine()) != null) {
			if (s.indexOf(":") == -1)
				continue;

			String nom = s.split(":")[0].trim().toLowerCase();
			String valeur = s.split(":")[1].trim().toLowerCase();

			// marche automatique
			if (nom.matches("marche auto.*")) {
				marcheAuto = valeur.equals("oui");
			}

			// temporisation des lignes
			if (nom.matches("temp.*")) {
				if (nom.matches("temp globale.*")) {
					this.tempDefaut = Double.parseDouble(valeur);
				}
				if (nom.matches("temp l[0-9]+.*")) {
					int ligne = Integer.parseInt(nom.replaceAll("temp l([0-9]+).*", "$1"));
					temps.put(ligne, Double.parseDouble(valeur));
				}
			}

			// commentaires
			if (nom.matches("comm l[0-9]+.*")) {
				int ligne = Integer.parseInt(nom.replaceAll("comm l([0-9]+).*", "$1"));
				comms.put(ligne, valeur);
			}
		}

	}

	/**
	 * Permet à l'utilisateur de renseigner la valeur d'une variable
	 * 
	 * @param nomVar nom de la variable
	 */
	public void lireVariable(String nomVar) {
		String valeur = "";
		if (this.varsALire.isEmpty()) {

			if (this.modeGui)
				gui.repaint();
			else
				aff.afficher();

			System.out.print("Entrez la valeur de " + nomVar + " : ");
			if (this.modeGui) {
				do {
					System.out.print("");
				} while (!peutContinuer);
				valeur = this.derniereCommande;
				peutContinuer = false;
			} else {
				valeur = this.sc.nextLine();
				this.derniereCommande = valeur;
			}
		} else {
			valeur = varsALire.remove(0);
		}
		this.varsLu.add(valeur);
		this.prog.traceExec += "l:" + valeur + "\n";
		this.prog.getCurrent().setValeur(nomVar, valeur);
	}

	/**
	 * Demande à l'utilisateur les variables à tracer
	 */
	public void getVariableATracer() {
		for (Algorithme algo : this.prog.getAlgos()) {
			for (Donnee d : algo.getDonnees()) {
				if (d.estTracable()) {
					System.out.print(
							"Tracer la variable \"" + d.getNom() + "\" de l'algo " + algo.getNom() + " (Y/n) : ");
					String reponse = sc.nextLine();
					if (reponse.trim().equalsIgnoreCase("Y") || reponse.trim().equals("")) {
						prog.ajouterDonneeATracer(d);
					}
				}

			}
		}
	}

	/**
	 * Attend une action de l'utilisateur
	 */
	public void attend() {
		if (Controleur.DEBUG)
			return;

		// affichage de l'IHM
		if (this.modeGui)
			this.gui.repaint();
		else
			this.aff.afficher();

		if (!this.prog.getMain().estEnTrainDeReset())
			etapes.add(this.prog.getCurrent().getLigneCourrante());

		boolean estSurBreakpoint = false;
		for (int i = 0; i < breakpoints.size(); i++) {
			if (this.breakpoints.get(i) == this.prog.getCurrent().getLigneCourrante()) {
				estSurBreakpoint = true;
				break;
			}
		}

		if (this.ligneRestantes > 0) {
			ligneRestantes--;
		} else if (revenir != -1 && this.anciennesEtapes.size() > revenir) {
			if (this.anciennesEtapes.get(0) == this.prog.getCurrent().getLigneCourrante())
				this.anciennesEtapes.remove(0);
		} else if (ligneAAttendre != -1 && ligneAAttendre > prog.getCurrent().getLigneCourrante()) {

		} else if (attendBreakpoint && !estSurBreakpoint) {

		} else if (!marcheAuto) {
			revenir = -1;
			ligneRestantes = -1;
			ligneAAttendre = -1;
			this.attendBreakpoint = false;
			String commande;
			if (this.modeGui) {
				do {
					System.out.print("");
				} while (!peutContinuer);
				commande = this.derniereCommande;
				peutContinuer = false;
			} else {
				commande = this.sc.nextLine();
				this.derniereCommande = commande;
			}
			
			/*
			 * Gestion des commandes
			 */
			if (commande.equalsIgnoreCase("b")) {
				retour();
				this.prog.traceExec += "a:" + commande + "\n";
			} else if (commande.matches("[\\+\\-] var [\\w]+")) {
				String varATracer = commande.replaceAll("[\\+\\-] var ([\\w]+)", "$1");
				boolean ajouter = commande.replaceAll("([\\+\\-]) var [\\w]+", "$1").equals("+");
				for (Algorithme algo : this.prog.getAlgos()) {
					for (Donnee d : algo.getDonnees()) {
						if (d.getNom().equals(varATracer)) {
							if (ajouter) {
								prog.ajouterDonneeATracer(d);
							} else {
								prog.enleverDonneeATracer(d);
							}
						}
					}
				}
				reste();
				this.prog.traceExec += "a:" + commande + "\n";
			} else if (commande.matches("[Ll][0-9]+")) {
				int ligne = Integer.parseInt(commande.replaceAll("[Ll]([0-9]+)", "$1")) - 1;
				allerA(ligne);
			} else if (commande.matches("cp tab [\\w]+")) {
				String nomVar = commande.replaceAll("cp tab ([\\w]+)", "$1");
				Tableau t = (Tableau) prog.getCurrent().getDonnee(nomVar);
				if (t != null) {
					StringSelection selection = new StringSelection(t.toStringVertical());
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					clipboard.setContents(selection, selection);
				}
				reste();
				this.prog.traceExec += "a:" + commande + "\n";
			} else if (commande.matches("cp var [[\\w]+[ ]*]+")) {
				ArrayList<String> vars = new ArrayList<String>();
				for (int i = 2; i < commande.split(" ").length; i++)
					vars.add(commande.split(" ")[i]);
				StringSelection selection = new StringSelection(prog.getTraceDonnee(vars));
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
				this.reste();
				this.prog.traceExec += "a:" + commande + "\n";
			} else if (commande.matches("[\\+-][ ]*bk")) {
				this.setBreakPoint(this.prog.getCurrent().getLigneCourrante());
				reste();
				this.prog.traceExec += "a:" + commande + "\n";
			} else if (commande.equals("go bk")) {
				if (this.breakpoints.size() != 0) {
					this.attendBreakpoint = true;
				} else {
					reste();
					this.prog.traceExec += "a:Pas de breakpoint défini\n";
				}
			} else if (commande.equals("quit")) {
				System.exit(0);
			}
		} else {
			int ligneCourrante = this.prog.getCurrent().getLigneCourrante() + this.prog.getCurrent().getLigneDebut()
					+ 1;
			if (this.temps.containsKey(ligneCourrante)) {
				try {
					Thread.sleep((long) (this.temps.get(ligneCourrante) * 1000));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					Thread.sleep((long) (tempDefaut * 1000));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * Retourne les points d'arrêt
	 * 
	 * @return Liste de breakpoints
	 */
	public ArrayList<Integer> getBreakpoints() {
		return this.breakpoints;
	}

	/**
	 * Défini un point d'arrêt
	 * 
	 * @param ligne numéro de la ligne
	 */
	private void setBreakPoint(int ligne) {
		for (int i = 0; i < this.breakpoints.size(); i++) {
			if (this.breakpoints.get(i) == ligne) {
				this.breakpoints.remove(i);
				return;
			}
		}

		this.breakpoints.add(ligne);
	}

	public HashMap<Integer, String> getComms() {
		return this.comms;
	}

	private void reste() {
		this.revenir(0);
	}

	private void retour() {
		this.revenir(1);
	}

	private void revenir(int i) {
		this.varsALire = (ArrayList<String>) varsLu.clone();
		this.varsLu = new ArrayList<String>();
		this.revenir = i + 1;
		anciennesEtapes = etapes;
		etapes = new ArrayList<Integer>();
		this.prog.reset();
	}

	public Programme getProgramme() {
		return this.prog;
	}

	public void allerA(int ligne) {
		ligneAAttendre = ligne;
		this.etapes = new ArrayList<Integer>();
		this.prog.reset();
	}

	public void refresh() {
		this.aff.afficher();
	}

	/**
	 * Fonction main.
	 */
	public static void main(String[] a) {
		if (a.length < 1 || a.length > 3) {
			System.out.println(
					"ERREUR : veuillez spécifier le chemin du fichier à interpréter et le chemin du fichier de configuration");
			System.exit(1);
		} else {
			boolean gui = false;
			for (String arg : a)
				if (arg.equals("-gui"))
					gui = true;

			if (a.length == 1)
				new Controleur(a[0], "", gui);
			if (a.length == 2)
				new Controleur(a[0], a[1], gui);
		}

	}
}