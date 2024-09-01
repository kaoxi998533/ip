import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public abstract String getTaskType();

    public abstract String getTimeDescription();

    public static Task fromFile(String s) throws DukeException {
        s = s.trim();
        if (s.isEmpty()) {
            throw new NoInputException();
        }

        String[] parts = s.split(" | ", 4);
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
        public String getTaskType() {
            return "T";
        }

        @Override
        public String getTimeDescription(){
            return "";
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
        public String getTimeDescription(){
            return "by " + by;
        }

        @Override
        public String getTaskType() {
            return "D";
        }

        @Override
        public String toString() {
            return String.format("[D][%s] %s (by: %s)",
                    super.getStatusIcon(), super.description, by);
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
        public String getTimeDescription(){
            return "from " + from + " to " + to;
        }

        @Override
        public String getTaskType() {
            return "E";
        }

        @Override
        public String toString() {
            return String.format("[T][%s] %s (from: %s to: %s)",
                    super.getStatusIcon(), super.description, from, to);
        }
    }
}






