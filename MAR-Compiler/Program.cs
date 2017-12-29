using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using MAR_Compiler.Tokenizing;

namespace MAR_Compiler
{
    public class Program
    {
        static void Main(string[] args)
        {
            var e = new PeekableIEnumerable<char>("abcdefghijklmnopqrstuvwxyz0123456789");
            var en = e.GetEnumerator();
            for(int i =0; i< 10; i++)
            {
                en.MoveNext();
                Console.Write(en.Current);
            }
            Console.WriteLine();
            Console.WriteLine(e.Peek());
            Console.WriteLine(e.Peek(1));
            Console.WriteLine(e.Peek(2));
            Console.WriteLine(e.Peek(3));
            for(int i = 0; i < 10; i++)
            {
                en.MoveNext();
                Console.Write(en.Current);
            }

            Console.ReadKey();
        }
    }
}
