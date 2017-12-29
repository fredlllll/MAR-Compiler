
public class While extends Statement{
private Condition condition;
private Statement body;
public Condition getCond() {
	return condition;
}
public Statement getBody() {
	return body;
}

public While(Condition c, Statement b) {
	condition = c;
	body = b;
}
public void accept(Visitor visitor) {
	visitor.visit(this);
}
}
