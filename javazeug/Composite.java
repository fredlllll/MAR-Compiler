
public class Composite extends Statement{
	private Statement[] statements;
	
	public Statement[] getStatements() {
		return statements;
	}
	
	public Composite(Statement[] statements) {
		this.statements = statements;
	}
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
