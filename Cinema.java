package cinema;

import java.util.Arrays;
import java.util.Scanner;

public class Cinema {
    private final static Scanner scanner = new Scanner(System.in);
    private final byte rowCinema;
    private final byte seatsCinema;
    private char[][] roomCinema;
    private final static char emptySeat = 'S';
    private final static char chosenSeat = 'B';
    private final static int TOTAL_SMALL_ROOM = 60;
    private final static int PRICE_SMALL_ROOM = 10;
    private final static int PRICE_FRONT_LARGER_ROOM = 10;
    private final static int PRICE_BACK_LARGER_ROOM = 8;
    private static boolean exitCinema = false;
    private int numberOfPurchasedTickets = 0;
    private int currentIncome = 0;
    private int totalIncome = 0;
    private int totalTickets = 0;

    public Cinema(byte rowCinema, byte seatsCinema) {
        this.rowCinema = rowCinema;
        this.seatsCinema = seatsCinema;
        this.roomCinema = createEmptyCinema(rowCinema, seatsCinema).clone();
        this.totalIncome = calculateTotalIncome(rowCinema, seatsCinema);
        this.totalTickets = rowCinema * seatsCinema;
    }

    public static void main(String[] args) {
        byte rows = inputRowsCinema();
        byte seats = inputSeatsCinema();
        Cinema cinema = new Cinema(rows, seats);

        while (!exitCinema) {
            printMenuCinema();
            byte action = scanner.nextByte();
            switch (action) {
                case 1 -> printSchemaCinema(cinema.roomCinema);
                case 2 -> buyTicket(cinema);
                case 3 -> statistics(cinema);
                case 0 -> exitCinema = true;
                default -> throw new RuntimeException("I don't know what to do!");
            }
        }
    }

    private static void statistics(Cinema cinema) {
        System.out.println("Number of purchased tickets: " + cinema.numberOfPurchasedTickets);
        double result = cinema.numberOfPurchasedTickets * 1.0 / cinema.totalTickets * 100;
        String str = String.format("Percentage: %.2f", result);
        System.out.println(str + "%");
        System.out.println("Current income: $" + cinema.currentIncome);
        System.out.println("Total income: $" + cinema.totalIncome);
    }

    private int calculateTotalIncome(byte rowCinema, byte seatsCinema) {
        if (rowCinema * seatsCinema < TOTAL_SMALL_ROOM) {
            return rowCinema * seatsCinema * PRICE_SMALL_ROOM;
        } else {
            int frontHalf = rowCinema / 2;
            int backHalf = rowCinema - frontHalf;
            return frontHalf * seatsCinema * PRICE_FRONT_LARGER_ROOM + backHalf * seatsCinema * PRICE_BACK_LARGER_ROOM;
        }
    }

    private static void buyTicket(Cinema cinema) {
        boolean exitChoose = false;
        while (!exitChoose) {
            byte chooseRows = chooseRowsCinema();
            byte chooseSeats = chooseSeatsCinema();
            if (chooseRows >= cinema.roomCinema.length || chooseRows <= 0) {
                System.out.println("Wrong input!");
                continue;
            } else if (chooseSeats >= cinema.roomCinema[chooseRows].length || chooseSeats <= 0) {
                System.out.println("Wrong input!");
                continue;
            } else if (cinema.roomCinema[chooseRows][chooseSeats] == chosenSeat) {
                System.out.println("That ticket has already been purchased");
                continue;
            }
            calculatePriceTicket(cinema.roomCinema, chooseRows, cinema);
            cinema.roomCinema = choosePlaceCinema(cinema, chooseRows, chooseSeats);
            cinema.numberOfPurchasedTickets++;
            exitChoose = true;
        }
    }

    private static void printMenuCinema() {
        System.out.println("\n1. Show the seats\n" + "2. Buy a ticket\n" + "3. Statistics\n" + "0. Exit");
    }

    private static byte inputRowsCinema() {
        System.out.println("Enter the number of rows:");
        byte rows = scanner.nextByte();
        return rows;
    }

    private static byte inputSeatsCinema() {
        System.out.println("Enter the number of seats in each row:");
        byte seats = scanner.nextByte();
        return seats;
    }

    private static byte chooseRowsCinema() {
        System.out.println("Enter a row number:");
        byte rows = scanner.nextByte();
        return rows;
    }

    private static byte chooseSeatsCinema() {
        System.out.println("Enter a seat number in that row:");
        byte seats = scanner.nextByte();
        return seats;
    }

    private static void printSchemaCinema(char[][] roomCinema) {
        System.out.println("Cinema:");
        for (int i = 0; i < roomCinema.length; i++) {
            for (int j = 0; j < roomCinema[i].length; j++) {
                System.out.print(roomCinema[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void calculatePriceTicket(char[][] roomCinema, byte chooseRows, Cinema cinema) {
        String suffixResult = "Ticket price: $";
        byte rows = (byte) roomCinema.length;
        byte seatsInRow = (byte) roomCinema[rows - 1].length;
        byte frontHalf = (byte) (roomCinema.length / 2 - 1);
        if (rows * seatsInRow < TOTAL_SMALL_ROOM) {
            cinema.currentIncome += PRICE_SMALL_ROOM;
            System.out.println(suffixResult + PRICE_SMALL_ROOM);
        } else if (chooseRows <= frontHalf) {
            cinema.currentIncome += PRICE_FRONT_LARGER_ROOM;
            System.out.println(suffixResult + PRICE_FRONT_LARGER_ROOM);
        } else {
            cinema.currentIncome += PRICE_BACK_LARGER_ROOM;
            System.out.println(suffixResult + PRICE_BACK_LARGER_ROOM);
        }
    }

    private static char[][] choosePlaceCinema(Cinema cinema, byte chooseRows, byte chooseSeats) {
        char[][] array = cinema.roomCinema.clone();
        array[chooseRows][chooseSeats] = chosenSeat;
        return array.clone();
    }

    private char[][] createEmptyCinema(byte rowCinema, byte seatsCinema) {
        char[][] array = new char[rowCinema + 1][seatsCinema + 1];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[0][0] = ' ';
                array[0][j] = Character.forDigit(j, 10);
                array[i][0] = Character.forDigit(i, 10);
                array[i][j] = emptySeat;
            }
        }
        return array.clone();
    }
}