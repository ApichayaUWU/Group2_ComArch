import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

interface Tokenizer {
    boolean hasNextToken();
    void checkNextToken();
    String peek();
    String consume() throws SyntaxError;
}

public class AssemblyTokenizerV2 implements Tokenizer {
    String[] tokens;
    LinkedList<String> list = new LinkedList<>();

    // Method to tokenize each line from the file
    public static String[] tokenizeLine(String line) {
        // Remove leading/trailing whitespace and split by space or special characters
        return line.trim().split("\\s+");

    }

    // Method to read and tokenize an entire assembly file
    public static List<String[]> tokenizeAssemblyFile(String filename) throws IOException {
        List<String[]> tokensList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;

        // Read each line in the file
        while ((line = reader.readLine()) != null) {
            // Trim and skip empty lines
            if (!line.trim().isEmpty()) {
                String[] tokens = tokenizeLine(line.trim());
                if (tokens.length > 0) {
                    tokensList.add(tokens);
                }
            }
        }
        reader.close();
        return tokensList;
    }

    // Constructor to tokenize a string source
    public AssemblyTokenizerV2(String src) throws SyntaxError {
        tokens = src.split("(?=[!\\s])|(?<=[!\\s])");
        createList();
    }

    @Override
    public boolean hasNextToken() {
        return !list.isEmpty();
    }

    @Override
    public void checkNextToken() {
        if (!hasNextToken()) throw new NoSuchElementException("No more tokens");
    }

    @Override
    public String peek() {
        checkNextToken();
        return list.peekFirst();
    }

    public boolean peek(String s) {
        if (!hasNextToken()) return false;
        return peek().equals(s);
    }

    @Override
    public String consume() throws SyntaxError {
        checkNextToken();
        return list.removeFirst();
    }

    public void consume(String s) throws SyntaxError {
        if (peek(s)) consume();
        else throw new SyntaxError(s + " expected");
    }

    // Helper method to convert tokens into the LinkedList
    private void createList() throws SyntaxError {
        int tokenIndex = 0; // Track the token index to handle comment after 4 tokens


        for (String token : tokens) {
            token = token.trim(); // Remove leading/trailing whitespace
            if (token.isEmpty()) continue; // Skip empty tokens
                // Add numeric values (e.g., immediate values, addresses)
                if (token.matches("\\d+|-\\d+")) {
                    list.addLast(token);
                }
                // Add labels or instructions (starting with a letter and allowing alphanumeric characters)
                else if (token.matches("[A-Za-z][A-Za-z0-9]*")) {
                    list.addLast(token);
                }
                // Handle invalid tokens by throwing an error
                else {
                    System.out.println("Invalid token: " + token);
                    throw new SyntaxError("Invalid token");
                }
                tokenIndex++; // Increment the token index

        }

    }

    private boolean isCharacter(String s) {
        return s.length() == 1 && Character.isLetter(s.charAt(0));
    }
}

// Exception class to handle syntax errors
class SyntaxError extends Exception {
    public SyntaxError(String message) {
        super(message);
    }
}