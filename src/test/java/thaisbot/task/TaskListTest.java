package thaisbot.task;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class TaskListTest {
    @Test
    public void add_get_remove_size_behaviour() {
        TaskList list = new TaskList();
        Todo t1 = new Todo("one");
        Todo t2 = new Todo("two");
        list.add(t1);
        list.add(t2);
        assertEquals(2, list.size());
        assertSame(t1, list.get(0));
        Task removed = list.remove(0);
        assertSame(t1, removed);
        assertEquals(1, list.size());
    }

    @Test
    public void iterator_returns_all_tasks() {
        TaskList list = new TaskList();
        list.add(new Todo("a"));
        list.add(new Todo("b"));
        List<String> descs = new ArrayList<>();
        for (Task t : list) {
            descs.add(t.toString());
        }
        assertEquals(2, descs.size());
        assertTrue(descs.get(0).contains("a"));
        assertTrue(descs.get(1).contains("b"));
    }
}
