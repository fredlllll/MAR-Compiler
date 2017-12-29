using System;
using System.Collections.Generic;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace MAR_Compiler.Tokenizing
{
    /// <summary>
    /// The possible classes for a character.
    /// This is a Flags-enum, please note that there are
    /// compund values.
    /// </summary>
    [Flags]
    enum CharType
    {
        /// <summary>
        /// Unknown. This is 0x00, so it can't be checked with HasFlag!
        /// (A character can only be unknown or anything else)
        /// </summary>
        Unknown = 0x00,
        /// <summary>
        /// a-z,A-Z,_. Anything acceptable to start an identifier.
        /// </summary>
        Letter = 0x01, //we need binary literals!
        /// <summary>
        /// 0-9.
        /// </summary>
        Numeric = 0x02,
        /// <summary>
        /// Spaces,tabs. Whitespace, but no newline.
        /// </summary>
        LineSpace = 0x04,
        /// <summary>
        /// A newline character.
        /// </summary>
        NewLine = 0x08,
        /// <summary>
        /// +,-,*,/,%,&,|,=,&gt;,&lt;,!.
        /// </summary>
        Operator = 0x10,
        /// <summary>
        /// (,[,{.
        /// </summary>
        OpenBrace = 0x20,
        /// <summary>
        /// ),],}.
        /// </summary>
        CloseBrace = 0x40,
        /// <summary>
        /// ,. Comma used to seperate arguments to functions.
        /// </summary>
        ArgSeperator = 0x80,
        /// <summary>
        /// ;. Semicolon used to seperate statements.
        /// </summary>
        StatementSeperator = 0x100,

        //compund values:
        AlphaNumeric = Letter | Numeric,
        WhiteSpace = LineSpace | NewLine,
        Brace = OpenBrace | CloseBrace,
        /// <summary>
        /// Chars that "have a special meaning".
        /// </summary>
        MetaChar = Operator | Brace | ArgSeperator | StatementSeperator,
        All = AlphaNumeric | WhiteSpace | MetaChar,
    }

    static class CharTypeHelper
    {
        public static CharType GetCharType(char c)
        {
            //First the small sets
            switch(c)
            {
                case '+':
                case '-':
                case '*':
                case '/':
                case '%':
                case '&':
                case '|':
                case '=':
                    return CharType.Operator;
                case '(':
                case '[':
                case '{':
                    return CharType.OpenBrace;
                case ')':
                case ']':
                case '}':
                    return CharType.CloseBrace;
                case ',':
                    return CharType.ArgSeperator;
                case ';':
                    return CharType.StatementSeperator;
                case '\r': //\r and \n have UnicodeCategory.Control, not LineSeperator...
                case '\n':
                    return CharType.NewLine;
            }

            //then the categories
            switch(char.GetUnicodeCategory(c))
            {
                case UnicodeCategory.DecimalDigitNumber:
                    return CharType.Numeric;
                case UnicodeCategory.LineSeparator: //just in case... (see above)
                    return CharType.NewLine;
                case UnicodeCategory.ParagraphSeparator:
                case UnicodeCategory.LowercaseLetter:
                case UnicodeCategory.OtherLetter:
                case UnicodeCategory.UppercaseLetter:
                    return CharType.Letter;
                case UnicodeCategory.SpaceSeparator:
                    return CharType.LineSpace;
            }

            return CharType.Unknown; //something really odd, we could probably allow it as a CharType.Alpha, when its not a Control-Char.
        }
    }
}
