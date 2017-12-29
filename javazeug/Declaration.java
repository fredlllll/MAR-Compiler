
public class Declaration{
	//repräsentiert eine liste von varablendeclarationen von int variablen 
	private String[] names; 
	
	public String[] getNames() {
		return names; 
	}
	
	public Declaration(String[] names) {
		this.names = names;
	}
	
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
