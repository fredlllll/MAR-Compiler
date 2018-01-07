using System;
using System.IO;
using System.Linq;

namespace MAR_Simplifier
{
    public class Program
    {
        static void Main(string[] args)
        {
            if(args.Length > 0)
            {
                if(args[0] == "-test")
                {
                    Test();
                    return;
                }
                Tokenizer t = new Tokenizer();
                var tokens = t.Tokenize(File.ReadAllText(args[0]));
                Simplifier s = new Simplifier();
                Console.Write(s.Simplify(tokens.ToArray()));
            }
            else
            {
                Console.WriteLine("i need a file as argument pls");
            }
        }

        static void Test()
        {
            string code = @"
; Define constants
HWID_LEGS equ 0x1
HWID_LASER equ 0x2
HWID_LIDAR equ 0x3
HWID_KEYBOARD equ 0x4
HWID_DRILL equ 0x5
HWID_INVENTORY equ 0x6
HWID_RNG equ 0x7 ; random number generator
HWID_CLOCK equ 0x8
HWID_HOLO equ 0x9
HWID_BATTERY equ 0xA
HWID_FLOPPY equ 0xB

TILE_WALL equ 0x8000
TILE_BIOMASS equ 0x4000
TILE_IRON equ 0x200
TILE_COPPER equ 0x100
TILE_CUBOT equ 0x80

.data
the_val: dw 0

.text
    #callFunc(loop);
    brk
	
    #func(loop){
        mov A, 3
        #while(A){
            add [the_val],1
            sub A, 1
        #}
        mov Y, 0
        mov A, [the_val]
        div 9
        #if(Y){
            mov A, 1
            mov B, [the_val]
            hwi HWID_HOLO
        #}else{
            mov A, 1
            mov B, 0xFFFF
            hwi HWID_HOLO
        #}
    #}
";
            code = code.Replace("\r", "");

            Tokenizer t = new Tokenizer();
            var tokens = t.Tokenize(code);
            Simplifier s = new Simplifier();
            Console.Write(s.Simplify(tokens.ToArray()));
            Console.ReadKey();
        }
    }
}
