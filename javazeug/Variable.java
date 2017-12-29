
public class Variable extends Expression{
private String name;

public String getName() {
	return name;
}

public Variable(String name) {
	this.name =name;
}
public void accept(Visitor visitor) {
	visitor.visit(this);
}
}
