import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Duke {
    private static List<Task> taskList = new ArrayList<>();

    public static String addHorizontalLinesAndIndentation(String dialog) {
        StringBuilder res = new StringBuilder("    ____________________________________________________________\n");
        Scanner sc = new Scanner(dialog);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            res.append("      ").append(line.trim()).append("\n");
        }
        res.append("    ____________________________________________________________");
        return res.toString();
    }

    public static void taskAppendToFile(String path, Task task) {
        File f = new File(path);
        try {
            FileWriter fw = new FileWriter(f, true);
            String timeDescription = task.getTimeDescription();
            fw.append(task.getTaskType()).append(" | ").append(task.isDone ? "1" : "0").append(" | ").append(task.description).append(timeDescription.isEmpty() ? "" : " | " + timeDescription).append("\n");
            fw.close();
        } catch (IOException e) {
            System.err.println("File write permission error.");
        }
    }


    public static void addToList(String dialog) {
        Task task = Task.of(dialog);
        taskList.add(task);
        taskAppendToFile("./data/duke.txt", task);
        String res = addHorizontalLinesAndIndentation("Got it. I've added this task: \n"
                + String.format("  %s", task) + "\n" + String.format("Now you have %d tasks in the list.", taskList.size()));
        System.out.println(res);
    }

    public static void displayList() {
        String lString = "";
        int index = 1;
        for (Task task : taskList) {
            lString += String.valueOf(index) + "."  + task.toString();
            if (index == taskList.size()) {
                break;
            }
            lString += "\n";
            index++;
        }
        lString = addHorizontalLinesAndIndentation(lString);
        System.out.println(lString);
    }

    public static void mark(int index) {
        taskList.get(index - 1).setIsDone(true);
        System.out.println(addHorizontalLinesAndIndentation(
                        "Nice! I've marked this task as done:\n" +
                                taskList.get(index - 1) + "\n"
                ));
    }

    public static void unmark(int index) {
        taskList.get(index - 1).setIsDone(false);
        System.out.println(
                addHorizontalLinesAndIndentation(
                        "Nice! I've marked this task as undone:\n" +
                                taskList.get(index - 1) + "\n"
                )
        );
    }

    public static void removeLineAt(int index, String path) {
        File inputFile = new File(path);
        File tempFile = new File(path.replace("duke.txt", "tmp.txt"));

        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            int lineIndexToRemove = index;
            String currentLine;

            while((currentLine = reader.readLine()) != null) {
                String trimmedLine = currentLine.trim();
                if(index++ == lineIndexToRemove) continue;
                writer.write(currentLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
            tempFile.renameTo(inputFile);
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public static void delete(int index) {
        Task task = taskList.remove(index - 1);
        removeLineAt(index - 1, "./data/duke.txt");
        System.out.println(
                addHorizontalLinesAndIndentation(
                        String.format("Noted. I've removed this task:\n"
                                + task
                                + " Now you have %d tasks in the list.", taskList.size())
                )
        );
    }

    public static void initializeTaskListFromFile(String path) {
        File f = new File(path);
        if (!f.exists()) {
            try {
                f.getParentFile().mkdirs(); // Ensure the directory exists
                f.createNewFile();
            } catch (IOException e) {
                System.out.println("Error! File writing access is not granted!");
            }
            return;
        }

        try {
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                taskList.add(Task.fromFile(sc.nextLine()));
            }
            sc.close();
        } catch (FileNotFoundException e) {
            System.err.println("Error! File is not found!");
        }
    }


    public static void main(String[] args) {
        String hi = "Hello! I'm Foo\n" +
                "What can I do for you?";
        System.out.println(addHorizontalLinesAndIndentation(hi));
        initializeTaskListFromFile("./data/duke.txt");
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                String line = sc.nextLine();
                if (line.equals("bye")) {
                    break;
                } else if (line.equals("list")) {
                    displayList();
                } else if (line.startsWith("mark")) {
                    int index = Integer.parseInt(line.substring(5));
                    mark(index);
                } else if (line.startsWith("unmark")) {
                    int index = Integer.parseInt(line.substring(7));
                    unmark(index);
                } else if (line.startsWith("delete")) {
                    int index = Integer.parseInt(line.substring(7));
                    delete(index);
                } else {
                    addToList(line);
                }
            } catch (DukeException e) {
                System.err.println(addHorizontalLinesAndIndentation("BRUH... " + e.getMessage()));
            } catch (Exception e) {
                System.err.println(addHorizontalLinesAndIndentation("An unexpected error occurred."));
            }
        }
        System.out.println(addHorizontalLinesAndIndentation("Bye. Hope to see you again soon!"));
    }
}
