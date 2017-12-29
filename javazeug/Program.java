
public class Program {

	// repräsentiert programm, welches aus funktionen besteht
	private Function[] functions;

	public Program(Function[] functions) {
		this.functions = functions;
	}

	public Function[] getFunctions() {
		// liefert die funktion des programs zurück
		return functions;
	}

	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
