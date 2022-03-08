package me.gmx;

import me.gmx.parser.CCSParser;

import java.util.Scanner;

public class RCCS {
    static Scanner scan;
    //TODO: unimplemented execution
    public static void main(String[] args){
        /*if (args.length != 1){
            System.out.println("Please enter a process formula via java -jar [this].jar \"a.b.P | c.Q\"");
            System.exit(1);
        }
        String s = "a.b.V + c.a.P|b.Q";
        new CCSParser(s);*/

        String s = "a.b.P + c.a.Q|b.a.P\\{zg}";

        CCSParser c = new CCSParser(s);

    }

    public void start(){

    }





}
