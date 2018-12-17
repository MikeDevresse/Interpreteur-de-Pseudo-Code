package pseudoCode;

public class Variable<T> {

	private String name;
	private String type;
	private T value;

	public Variable(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public T getValue() {
		return value;
	}

	public String getName() {
		return name;
	}

	public void setValue(T content) {
		this.value = content;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Variable [name=" + this.name + ", type = " + this.type + "]";
	}

}
