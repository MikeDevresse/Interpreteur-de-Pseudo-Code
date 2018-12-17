package pseudoCode;

public class Variable<T> {

	private String name;
	private String type;
	private T content;

	public Variable(String name, String type) {
		this.name = name;
		this.type = type;
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
		return "Variable [name=" + this.name + ", type = " + this.type + "]";
	}

}
