import java.util.Scanner;

public class HotelRoomBooking {
    public static void main(String[] args) {
        final int FLOORS = 3;
        final int ROOMS_PER_FLOOR = 5;

        boolean[][] rooms = new boolean[FLOORS][ROOMS_PER_FLOOR]; 
        // false = available, true = booked

        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("Welcome to the Hotel Room Booking System!");

        do {
            System.out.println("\n===== Menu =====");
            System.out.println("1. View Room Status");
            System.out.println("2. Book a Room");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = getValidInt(scanner);

            switch (choice) {
                case 1:
                    displayRooms(rooms);
                    break;

                case 2:
                    bookRoom(rooms, scanner);
                    break;

                case 3:
                    System.out.println("Thank you for using the system. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please select 1, 2, or 3.");
            }

        } while (choice != 3);

        scanner.close();
    }

    // Function to display room status
    public static void displayRooms(boolean[][] rooms) {
        System.out.println("\nRoom Status (F = Free, B = Booked):");
        for (int i = 0; i < rooms.length; i++) {
            System.out.print("Floor " + (i + 1) + ": ");
            for (int j = 0; j < rooms[i].length; j++) {
                System.out.print(rooms[i][j] ? "B " : "F ");
            }
            System.out.println();
        }
    }

    // Function to book a room
    public static void bookRoom(boolean[][] rooms, Scanner scanner) {
        System.out.print("Enter floor number (1-" + rooms.length + "): ");
        int floor = getValidInt(scanner) - 1;

        System.out.print("Enter room number (1-" + rooms[0].length + "): ");
        int room = getValidInt(scanner) - 1;

        if (floor >= 0 && floor < rooms.length && room >= 0 && room < rooms[0].length) {
            if (!rooms[floor][room]) {
                rooms[floor][room] = true;
                System.out.println("Room booked successfully!");
            } else {
                System.out.println("Sorry, this room is already booked.");
            }
        } else {
            System.out.println("Invalid floor or room number.");
        }
    }

    // Function to safely get integer input
    public static int getValidInt(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid input. Please enter a number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
