package pseudoCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.ResourceBundle.Control;

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

	private int niveauCondition;

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
		this.niveauCondition = 0;
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
			if (current.matches("\\w*[ ]*<--[ ]*.*")) {
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
				interpreterCondition(condition, this.niveauCondition);
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

				ligneCourrante = i; // saut à la fin de la condition
				interpreterCondition(condition, this.niveauCondition); // interprétation de la condition
			}

			/*
			 * Gestion des boucles
			 */
			if (current.matches(".*tq.*alors.*")) {
				String condition = current.split("tq | alors")[1];
				interpreterBoucle(ligneCourrante, condition);

			} else if (current.matches(".*tq .*")) {
				String condition = current.split("tq")[1];
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

				ligneCourrante = i; // saut à la fin de la condition
				interpreterBoucle(ligneCourrante, condition); // interprétation de la boucle
			}
		}

		/*
		 * Fin de la l'algorithme
		 */
		if (mots[0].equals("FIN"))
			this.fin = true;

		// Controleur.getControleur().attend();

		return true;
	}

	/**
	 * Interprète une structure conditionnelle
	 * 
	 * @param condition condition
	 * @throws AlgorithmeException
	 */
	public void interpreterCondition(String condition, int niveauCondition) throws AlgorithmeException {

		this.niveauCondition++;

		// System.out.println("debut : " + condition + " | niveau : " + niveauCondition
		// + " | ligne : " + ligneCourrante);
		if (Condition.condition(condition, this.getInterpreteur()) && this.niveauCondition == niveauCondition) {
			// interprétation de la condition
			do {
				ligneSuivante();
			} while (!fichier[ligneCourrante].trim().equals("fsi") && !fichier[ligneCourrante].trim().equals("sinon")
					&& this.niveauCondition == niveauCondition);

			// saut jusqu'à la fin de la condition
			if (fichier[ligneCourrante].trim().equals("sinon")) {
				do {
					ligneCourrante++;
				} while (!fichier[ligneCourrante].trim().equals("fsi") && this.niveauCondition == niveauCondition);
			}

			this.niveauCondition--;

		} else { // condition invalide
			do {
				ligneCourrante++; // saut à l'alternative ou la fin de la condition
			} while (!fichier[ligneCourrante].trim().equals("fsi") && !fichier[ligneCourrante].trim().equals("sinon")
					&& this.niveauCondition == niveauCondition);
		}

	}

	/**
	 * Interprète une structure itérative
	 * 
	 * @param ligneBoucle ligne de début de boucle
	 * @param condition   condition
	 * @throws AlgorithmeException
	 */
	public void interpreterBoucle(int ligneBoucle, String condition) throws AlgorithmeException {

		while (Condition.condition(condition, this.getInterpreteur())) {
			ligneCourrante = ligneBoucle; // retour en haut de la boucle
			do {
				ligneSuivante();
			} while (!fichier[ligneCourrante].trim().equals("ftq"));
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
