import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chizhang on 6/1/16.
 */
public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char c : inputSymbols) {
            if (!frequencyTable.containsKey(c)) {
                frequencyTable.put(c, 0);
            } else {
                frequencyTable.put(c, frequencyTable.get(c) + 1);
            }
        }
        return frequencyTable;
    }

    public static void main(String[] args) {
        char[] inputs = FileUtils.readFile(args[0]);
        // Build frequency table.
        Map<Character, Integer> frequencyTable = buildFrequencyTable(inputs);
        // Use frequency table to construct a binary decoding trie.
        BinaryTrie trie = new BinaryTrie(frequencyTable);
        // Write the binary decoding trie to the .huf file.
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(trie);
        // (optional: write the number of symbols to the .huf file)
        ow.writeObject(inputs.length);
        // Use binary trie to create lookup table for encoding.
        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();
        // Create a list of bitsequences.
        List<BitSequence> bits = new ArrayList<>();

        for (char c : inputs) {
            BitSequence b = lookupTable.get(c);
            bits.add(b);
        }
        ow.writeObject(BitSequence.assemble(bits));
    }
}
