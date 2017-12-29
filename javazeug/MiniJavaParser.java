
import java.util.ArrayList;
import java.util.Scanner;

public class MiniJavaParser {
	public static String readProgramConsole() {
		@SuppressWarnings("resource")
		Scanner sin = new Scanner(System.in);
		StringBuilder builder = new StringBuilder();
		while (true) {
			String nextLine = sin.nextLine();
			if (nextLine.equals("")) {
				nextLine = sin.nextLine();
				if (nextLine.equals(""))
					break;
			}
			if (nextLine.startsWith("//"))
				continue;
			builder.append(nextLine);
			builder.append('\n');
		}
		return builder.toString();
	}

	public static String[] lex(String program) {
		// String[] arr = program.split(" ");
		// return arr; // Todo
		ArrayList<String> tokens = new ArrayList<String>();

		String currentToken = "";
		for (int i = 0; i < program.length(); i++) {
			char c = program.charAt(i);
			if (Character.isWhitespace(c)) {
				if (currentToken.length() > 0) {
					tokens.add(currentToken);
				}
				currentToken = "";
			} else {
				char next;
				switch (c) {
				case '(':
				case ')':
				case '{':
				case '}':
				case ',':
				case ';':
				case '+':
				case '-':
				case '*':
				case '/':
				case '%':
					if (currentToken.length() > 0) {
						tokens.add(currentToken);
					}
					currentToken = "";
					tokens.add("" + c);
					break;
				case '!':
				case '=':
				case '<':
				case '>':
					if (currentToken.length() > 0) {
						tokens.add(currentToken);
					}
					currentToken = "";
					next = program.charAt(i + 1);
					if (next == '=') {
						i++;
						tokens.add(c + "=");
					} else {
						tokens.add("" + c);
					}
					break;
				case '&':
					if (currentToken.length() > 0) {
						tokens.add(currentToken);
					}
					currentToken = "";
					next = program.charAt(i + 1);
					if (next == '&') {
						i++;
						tokens.add(c + "&");
					} else {
						// fehler?
						System.out.println("REEEEEEEE");
					}
					break;
				case '|':
					if (currentToken.length() > 0) {
						tokens.add(currentToken);
					}
					currentToken = "";
					next = program.charAt(i + 1);
					if (next == '|') {
						i++;
						tokens.add(c + "|");
					} else {
						// fehler?
						System.out.println("REEEEEEEE");
					}
					break;
				default:
					currentToken += c;
					break;
				}
			}
		}

		tokens.add("");
		
		return tokens.toArray(new String[tokens.size()]);
	}

	static boolean isDigit(char c) {
		return c - '0' >= 0 && c - '0' <= 9;
	}

	static boolean isLetter(char c) {
		return (c - 'a' >= 0 && c - 'a' <= 'z' - 'a') || (c - 'A' >= 0 && c - 'A' <= 'Z' - 'A');
	}
	
	static boolean isBinop(String str) {
		switch (str) {
		case "+":
		case "-":
		case "*":
		case "/":
		case "%":
			return true;
		default:
			return false;
		}
	}
	
	static boolean isBBinop(String str) {
		switch (str) {
		case "&&":
		case "||":
			return true;
		default:
			return false;
		}
	}
	
	static void error(String str,int from) {
		System.out.println(str+" bei token "+from);
	}

	public static int parseNumber(String[] program, int from) {
		String number = program[from];
		if (number.length() <= 0) {
			error("zahl hat länge 0",from);
			return -1;
		}
		for (int i = 0; i < number.length(); i++) {
			char c = number.charAt(i);
			if (!isDigit(c)) {
				error("zahl hat nicht digit charachter",from);
				return -1;
			}
		}
		return from + 1;
	}

	public static int parseName(String[] program, int from) {
		String name = program[from];
		if (name.length() <= 0) {
			error("name hat länge 0",from);
			return -1;
		}
		if (!isLetter(name.charAt(0))) {
			error("name beginnt nicht mit letter",from);
			return -1;
		}
		for (int i = 1; i < name.length(); i++) {
			char c = name.charAt(i);

			if (!isLetter(c) && !isDigit(c)) {
				error("name enthält ungültiges zeichen",from);
				return -1;
			}
		}
		return from + 1;
	}

	public static int parseType(String[] program, int from) {
		String type = program[from];
		if (!type.equals("int")) {
			error("type ist nicht int",from);
			return -1;
		}
		return from + 1;
	}

	public static int parseDecl(String[] program, int from) {
		from = parseType(program, from);
		if (from < 0) {
			return from;
		}
		from = parseName(program, from);
		if (from < 0) {
			return from;
		}
		while (true) {
			if (!program[from].equals(",")) {
				break;
			}
			from++;
			from = parseName(program, from);
			if (from < 0) {
				return from;
			}
		}
		if (!program[from].equals(";")) {
			error("; am ende von declaration erwartet",from);
			return -1;
		}
		from++;
		return from;
	}

	public static int parseUnop(String[] program, int from) {
		if (!program[from].equals("-")) {
			error("token ist kein unärer operator",from);
			return -1;
		}
		return from + 1;
	}

	public static int parseBinop(String[] program, int from) {
		String op = program[from];
		if(isBinop(op)) {
			return from +1;
		}
		error("token ist kein binärer operator",from);
		return -1;
	}

	public static int parseComp(String[] program, int from) {
		String op = program[from];
		switch (op) {
		case ">":
		case "<":
		case "==":
		case ">=":
		case "<=":
		case "!=":
			return from + 1;
		default:
			error("token ist kein comparator",from);
			return -1;
		}
	}

	public static int parseExpression(String[] program, int from) {
		String token =program[from];
		boolean worked = false;
		if(isLetter(token.charAt(0))) {
			from = parseName(program, from);
			worked = true;
		}else if(isDigit(token.charAt(0))) {
			from = parseNumber(program,from);
			worked = true;
		}else	if(program[from].equals("-")) {
			from++;
			from = parseExpression(program,from);
			worked = true;
		}else	if(program[from].equals("(")) {
			from++;
			from = parseExpression(program,from);
			if(from < 0) {
				return from;
			}
			if(!program[from].equals(")")) {
				error(") erwartet in expression",from);
				return -1;
			}
			from++;
			worked = true;
		}
		
		if(!worked) {
			error("expression hat nicht funktioniert",from);
			return -1;
		}
		
		if(isBinop(program[from])) {
			from = parseBinop(program,from);
			if(from <0) {
				return from;
			}
			from = parseExpression(program,from);
		}
		return from;
	}

	public static int parseBbinop(String[] program, int from) {
		String op = program[from];
		switch (op) {
		case "&&":
		case "||":
			return from + 1;
		default:
			error("token ist kein boolscher binär operator",from);
			return -1;
		}
	}

	public static int parseBunop(String[] program, int from) {
		if(!program[from].equals("!")) {
			error("token ist kein boolscher unär operator",from);
			return -1;
		}
		return from +1;
	}

	public static int parseCondition(String[] program, int from) {
		String token = program[from];
		boolean worked = false;
		if(token.equals("true")||token.equals("false")) {
			from++;
			worked = true;
		}else if(program[from].equals("(")) {
			from++;
			from = parseCondition(program,from);
			if(from < 0) {
				return from;
			}
			if(!program[from].equals(")")) {
				error(") in condition erwartet",from);
				return -1;
			}
			from++;
			worked = true;
		}else if(program[from].equals("!")) {
			if(!program[from].equals("(")) {
				error("( in condition erwartet",from);
				return -1;
			}
			from = parseCondition(program,from);
			if(from < 0) {
				return from;
			}
			if(!program[from].equals(")")) {
				error(") in condition erwartet",from);
				return -1;
			}
			from++;
			worked = true;
		}else {
			from = parseExpression(program,from);
			if(from < 0) {
				return from;
			}
			from = parseComp(program, from);
			if(from < 0) {
				return from;
			}
			from = parseExpression(program,from);
			if(from < 0) {
				return from;
			}
			worked = true;
		}
		
		if(!worked) {
			error("condition hat nicht funktioniert",from);
			return -1;
		}

		if(isBBinop(program[from])) {
			from = parseBbinop(program,from);
			if(from <0) {
				return from;
			}
			from = parseCondition(program,from);
		}
		return from;
	}

	public static int parseStatement(String[] program, int from) {
		if (program[from].equals(";")) {
			return from + 1;
		}
		if (program[from].equals("{")) {
			from++;
			from = parseStatement(program, from);
			if (from < 0) {
				return from;
			}
			while(true) {
				if(program[from].equals("}")) {
					break;
				}
				from=parseStatement(program,from);
				if(from <0) {
					return from;
				}
			}
			if (!program[from].equals("}")) {
				error("} in statement erwartet",from);
				return -1;
			}
			return from + 1;
		}
		if (program[from].equals("write")) {
			from++;
			if (!program[from].equals("(")) {
				error("( in statement erwartet",from);
				return -1;
			}
			from++;
			from = parseExpression(program, from);
			if (from < 0) {
				return from;
			}
			if (!program[from].equals(")")) {
				error(") in statement erwartet",from);
				return -1;
			}
			from++;
			if (!program[from].equals(";")) {
				error("; in statement erwartet",from);
				return -1;
			}
			return from + 1;
		}
		if (program[from].equals("while")) {
			from++;
			if (!program[from].equals("(")) {
				error("( in statement erwartet",from);
				return -1;
			}
			from++;
			from = parseCondition(program, from);
			if (from < 0) {
				return from;
			}
			if (!program[from].equals(")")) {
				error(") in statement erwartet",from);
				return -1;
			}
			from++;
			from = parseStatement(program, from);
			return from;
		}
		if (program[from].equals("if")) {
			from++;
			if (!program[from].equals("(")) {
				error("( in statement erwartet",from);
				return -1;
			}
			from++;
			from = parseCondition(program, from);
			if (from < 0) {
				return from;
			}
			if (!program[from].equals(")")) {
				error(") in statement erwartet",from);
				return -1;
			}
			from++;
			from = parseStatement(program, from);
			if (from < 0) {
				return from;
			}
			if (program[from].equals("else")) {
				from++;
				from = parseStatement(program, from);
			}
			return from;
		}
		
		String token = program[from];
		if(isLetter(token.charAt(0))) {
			from = parseName(program,from);
			if(from < 0) {
				return from;
			}
			if(!program[from].equals("=")) {
				error("= erwartet in statement",from);
				return -1;
			}
			from++;
			if(program[from].equals("read")) {
				from++;
				if(!program[from].equals("(")) {
					error("( erwartet nach read",from);
					return -1;
				}
				from++;
				if(!program[from].equals(")")){
					error(") erwartet in statement",from);
					return -1;
				}
				from++;
				if (!program[from].equals(";")) {
					error("; in statement erwartet",from);
					return -1;
				}
				return from +1;
			}else {
				from = parseExpression(program,from);
				if(from < 0) {
					return from;
				}
				if (!program[from].equals(";")) {
					error("; in statement erwartet",from);
					return -1;
				}
				return from +1;
			}
		}

		error("statement hat nicht funktioniert",from);
		return -1;
	}

	public static int parseProgram(String[] program) {
		int here = 0;
		while (program[here].equals("int")) {
			here = parseDecl(program, here);
			if (here < 0) {
				return here;
			}
		}
		while (program[here].length() > 0) {
			here = parseStatement(program, here);
			if (here < 0) {
				return here;
			}
		}
		return here;
	}

	public static void main(String[] args) { // Todo
		String schwerzuparsen="-(((------((-(-(500))))))*(-(-(-(test)))))";
		String program = "int sum, n, i;\n" + "n = read();\n" + "while (n < 0) {\n" + "n = read();\n" + "}\n" + "\n"
				+ "sum = 0;\n" + "i = 0;\n" + "while (i < n) {\n" + "{\n" + "if (i % 3 == 0 || i % 7 == 0) {\n"
				+ "sum = sum + "+schwerzuparsen+";\n" + "if (i % 3 == 0 || i % 7 == 0) {\n" + "sum = sum + i;\n" + "} else\n"
				+ "sum = 99;\n" + "}\n" + "i = i + 1;\n" + "}\n" + "}\n" + "\n" + "write(sum);";
		
		
		String[] tokens = lex(program);
		for(int i =0; i< tokens.length;i++) {
			System.out.println(Integer.toString(i)+": "+tokens[i]);
		}
		
		int result = parseProgram(tokens);
		if(result >=0) {
			System.out.println("success");
		}else {
			System.out.println("möhhh");
		}
	}
}