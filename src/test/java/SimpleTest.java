import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleTest {

    @Test
    public void canCastToInt() {
        final float x = 23.45f;
        final int k = (int) x;
        assertEquals(k, 23);
    }
}
