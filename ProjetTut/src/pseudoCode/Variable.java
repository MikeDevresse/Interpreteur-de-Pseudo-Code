package pseudoCode;

public class Variable<T> {

	private String name;
	private String type;
	private T value;
	private boolean constante;

	public Variable(String name, String type, boolean constante) {
		this.name = name;
		this.type = type;
		this.constante = constante;
	}

	public T getValue() {
		return value;
	}

	public String getName() {
		return name;
	}
	
	public boolean isConstante() {
		return this.constante;
	}

	public void setValue(T content) {
		this.value = content;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		String s = "";
		s += "Variable [name=" + this.name + ", type = " + this.type + ", constante = " + this.constante;
		
		if (this.value != null)
			s += ", valeur = " + this.value;
		
		s += " ]";
		
		return s;
	}

}
