package pseudoCode;

<<<<<<< HEAD
public class Variable<T> {

	private String name;
	private T content;

	public Variable(String name) {
		this.name = name;
	}

	public T getContent() {
		return content;
	}

	public String getName() {
		return name;
	}

	public void setContent(T content) {
		this.content = content;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Variable [content=" + content + ", name=" + name + "]";
=======
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
>>>>>>> branch 'master' of https://github.com/MikeDevresse/ProjetTutS3.git
	}

}
