
public class Return extends Statement{
	private Expression exp;
	public Expression getExpression() {
		return exp;
	}
	public Return(Expression exp) {
		this.exp = exp;
	}
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
