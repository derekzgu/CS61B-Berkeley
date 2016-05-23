package synthesizer;
import org.junit.Test;
import org.omg.PortableInterceptor.Interceptor;

import static org.junit.Assert.*;

/** Tests the ArrayRingBuffer class.
 *  @author Josh Hug
 */

public class TestArrayRingBuffer {
    @Test
    public void someTest() {
        ArrayRingBuffer<Integer> arb = new ArrayRingBuffer(10);
        // arb.dequeue();
        for (int i = 0; i < 10; i++) {
            arb.enqueue(i);
        }
        assertEquals(0, (int)arb.dequeue());
        assertEquals(1, (int)arb.peek());
        arb.enqueue(11);
        assertEquals(1, (int)arb.dequeue());
        assertTrue(!arb.isFull());
    }

    /** Calls tests for ArrayRingBuffer. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestArrayRingBuffer.class);
    }
} 
