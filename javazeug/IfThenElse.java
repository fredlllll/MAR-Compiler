
public class IfThenElse extends Statement {
	private Condition condition;
	private Statement thenBranch;
	private Statement elseBranch;

	public Condition getCond() {
		return condition;
	}

	public Statement getThenBranch() {
		return thenBranch;
	}

	public Statement getElseBranch() {
		return elseBranch;
	}

	public IfThenElse(Condition c, Statement t, Statement e) {
		condition = c;
		thenBranch = t;
		elseBranch = e;
	}
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
