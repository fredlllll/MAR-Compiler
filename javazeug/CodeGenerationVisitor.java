import java.util.ArrayList;
import java.util.HashMap;

public class CodeGenerationVisitor extends Visitor {
	public int[] getProgram() {
		String program = getCode();
		return Interpreter.parse(program);
	}
	
	public String getCode() {
		return String.join("\n", lines);
	}
	
	ArrayList<String> lines = new ArrayList<String>();
	
	Function currentFunction;
	HashMap<String,Integer> currentDeclarations = new HashMap<String,Integer>();
	HashMap<String,Integer> currentParameters = new HashMap<String,Integer>();
	
	int emit(String s) {
		lines.add(s);
		return lines.size()-1;
	}
	
	void replaceLine(int index, String str) {
		lines.set(index, str);
	}
	
	void ensureVariableExists(String name) {
		if(!currentDeclarations.containsKey(name) && !currentParameters.containsKey(name)) {
			throw new RuntimeException("undeklarierte variable: "+name);
		}
	}
	
	int getVariable(String name) {
		if(currentDeclarations.containsKey(name)) {
			return currentDeclarations.get(name);
		}
		return currentParameters.get(name);
	}

	public void visit(Program p) {
		int mainIndex = -1;
		for (int i = 0; i < p.getFunctions().length; i++) {
			Function f = p.getFunctions()[i];
			if (f.getName().equals("main")) {
				mainIndex = i;
				break;
			}
		}
		if (mainIndex < 0) {
			throw new RuntimeException("keine main methode");
		}
		emit("LDI 0");
		int returnToEnd = emit("LDI 0");
		visit(p.getFunctions()[mainIndex]);
		for (int i = 0; i < p.getFunctions().length; i++) {
			Function f = p.getFunctions()[i];
			if (i != mainIndex) {
				visit(f);
			}
		}
		emit("HALT");
		replaceLine(returnToEnd, "LDI "+(lines.size()-1));
	}

	public void visit(Declaration d) {
		emit("ALLOC "+d.getNames().length);
		for(int i =0; i< d.getNames().length;i++) {
			currentDeclarations.put(d.getNames()[i], currentDeclarations.size()+1);
		}
	}

	public void visit(Function f) {
		currentFunction = f;
		for(int i =0; i< f.getParameters().length;i++) {
			currentParameters.put(f.getParameters()[i], -i);
		}
		emit(f.getName()+":");
		for(int i =0; i< f.getDeclarations().length;i++) {
			visit(f.getDeclarations()[i]);
		}
		for(int i =0; i< f.getStatements().length;i++) {
			visit(f.getStatements()[i]);
		}
		emit("LDI 0");
		emit("RETURN "+(f.getParameters().length+f.getDeclarations().length));
		currentDeclarations.clear();
		currentParameters.clear();
		currentFunction = null;
	}

	public void visit(Statement s) {
		if(s instanceof Assignment) {
			visit((Assignment)s);
		}
		else if (s instanceof Composite) {
			visit((Composite)s);
		}
		else if (s instanceof IfThen) {
			visit((IfThen)s);
		}
		else if (s instanceof IfThenElse) {
			visit((IfThenElse)s);
		}
		else if (s instanceof While) {
			visit((While)s);
		}
		else if (s instanceof Read) {
			visit((Read)s);
		}
		else if (s instanceof Write) {
			visit((Write)s);
		}
		else if (s instanceof Return) {
			visit((Return)s);
		}else {
			throw new RuntimeException("unbekanntes statement "+s);
	}
		//TODO: rest von statements;
		//TODO: bei den andern sich selber auch ?
	}
	
	// statements
	public void visit(Assignment a) {
		visit(a.getExpression());
		ensureVariableExists(a.getName());
		emit("STS "+getVariable(a.getName()));
	}

	public void visit(Composite c) {
		for(int i =0; i< c.getStatements().length;i++) {
			visit(c.getStatements()[i]);
		}
	}

	public void visit(IfThen it) {
		visit(it.getCond());
		emit("NOT");
		int jmp = emit("JUMP 0");
		visit(it.getThenBranch());
		int target = emit("NOP");
		replaceLine(jmp,"JUMP "+target);
	}

	public void visit(IfThenElse ite) {
		visit(ite.getCond());
		emit("NOT");
		int jmpToElse = emit("JUMP 0");
		visit(ite.getThenBranch());
		emit("LDI -1");
		int jmpToEnd = emit("JUMP 0");
		int target = emit("NOP");
		replaceLine(jmpToElse,"JUMP "+target);
		visit(ite.getElseBranch());
		target = emit("NOP");
		replaceLine(jmpToEnd,"JUMP "+target);
	}

	public void visit(Read r) {
		emit("IN");
		ensureVariableExists(r.getName());
		emit("STS "+getVariable(r.getName()));
	}

	public void visit(Return r) {
		visit(r.getExpression());
		emit("RETURN "+(currentFunction.getParameters().length+currentFunction.getDeclarations().length));
	}

	public void visit(While w) {
		int targetStart = emit("NOP");
		visit(w.getCond());
		emit("NOT");
		int jmpToEnd = emit("JUMP 0");
		visit(w.getBody());
		emit("LDI -1");
		emit("JUMP "+targetStart);
		int targetEnd = emit("NOP");
		replaceLine(jmpToEnd,"JUMP "+targetEnd);
	}

	public void visit(Write w) {
		visit(w.getExpression());
		emit("OUT");
	}

	// expressions
	
	public void visit(Expression e) {
		if(e instanceof Binary) {
			visit((Binary)e);
		}else if(e instanceof Call) {
			visit((Call)e);
		}else if(e instanceof Number) {
			visit((Number)e);
		}else if(e instanceof Unary) {
			visit((Unary)e);
		}else if(e instanceof Variable) {
			visit((Variable)e);
		}else {
			throw new RuntimeException("unbekannte expression "+e);
		}
	}
	
	public void visit(Binary b) {
		visit(b.getRhs());
		visit(b.getLhs());
		Binop op = b.getOperator();
		switch(op) {
		case DivisionOperator:
			emit("DIV");
			break;
		case Minus:
			emit("SUB");
			break;
		case Modulo:
			emit("MOD");
			break;
		case MultiplicationOperator:
			emit("MUL");
			break;
		case Plus:
			emit("ADD");
			break;
		default:
			throw new RuntimeException("unbekannter binär operator: "+op);
		}
	}

	public void visit(Call c) {
		for(int i =0; i< c.getArguments().length;i++) {
			visit(c.getArguments()[i]);
		}
		emit("LDI "+c.getFunctionName());
		emit("CALL "+c.getArguments().length);
	}

	public void visit(Number n) {
		emit("LDI "+n.getValue());
	}

	public void visit(Unary u) {
		visit(u.getOperand());
		switch(u.getOperator()) {
		case Minus:
			emit("LDI -1");
			emit("MUL");
			break;
		default:
			throw new RuntimeException("unbekannter unär operator: "+u.getOperator());
		}
	}

	public void visit(Variable v) {
		ensureVariableExists(v.getName());
		emit("LDS "+getVariable(v.getName()));
	}

	// conditions
	
	public void visit(Condition c) {
		if(c instanceof BinaryCondition) {
			visit((BinaryCondition)c);
		}else if(c instanceof Comparison) {
			visit((Comparison)c);
		}else if(c instanceof True) {
			visit((True)c);
		}else if(c instanceof False) {
			visit((False)c);
		}else if(c instanceof UnaryCondition) {
			visit((UnaryCondition)c);
		}else {
			throw new RuntimeException("unbekannte condition "+c);
		}
		//TODO: elsif kette usw wie oben
	}

	public void visit(BinaryCondition bc) {
		visit(bc.getRhs());
		visit(bc.getLhs());
		switch(bc.getOperator()) {
		case And:
			emit("AND");
			break;
		case Or:
			emit("OR");
			break;
		default:
			throw new RuntimeException("unbekannter operator "+bc.getOperator());
		}
	}

	public void visit(Comparison c) {
		visit(c.getRhs());
		visit(c.getLhs());
		switch(c.getOperator()) {
		case Equals:
			emit("EQ");
			break;
		case Greater:
			emit("LE");
			emit("NOT");
			break;
		case GreaterEqual:
			emit("LT");
			emit("NOT");
			break;
		case Less:
			emit("LT");
			break;
		case LessEqual:
			emit("LE");
			break;
		case NotEquals:
			emit("EQ");
			emit("NOT");
			break;
		default:
			throw new RuntimeException("unbekannter comparator: "+c.getOperator());
		}
	}

	public void visit(True t) {
		emit("LDI -1");
	}

	public void visit(False f) {
		emit("LDI 0");
	}

	public void visit(UnaryCondition uc) {
		visit(uc.getOperand());
		switch(uc.getOperator()) {
		case Not:
			emit("NOT");
			break;
		default:
			throw new RuntimeException("unbekannter operator: "+uc.getOperator());
		}
	}
}
