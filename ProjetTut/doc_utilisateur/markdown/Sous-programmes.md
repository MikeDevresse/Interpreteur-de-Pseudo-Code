# Sous programmes

## Renvoi de valeur par "retourne"
```
ALGORITHME SousAlgo
variable:
	a : reel
DEBUT
	a <-- appel sousProg1()
FIN

ALGORITHME sousProg1() -> reel
DEBUT	
	retourne 2.6
FIN
```

## Envoi de paramètres

### Passage par valeur
```
ALGORITHME TestSousAlgoVariables
variable:
	a : entier	
DEBUT	
	appel SousProg2(2, 3)
FIN

ALGORITHME SousProg2(e var1 : entier, e var2 : entier)
variable:
	a : entier
	
DEBUT
	a <-- var1 + var2
	ecrire(a)
FIN
```
### Passage par référence
```
ALGORITHME TestSousAlgoVariables2
variable:
	z : entier
	
DEBUT
	appel SousProg3(2, z)
	
	ecrire(z)
FIN

ALGORITHME SousProg3(e var1 : entier, s var3)
variable:
	a : entier
	
DEBUT
	var3 <-- 17
FIN
