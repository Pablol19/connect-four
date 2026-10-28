package connectfour.command;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CommandHistory<C extends Command> {
    private final Deque<C> executed = new ArrayDeque<>();

    public void push(C command) {
        executed.push(command);
    }

    public C pop() {
        if (executed.isEmpty()) {
            throw new IllegalStateException("There is nothing to undo");
        }
        return executed.pop();
    }

    public boolean isEmpty() {
        return executed.isEmpty();
    }

    public List<C> inOrder() {
        List<C> ordered = new ArrayList<>(executed);
        java.util.Collections.reverse(ordered);
        return ordered;
    }
}
