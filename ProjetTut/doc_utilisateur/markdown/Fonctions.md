### Les fonctions de base qui sont disponibles dans notre interpréteur de pseudo-code sont :  
- lire(variable) permet d'affecter une valeur à une variable
- écrire(variable) permet d'afficher la valeur de la variable
***
### Il y a aussi les fonctions de conversions : 
- enChaine(expression numérique) permet de convertir l'expression numérique en chaine de caractères
- enEntier(expression chaine de caractères) permet de convertir une chaine en entier
- enRéel(expression chaine de caractères) permet de convertir une chaine en réel
- car(entier) permet de convertir l'entier en caractère grâce à la table ASCII
- ord(caractère) permet de convertir le caractère donnée en  entier grâce à la table ASCII
***
### De plus nous avons les arrondis : 
- plancher(réel) permet de convertir le réel en entier (exemple : 2.8 donnera 2)
- plafond(réel) permet de convertir le réel en entier du dessus (exemple : 2.1 donnera 3)
- arrondi(réel) permet de convertir le réel en entier le plus proche (exemples : 2.4 donnera 2 et 2.6 donnera 3)
***
### Et pour finir les autres primitives : 
- aujourd'hui() donne la date du jour
- jour(chaine) donne le jour dans une date format JJ/MM/AAAA
- mois(chaine) donne le mois dans une date format JJ/MM/AAAA
- annee(chaine) donne l'année dans une date format JJ/MM/AAAA
- estRéel(chaine) retourne vrai si la chaine entrée en paramètre est un réel
- estEntier(entier) retourne vrai si la chaine entrée en paramètre est un entier
- hasard(entier) retourne en chiffre aléatoire entre 0 et l'entier-1 (exemple : hasard(10) retournera un entier entre 0 et 9