
public class Assignment extends Statement{
	private String name;
	private Expression expression;
	
	//repräsentiert eine zuweiseung der form : variable=expression
	public String getName() {
		//liefert den namen 
		return name; 
	}
	
	public Expression getExpression() {
		return expression;
	}
	
	public Assignment(String name,Expression expression) {
		this.name = name;
		this.expression = expression;
	}
	
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
