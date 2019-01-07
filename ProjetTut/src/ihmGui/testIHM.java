package ihmGui;

public class testIHM {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new GUI("ALGORITHME TestConditionsV2\n" + 
				"constante:\n" + 
				"\n" + 
				"variable :\n" + 
				"	a, b	: entier\n" + 
				"	\n" + 
				"DEBUT\n" + 
				"	a <-- 10\n" + 
				"\n" + 
				"	si a >= 10 et a <= 20 alors \n" + 
				"		ecrire(\"vrai\")\n" + 
				"	sinon\n" + 
				"		écrire(\"faux\")\n" + 
				"	fsi	\n" + 
				"		\n" + 
				"	si a = 10 ou a = 15 alors\n" + 
				"		écrire(\"vrai\")\n" + 
				"	sinon\n" + 
				"		écrire(\"faux\")\n" + 
				"	fsi	\n" + 
				"		\n" + 
				"	b <-- 15\n" + 
				"	si a = 10 xou b = 15 alors\n" + 
				"		ecrire(\"Les deux valeurs sont différentes\")\n" + 
				"	sinon\n" + 
				"		ecrire(\"Les deux valeurs sont égales\")\n" + 
				"	fsi\n" + 
				"	\n" + 
				"FIN\n" + 
				"		");
	}

}
