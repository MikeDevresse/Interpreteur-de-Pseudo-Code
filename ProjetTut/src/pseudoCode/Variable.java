package pseudoCode;

public class Variable
{
	private String contenu;
	private String type;

	public Variable ( String type, String contenu )
	{
		this.type = type;
		this.contenu = contenu;
	}

	public Variable ( String type )
	{
		this( type, "" );
	}

	public void setContent ( String contenu )
	{
		this.contenu = contenu;
	}

}
