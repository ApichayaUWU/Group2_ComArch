import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;

// Interface Tokenizer that defines the methods for token handling
interface Tokenizer {
    boolean hasNextToken();
    void checkNextToken();
    String peek();
    String consume() throws SyntaxError;
}

public class AssemblyTokenizerV2 implements Tokenizer {
    List<String> tokensList = new ArrayList<>(); // List to store tokens from the file
    String fileName;

    // Constructor that takes the file name as input and starts tokenizing the file
    public AssemblyTokenizerV2(String fileName) throws IOException {
        this.fileName = fileName;
        tokenizeAssemblyFile();
    }

    // Method to tokenize each line from the file
    public static ArrayList<String> tokenizeLine(String line) {
        // Remove leading/trailing whitespace and split by space or special characters
        line = line.trim();
        // Split by spaces or special characters and return as an ArrayList
        String[] tokens = line.split("\\s+"); // Splitting by spaces or newlines
        return new ArrayList<>(Arrays.asList(tokens)); // Convert List to ArrayList
    }

    // Method to read and tokenize an entire assembly file
    public void tokenizeAssemblyFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(this.fileName));
        String line;
        StringBuilder src = new StringBuilder(); // To store the assembly code as a string
        ArrayList<String> tokens = new ArrayList<>();
        // Read each line in the file
        while ((line = reader.readLine()) != null) {
            // Trim and skip empty lines
            if (!line.trim().isEmpty()) {
                line = line + " ! "; // Append a special character to mark the end of the line
                tokens = tokenizeLine(line.trim()); // Tokenize the line
                // Add each token to the list of tokens
                for(int l=0; l<tokens.size(); l++){
                    src.append(tokens.get(l));
                    tokensList.add(tokens.get(l));
                }
            }
        }
        reader.close();
    }

    // Method to check if there are more tokens to be processed
    @Override
    public boolean hasNextToken() {
        return !tokensList.isEmpty();
    }

    // Method to verify that a next token exists
    @Override
    public void checkNextToken() {
        // If no tokens are available, throw an exception
        if (!hasNextToken()) throw new NoSuchElementException("No more tokens");
    }

    // Method to retrieve the first token without removing it from the list
    @Override
    public String peek() {
        checkNextToken(); // Ensure that there is a next token
        return tokensList.getFirst(); // Return the first token in the list
    }

    // Method to retrieve and remove the first token from the list
    @Override
    public String consume() throws SyntaxError {
        checkNextToken(); // Ensure that there is a next token
        return tokensList.removeFirst(); // Remove and return the first token from the list
    }

    // Method to print all tokens in the token list (for debug)
    public void printToken(){
        System.out.println(tokensList);
    }
}

    // Exception class to handle syntax errors
    class SyntaxError extends Exception {
        public SyntaxError(String message) {
            super(message); // Pass the error message to the Exception class
        }
    }