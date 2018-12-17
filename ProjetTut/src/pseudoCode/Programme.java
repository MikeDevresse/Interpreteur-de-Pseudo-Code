package pseudoCode;

public class Programme
{
	private String[] fichier;
	private Algorithme algo;
	
	public Programme ( String[] fichier ) throws AlgorithmeException
	{
		this.fichier = fichier;
		
		for ( int i = 0 ; i < fichier.length ; i++ )
		{
			String current = fichier[i];
			
			if ( current.replaceAll( " ", "" ).equals( "" ));
			{
				continue;
			}
				
				
			/*if ( algo == null )
			{
				if ( !current.split( " " )[0].equals( "ALGORITHME" ))
				{
					throw new AlgorithmeException("Mauvaise structure du fichier");
				}
			}*/
		}
	}
}
