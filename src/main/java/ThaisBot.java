import java.util.Scanner;

public class ThaisBot {
    public static void main(String[] args) {
        System.out.println("Hello! I'm Thai's Bot.");
        System.out.println("What can I do for you today?");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String userInput = scanner.nextLine();
            System.out.println(userInput);

            if (userInput.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;
            }
        }
    }
}
