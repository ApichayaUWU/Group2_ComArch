public interface Token {
    boolean hasNextToken();
    String peek();
    String consume() throws Exception;
}
