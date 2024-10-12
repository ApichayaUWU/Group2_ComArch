package Assembler;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;

interface Tokenizer {
    boolean hasNextToken();
    void checkNextToken();
    String peek();
    String consume() throws SyntaxError;
}

public class AssemblyTokenizer implements Tokenizer {
    String[] tokens;
    List<String> tokensList = new ArrayList<>();
    String fileName;
    public AssemblyTokenizer(String fileName) throws IOException {
        this.fileName = fileName;
        tokenizeAssemblyFile();
    }

    // Method to tokenize each line from the file
    public static ArrayList<String> tokenizeLine(String line) {
        // Remove leading/trailing whitespace and split by space or special characters
        line = line.trim();

        // Split by spaces or special characters and return as an ArrayList
        String[] tokens = line.split("\\s+"); // Splitting by spaces or newlines
        int i=0;

        return new ArrayList<>(Arrays.asList(tokens)); // Convert List to ArrayList
    }


    public List<String> getTokensList(){
        return tokensList;
    }

    // Method to read and tokenize an entire assembly file
    public void tokenizeAssemblyFile() throws IOException {
//        List<String[]> tokensList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(this.fileName));
        String line;
        StringBuilder src = new StringBuilder();
        ArrayList<String> tokens = new ArrayList<>();
        // Read each line in the file
        while ((line = reader.readLine()) != null) {

            // Trim and skip empty lines
            if (!line.trim().isEmpty()) {
                line = line + " ! ";
                tokens = tokenizeLine(line.trim());

                for(int l=0; l<tokens.size(); l++){
                    src.append(tokens.get(l));
                    tokensList.add(tokens.get(l));
                }


            }


        }

        reader.close();
    }


    @Override
    public boolean hasNextToken() {
        return !tokensList.isEmpty();
    }

    @Override
    public void checkNextToken() {
        if (!hasNextToken()) throw new NoSuchElementException("No more tokens");
    }

    @Override
    public String peek() {
        checkNextToken();
        return tokensList.getFirst();
    }

//    public boolean peek(String s) {
//        if (!hasNextToken()) return false;
//        return peek().equals(s);
//    }

    @Override
    public String consume() throws SyntaxError {
        checkNextToken();
        return tokensList.removeFirst();
    }

    public void consume(String s) throws SyntaxError {
        if (hasNextToken()) consume();
        else throw new SyntaxError(s + " expected");
    }

    // Helper method to convert tokens into the LinkedList
//    private void createList() throws Assembler.SyntaxError {
//        int tokenIndex = 0; // Track the token index to handle comment after 4 tokens
//
//
//        for (String token : tokens) {
//            token = token.trim(); // Remove leading/trailing whitespace
//            if (token.isEmpty()) continue; // Skip empty tokens
//                // Add numeric values (e.g., immediate values, addresses)
//                if (token.matches("\\d+|-\\d+")) {
//                    list.addLast(token);
//                }
//                // Add labels or instructions (starting with a letter and allowing alphanumeric characters)
//                else if (token.matches("[A-Za-z][A-Za-z0-9]*")) {
//                    list.addLast(token);
//                }
//                // Handle invalid tokens by throwing an error
//                else {
//                    System.out.println("Invalid token: " + token);
//                    throw new Assembler.SyntaxError("Invalid token");
//                }
//                tokenIndex++; // Increment the token index
//
//        }
//
//    }
    public void printToken(){
        System.out.println(tokensList);
    }

}


// Exception class to handle syntax errors
class SyntaxError extends Exception {
    public SyntaxError(String message) {
        super(message);
    }
}