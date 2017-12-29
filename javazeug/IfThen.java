
public class IfThen extends Statement {
	private Condition condition;
	private Statement thenBranch;

	public Condition getCond() {
		return condition;
	}

	public Statement getThenBranch() {
		return thenBranch;
	}

	public IfThen(Condition c, Statement t) {
		condition = c;
		thenBranch = t;
	}
	
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
