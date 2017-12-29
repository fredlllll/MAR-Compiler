using System;
using System.IO;

namespace MAR_Simplifier
{
    public class Program
    {
        static void Main(string[] args)
        {
            if(args.Length > 0)
            {
                Simplifier s = new Simplifier();
                Console.Write(s.Simplify(File.ReadAllText(args[0])));
            }
            else
            {
                Console.WriteLine("i need a file as argument pls");
            }
        }
    }
}
