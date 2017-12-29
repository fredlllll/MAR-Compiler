using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MAR_Compiler.Tokenizing
{
    public class Token
    {
        public string Text { get; }
        public TokenType Type { get; }

        public Token(string text, TokenType type)
        {
            Text = text;
            Type = type;
        }
    }
}
