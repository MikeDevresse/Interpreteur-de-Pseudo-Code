package pseudoCode;

import java.io.Serializable;
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

	private boolean estVrai;

	/**
	 * Instanciation de algorithme.
	 *
	 * @param nom nom
	 */
	public Algorithme(String nom, int ligneDebut, String[] fichier, Programme p) {
		this.prog = p;
		this.ligneDebut = ligneDebut;
		this.interpreteur = new Interpreter();
		this.nom = nom;
		this.ensVariables = new ArrayList<Variable>();
		this.fichier = fichier;
	}

	/**
	 * Interprète la ligne suivante
	 * 
	 * @throws AlgorithmeException
	 */
	public boolean ligneSuivante() throws AlgorithmeException {
		if (ligneCourrante == fichier.length) {
			this.fin = true;
			return false;
		}

		String current = fichier[ligneCourrante++];
		if (current.trim().equals(""))
			return false;

		/*
		 * Gestion des variables
		 */
		String[] mots = current.split(" ");
		if (mots[0].replace(":", "").equals("constante")) {
			this.def = "const";
		} else if (mots[0].replace(":", "").equals("variable")) {
			this.def = "var";
		} else if (this.def != null || !this.def.equals("")) {
			if (current.matches("[[\\w*],*]*[ ]*\\w*:[ ]*\\w*")) {
				String type = current.split(":")[1].trim();
				for (String var : current.split(":")[0].split(",")) {
					ajouterVariable(VariableFactory.createVariable(var.trim(), type, this.def.equals("const")));
				}

			}
		}

		if (mots[0].equals("DEBUT"))
			this.def = "algo";

		/*
		 * Début de l'algorithme
		 */
		if (this.def.equals("algo")) {
			if (current.matches("\\w*[ ]*<--[ ]*\\w*")) {
				String[] parties = current.split("<--");
				setValeur(parties[0].trim(), parties[1].trim());
			}

			/*
			 * Gestion des fonctions
			 */
			if (current.matches(".*\\(.*\\)")) {
				Fonctions.evaluer(current.split("\\(|\\)")[0], Variable.traduire(current.split("\\(|\\)")[1]), this);
			}

			/*
			 * Gestion des conditions
			 */
			if (current.matches(".*si .* alors.*")) {
				String condition = current.split("si | alors")[1];
				interpreterCondition(condition);
			} else if (current.matches(".*si .*") && !current.contains("alors")) {
				/*
				 * Gestion des conditions sur plusieurs lignes
				 */
				String condition = current.split("si")[1];
				boolean conditionFinie = false;
				int i;
				for (i = ligneCourrante; i < fichier.length && !conditionFinie; i++) {

					if (!fichier[i].trim().matches(".* alors$")) {
						condition += fichier[i].trim() + " ";
					}

					if (fichier[i].trim().contains("alors")) {
						condition += fichier[i].split("alors")[0];
						conditionFinie = true;
					}
				}

				ligneCourrante = i;
				interpreterCondition(condition);
			}
		}

		if (mots[0].equals("FIN"))
			this.fin = true;
//		}
		return true;
	}

	public void interpreterCondition(String condition) throws AlgorithmeException {
		// Condition valide
		if (Condition.condition(condition, this.interpreteur)) {
			do {
				ligneSuivante();
			} while (!fichier[this.ligneCourrante - 1].equals("fsi"));
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
	 * 
	 * @return tableau de variables
	 */
	public Variable[] getVariables() {
		return this.ensVariables.toArray(new Variable[this.ensVariables.size()]);
	}

	/**
	 * Défini la valeur d'une variable
	 * 
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
	 * 
	 * @param condition condition
	 * @return vrai si syntaxiquement valide
	 */

	/**
	 * Retourne le programme
	 * 
	 * @return programme
	 */
	public Programme getProgramme() {
		return this.prog;
	}

	public String[] getFichier() {
		return this.fichier;
	}

	/**
	 * Retourne si le programme est terminé
	 * 
	 * @return vrai si terminé
	 */
	public boolean estTerminer() {
		return this.fin;
	}

	public int getLigneDebut() {
		return this.ligneDebut;
	}

	public int getLigneCourrante() {
		return this.ligneCourrante;
	}

	public boolean getEstVrai() {
		return this.estVrai;
	}
}
