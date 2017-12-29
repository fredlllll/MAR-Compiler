
public class Write extends Statement {
private Expression exp;
public Expression getExpression() {
	return exp;
}
public Write(Expression exp) {
	this.exp = exp;
}
public void accept(Visitor visitor) {
	visitor.visit(this);
}
}
