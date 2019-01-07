package main;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ihmCui.Affichage;
import pseudoCode.Algorithme;
import pseudoCode.AlgorithmeException;
import pseudoCode.Donnee;
import pseudoCode.Programme;
import pseudoCode.Tableau;

/*
 * Retour en arriere :
 * Tableau du projet en serialize qu'on reprend
 */

public class Controleur {

	public static boolean DEBUG = false;

	/** nom du fichier */
	private String input;

	/** objet programme */
	private Programme prog;

	/** lecteur de fichier */
	private LectureFichier lecture;

	private Scanner sc;

	private int ligneAAttendre = -1;

	private int ligneRestantes = -1;

	private ArrayList<Integer> etapes;

	private ArrayList<Integer> anciennesEtapes;

	private static Controleur ctrl;

	private Affichage aff;

	private ArrayList<Integer> breakpoints;

	private boolean attendBreakpoint = false;

	private int revenir = -1;
	
	private ArrayList<String> varsALire;
	
	private ArrayList<String> varsLu;
	
	private boolean marcheAuto;
	
	private double tempDefaut;
	
	private HashMap<Integer,Double> temps;
	private HashMap<Integer,String> comms;

	public static Controleur getControleur() {
		if (Controleur.ctrl == null)
			return new Controleur("");
		else
			return Controleur.ctrl;
	}

	/**
	 * Constructeur du controleur.
	 */
	private Controleur(String fichier) {
		lancerConfig();
		this.varsALire = new ArrayList<String>();
		this.varsLu = new ArrayList<String>();
		this.input = fichier;
		this.breakpoints = new ArrayList<Integer>();
		this.etapes = new ArrayList<Integer>();
		Controleur.ctrl = this;
		this.sc = new Scanner(System.in);
		this.lecture = new LectureFichier(input);

		// création du programme
		try {
			this.prog = new Programme(lecture.getTexteParLigne());
		} catch (AlgorithmeException e) {
			e.printStackTrace();
		}

		// Récupération des variables à tracer
		getVariableATracer();

		// création de l'IHM
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
	
	public void lancerConfig ()
	{
		temps = new HashMap<Integer,Double>();
		comms = new HashMap<Integer,String>();
		try
		{
    		String s = "";
    		BufferedReader reader = new BufferedReader( new FileReader( "config.txt" ) );
    		
    		while ( ( s = reader.readLine() ) != null )
    		{
    			String nom    = s.split( ":" )[0].trim().toLowerCase();
    			String valeur = s.split( ":" )[1].trim().toLowerCase();
    			
    			if ( nom.matches( "marche auto.*" ))
    			{
    				marcheAuto = valeur.equals( "oui" );
    			}
    			if ( nom.matches("temp.*"))
    			{
    				if ( nom.matches( "temp globale.*" ) )
    				{
    					this.tempDefaut = Double.parseDouble( valeur );
    				}
    				if ( nom.matches( "temp l[0-9]+.*" ))
    				{
    					int ligne = Integer.parseInt( nom.replaceAll( "temp l([0-9]+).*", "$1" ) );
    					temps.put( ligne, Double.parseDouble( valeur ) );
    				}
    			}
    			if ( nom.matches( "comm l[0-9]+.*" ))
    			{
					int ligne = Integer.parseInt( nom.replaceAll( "comm l([0-9]+).*", "$1" ) );
					comms.put( ligne, valeur );
    			}
    		}
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}

	/**
	 * Permet à l'utilisateur de renseigner la valeur d'une variable
	 * 
	 * @param nomVar nom de la variable
	 */
	public void lireVariable(String nomVar) {
		String valeur = "";
		if ( this.varsALire.isEmpty())
		{
    		System.out.print("Entrez la valeur de " + nomVar + " : ");
    		valeur = this.sc.nextLine();
		}
		else
		{
			valeur = varsALire.remove( 0 );
		}
		this.varsLu.add( valeur );
		this.prog.traceExec += "l:" + valeur + "\n";
		this.prog.getCurrent().setValeur(nomVar, valeur);
	}

	/**
	 * Demande à l'utilisateur les variables à tracer
	 */
	public void getVariableATracer() {
		for (Algorithme algo : this.prog.getAlgos()) {
			for (Donnee d : algo.getDonnees()) {
				System.out.print("Tracer la variable \"" + d.getNom() + "\" de l'algo " + algo.getNom() + " (Y/n) : ");
				String reponse = sc.nextLine();
				if (reponse.trim().equalsIgnoreCase("Y") || reponse.trim().equals("")) {
					prog.ajouterDonneeATracer(d);
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

		} else if ( !marcheAuto ){
			revenir = -1;
			ligneRestantes = -1;
			ligneAAttendre = -1;
			this.attendBreakpoint = false;
			String commande = this.sc.nextLine();
			if (!commande.equals(""))
				this.prog.traceExec += "a:" + commande + "\n";
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
		}
		else
		{
			int ligneCourrante = this.prog.getCurrent().getLigneCourrante() + this.prog.getCurrent().getLigneDebut() + 1;
			if ( this.temps.containsKey( ligneCourrante ) )
			{
				try {
					Thread.sleep( (long)( this.temps.get( ligneCourrante )*1000) );
				}
				catch ( Exception e )
				{
					e.printStackTrace();
				}
			}
			else
			{
				try {
					Thread.sleep( (long)( tempDefaut*1000 ));
				}
				catch ( Exception e )
				{
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
	
	public HashMap<Integer,String> getComms ()
	{
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
	
	/**
	 * Fonction main.
	 */
	public static void main(String[] a) {
		if (a.length != 1) {
			System.out.println("ERREUR : veuillez spécifier le chemin du fichier à interpréter");
			System.exit(1);
		} else
			new Controleur(a[0]);
	}
}