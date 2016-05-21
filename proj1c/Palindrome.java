/**
 * Created by chizhang on 5/20/16.
 */
public class Palindrome {
    public static Deque<Character> wordToDeque(String word) {
        LinkedListDequeSolution<Character> characterDeque = new LinkedListDequeSolution<>();
        for (int i = 0; i < word.length(); i++) {
            characterDeque.addFirst(word.charAt(i));
        }
        return (Deque) characterDeque;
    }

    private static boolean isPalindrome(Deque<Character> characterDeque) {
        int characterDequeSize = characterDeque.size();
        if (characterDequeSize == 0 || characterDequeSize == 1) {
            return true;
        } else {
            Character firstItem = characterDeque.removeFirst();
            Character lastItem = characterDeque.removeLast();
            return firstItem == lastItem && isPalindrome(characterDeque);
        }
    }

    public static boolean isPalindrome(String word) {
        // convert the string to deque
        Deque<Character> characterDeque = wordToDeque(word);
        return isPalindrome(characterDeque);
    }
}
