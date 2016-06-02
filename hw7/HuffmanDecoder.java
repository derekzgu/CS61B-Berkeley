/**
 * Created by chizhang on 6/1/16.
 */
public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader or = new ObjectReader(args[0]);
        // Read the Huffman coding trie.
        BinaryTrie trie = (BinaryTrie) or.readObject();
        // If applicable, read the number of symbols.
        int numberOfSymbols = (int) or.readObject();
        // Read the massive bit sequence corresponding to the original txt.
        BitSequence sequences = (BitSequence) or.readObject();

        char[] outputs = new char[numberOfSymbols];
        for (int i = 0; i < numberOfSymbols; i++) {
            Match m = trie.longestPrefixMatch(sequences);
            outputs[i] = m.getSymbol();
            sequences = sequences.allButFirstNBits(m.getSequence().length());
        }
        FileUtils.writeCharArray(args[1], outputs);
    }
}
