/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package onepassassembler;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rahul
 */
public class OnePassAssembler {

    /**
     * @param args the command line arguments
     */
    static Hashtable opTab;
    static Hashtable registers;
    static Hashtable symTab;
    static Hashtable conditions;
    static int symTabCounter=1;
    static int literalCount=1;
    
    static void readFile()
    {
        try {
            FileInputStream fstream = new FileInputStream("C:\\study\\example.txt");
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String str;
            while ((str = br.readLine()) != null)   {
                // Print the content on the console
                parse(str);
                //System.out.println (str);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(OnePassAssembler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(OnePassAssembler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    static void parse(String line)
    {
        String [] tokens = line.split(" |,.");
        
        if(tokens.length == 1)
        {
            dealWithFirst(tokens[0]);
        }
        if(tokens.length == 2)
        {
            dealWithFirst(tokens[0]);
            //dealing with the second token
            if(getConstantCode(tokens[1]).equals("null"))
            {
                System.out.print(fillSymTab(tokens[1])+" ");
            }
            else
            {
                System.out.print(getConstantCode(tokens[1])+" ");
            }
            
        }
        else if(tokens.length == 3)
        {
            dealWithFirst(tokens[0]);
            dealWithOperands(tokens[1]);
            dealWithOperands(tokens[1]);
        }
        else if(tokens.length == 4)
        {
            dealWithFirst(tokens[0]);
            dealWithFirst(tokens[1]);
            dealWithOperands(tokens[2]);
            dealWithOperands(tokens[3]);
        }
        System.out.println();
        
    }
    
    static void dealWithFirst(String label)
    {
        //dealing with the first label
            if(getOpCodes(label).equals("null"))
            {
                System.out.print(fillSymTab(label)+" ");
            }
            else
            {
                System.out.print(getOpCodes(label)+" ");
            }
    }
    
    static void fillConditions()
    {
        conditions = new Hashtable();
        conditions.put("LT", "1");
        conditions.put("LE", "2");
        conditions.put("EQ", "3");
        conditions.put("GT", "4");
        conditions.put("GE", "5");
        conditions.put("ANY", "6");
    }
    
    static void fillRegisters()
    {
        registers = new Hashtable();
        registers.put("AREG", "1");
        registers.put("BREG", "2");
        registers.put("CREG", "3");
        registers.put("DREG", "4");
    }
    
    static String getConstantCode(String label)
    {
        
        try
        {
            int x = Integer.parseInt(label);
        }
        catch(Exception e)
        {
            return "null";
        }
        return "C,"+Integer.parseInt(label);
        
    }
    static String getRegisterCode(String label)
    {
        String temp = (String) registers.get(label);
        if(temp==null)        
            return "null";        
        else
            return (String) registers.get(label);
    }
       
    static String getConditionalCode(String label)
    {
        String temp = (String) conditions.get(label);
        if(temp==null)
            return "null";
        else
            return (String) conditions.get(label);
    }
    
    static String getOpCodes(String label)
    {
        String temp = (String) opTab.get(label);
        if(temp==null)         
            return "null";
        else
            return (String) opTab.get(label);
    }
    static String fillSymTab(String label)
    {
        if(symTab.containsKey(label))
        {
            return (String) symTab.get(label);
        }
        else
        {
            symTab.put(label,"S"+","+new Integer(symTabCounter).toString());
            symTabCounter++;
            return (String) symTab.get(label);
        }
    }
    
    static void dealWithOperands(String label)
    {
         //dealing with second token
            if(!getRegisterCode(label).equals("null"))
            {
                System.out.print(getRegisterCode(label)+" ");
            }
            else if(!getConstantCode(label).equals("null"))
            {
                System.out.print(getConstantCode(label)+" ");
            }
            else if(!getConditionalCode(label).equals("null"))
            {
                System.out.print(getConditionalCode(label)+" ");
            }
            else if(label.contains("'"))
            {
                System.out.print("L,"+literalCount+" ");
                literalCount++;
            } 
            else
            {
                System.out.print(fillSymTab(label)+" ");
            }
    }
    
    static void fillOptab()
    {
        opTab = new Hashtable();        
        symTab = new Hashtable();
        opTab.put("STOP", "IS,00");
        opTab.put("ADD", "IS,01");
        opTab.put("SUB", "IS,02");
        opTab.put("MULT", "IS,03");
        opTab.put("MOVER", "IS,04");
        opTab.put("MOVEM", "IS,05");
        opTab.put("COMP", "IS,06");
        opTab.put("BC", "IS,07");
        opTab.put("DIV", "IS,08");
        opTab.put("READ", "IS,09");
        opTab.put("PRINT", "DL,10");
        opTab.put("DC", "DL,R#7");
        opTab.put("DS", "AD,R#8");
        opTab.put("START", "AD,R#11");
        opTab.put("END", "AD,R#12");
        opTab.put("BEGIN", "AD,R#13");
        opTab.put("EQU", "AD,R#14");
    }
    public static void main(String[] args) {
        // TODO code application logic here
        fillOptab();
        fillRegisters();
        fillConditions();
        readFile();
    }
}
