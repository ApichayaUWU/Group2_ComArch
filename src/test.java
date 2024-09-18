import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;

public class test {

    // Test tokenizeLine method
    @Test
    public void testTokenizeLine() {
        String line = "lw 0 1 five";
        String[] tokens = AssemblyTokenizerV2.tokenizeLine(line);
        assertArrayEquals(new String[]{"lw", "0", "1", "five"}, tokens, "Tokenization should split the line correctly.");
    }

    // Test tokenizeAssemblyFile method (Assuming there's a valid assembly file)
    @Test
    public void testTokenizeAssemblyFile() throws Exception {
        List<String[]> tokensList = AssemblyTokenizerV2.tokenizeAssemblyFile("src\\example.txt");
        assertNotNull(tokensList, "Tokens list should not be null.");
        assertTrue(tokensList.size() > 0, "Tokens list should contain lines.");
    }

    // Test hasNextToken method
    @Test
    public void testHasNextToken() throws SyntaxError {
        AssemblyTokenizerV2 tokenizer = new AssemblyTokenizerV2("lw 0 1 five");
        assertTrue(tokenizer.hasNextToken(), "There should be more tokens.");
    }

    // Test peek method
    @Test
    public void testPeek() throws SyntaxError {
        AssemblyTokenizerV2 tokenizer = new AssemblyTokenizerV2("lw 0 1 five");
        assertEquals("lw", tokenizer.peek(), "Peek should return the first token.");
    }

    // Test consume method
    @Test
    public void testConsume() throws SyntaxError {
        AssemblyTokenizerV2 tokenizer = new AssemblyTokenizerV2("lw 0 1 five");
        assertEquals("lw", tokenizer.consume(), "Consume should return and remove the first token.");
        assertEquals("0", tokenizer.consume(), "Consume should return and remove the next token.");
    }

    // Test consume with expected token
    @Test
    public void testConsumeExpectedToken() throws SyntaxError {
        AssemblyTokenizerV2 tokenizer = new AssemblyTokenizerV2("lw 0 1 five");
        tokenizer.consume("lw");  // This should pass
        assertThrows(SyntaxError.class, () -> tokenizer.consume("add"), "Should throw SyntaxError when the expected token doesn't match.");
    }

    // Test empty token list
    @Test
    public void testEmptyTokenList() throws SyntaxError {
        AssemblyTokenizerV2 tokenizer = new AssemblyTokenizerV2("    ");  // empty input
        assertFalse(tokenizer.hasNextToken(), "Tokenizer should not have any tokens for an empty input.");
    }

    // Test createList method with valid input
    @Test
    public void testCreateList() throws SyntaxError {
        AssemblyTokenizerV2 tokenizer = new AssemblyTokenizerV2("add 1 2 3");
        assertTrue(tokenizer.hasNextToken(), "Tokenizer should have tokens.");
        assertEquals("add", tokenizer.consume(), "First token should be 'add'.");
    }
}
