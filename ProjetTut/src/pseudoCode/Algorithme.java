package pseudoCode;

import java.util.ArrayList;

/**
 * Interprète le code d'un algorithme
 */

import bsh.EvalError;
import bsh.Interpreter;
import main.Controleur;
import util.Condition;
import util.Fonctions;

public class Algorithme {

	private Interpreter interpreteur;

	/** nom. */
	private String nom;

	/** ens variables. */
	private ArrayList<Variable> ensVariables;

	private String[] fichier;

	/** debut. */
	private boolean debut = false;

	/** fin. */
	private boolean fin = false;

	/** def. */
	private String def;

	/** ligne courrante. */
	private int ligneCourrante = 0;
	
	private int ligneDebut;

	private Programme prog;
	
	private Controleur ctrl;

	/**
	 * Instanciation de algorithme.
	 *
	 * @param nom nom
	 */
	public Algorithme(String nom, int ligneDebut, String[] fichier, Programme p, Controleur ctrl) {
		this.ctrl = ctrl;
		this.prog = p;
		this.ligneDebut = ligneDebut;
		this.interpreteur = new Interpreter();
		this.nom = nom;
		this.ensVariables = new ArrayList<Variable>();
		this.fichier = fichier;
	}

	/**
	 * Interprète la ligne suivante
	 */
	public void ligneSuivante() {
		if (ligneCourrante == fichier.length) {
			this.fin = true;
			return;
		}
		String current = fichier[ligneCourrante++];
		String[] mots = current.split(" ");
		boolean ignore = current.trim().equals("");
		if (!debut && !ignore) {
			// type de variables : constantes
			if (mots[0].replaceAll(":", "").equals("constante")) {
				this.def = "constante";
			}
			// type de variables : variables
			else if (mots[0].replaceAll(":", "").equals("variable")) {
				this.def = "variable";
			} else if (mots[0].equals("DEBUT")) {
				this.debut = true;
			} else {
				// définition des variables
				if (this.def == null || this.def == "") {
					return;
				}
				boolean estConstante = def.equals("constante");
				String type = current.split(":")[1].trim();
				for (String s : current.split(":")[0].split(","))
					ajouterVariable(VariableFactory.createVariable(s.trim(), type, estConstante));
			}
		} else if (debut && !ignore) {
			if (current.split("<--").length == 2) {
				String[] parties = current.split("<--");
				setValeur(parties[0].trim(), parties[1]);
			}

			if (current.matches(".*\\(.*\\)")) {
				Fonctions.evaluer(current.split("\\(|\\)")[0], Variable.traduire(current.split("\\(|\\)")[1]), this, this.ctrl);
			}

			if (current.matches(".*si.*alors.*")) {
				String condition = current.split("si | alors")[1];
				if (!Condition.condition(condition, this.getInterpreteur())) {
					do {
						ligneCourrante++;
					} while (!fichier[ligneCourrante].trim().equals("fsi")
							&& !fichier[ligneCourrante].trim().equals("sinon"));
				} else {
					do {
						ligneSuivante();
					} while (!fichier[ligneCourrante].trim().equals("fsi")
							&& !fichier[ligneCourrante].trim().equals("sinon"));

					if (fichier[ligneCourrante].trim().equals("sinon")) {
						do {
							ligneCourrante++;
						} while (!fichier[ligneCourrante].trim().equals("fsi"));
					}
				}

			}

			if (mots[0].equals("FIN"))
				this.fin = true;
		}
	}

	/**
	 * Retourne l'interpréteur BeanShell
	 * 
	 * @return interpréteur BeanShell
	 */
	public Interpreter getInterpreteur() {
		return this.interpreteur;
	}

	/**
	 * Ajoute une variable à la liste des variables
	 *
	 * @param v variable
	 */
	public void ajouterVariable(Variable v) {
		this.ensVariables.add(v);
	}

	/**
	 * Retourne une variable donnée
	 *
	 * @param nomVar nom de la variable
	 * @return variable
	 */
	public Variable getVariable(String nomVar) {
		for (Variable v : ensVariables) {
			if (v.getNom().equals(nomVar)) {
				return v;
			}
		}
		return null;
	}

	/**
	 * Retourne un tableau de variables
	 * @return tableau de variables
	 */
	public Variable[] getVariables() {
		return this.ensVariables.toArray(new Variable[this.ensVariables.size()]);
	}

	/**
	 * Défini la valeur d'une variable
	 * @param nomVar nom de la variable
	 * @param valeur valeur de la variable
	 */
	public void setValeur(String nomVar, String valeur) {
		Interpreter interpreter = this.getInterpreteur();

		valeur = Variable.traduire(valeur);

		try {
			this.getVariable(nomVar).setValeur(interpreter.eval(valeur));
			interpreter.eval(nomVar + " = " + this.getVariable(nomVar).getValeur());
		} catch (EvalError e) {
			e.printStackTrace();
		}
	}

	public String toString() {
		String s = "Algorithme : " + this.nom + "\n";
		for (Variable v : ensVariables) {
			s += v + "\n";
		}
		return s;
	}

	/**
	 * Interprète une condition
	 * @param condition condition
	 * @return vrai si syntaxiquement valide
	 */
	

	/**
	 * Retourne le programme
	 * @return programme
	 */
	public Programme getProgramme() {
		return this.prog;
	}

	/**
	 * Retourne si le programme est terminé
	 * @return vrai si terminé
	 */
	public boolean estTerminer() {
		return this.fin;
	}
	
	public int getLigneDebut()
	{
		return this.ligneDebut;
	}
	
	public int getLigneCourrante()
	{
		return this.getLigneCourrante();
	}

}
