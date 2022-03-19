package edu.iastate.cs288.hw4;

/**
 * 
 * @author Tanay Parikh
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;

// Message Tree..
public class MsgTree {

    // Attributes..
    public char payloadChar;
    public MsgTree left;
    public MsgTree right;
    private static int characterCount = 0;
    private static int bits = 0;
   
    private static int staticCharIdx = 0;
    
    //Constructor building the tree from a string
    public MsgTree(String encodingString){
        
        Stack<MsgTree> stack = new Stack<>();
        char[] characters = encodingString.toCharArray();
        int index = 0;
        this.payloadChar = characters[index++];
        stack.add(this);
        while(!stack.isEmpty()) {
            
            MsgTree tree = stack.pop();
            if((tree.left == null || tree.right == null) && tree.payloadChar =='^') {
                
                if(index < characters.length) {
                    
                    stack.push(tree);
                    MsgTree subTree = new MsgTree(characters[index++]);
                    if(tree.left == null) {
                        tree.left = subTree;
                    } else {
                        tree.right = subTree;
                    }
                    stack.push(subTree);
                
                }
                
            }
            
        }
        characterCount = 0;
        bits = 0;
        
    }
    
    //Constructor for a single node with null children
    public MsgTree(char payloadChar){
        
        this.payloadChar = payloadChar;
        this.left = null;
        this.right = null;
        
    }
    
    //method to print characters and their binary codes
    public static void printCodes(MsgTree root, String code){
        
        System.out.println("\ncharacter code\n"
                         + "-------------------------\n");
        Stack<Character> stack = new Stack<>();
        printCodes(root, stack);
        System.out.println();
        
    }
    
    // Print codes..
    private static void printCodes(MsgTree root, Stack<Character> stack) {
        
        if(root == null) {
            return;
        } else if(root.payloadChar == '^') {
            // For Left..
            stack.add('0');
            printCodes(root.left, stack);
            stack.pop();
            stack.add('1');
            printCodes(root.right, stack);
            stack.pop();
        } else {
            String code = "";
            bits += stack.size();
            Iterator<Character> iterator = stack.iterator();
            while(iterator.hasNext()) {
                code += iterator.next();
            }
            System.out.println("   " + root.payloadChar + "       " + code);
            characterCount++;
        }
        
    }
    
    // decoding
    public static void decode(MsgTree tree, String code) {
        
        System.out.println("Decoding String: \n");
        staticCharIdx = 0;
        char[] codes = code.toCharArray();
        MsgTree root = (MsgTree) tree;
        while(staticCharIdx < codes.length) {
            
            MsgTree current = root;
            while(current != null && current.payloadChar == '^') {
                
                char payload = codes[staticCharIdx++];
                if(payload == '1') {
                    current = current.right;
                } else {
                    current = current.left;
                }
                
            }
            if(current != null) {
                System.out.print(current.payloadChar);
            }
            
        }
        System.out.println();
        
    }
    
    // Printing the statistics..
    private static void printStatistics(String encryptedText) {
     
        double avg = ((double)(bits)) / characterCount;
        double savings = (avg * characterCount * 100) / (16 * characterCount);
        System.out.println("\nSTATISTICS: ");
        System.out.println("Avg bits/char:       "+String.format("%.1f", avg));
        System.out.println("Total characters:    "+bits);
        System.out.println("Space savings:       "+String.format("%.1f", (100 - savings))+"%");
        
    }
    
    // Main method..
    public static void main(String[] args) {
        
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Please enter filename to decode: ");
        String filename = keyboard.nextLine();
        keyboard.close();
        File file = new File(filename);
        String encodingTree = "";
        String encryptedText = "";
        try {
        
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()) {
                
                String line = reader.nextLine();
                if(isBinaryText(line)) {
                    encryptedText += line;
                } else {
                    encodingTree += line + '\n';
                }
                
            }
            reader.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("File not found: "+file.getName());
            return;
        }
        
        encodingTree = encodingTree.trim();
        encryptedText = encryptedText.trim();
        
        MsgTree root = new MsgTree(encodingTree);
        MsgTree.printCodes(root, encryptedText);
        MsgTree.decode(root, encryptedText);
        MsgTree.printStatistics(encryptedText);
        
    }

    /**
     * To check if given string is a binary or not.
     * 
     * @param text  text
     * @return true if it is binary else false
     */
    private static boolean isBinaryText(String text) {
        
        char[] tokens = text.toCharArray();
        for(char token: tokens) {
            if(!(token == '1' || token == '0')) {
                return false;
            }
        }
        return true;
        
    }
    
}
