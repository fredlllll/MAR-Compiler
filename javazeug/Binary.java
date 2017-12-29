
public class Binary extends Expression{
private Expression lhs;
private Expression rhs;
private Binop operator;
public Expression getLhs() {
	return lhs;
}
public Expression getRhs() {
	return rhs;
}
public Binop getOperator() {
	return operator;
}
public Binary(Expression lhs,  Binop operator,Expression rhs) {
	this.lhs = lhs;
	this.rhs = rhs;
	this.operator = operator;
}

public void accept(Visitor visitor) {
	visitor.visit(this);
}
}
