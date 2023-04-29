package src;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: ARXML file path is required as an argument.");
            System.exit(1);
        }

        System.out.println(args[0]);
    }
}