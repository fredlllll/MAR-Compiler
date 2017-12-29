
public class False extends Condition {
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
