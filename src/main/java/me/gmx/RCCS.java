package me.gmx;

import me.gmx.parser.CCSParser;
import me.gmx.process.ProcessTemplate;
import me.gmx.process.nodes.LabelNode;
import me.gmx.process.nodes.LabelNodeFactory;
import me.gmx.process.process.ProcessFormula;

import javax.swing.*;
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
        String s;
        if (args.length > 0)
            s = args[0];
        else
            s = "a.b.P + c.r.Q|b.g.P\\{zg}";

        ProcessTemplate a = null;
        CCSParser c = new CCSParser();
        try {
            a = c.parseLine(s);
        }catch(Exception e){
            e.printStackTrace();
        }

        System.out.println(String.format("Formula before complex init and minimization: %s", a.prettyString()));
        System.out.println("\nMinimizing and initializing function...");
        a.initComplex();
        System.out.println(String.format("Formula after complex init and minimization: %s", a.prettyString()));
        while(!a.getActionableLabels().isEmpty()){
            System.out.println(String.format("%s", a.prettyString()));
            System.out.println("Please type the label you'd like to act on:");
            String st = scan.next();
            if (st == "") continue;
            LabelNode n = LabelNodeFactory.parseNode(st);
            try{
                System.out.println(String.format("%s -%s-> %s",
                        a.prettyString(),n.origin(),a.actOn(n).prettyString()));
                break;
            }catch (Exception e){
                System.out.println("Could not act on label!");
            }

        }



    }

    public void start(){

    }





}
