package main;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

import ihmCui.Affichage;
import pseudoCode.AlgorithmeException;
import pseudoCode.Programme;

/*
 * Retour en arriere :
 * Tableau du projet en serialize qu'on reprend
 */

public class Controleur
{
	
	/** nom du fichier */
	private final String input = "tests/testConditions.algo";
	
	/** objet programme */
	private Programme prog;
	
	/** lecteur de fichier */
	private LectureFichier lecture;
	
	private Scanner sc;
	
	private int ligneAAttendre = -1;
	
	private int ligneRestantes = -1;
	
	private int nbSteps = 0;
	
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
		if ( this.ligneRestantes > 0)
		{
			ligneRestantes--;
		}
		else if ( ligneAAttendre != -1 && ligneAAttendre != prog.getCurrent().getLigneCourrante() )
		{
			
		}
		else
		{
			ligneRestantes = -1;
			ligneAAttendre = -1;
			this.sc.nextLine();
		}
		try
		{
			FileOutputStream fichier = new FileOutputStream("histo/progstep" + nbSteps++ + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject( prog );
			oos.flush();
			oos.close();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Fonction main.
	 */
	public static void main ( String[] a )
	{
		new Controleur ();
	}
}
