import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    public String addHorizontalLinesAndIndentation(String dialog) {
            StringBuilder res = new StringBuilder("    ____________________________________________________________\n");
            Scanner sc = new Scanner(dialog);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                res.append("      ").append(line.trim()).append("\n");  // Ensure consistent indentation
            }
            res.append("    ____________________________________________________________");
            return res.toString();
        }

        private Command parseUserInput(String dialog) throws FormatException, NoInputException {
            if (dialog.isEmpty()) {
                throw new NoInputException();
            }
            if (dialog.equals("bye")) {
                return new ExitCommand();
            } else if (dialog.equals("list")) {
                return new ListCommand();
            } else if (dialog.startsWith("mark")) {
                try {
                    int index = Integer.parseInt(dialog.substring(5));
                    return new MarkCommand(index);
                } catch (NumberFormatException e) {
                    throw new FormatException("mark");
                }
            } else if (dialog.startsWith("unmark")) {
                try {
                    int index = Integer.parseInt(dialog.substring(7));
                    return new UnmarkCommand(index);
                } catch (NumberFormatException e) {
                    throw new FormatException("unmark");
                }
            } else if (dialog.startsWith("delete")) {
                try {
                    int index = Integer.parseInt(dialog.substring(7));
                    return new DeleteCommand(index);
                } catch (NumberFormatException e) {
                    throw new FormatException("delete");
                }
            } else {
                throw new NoInputException();
            }
        }

        public Command getNextCommand(Scanner sc) {
            try {
                String input = sc.nextLine();
                return parseUserInput(input);
            } catch (FormatException e) {
                System.out.println("FormatException: " + e.getMessage());
                return null;
            } catch (NoInputException e) {
                System.out.println("NoInputException: " + e.getMessage());
                return null;
            } catch (Exception e) {
                System.out.println("Exception: " + e.getMessage());
                return null;
            }
        }
    public static void main(String[] args) {
        
    }
}
