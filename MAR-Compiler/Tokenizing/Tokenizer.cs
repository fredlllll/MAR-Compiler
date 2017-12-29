using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MAR_Compiler.Tokenizing
{
    public class Tokenizer
    {
        public PeekableIEnumerable<char> Code { get; private set; }
        IEnumerator<char> codeEnum;

        public Tokenizer(IEnumerable<char> code)
        {
            Code = new PeekableIEnumerable<char>(code);
        }

        public IEnumerable<Token> GetTokenStream()
        {
            codeEnum = Code.GetEnumerator();

            string currentTokenText = "";

            while(codeEnum.MoveNext())
            {
                Char c = codeEnum.Current;

            }
        }

        /*public IEnumerable<char> Code {get;private set;}

        private int readingPosition = 0;

        public Tokenizer(IEnumerable<char> code)
        {
            Code = code;
        }

        public IEnumerable<Token> GetTokenStream()
        {
            for(int i =0; i< )

            var tokens = new List<Token>();

            var builder = new StringBuilder();

            while(!EOF())
            {
                skip(CharType.WhiteSpace); //white space has no meaning besides sperating tokens (we're not python!)
                switch(peekType())
                {
                    case CharType.Letter: //start of identifier
                        readToken(builder, CharType.AlphaNumeric);
                        string s = builder.ToString();
                        if(KeywordToken.IsKeyword(s))
                            tokens.Add(new KeywordToken(s));
                        else
                            tokens.Add(new IdentifierToken(s));
                        builder.Clear();
                        break;
                    case CharType.Numeric: //start of number literal
                        readToken(builder, CharType.Numeric);
                        tokens.Add(new NumberLiteralToken(builder.ToString()));
                        builder.Clear();
                        break;
                    case CharType.Operator:
                        readToken(builder, CharType.Operator);
                        tokens.Add(new OperatorToken(builder.ToString()));
                        builder.Clear();
                        break;
                    case CharType.OpenBrace:
                        tokens.Add(new OpenBraceToken(next().ToString()));
                        break;
                    case CharType.CloseBrace:
                        tokens.Add(new CloseBraceToken(next().ToString()));
                        break;
                    case CharType.ArgSeperator:
                        tokens.Add(new ArgSeperatorToken(next().ToString()));
                        break;
                    case CharType.StatementSeperator:
                        tokens.Add(new StatementSperatorToken(next().ToString()));
                        break;
                    default:
                        throw new Exception("The tokenizer found an unidentifiable character.");
                }
            }

            return tokens.ToArray();
        }

        /// <summary>
        /// Reads characters into the StringBuilder while they match
        /// the given type(s).
        /// </summary>
        private void readToken(StringBuilder builder, CharType typeToRead)
        {
            while(!EOF() && peekType().HasAnyFlag(typeToRead))
                builder.Append(next());
        }

        /// <summary>
        /// Skips any count of occurences of the given char class(es).
        /// </summary>
        private void skip(CharType typeToSkip)
        {
            while(peekType().HasAnyFlag(typeToSkip))
                next();
        }

        /// <summary>
        /// Returns the CharType of the next char,
        /// without advancing the pointer.
        /// </summary>
        /// <returns></returns>
        private CharType peekType()
        {
            return CharTypeHelper.GetCharType(peek());
        }

        /// <summary>
        /// Returns the CharType of the next char,
        /// advancing the pointer.
        /// </summary>
        /// <returns></returns>
        private CharType nextType()
        {
            return CharTypeHelper.GetCharType(next());
        }


        /// <summary>
        /// Returns the next character without advancing the pointer.
        /// </summary>
        private char peek()
        {
            //TODO: Check for eof()
            return Code[readingPosition];
        }

        /// <summary>
        /// Returns the next character and advances the pointer.
        /// </summary>
        private char next()
        {
            var ret = peek();
            readingPosition++;
            return ret;
        }

        /// <summary>
        /// Returns true if there are no more chars left.
        /// </summary>
        private bool EOF()
        {
            return readingPosition >= Code.Length;
        }*/
    }
}
