package pseudoCode;

public class Variable <T>
{

	private String name;
	private T	   content;

	public Variable ( String name )
	{
		this.name = name;
	}

	public T getContent ()
	{
		return content;
	}

	public String getName ()
	{
		return name;
	}

	public void setContent ( T content )
	{
		this.content = content;
	}

	public void setName ( String name )
	{
		this.name = name;
	}

	@Override
	public String toString ()
	{
		return "Variable [content=" + content + ", name=" + name + "]";
	}

}
