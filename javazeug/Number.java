
public class Number extends Expression{
private int value;
public int getValue() {
	return value;
}
public Number(int value) {
	this.value = value;
}
public void accept(Visitor visitor) {
	visitor.visit(this);
}
}
