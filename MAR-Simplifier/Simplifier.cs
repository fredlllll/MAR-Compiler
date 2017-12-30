using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MAR_Simplifier
{
    public class Simplifier
    {
        /*
        supports the following:
        while(mem/reg){
        }
        if(mem/reg){

        }else{

        }
        func(name,arg1,arg2,arg3){

            }
        return mem/reg
        callfunc(name,arg...)=>mem/reg;

    */
        Dictionary<TokenType, Action<Token>> handlers = new Dictionary<TokenType, Action<Token>>();
        List<string> newLines = new List<string>();

        Stack<Token> blockStarts = new Stack<Token>();

        int labelIndex = 0;

        public Simplifier()
        {
            handlers[TokenType.Assembly] = EmitAssembly;
            handlers[TokenType.While] = EmitWhile;
            handlers[TokenType.If] = EmitIf;
            handlers[TokenType.Else] = EmitElse;
            handlers[TokenType.Func] = EmitFunc;
            handlers[TokenType.Return] = EmitReturn;
            handlers[TokenType.CallFunc] = EmitCallFunc;
            handlers[TokenType.BlockEnd] = EmitBlockEnd;
        }

        public string Simplify(Token[] tokens)
        {
            newLines.Clear();
            blockStarts.Clear();

            for(int i = 0; i < tokens.Length; i++)
            {
                Token t = tokens[i];
                var handler = handlers[t.Type];
                handler(t);
            }

            return string.Join("\n", newLines);
        }

        int Emit(string line)
        {
            string[] lines = line.Split('\n');
            newLines.AddRange(lines);
            return newLines.Count - 1;
        }

        void ReplaceLine(int index, string line)
        {
            newLines[index] = line;
        }

        int GetIndex()
        {
            return labelIndex++;
        }

        void EmitAssembly(Token t)
        {
            Emit(t.Content);
        }

        void EmitWhile(Token t)
        {
            blockStarts.Push(t);

            var what = t.Content.Substring("while(".Length).TrimEnd("){");
            int index = GetIndex();
            Emit(t.Indent + "whilestart" + index + ":");
            Emit(t.Indent + "cmp " + what + ",0");
            Emit(t.Indent + "jz whileend" + index); // jump if condition is 0
            t.Data = index;
        }

        void EmitWhileEnd(Token t)
        {
            int index = (int)t.Data;
            Emit(t.Indent + "jmp whilestart" + index);
            Emit(t.Indent + "whileend" + index + ":");
        }

        void EmitIf(Token t)
        {
            blockStarts.Push(t);

            var what = t.Content.Substring("if(".Length).TrimEnd("){");
            int index = GetIndex();
            Emit(t.Indent + "cmp " + what + ",0");
            int jmpToEndOrElse = Emit(t.Indent + "jz ifend" + index);
            t.Data = new int[] { index, jmpToEndOrElse };
        }

        void EmitElse(Token t)
        {
            Token ifToken = blockStarts.Peek();
            int[] d = (int[])ifToken.Data;
            int index = d[0];
            int jmpToEndOrElse = d[1];
            ReplaceLine(jmpToEndOrElse, t.Indent + "jz ifelse" + index);
            Emit(t.Indent + "jmp ifend" + index);
            Emit(t.Indent + "ifelse" + index + ":");
            ifToken.Data = new int[] { index };
        }

        void EmitIfEnd(Token t)
        {
            int[] d = (int[])t.Data;
            int index = d[0];
            Emit(t.Indent + "ifend" + index + ":");
        }

        void EmitFunc(Token t)
        {
            blockStarts.Push(t);
            string starter = "func(";
            string what = t.Content.Substring(starter.Length).TrimEnd("){");
            string[] parts = what.Split(',');
            Emit(t.Indent + parts[0] + ":");
            Emit(t.Indent + "push BP");
            Emit(t.Indent + "mov BP, SP");
            if(parts.Length > 1)
            {
                Emit(t.Indent + "sub SP , " + (parts.Length - 1));
            }
        }

        void EmitFuncEnd(Token t)
        {
            Emit(t.Indent + "mov SP,BP");
            Emit(t.Indent + "pop BP");
            Emit(t.Indent + "ret 0");
        }

        void EmitCallFunc(Token t)
        {
            //callfunc(name,arg...)=>mem/reg;

            int firstClosingBracket = t.Content.IndexOf(')');
            string starter = "callFunc(";
            string what = t.Content.Substring(starter.Length, firstClosingBracket - starter.Length);
            string[] parts = what.Split(',');

            string functionName = parts[0];
            if(parts.Length > 1)
            {
                int index = GetIndex();
                Emit(t.Indent + "call tmp" + index); //putting IP on stack here
                Emit(t.Indent + "tmp" + index + ":");

                for(int i = 0; i < parts.Length - 1; i++)
                {
                    Emit(t.Indent + "push " + parts[parts.Length - i - 1]);
                }
                Emit(t.Indent + "jmp " + functionName);
            }
            else
            {
                Emit(t.Indent + "call " + functionName);
            }

            if(t.Content.Contains("=>"))
            {
                string target = t.Content.Substring(t.Content.LastIndexOf('>')).TrimEnd(';');
                Emit(t.Indent + "pop " + target);
            }
        }

        void EmitReturn(Token t)
        {
            if(t.Content.Equals("return;"))
            {
                Emit(t.Indent + "mov SP,BP");
                Emit(t.Indent + "pop BP");
                Emit(t.Indent + "ret 0");
            }
            else
            {
                Token ft = blockStarts.Last(x => x.Type == TokenType.Func);

                string what = t.Content.Substring("return ".Length).TrimEnd(';');
                Emit(t.Indent + "mov SP,BP");
                Emit(t.Indent + "pop BP");

                //getting args count
                string starter = "func(";
                string whatFunc = ft.Content.Substring(starter.Length).TrimEnd("){");
                string[] parts = whatFunc.Split(',');
                int argsCount = parts.Length - 1;
                Emit(t.Indent + "add SP," + argsCount);
                Emit(t.Indent + "push [SP+1]");
                Emit(t.Indent + "mov [SP+2], " + what);
                Emit(t.Indent + "ret 0");

                //Emit(t.Indent + "pop A");
                //Emit(t.Indent + "push " + what);
                //Emit(t.Indent + "push A");
            }
        }

        void EmitBlockEnd(Token t)
        {
            Token start = blockStarts.Pop();
            switch(start.Type)
            {
                case TokenType.While:
                    EmitWhileEnd(start);
                    break;
                case TokenType.If:
                    EmitIfEnd(start);
                    break;
                case TokenType.Func:
                    EmitFuncEnd(start);
                    break;
                default:
                    throw new InvalidOperationException("block start token was of wrong type: " + start.Type);
            }
        }
    }
}
