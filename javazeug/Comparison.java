
public class Comparison extends Condition {
	private Expression lhs;
	private Expression rhs;
	private Comp operator;
	public Comparison(Expression lhs,  Comp operator,Expression rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
		this.operator = operator;
	}
	public Expression getLhs() {
		return lhs;
	}
	public Expression getRhs() {
		return rhs;
	}
	public Comp getOperator() {
		return operator;
	}
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
