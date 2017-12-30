using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MAR_Simplifier
{
    public class Tokenizer
    {
        Dictionary<string, TokenType> types = new Dictionary<string, TokenType>();

        public Tokenizer()
        {
            types["while"] = TokenType.While;
            types["if"] = TokenType.If;
            types["}else"] = TokenType.Else;
            types["func"] = TokenType.Func;
            types["return"] = TokenType.Return;
            types["callFunc"] = TokenType.CallFunc;
            types["}"] = TokenType.BlockEnd;
        }

        public IEnumerable<Token> Tokenize(string simplecode)
        {
            List<Token> tokens = new List<Token>();

            var tokenLines = new List<string>();
            var lines = simplecode.Split('\n').Select(x => x).GetEnumerator();

            while(lines.MoveNext())
            {
                string line = lines.Current.TrimStart(' ', '\t');
                if(line.StartsWith("#"))
                {
                    if(tokenLines.Count > 0)
                    {
                        tokens.Add(new Token(TokenType.Assembly, string.Join("\n", tokenLines)));
                        tokenLines.Clear();
                    }
                    string part = line.Substring(1);
                    var starter = new string(part.TakeWhile((x) => char.IsLetterOrDigit(x) || x == '}').ToArray());
                    var indent = new string(lines.Current.TakeWhile(x => char.IsWhiteSpace(x)).ToArray());
                    var type = types[starter];
                    tokens.Add(new Token(type, part) { Indent = indent });
                }
                else
                {
                    tokenLines.Add(lines.Current);
                }
            }
            if(tokenLines.Count > 0)
            {
                tokens.Add(new Token(TokenType.Assembly, string.Join("\n", tokenLines)));
                tokenLines.Clear();
            }

            return tokens;
        }
    }
}
