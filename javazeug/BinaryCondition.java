
public class BinaryCondition extends Condition {
private Condition lhs;
private Condition rhs;
private Bbinop operator;
public BinaryCondition(Condition lhs, Condition rhs, Bbinop operator) {
	this.lhs = lhs;
	this.rhs = rhs;
	this.operator = operator;
}
public Condition getLhs() {
	return lhs;
}
public Condition getRhs() {
	return rhs;
}
public Bbinop getOperator() {
	return operator;
}
public void accept(Visitor visitor) {
	visitor.visit(this);
}
}
