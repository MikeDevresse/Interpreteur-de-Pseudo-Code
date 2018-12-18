package main;
import java.util.Scanner;

import ihmCui.Affichage;
import pseudoCode.Algorithme;
import pseudoCode.AlgorithmeException;
import pseudoCode.Programme;

/*
 * Retour en arriere :
 * Tableau du projet en serialize qu'on reprend
 */

public class Controleur
{
	
	/** nom du fichier */
	private final String input = "tests/TestLire.algo";
	
	/** objet programme */
	private Programme prog;
	
	/** lecteur de fichier */
	private LectureFichier lecture;
	
	private Scanner sc;
	
	/**
	 * Constructeur du controleur.
	 */
	private Controleur ()
	{
		this.sc = new Scanner(System.in);
		this.lecture = new LectureFichier(input);
		try
		{
			this.prog = new Programme(lecture.getTexteParLigne(), this);
		}
		catch ( AlgorithmeException e )
		{
			e.printStackTrace();
		}
		

		Affichage a = new Affichage(lecture.getTexteParLigne());
		
		while ( !prog.getMain().estTerminer() )
		{
			a.afficher( prog.getMain().getVariables(), prog.getTraceExec() );
			prog.getCurrent().ligneSuivante();
		}
		
	}
	
	public void lireVariable(String nomVar) {
		System.out.print("Entrez la valeur de " + nomVar + " : ");
		String valeur = this.sc.nextLine();
		this.prog.getCurrent().setValeur(nomVar, valeur);
	}
	
	public void attend ()
	{
		this.sc.nextLine();
	}
	
	/**
	 * Fonction main.
	 */
	public static void main ( String[] a )
	{
		new Controleur ();
	}
}
