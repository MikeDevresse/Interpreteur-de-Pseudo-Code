package pseudoCode;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interprète le code d'un algorithme
 */

import bsh.EvalError;
import bsh.Interpreter;
import util.Condition;
import util.Fonctions;

public class Algorithme {

	private Interpreter interpreteur;

	/** nom. */
	private String nom;

	/** ens variables. */
	private ArrayList<Donnee> ensDonnees;

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

	private boolean reset = false;

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
		this.ensDonnees = new ArrayList<Donnee>();
		this.fichier = fichier;
		initialiser();
	}

	/**
	 * Initialise les données de l'algorithme
	 */
	public void initialiser() {

		Fonctions.initFonctions(this.interpreteur);

		String current = fichier[ligneCourrante++];
		String[] mots = current.split(" ");
		do {
			if (mots[0].replace(":", "").equals("constante")) {
				this.def = "const";
			} else if (mots[0].replace(":", "").equals("variable")) {
				this.def = "var";
			} else if (this.def != null || !this.def.equals("")) {
				if (current.matches("[[\\w*],*]*[ ]*\\w*:.*")) {
					String type = current.split(":")[1].trim();
					for (String var : current.split(":")[0].split(",")) {
						ajouterDonnee(DonneeFactory.createVariable(var.trim(), type, this.def.equals("const"), this));
					}

				}
				if (current.matches("[[\\w*],*]*[ ]*\\w*<--[ ]*\\w*")) {
					String type = "";
					String valeur = current.replaceAll("[[\\w*],*]*[ ]*\\w*<--[ ]*(\\w+)", "$1");
					if (valeur.matches("\"[\\w]*\""))
						type = "chaine";
					else if (valeur.matches("'[\\w]'"))
						type = "caractere";
					else if (valeur.matches("[0-9]+"))
						type = "entier";
					else if (valeur.matches("[0-9]+.[0-9]*"))
						type = "reel";
					else if (valeur.equals("true") || valeur.equals("false"))
						type = "booleen";

					for (String var : current.split("<--")[0].split(",")) {
						ajouterDonnee(DonneeFactory.createVariable(var.trim(), type, this.def.equals("const"), this));
						this.setValeur(var.trim(), valeur);
					}
				}
			}
			current = fichier[ligneCourrante++];
			mots = current.split(" ");
		} while (!mots[0].equals("DEBUT"));
		this.ligneDebutAlgorithme = this.ligneCourrante;
		this.def = "algo";
	}

	/**
	 * Interprète la ligne suivante
	 * 
	 * @throws AlgorithmeException
	 */
	public boolean ligneSuivante() throws AlgorithmeException {
		if (this.reset) {
			this.ligneCourrante = this.ligneDebutAlgorithme;
			this.reset = false;
		}

		if (this.ligneCourrante == this.fichier.length) {
			this.fin = true;
			return false;
		}

		String current = this.fichier[ligneCourrante++];
		if (current.matches(".*//.*"))
			current = current.replaceAll("(.*)//.*", "$1");
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
			if (current.matches(".*<--.*")) {
				String[] parties = current.split("<--");
				setValeur(parties[0].trim(), parties[1].trim());
			}

			/*
			 * Gestion des fonctions
			 */
			if (current.matches(".+\\(.*\\)")) {
				String toInterpret;
				Pattern pattern = Pattern.compile("\\(.*\\)");
				Matcher matcher = pattern.matcher(current);
				if (matcher.find()) {
					toInterpret = matcher.group(0).substring(1, matcher.group(0).length() - 1);
					Fonctions.evaluer(current.split("\\(|\\)")[0], Variable.traduire(toInterpret), this);
				}
			}

			/*
			 * Gestion des conditions
			 */
			if (current.matches("si .* alors.*")) {
				String condition = current.split("si | alors")[1];
				interpreterCondition(condition, this.ligneCourrante, this.ligneCourrante);
			} else if (current.matches(".*si .*") && !current.contains("alors")) {
				/*
				 * Gestion des conditions sur plusieurs lignes
				 */
				String condition = current.split("si")[1];
				boolean conditionFinie = false;
				int ligneDebutCondition = this.ligneCourrante;
				int ligneFinCondition;
				for (ligneFinCondition = this.ligneCourrante; ligneFinCondition < this.fichier.length
						&& !conditionFinie; ligneFinCondition++) {

					if (!this.fichier[ligneFinCondition].trim().matches(".* alors$")) {
						condition += this.fichier[ligneFinCondition].trim() + " ";
					}

					if (this.fichier[ligneFinCondition].trim().contains("alors")) {
						condition += this.fichier[ligneFinCondition].split("alors")[0];
						conditionFinie = true;
					}
				}

				this.ligneCourrante = ligneFinCondition; // saut à la fin de la condition
				interpreterCondition(condition, ligneDebutCondition, ligneFinCondition); // interprétation de la
																							// condition
			}

			/*
			 * Gestion des boucles
			 */
			if (current.matches(".*tq.*alors.*")) {
				String condition = current.split("tq | alors")[1];
				interpreterBoucle(this.ligneCourrante, condition, this.ligneCourrante, this.ligneCourrante);

			} else if (current.matches(".*tq .*")) {
				/*
				 * Gestion des boucles sur plusieurs lignes
				 */
				String condition = current.split("tq")[1];
				boolean conditionFinie = false;
				int ligneDebutBoucle = this.ligneCourrante;
				int ligneFinBoucle;
				for (ligneFinBoucle = this.ligneCourrante; ligneFinBoucle < this.fichier.length
						&& !conditionFinie; ligneFinBoucle++) {

					if (!this.fichier[ligneFinBoucle].trim().matches(".* alors$")) {
						condition += this.fichier[ligneFinBoucle].trim() + " ";
					}

					if (this.fichier[ligneFinBoucle].trim().contains("alors")) {
						condition += this.fichier[ligneFinBoucle].split("alors")[0];
						conditionFinie = true;
					}
				}

				this.ligneCourrante = ligneFinBoucle; // saut à la fin de la condition
				interpreterBoucle(this.ligneCourrante, condition, ligneDebutBoucle, ligneFinBoucle); // interprétation
																										// de la boucle
			}

			/*
			 * Gestion des switchs
			 */
			if (current.matches("selon .*")) {
				String varSelon = current.substring("selon ".length());
				System.out.println("SWITCH : selon " + varSelon);
				interpreterSwitch(varSelon);
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
	public void interpreterCondition(String condition, int ligneDebut, int ligneFin) throws AlgorithmeException {

		/*
		 * Identification des conditions imbriquées
		 */
		int cptLigne = this.ligneCourrante;
		int nbSi = 0;
		int ligneSinon = 0;
		int ligneFsi = 0;
		for (int i = cptLigne; i < this.fichier.length; i++) {

			if (this.fichier[i].matches(".*si .* alors.*"))
				nbSi++;
			if (this.fichier[i].matches("sinon"))
				if (nbSi == 0)
					ligneSinon = i;

			if (this.fichier[i].matches("fsi"))
				if (nbSi == 0) {
					ligneFsi = i;
					break;
				} else
					nbSi--;
		}

		if (Condition.condition(condition, this.getInterpreteur())) {
			// interprétation de la condition
			//Controleur.getControleur().attend();
			do {
				ligneSuivante();
			} while (this.ligneCourrante != ligneSinon && this.ligneCourrante != ligneFsi);

			// saut jusqu'à la fin de la condition
			if (this.fichier[this.ligneCourrante].trim().equals("sinon")) {
				do {
					this.ligneCourrante++;
				} while (this.ligneCourrante != ligneFsi + 1);
			}

		} else { // condition invalide
			for (int i = ligneDebut; i <= ligneFin; i++)
				this.prog.ajouterLigneFausse(i);

			//Controleur.getControleur().attend();

			this.prog.resetLigneFausse();

			do {
				this.ligneCourrante++; // saut à l'alternative ou la fin de la condition
			} while ((this.ligneCourrante != ligneSinon + 1 && this.ligneCourrante != ligneFsi + 1));
		}
	}

	/**
	 * Interprète une structure itérative
	 * 
	 * @param ligneBoucle ligne de début de boucle
	 * @param condition   condition
	 * @throws AlgorithmeException
	 */
	public void interpreterBoucle(int ligneBoucle, String condition, int ligneDebut, int ligneFin)
			throws AlgorithmeException {
		
		/*
		 * Identification des boucles imbriquées
		 */
		int cptLigne = this.ligneCourrante;
		int nbTq = 0;
		int ligneFtq = 0;
		for (int i = cptLigne; i < this.fichier.length; i++) {
			if (this.fichier[i].matches(".*tq .* alors.*"))
				nbTq++;

			if (this.fichier[i].matches("ftq"))
				if (nbTq == 0) {
					ligneFtq = i;
					break;
				} else
					nbTq--;
		}

		/*
		 * Test de boucle infinie
		 */
		if (Condition.condition(condition, this.interpreteur)) {

			String[] conditions = condition.split("et|ou|xou|non");
			ArrayList<String> condVariables = new ArrayList<String>();
			
			// découpage de la condition et identification des variables impliquées
			for (String cond : conditions) {
				cond = cond.trim();

				String[] vars = cond.split("<=|>=|<|>|=|/=");
				for (int i = 0; i < vars.length; i += 2)
					condVariables.add(vars[i]);
			}

			// parcours à vide de la condition pour détecter les modifications de variable
			boolean estInfinie = true;
			for (int i = this.ligneCourrante; i < ligneFtq; i++) {
				for (String var : condVariables) {
					// si une variable de la condition est modifiée
					if (this.fichier[i].matches(var + "[ ]*<--.*"))
						estInfinie = false;
				}
			}
			
			if (estInfinie) {
				System.out.println("boucle infinie");
				System.exit(1);
			}
		}

		/*
		 * Interprétation de la boucle
		 */
		while (Condition.condition(condition, this.interpreteur)) {
			this.ligneCourrante = ligneBoucle; // retour en haut de la boucle
			//Controleur.getControleur().attend();
			do {
				ligneSuivante();
			} while (this.ligneCourrante != ligneFtq);
		}

		// indication que la condition est fausse
		for (int i = ligneDebut; i <= ligneFin; i++)
			this.prog.ajouterLigneFausse(i);

		this.ligneCourrante = ligneDebut;
		//Controleur.getControleur().attend();
		this.prog.resetLigneFausse();

		// retour au point d'origine
		this.ligneCourrante = ligneFtq + 1;

	}

	/**
	 * Interprète une structure alternative généralisée
	 * 
	 * @param varName nom de la variable
	 */
	public void interpreterSwitch(String varName) throws AlgorithmeException {

		/*
		 * Identification des switchs imbriqués
		 */
		int cptLigne = this.ligneCourrante;
		int nbSelon = 0;
		int ligneAutrecas = 0;
		int ligneFselon = 0;
		for (int i = cptLigne; i < this.fichier.length; i++) {
			if (this.fichier[i].matches("selon .*"))
				nbSelon++;

			if (this.fichier[i].matches("fselon")) {
				if (nbSelon == 0) {
					ligneFselon = i;
					break;
				} else
					nbSelon--;
			} else if (this.fichier[i].matches("autrecas.*"))
				ligneAutrecas = i;

		}

		Variable v = this.getVariable(varName);

		/*
		 * Interprétation du switch
		 */
		this.ligneCourrante--;
		do {
			this.ligneCourrante++;

			// cas identifié
			if (this.fichier[this.ligneCourrante].matches("cas .*[ ]*:")) {
				String cas = this.fichier[this.ligneCourrante].split("cas |[ ]*:")[1].replaceAll("\"", "");

				// cas valide
				if (String.valueOf(v.getValeur()).equals(cas)) {
					ligneCourrante++;
					do {
						ligneSuivante();

					} while (!this.fichier[this.ligneCourrante].matches("cas.*")
							&& !this.fichier[this.ligneCourrante].matches("autrecas .*"));
					ligneCourrante = ligneFselon;
				}
			} else if (this.fichier[this.ligneCourrante].matches("autrecas.*")) {
				// gestion des autres cas
				ligneCourrante++;

				do {
					ligneSuivante();
				} while (!this.fichier[this.ligneCourrante].matches("fselon"));
				ligneCourrante = ligneFselon;
			}

		} while (this.ligneCourrante != ligneFselon);

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
	public void ajouterDonnee(Donnee d) {
		this.ensDonnees.add(d);
	}

	/**
	 * Retourne une variable donnée
	 *
	 * @param nomVar nom de la variable
	 * @return variable
	 */
	public Donnee getDonnee(String nomDonnee) {
		for (Donnee d : this.ensDonnees) {
			if (d != null && d.getNom().equals(nomDonnee)) {
				return d;
			}
		}
		return null;
	}

	/**
	 * Retourne une variable spécifique
	 * @param nomVariable nom de la variable
	 * @return variable
	 */
	public Variable getVariable(String nomVariable) {
		for (Donnee d : this.ensDonnees) {
			if (d.getNom().equals(nomVariable) && d instanceof Variable) {
				return (Variable) d;
			}
		}
		return null;
	}

	/**
	 * Retourne un tableau de variables
	 * 
	 * @return tableau de variables
	 */
	public Donnee[] getDonnees() {
		return ensDonnees.toArray(new Donnee[ensDonnees.size()]);
	}

	/**
	 * Défini la valeur d'une variable
	 * 
	 * @param nomVar nom de la variable
	 * @param valeur valeur de la variable
	 */
	public void setValeur(String nomDonnee, String valeur) {
		Interpreter interpreter = this.getInterpreteur();

		valeur = Donnee.traduire(valeur);

		if (this.getDonnee(nomDonnee) instanceof Variable) {
			// évite l'interprétation du caractère
			if (this.getVariable(nomDonnee).getType().equals("caractere"))
				valeur = "\"" + valeur + "\"";

			try {
				this.getVariable(nomDonnee).setValeur(interpreter.eval(valeur));

				// évite l'interprétation de la chaîne de caractère
				Object interpretValeur;
				if (this.getVariable(nomDonnee).getType().equals("chaine"))
					interpretValeur = "\"" + this.getVariable(nomDonnee).getValeur() + "\"";
				else
					interpretValeur = this.getVariable(nomDonnee).getValeur();

				interpreter.eval(nomDonnee + " = " + interpretValeur);

				if (prog.getDonneesATracer().contains(this.getVariable(nomDonnee)))
					prog.traceVariable += this.getVariable(nomDonnee).toString() + "\n";
			} catch (EvalError e) {
				e.printStackTrace();
			}
		} else {
			int indice = -1;
			try {
				indice = (int) interpreteur.eval(nomDonnee.split("\\[|\\]")[1].trim());
			} catch (NumberFormatException | EvalError e) {
				e.printStackTrace();
			}
			nomDonnee = nomDonnee.split("\\[")[0];
			if (this.getDonnee(nomDonnee) instanceof Tableau) {
				Tableau tab = (Tableau) this.getDonnee(nomDonnee);
				tab.setValeur(indice, new Variable("", "", false, this));
				Variable var = tab.get(indice);
				// évite l'interprétation du caractère
				if (var.getType().equals("caractere"))
					valeur = "\"" + valeur + "\"";

				try {
					var.setValeur(interpreter.eval(valeur));

					// évite l'interprétation de la chaîne de caractère
					Object interpretValeur;
					if (var.getType().equals("chaine"))
						interpretValeur = "\"" + var.getValeur() + "\"";
					else
						interpretValeur = var.getValeur();

					interpreter.eval(nomDonnee + "[" + indice + "]" + " = " + interpretValeur);

					if (prog.getDonneesATracer().contains(this.getDonnee(nomDonnee)))
						prog.traceVariable += this.getDonnee(nomDonnee).toString() + "\n";
				} catch (EvalError e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String toString() {
		String s = "Algorithme : " + this.nom + "\n";
		for (Donnee d : this.ensDonnees) {
			s += d + "\n";
		}
		return s;
	}

	/**
	 * Retourne le programme
	 * 
	 * @return programme
	 */
	public Programme getProgramme() {
		return this.prog;
	}

	/**
	 * Retourne le contenu de l'algorithme
	 * @return tableau de ligne
	 */
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

	/**
	 * Retourne la ligne de début d'algorithme
	 * @return
	 */
	public int getLigneDebut() {
		return this.ligneDebut;
	}

	/**
	 * Retourne la ligne courrante
	 * @return
	 */
	public int getLigneCourrante() {
		return this.ligneCourrante;
	}

	/**
	 * Retourne le nom de l'algorithme
	 * @return
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * Reset l'algorithme
	 */
	public void reset() {
		this.reset = true;
		this.ligneCourrante = this.ligneDebutAlgorithme;

	}

	/**
	 * Retourne si l'algorithme est en cours de reset
	 * @return vrai si en cours de reset
	 */
	public boolean estEnTrainDeReset() {
		return reset;
	}
}
