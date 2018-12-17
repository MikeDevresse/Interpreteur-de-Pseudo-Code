package main;

public class Fonction
{
	public static void evaluer ( String nomFonction, String contenue)
	{
		nomFonction = nomFonction.trim();
		switch ( nomFonction )
		{
			case "ecrire" :
				System.out.println( contenue.split( "\"" )[1] );
				break;
		}
	}
}
