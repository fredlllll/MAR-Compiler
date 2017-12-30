namespace MAR_Simplifier
{
    public class Token
    {
        public TokenType Type { get; }
        public string Content { get; }
        public string Indent { get; set; } = "";

        public object Data { get; set; }

        public Token(TokenType type, string content)
        {
            Type = type;
            Content = content;
        }
    }
}
