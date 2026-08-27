package thaisbot.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTest {
    @Test
    public void toString_notDone_showsTypeAndStatus() {
        Todo t = new Todo("read");
        assertEquals("[T][ ] read", t.toString());
    }

    @Test
    public void markAsDone_toFileString_showsDoneFlag() {
        Todo t = new Todo("read");
        t.markAsDone();
        assertEquals("T | 1 | read", t.toFileString());
    }
}
