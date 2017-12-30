using System;


public static class Extensions
{
    public static string TrimEnd(this string input, string suffixToRemove, StringComparison comparisonType = StringComparison.Ordinal, int count = 1)
    {
        if(input != null && suffixToRemove != null)
        {
            for(int i = 0; i < count && input.EndsWith(suffixToRemove, comparisonType); i++)
            {
                input = input.Substring(0, input.Length - suffixToRemove.Length);
            }
        }
        return input;

    }
}

