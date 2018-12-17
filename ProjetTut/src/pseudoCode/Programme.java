package pseudoCode;

public class Programme
{
	private String[] fichier;
	private Algorithme algo;
	
	private boolean fin = false;
	private int ligneCourrante = 0;
	
	public Programme ( String[] fichier ) throws AlgorithmeException
	{
		this.fichier = fichier;
		
		while ( !fin )
		{
			LigneSuivante();
		}
	}
	
	public void LigneSuivante() throws AlgorithmeException
	{
		String current = fichier[ligneCourrante++];
		String[] mots = current.split( " " );
		
		if ( algo == null )
		{
			if ( !mots[0].equals( "ALGORITHME" ) )
			{
				throw new AlgorithmeException("Mauvaise structure du fichier");
			}
		}
		if ( mots[0].equals( "FIN" ))
		{
			this.fin = true;
		}
		
		
		
		if ( current.replaceAll( " ", "" ).equals( "" ));
		{
			return;
		}
			
			
	}
}
