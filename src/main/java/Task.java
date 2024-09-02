import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }


    public static Task of(String s) throws DukeException {
        s = s.trim();
        if (s.isEmpty()) {
            throw new NoInputException();
        }

        String[] parts = s.split(" ", 2);
        String command = parts[0].toLowerCase();

        if (command.equals("todo")) {
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                throw new NoInputException();
            }
            return new Todo(parts[1].trim());

        } else if (command.equals("deadline")) {
            Pattern deadlinePattern = Pattern.compile("(.+) /by (.+)");
            Matcher deadlineMatcher = deadlinePattern.matcher(parts[1]);
            if (!deadlineMatcher.matches()) {
                throw new FormatException("deadline");
            }
            String description = deadlineMatcher.group(1).trim();
            String by = deadlineMatcher.group(2).trim();
            return new Deadline(description, by);

        } else if (command.equals("event")) {
            Pattern eventPattern = Pattern.compile("(.+) /from (.+) /to (.+)");
            Matcher eventMatcher = eventPattern.matcher(parts[1]);
            if (!eventMatcher.matches()) {
                throw new FormatException("event");
            }
            String description = eventMatcher.group(1).trim();
            String from = eventMatcher.group(2).trim();
            String to = eventMatcher.group(3).trim();
            return new Event(description, from, to);

        } else {
            throw new NoInputException();
        }
    }



    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void setIsDone(boolean val) {
        isDone = val;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), description);
    }

    public static class Todo extends Task {
        public Todo(String description) {
            super(description);
        }

        @Override
        public String toString() {
            return String.format("[T][%s] %s",
                    super.getStatusIcon(), super.description);
        }
    }

    public static class Deadline extends Task {
        String by;
        public Deadline(String description, String by) {
            super(description);
            this.by = by;
        }

        @Override
        public String toString() {
            String printedBy = "";
            try {
                printedBy = LocalDate.parse(by).format(DateTimeFormatter.ofPattern("MMM d yyyy"));
            } catch (DateTimeParseException e) {
                printedBy = by;
            }
            return String.format("[D][%s] %s (by: %s)",
                    super.getStatusIcon(), super.description, printedBy);
        }
    }

    public static class Event extends Task {
        private String from;
        private String to;
        public Event(String description, String from, String to) {
            super(description);
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            String printedFrom = "";
            String printedTo = "";
            try {
                printedFrom = LocalDate.parse(from)
                        .format(DateTimeFormatter.ofPattern("MMM d yyyy"));
            } catch (DateTimeParseException e) {
                printedFrom = LocalDate.parse(from).toString();
            }
            try {
                printedTo = LocalDate.parse(to)
                        .format(DateTimeFormatter.ofPattern("MMM d yyyy"));
            } catch (DateTimeParseException e) {
                printedTo = LocalDate.parse(to).toString();
            }
            return String.format("[T][%s] %s (from: %s to: %s)",
                    super.getStatusIcon(), super.description, printedFrom, printedTo);
        }
    }
}






