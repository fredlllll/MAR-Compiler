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
        Dictionary<string, Action<string>> handlers = new Dictionary<string, Action<string>>();
        List<string> newLines = null;
        IEnumerator<string> lines; 

        public Simplifier()
        {
            handlers["while"] = EmitWhile;
            handlers["if"] = EmitIf;
            handlers["func"] = EmitFunc;
            handlers["return"] = EmitReturn;
            handlers["callfunc"] = EmitCallFunc;
        }

        public string Simplify(string simplecode)
        {
            newLines = new List<string>();
            lines = simplecode.Split('\n').Select(x => x).GetEnumerator();

            while(lines.MoveNext())
            {
                string line = lines.Current;
                if(line.StartsWith("#"))
                {
                    string part = line.Substring(1);
                    var starter = new string(line.TakeWhile((x) => char.IsLetterOrDigit(x)).ToArray());
                    var handler = handlers[starter];
                    handler(part);
                }
            }

            return string.Join("\n", newLines);
        }

        void EmitWhile(string part)
        {

        }

        void EmitIf(string part)
        {

        }

        void EmitFunc(string part)
        {

        }

        void EmitCallFunc(string part)
        {

        }

        void EmitReturn(string part)
        {

        }
    }
}
