package me.gmx;

import me.gmx.parser.CCSParser;
import me.gmx.process.ProcessTemplate;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.nodes.LabelNodeFactory;
import me.gmx.process.process.ProcessFormula;

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
        Scanner scan = new Scanner(System.in);
        String s = "a.b.P + c.r.Q|b.g.P\\{zg}";

        CCSParser c = new CCSParser();
        ProcessTemplate a = c.parseLine(s);

        a.prettyWrite();
        System.out.println("\nMinimizing and recursing function...");
        a.initComplex();
        a.prettyWrite();
        while(!a.getActionableLabels().isEmpty()){
            System.out.println("\nActionable labels:");
            //a.printActionableLabels();
            System.out.println("Please type the label you'd like to act on:");
            String st = scan.next();
            if (st == "") continue;
            LabelNode n = LabelNodeFactory.parseNode(st);
            try{
                System.out.println(a.actOn(n));
            }catch (Exception e){
                System.out.println("Could not act on label!");
            }
            a.prettyWrite();

        }



    }

    public void start(){

    }





}
