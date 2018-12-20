package main;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Scanner;

import ihmCui.Affichage;
import pseudoCode.Algorithme;
import pseudoCode.AlgorithmeException;
import pseudoCode.Programme;
import pseudoCode.Variable;

/*
 * Retour en arriere :
 * Tableau du projet en serialize qu'on reprend
 */

public class Controleur
{

	/** nom du fichier */
	private final String	   input		  = "tests/Test1.algo";


	/** objet programme */
	private Programme		   prog;

	/** lecteur de fichier */
	private LectureFichier	   lecture;

	private Scanner			   sc;

	private int				   ligneAAttendre = -1;

	private int				   ligneRestantes = -1;

	private ArrayList<Integer> etapes;
	
	private ArrayList<Integer> anciennesEtapes;

	private static Controleur  ctrl;

	private Affichage		   aff;
	
	private ArrayList<Integer> breakpoints;
	
	private boolean 		   attendBreakpoint = false;
	
	private int 			   revenir = -1;
	
	private int				   cptIteration = 0;

	public static Controleur getControleur ()
	{
		if ( Controleur.ctrl == null ) return new Controleur();
		else return Controleur.ctrl;
	}

	/**
	 * Constructeur du controleur.
	 */
	private Controleur ()
	{
		this.breakpoints = new ArrayList<Integer>();
		this.etapes = new ArrayList<Integer>();
		Controleur.ctrl = this;
		this.sc = new Scanner( System.in );
		this.lecture = new LectureFichier( input );
		try
		{
			this.prog = new Programme( lecture.getTexteParLigne() );
		}
		catch ( AlgorithmeException e )
		{
			e.printStackTrace();
		}
		getVariableATracer();
		this.aff = new Affichage( lecture.getTexteParLigne(), prog );

		while ( !prog.getMain().estTerminer() )
		{
			try
			{
				prog.getCurrent().ligneSuivante();
			}
			catch ( AlgorithmeException e )
			{
				e.printStackTrace();
			}
		}

	}

	/**
	 * Permet à l'utilisateur de renseigner la valeur d'une variable
	 * 
	 * @param nomVar
	 *            nom de la variable
	 */
	public void lireVariable ( String nomVar )
	{
		System.out.print( "Entrez la valeur de " + nomVar + " : " );
		String valeur = this.sc.nextLine();
		this.prog.traceExec += "l:"+valeur+"\n";
		this.prog.getCurrent().setValeur( nomVar, valeur );
	}

	public void getVariableATracer ()
	{
		for ( Algorithme algo : this.prog.getAlgos() )
		{
			for ( Variable var : algo.getVariables() )
			{
				System.out.print( "Tracer la variable \"" + var.getNom() + "\" de l'algo " + algo.getNom() + " (Y/n) : " );
				String reponse = sc.nextLine();
				if ( reponse.trim().equalsIgnoreCase( "Y" ) || reponse.trim().equals( "" ) )
				{
					prog.ajouterVariableATracer( var );
				}
			}
		}

	}

	/**
	 * Attend une action de l'utilisateur
	 */
	public void attend ()
	{
		this.aff.afficher();

		if ( !this.prog.getMain().estEnTrainDeReset() )
			etapes.add( this.prog.getCurrent().getLigneCourrante() );
		
		boolean estSurBreakpoint = false;
		for ( int i = 0 ; i < breakpoints.size() ; i++ )
		{
			if ( this.breakpoints.get( i ) == this.prog.getCurrent().getLigneCourrante() )
			{
				estSurBreakpoint = true;
				break;
			}
		}

		if ( this.ligneRestantes > 0 )
		{
			ligneRestantes--;
		}
		else if ( revenir != -1 && this.anciennesEtapes.size() > revenir )
		{
			if ( this.anciennesEtapes.get( 0 ) == this.prog.getCurrent().getLigneCourrante() )
				this.anciennesEtapes.remove( 0 );
		}
		else if ( ligneAAttendre != -1 && ligneAAttendre > prog.getCurrent().getLigneCourrante() )
		{

		}
		else if ( attendBreakpoint && !estSurBreakpoint)
		{
			
		}
		else
		{
			revenir = -1;
			ligneRestantes = -1;
			ligneAAttendre = -1;
			this.attendBreakpoint = false;
			String commande = this.sc.nextLine();
			if ( !commande.equals( "" ))
				this.prog.traceExec += "a:"+commande+"\n";
			/*
			 * Gestion des commandes
			 */
			if ( commande.equalsIgnoreCase( "b" ) )
			{
				retour();
				this.prog.traceExec += "a:"+commande+"\n";
			}
			else if ( commande.matches( "[\\+\\-] var [\\w]+" ) )
			{
				String varATracer = commande.replaceAll( "[\\+\\-] var ([\\w]+)", "$1" );
				boolean ajouter = commande.replaceAll( "([\\+\\-]) var [\\w]+", "$1" ).equals( "+" );
				for ( Algorithme algo : this.prog.getAlgos() )
				{
					for ( Variable var : algo.getVariables() )
					{
						if ( var.getNom().equals( varATracer ))
						{
							if ( ajouter )
							{
								prog.ajouterVariableATracer( var );
							}
							else
							{
								prog.enleverVariableATracer( var );
							}
						}
					}
				}
				reste();
				this.prog.traceExec += "a:"+commande+"\n";
			}
			else if ( commande.matches( "[Ll][0-9]+" ) )
			{
				int ligne = Integer.parseInt( commande.replaceAll( "[Ll]([0-9]+)", "$1" ) )-1;
				ligneAAttendre = ligne;
				this.etapes = new ArrayList<Integer>();
				this.prog.reset();
			}
			else if ( commande.matches( "cp var [[\\w]+[ ]*]+" ))
			{
				ArrayList<String> vars = new ArrayList<String>();
				for ( int i=2 ; i < commande.split( " " ).length ; i++ )
					vars.add( commande.split( " " )[i] );
				StringSelection selection = new StringSelection( prog.getTraceVar(vars) );
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(selection, selection);
				this.reste();
				this.prog.traceExec += "a:"+commande+"\n";
			}
			else if ( commande.matches( "[\\+-][ ]*bk" ))
			{
				this.setBreakPoint( this.prog.getCurrent().getLigneCourrante() );
				reste();
				this.prog.traceExec += "a:"+commande+"\n";
			}
			else if ( commande.equals( "go bk" ))
			{
				if ( this.breakpoints.size() != 0 )
				{
					this.attendBreakpoint = true;
				}
				else
				{
					reste();
					this.prog.traceExec += "a:Pas de breakpoint défini\n";
				}
			}
			else if ( commande.equals( "quit" ))
			{
				System.exit( 0 );
			}
		}
	}
	
	public ArrayList<Integer> getBreakpoints ()
	{
		return this.breakpoints;
	}
	
	private void setBreakPoint ( int ligne )
	{
		for ( int i = 0 ; i < this.breakpoints.size() ; i++ )
		{
			if ( this.breakpoints.get( i ) == ligne)
			{
				this.breakpoints.remove( i );
				return;
			}
		}
		
		this.breakpoints.add( ligne );
	}
	
	private void reste ()
	{
		this.revenir( 0 );
	}

	private void retour ()
	{
		this.revenir( 1 );
	}
	
	private void revenir ( int i )
	{
		this.revenir = i+1;
		anciennesEtapes = etapes;
		etapes = new ArrayList<Integer>();
		this.prog.reset();
	}

	/**
	 * Fonction main.
	 */
	public static void main ( String[] a )
	{
		new Controleur();
	}

	public Programme getProgramme ()
	{
		return this.prog;
		
	}
}