package main;
import java.util.Scanner;

import org.omg.CORBA.COMM_FAILURE;

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
	private final String input = "tests/testConditions.algo";
	
	/** objet programme */
	private Programme prog;
	
	/** lecteur de fichier */
	private LectureFichier lecture;
	
	private Scanner sc;
	
	private int ligneAAttendre = -1;
	
	private int ligneRestantes = -1;
	
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
	
	/**
	 * Permet à l'utilisateur de renseigner la valeur d'une variable
	 * @param nomVar nom de la variable
	 */
	public void lireVariable(String nomVar) {
		System.out.print("Entrez la valeur de " + nomVar + " : ");
		String valeur = this.sc.nextLine();
		this.prog.getCurrent().setValeur(nomVar, valeur);
	}
	
	/**
	 * Attend une action de l'utilisateur
	 */
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
			String commande = this.sc.nextLine();
			
			if (commande.equals("b")) {
				System.out.println("arrière");
			}else if (commande.matches("L[0-9]*")) {
				System.out.println("saut de ligne : " + commande.replaceAll("L([0-9]*)", "$1"));
			}else if (commande.matches("[+-] var \\w*")) {
				if (commande.startsWith("+"))
					System.out.println("ajout de la variable " + commande.replaceAll("[+-] var (\\w*)", "$1"));
				else
					System.out.println("suppression de variable " + commande.replaceAll("[+-] var (\\w*)", "$1"));
			}
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
