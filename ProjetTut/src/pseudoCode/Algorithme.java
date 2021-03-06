package pseudoCode;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import bsh.EvalError;
import bsh.Interpreter;
import main.Controleur;
import util.Condition;
import util.Fonctions;

/**
 * Interprète le code d'un algorithme
 */

public class Algorithme {

	private Interpreter interpreteur;

	private String nom;
	private ArrayList<Donnee> ensDonnees;
	private String[] fichier;

	private String def = "";
	private String returnValue;
	private boolean fin;
	private boolean reset;
	private int ligneCourante = 0;
	private int ligneDebutAlgorithme;
	private int ligneDebut;
	private Programme prog;

	/*
	 * Attributs pour les sous-algos
	 */
	private ArrayList<String[]> params;

	/**
	 * Constructeur de l'algorithme
	 * 
	 * @param nom        nom de l'algorithme
	 * @param ligneDebut ligne du début de l'algorithme
	 * @param fichier    ensemble de lignes de l'algo
	 * @param p          programme contenant l'algo
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

		String current = fichier[ligneCourante];
		String[] mots = current.split(" ");

		/*
		 * Création des constantes et des variables
		 */
		do {
			if (mots[0].replace(":", "").equals("constante")) {
				this.def = "const";
			} else if (mots[0].replace(":", "").equals("variable")) {
				this.def = "var";
			} else if (this.def != null || !this.def.equals("")) {
				// déclaration de variable
				if (current.matches("[[\\w*],*]*[ ]*\\w*:.*")) {
					String type = current.split(":")[1].trim();
					for (String var : current.split(":")[0].split(",")) {
						ajouterDonnee(DonneeFactory.creerDonnee(var.trim(), type, this.def.equals("const"), this));
					}
				}

				// déclaration de constantes
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
					else if (valeur.equals("true") || valeur.equals("false") || valeur.equals("vrai")
							|| valeur.equals("faux"))
						type = "booleen";

					for (String var : current.split("<--")[0].split(",")) {
						ajouterDonnee(DonneeFactory.creerDonnee(var.trim(), type, this.def.equals("const"), this));
						this.setValeur(var.trim(), valeur);
					}
				}
			}
			current = fichier[ligneCourante++];
			mots = current.split(" ");
		} while (!mots[0].equals("DEBUT"));
		this.ligneDebutAlgorithme = this.ligneCourante;
		this.def = "algo";
	}

	/**
	 * Interprète la ligne suivante
	 * 
	 * @throws AlgorithmeException
	 */
	public boolean ligneSuivante() throws AlgorithmeException {
		if (this.reset) {
			this.ligneCourante = this.ligneDebutAlgorithme;
			this.reset = false;
		}

		/*
		 * Traitement de la fin de l'algorithme
		 */
		if (this.ligneCourante == this.fichier.length-1 || this.fichier[this.ligneCourante].equals("FIN")) {
			this.fin = true;

			/*
			 * Si l'algorithme est un sous programme et qu'une variable est passée par
			 * référence
			 */
			if (params != null) {
				for (String[] param : this.params) {
					if (param[0].equals("s")) {
						Variable v = ((Variable) getDonnee(param[1]));
						v.getAlgo().setValeur(param[2], v.getValeur().toString());
					}
				}
			}

			return false;
		}

		String current = this.fichier[ligneCourante++]; // ligne lue

		// gestion des commentaires
		if (current.matches(".*//.*"))
			current = current.replaceAll("(.*)//.*", "$1");
		current = current.trim();

		// gestion des lignes vides
		if (current.equals(""))
			return false;

		String[] mots = current.split(" "); // découpage de la ligne en mots

		if (mots[0].equals("DEBUT"))
			this.def = "algo";

		/*
		 * Début de l'algorithme
		 */
		if (this.def.equals("algo")) {

			/*
			 * Gestion des appels des sous programmes
			 */
			if (current.matches(".*appel.*\\(.*\\)")) {
				Controleur.getControleur().attend();
				String nomSousProg = current.replaceAll(".*appel (.*)\\(.*\\)", "$1"); // récupération du nom du sous
																						// programme
				// récupération du sous programme appelé
				Algorithme sousAlgo = null;
				for (Algorithme a : this.prog.getAlgos())
					if (a.getNom().equals(nomSousProg))
						sousAlgo = a;

				// si le sous-programme existe
				if (sousAlgo != null) {

					// envoi des paramètres au sous programme
					String params[] = current.replaceAll(".*appel .*\\((.*)\\)", "$1").split(",");

					if (sousAlgo.params != null) {
						for (int i = 0; i < params.length; i++)
							if (sousAlgo.getDonnee(sousAlgo.params.get(i)[1]) instanceof Variable) {
								if (sousAlgo.params.get(i)[0].equals("e")) // passage par valeur
									sousAlgo.setValeur(sousAlgo.params.get(i)[1], params[i]);
								if (sousAlgo.params.get(i)[0].equals("s")) { // passage par référence
									sousAlgo.getDonnee(sousAlgo.params.get(i)[1]).setAlgo(this);
									sousAlgo.params.get(i)[2] = params[i].trim();
								}

							}
					}

					// définition du sous programme comme algo courant
					this.prog.setCourant(sousAlgo);

					// interprétation et récupération de la valeur de retour
					String returnValue;
					do {
						returnValue = sousAlgo.returnValue;
						sousAlgo.ligneSuivante();
					} while (returnValue == null && !sousAlgo.fin);

					// remplacement de l'appel au sous programme par sa valeur de retour
					if (returnValue != null)
						current = current.replaceAll("appel.*\\(.*\\)", returnValue);

					this.prog.setCourant(this); // retour à l'algo appelant
				}
			}

			/*
			 * Affectation des variables
			 */
			if (current.matches(".*<--.*")) { // gestion des affectations simples
				String[] parties = current.split("<--");
				setValeur(parties[0].trim(), parties[1].trim());
			}

			/*
			 * Gestion des return
			 */
			if (current.matches("retourne .*")) {
				try {
					Object val = this.interpreteur.eval(current.replaceAll("retourne (.*)", "$1"));
					if (val instanceof String)
						this.returnValue = "\"" + val + "\"";
					else
						this.returnValue = val.toString();
				} catch (EvalError e) {
					e.printStackTrace();
				}
			}

			/*
			 * Gestion des fonctions
			 */
			if (current.matches(".+\\(.*\\)")) {
				String toInterpret;
				Pattern pattern = Pattern.compile("\\(.*\\)");
				Matcher matcher = pattern.matcher(current);
				if (matcher.find()) {
					toInterpret = matcher.group(0).substring(1, matcher.group(0).length() - 1); // paramètres de la
																								// fonction
					Fonctions.evaluer(current.split("\\(|\\)")[0], Variable.traduire(toInterpret), this); // appel à la
																											// fonction
				}
			}

			/*
			 * Gestion des conditions
			 */
			if (current.matches("si .* alors.*")) {
				String condition = current.split("si | alors")[1];
				interpreterCondition(condition, this.ligneCourante, this.ligneCourante);
			} else if (current.matches(".*si .*") && !current.contains("alors")) {
				/*
				 * Gestion des conditions sur plusieurs lignes
				 */
				String condition = current.split("si")[1];
				boolean conditionFinie = false;
				int ligneDebutCondition = this.ligneCourante;
				int ligneFinCondition;
				for (ligneFinCondition = this.ligneCourante; ligneFinCondition < this.fichier.length
						&& !conditionFinie; ligneFinCondition++) {

					if (!this.fichier[ligneFinCondition].trim().matches(".* alors$")) {
						condition += this.fichier[ligneFinCondition].trim() + " ";
					}

					if (this.fichier[ligneFinCondition].trim().contains("alors")) {
						condition += this.fichier[ligneFinCondition].split("alors")[0];
						conditionFinie = true;
					}
				}

				this.ligneCourante = ligneFinCondition; // saut à la fin de la condition
				interpreterCondition(condition, ligneDebutCondition, ligneFinCondition); // interprétation de la
																							// condition
			}

			/*
			 * Gestion des boucles
			 */
			if (current.matches(".*tq.*alors.*")) {
				String condition = current.split("tq | alors")[1];
				interpreterBoucle(this.ligneCourante, condition, this.ligneCourante, this.ligneCourante);

			} else if (current.matches(".*tq .*")) {
				/*
				 * Gestion des boucles sur plusieurs lignes
				 */
				String condition = current.split("tq")[1];
				boolean conditionFinie = false;
				int ligneDebutBoucle = this.ligneCourante;
				int ligneFinBoucle;
				for (ligneFinBoucle = this.ligneCourante; ligneFinBoucle < this.fichier.length
						&& !conditionFinie; ligneFinBoucle++) {

					if (!this.fichier[ligneFinBoucle].trim().matches(".* alors$")) {
						condition += this.fichier[ligneFinBoucle].trim() + " ";
					}

					if (this.fichier[ligneFinBoucle].trim().contains("alors")) {
						condition += this.fichier[ligneFinBoucle].split("alors")[0];
						conditionFinie = true;
					}
				}

				this.ligneCourante = ligneFinBoucle; // saut à la fin de la condition
				interpreterBoucle(this.ligneCourante, condition, ligneDebutBoucle, ligneFinBoucle); // interprétation
																										// de la boucle
			}

			/*
			 * Gestion des switchs
			 */
			if (current.matches("selon .*")) {
				String varSelon = current.substring("selon ".length());
				interpreterSwitch(varSelon);
			}

		}

		Controleur.getControleur().attend();

		if (mots[0].equals("FIN")) {
			this.fin = true;
		}
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
		int cptLigne = this.ligneCourante;
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

		if (Condition.condition(condition, this.getInterpreteur())) { // condition valide
			Controleur.getControleur().attend();
			do {
				ligneSuivante();
			} while (this.ligneCourante != ligneSinon && this.ligneCourante != ligneFsi);

			// si on rencontre un "sinon", saut jusqu'à la fin de la condition
			if (this.fichier[this.ligneCourante].trim().equals("sinon")) {
				do {
					this.ligneCourante++;
				} while (this.ligneCourante != ligneFsi + 1);
			}

		} else { // condition invalide
			// affichage de la condition fausse
			for (int i = ligneDebut; i <= ligneFin; i++)
				this.prog.ajouterLigneFausse(i);

			Controleur.getControleur().attend();

			this.prog.resetLigneFausse();

			// saut à l'alternative ou la fin de la condition
			do {
				this.ligneCourante++;
			} while ((this.ligneCourante != ligneSinon + 1 && this.ligneCourante != ligneFsi + 1));
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
		int cptLigne = this.ligneCourante;
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

			// parcours à vide de la boucle pour détecter les modifications de variable
			boolean estInfinie = true;
			for (int i = this.ligneCourante; i < ligneFtq; i++) {
				for (String var : condVariables) {
					// si une variable de la condition est modifiée
					if (this.fichier[i].matches(var + "[ ]*<--.*"))
						estInfinie = false;
				}
			}

			if (estInfinie) {
				this.prog.traceExec += "r:/!\\ boucle infinie";
				Controleur.getControleur().attend();
			}
		}

		/*
		 * Interprétation de la boucle
		 */
		while (Condition.condition(condition, this.interpreteur)) {
			this.ligneCourante = ligneBoucle; // retour en haut de la boucle
			Controleur.getControleur().attend();
			do {
				ligneSuivante();
			} while (this.ligneCourante != ligneFtq);
		}

		// indication que la condition est fausse
		for (int i = ligneDebut; i <= ligneFin; i++)
			this.prog.ajouterLigneFausse(i);

		this.ligneCourante = ligneDebut;
		Controleur.getControleur().attend();
		this.prog.resetLigneFausse();

		// retour au point d'origine
		this.ligneCourante = ligneFtq + 1;

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
		int cptLigne = this.ligneCourante;
		int nbSelon = 0;
		int ligneAutrecas = 0;
		int ligneFselon = 0;
		for (int i = cptLigne; i < this.fichier.length; i++) {
			if (this.fichier[i].matches("selon .*"))
				nbSelon++;

			if (this.fichier[i].matches("fselon")) {
				if (nbSelon == 0) {
					ligneFselon = i+1;
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
		this.ligneCourante--;
		do {
			this.ligneCourante++;

			// cas identifié
			if (this.fichier[this.ligneCourante].matches("cas .*[ ]*:")) {
				String cas = this.fichier[this.ligneCourante].split("cas |[ ]*:")[1].replaceAll("\"", "");

				// cas valide
				if (String.valueOf(v.getValeur()).equals(cas)) {
					ligneCourante++;
					do {
						ligneSuivante();

					} while (!this.fichier[this.ligneCourante].matches("cas.*")
							&& !this.fichier[this.ligneCourante].matches("autrecas .*"));
					ligneCourante = ligneFselon;
				}
			} else if (this.fichier[this.ligneCourante].matches("autrecas.*")) {
				// gestion des autres cas
				ligneCourante++;

				do {
					ligneSuivante();
				} while (!this.fichier[this.ligneCourante].matches("fselon"));
				ligneCourante = ligneFselon;
			}

		} while (this.ligneCourante != ligneFselon);

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
	 * @param d donnée
	 */
	public void ajouterDonnee(Donnee d) {
		this.ensDonnees.add(d);
	}

	/**
	 * Retourne une variable donnée
	 *
	 * @param nomDonnee nom de la donnée
	 * @return donnée
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
	 * 
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
	 * Défini la valeur d'une donnée
	 * 
	 * @param nomDonnee nom de la donnée
	 * @param valeur    valeur de la donnée
	 */
	public void setValeur(String nomDonnee, String valeur) {
		setValeur(nomDonnee, valeur, this);
	}

	/**
	 * Défini la valeur d'une donnée
	 * 
	 * @param nomDonnee nom de la donnée
	 * @param valeur    valeur de la donnée
	 * @param source    algorithme possédant la donnée
	 */

	public void setValeur(String nomDonnee, String valeur, Algorithme source) {
		Interpreter interpreter = this.getInterpreteur();
		if (nomDonnee.contains("[")) {
			Tableau tab = ((Tableau) (this.getDonnee(nomDonnee.split("\\[")[0])));
			String[] indices = nomDonnee.split("\\[|\\]");
			for (int i = 1; i < indices.length - 1; i = i + 2) {
				tab = ((Tableau) (tab.get(Integer.parseInt(indices[i]))));
			}
			Variable v = new Variable("", tab.get(0).getType(), false, source);
			try {
				v.setValeur(interpreter.eval(valeur));
			} catch (EvalError e) {
				e.printStackTrace();
			}
			tab.setValeur(Integer.parseInt(indices[indices.length - 1]), v);

			try {
				interpreter.eval(nomDonnee + " = " + valeur);
			} catch (EvalError e) {
				e.printStackTrace();
			}

			if (prog.getDonneesATracer().contains(this.getDonnee(nomDonnee.split("\\[")[0])))
				prog.traceVariable += this.getDonnee(nomDonnee.split("\\[")[0]).toString() + "\n";
		} else {
			if (this.getDonnee(nomDonnee) instanceof Tableau) {
				((Tableau) (this.getDonnee(nomDonnee))).setValeur((Tableau) this.getDonnee(valeur));
				try {
					interpreteur.eval(nomDonnee + " = " + valeur);
				} catch (EvalError e) {
					e.printStackTrace();
				}
			} else if (this.getDonnee(nomDonnee) instanceof Variable) {
				try {
					interpreter.eval(nomDonnee + " = " + valeur);
					((Variable) (this.getDonnee(nomDonnee))).setValeur(interpreter.eval(nomDonnee));
					if (prog.getDonneesATracer().contains(this.getVariable(nomDonnee)))
						prog.traceVariable += this.getVariable(nomDonnee).toString() + "\n";
				} catch (EvalError e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String toString() {
		String s = "Algorithme : " + this.nom + "\n";
		for (Donnee d : this.ensDonnees) {
			s += d + "\t" + d.getAlgo().getNom() + "\n";
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
	 * 
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
	 * 
	 * @return ligne début
	 */
	public int getLigneDebut() {
		return this.ligneDebut;
	}

	/**
	 * Retourne la ligne courrante
	 * 
	 * @return ligne courante
	 */
	public int getLigneCourante() {
		return this.ligneCourante;
	}

	/**
	 * Retourne le nom de l'algorithme
	 * 
	 * @return nom de l'algorithme
	 */
	public String getNom() {
		return this.nom;
	}

	/**
	 * Reset l'algorithme
	 */
	public void reset() {
		this.reset = true;
		this.ligneCourante = this.ligneDebutAlgorithme;

	}

	/**
	 * Retourne si l'algorithme est en cours de reset
	 * 
	 * @return vrai si en cours de reset
	 */
	public boolean estEnTrainDeReset() {
		return reset;
	}

	/**
	 * Renvoi la valeur de retour de l'algorithme
	 * 
	 * @return valeur de retour
	 */
	public String getReturnValue() {
		return this.returnValue;
	}

	/**
	 * Défini les paramètres si l'algorithme est un sous-programme
	 * 
	 * @param params
	 */
	public void setParams(ArrayList<String[]> params) {
		this.params = params;
	}
}
