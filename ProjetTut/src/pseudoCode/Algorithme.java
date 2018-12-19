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
	private String def = "";

	/** ligne courrante. */
	private int ligneCourrante = 0;

	private int ligneDebutAlgorithme;

	private int ligneDebut;

	private Programme prog;

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
		initialiser();
	}

	public void initialiser() {
		String current = fichier[ligneCourrante++];
		String[] mots = current.split(" ");
		do {
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
			current = fichier[ligneCourrante++];
			mots = current.split(" ");
		} while (!mots[0].equals("DEBUT"));
		this.ligneDebutAlgorithme = ligneCourrante;
		this.def = "algo";
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
		if ( current.matches( ".*//.*" ))
			current = current.replaceAll( "(.*)//.*", "$1" );
		current = current.trim();
		if (current.equals(""))
			return false;

		String[] mots = current.split(" ");

		if (mots[0].equals("DEBUT"))
			this.def = "algo";

		/*
		 * Début de l'algorithme
		 */
		if (this.def.equals("algo")) {

			/*
			 * Affectation des variables
			 */
			if (current.matches("\\w*[ ]*<--[ ]*.*")) {
				String[] parties = current.split("<--");
				setValeur(parties[0].trim(), parties[1].trim());
			}

			/*
			 * Gestion des fonctions
			 */
			if (current.matches(".+\\(.*\\)")) {
				
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

				ligneCourrante = i; // saut à la fin de la condition
				interpreterCondition(condition); // interprétation de la condition
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

		Controleur.getControleur().attend();

		return true;
	}

	/**
	 * Interprète une structure conditionnelle
	 * 
	 * @param condition condition
	 * @throws AlgorithmeException
	 */
	public void interpreterCondition(String condition) throws AlgorithmeException {

		int cptLigne = ligneCourrante;

		int nbSi = 0;
		int ligneSinon = 0;
		int ligneFsi = 0;
		for (int i = cptLigne; i < fichier.length; i++) {
			
			if (fichier[i].matches(".*si .* alors.*"))
				nbSi++;
			if (fichier[i].matches("sinon")) 
				if (nbSi == 0) 
					ligneSinon = i;

			if (fichier[i].matches("fsi")) 
				if (nbSi == 0) {
					ligneFsi = i;
					break;
				} else 
					nbSi--;
		}		


		if (Condition.condition(condition, this.getInterpreteur())) {
			// interprétation de la condition
			Controleur.getControleur().attend();
			do {
				ligneSuivante();
			} while (ligneCourrante != ligneSinon && ligneCourrante != ligneFsi);
			
			
			// saut jusqu'à la fin de la condition
			if (fichier[ligneCourrante].trim().equals("sinon")) {
				do {
					ligneCourrante++;
				} while (ligneCourrante != ligneFsi+1);
			}
			
			
			
		} else { // condition invalide
			this.prog.ajouterLigneFausse( ligneCourrante );
			Controleur.getControleur().attend();
			do {
				ligneCourrante++; // saut à l'alternative ou la fin de la condition
			} while ((ligneCourrante != ligneSinon+1 && ligneCourrante != ligneFsi+1));
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

		int cptLigne = ligneCourrante;

		int nbTq = 0;
		int ligneFtq = 0;
		for (int i = cptLigne; i < fichier.length; i++) {
			
			if (fichier[i].matches(".*tq .* alors.*"))
				nbTq++;

			if (fichier[i].matches("ftq")) 
				if (nbTq == 0) {
					ligneFtq = i;
					break;
				} else 
					nbTq--;
		}
		
		
		while (Condition.condition(condition, this.interpreteur)) {
			ligneCourrante = ligneBoucle; // retour en haut de la boucle
			Controleur.getControleur().attend();
			do {
				ligneSuivante();
			} while (ligneCourrante != ligneFtq);
		}
		ligneCourrante = ligneBoucle;
		System.out.println( ligneCourrante );
		this.prog.ajouterLigneFausse( ligneCourrante );
		Controleur.getControleur().attend();
		ligneCourrante = ligneFtq+1;
		
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

	public String getNom() {
		return this.nom;
	}

	public void reset() {
		this.ligneCourrante = this.ligneDebutAlgorithme;

	}
}
