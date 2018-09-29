package net.zelinf.crypto_homework;

import net.zelinf.crypto_homework.classical.ex01.Virginia;
import net.zelinf.crypto_homework.classical.ex02.AffineHill;
import net.zelinf.crypto_homework.finitefield.FiniteFieldDemo;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.*;

public class AppMain {

    public static void main(String[] args) {
        addExercises();
        sortExercises();

        printMenu();

        try (Scanner scanner = CmdUtils.scannerFromStdin()) {
            while (scanner.hasNextLine()) {
                String input = scanner.nextLine();
                String[] parts = input.split("\\.");
                boolean isInputValid = false;
                try {
                    if (parts.length == 2) {
                        int x = Integer.parseUnsignedInt(parts[0]);
                        int y = Integer.parseUnsignedInt(parts[1]);
                        ImmutablePair<Integer, Integer> inputId = ImmutablePair.of(x, y);
                        for (Exercise exercise : registeredExercises) {
                            if (exercise.getId().equals(inputId)) {
                                isInputValid = true;
                                printHorizontalLine();
                                exercise.run();
                                printHorizontalLine();
                                printMenu();
                            }
                        }
                    }
                } catch (NumberFormatException ignored) {
                }
                if (!isInputValid) {
                    System.out.println("Input is not valid.");
                }
            }
        }

        System.out.println();
        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("Please select an exercise: (input ID in x.y format)");
        for (Exercise exercise : registeredExercises) {
            System.out.printf("    %d.%d (%s)",
                    exercise.getId().getLeft(),
                    exercise.getId().getRight(),
                    exercise.getClass().getSimpleName());
            System.out.println();
        }

    }

    private static void sortExercises() {
        if (registeredExercises != null)
            registeredExercises.sort(Comparator.comparing(Exercise::getId));
    }

    private static void printHorizontalLine() {
        for (int i = 0; i < 40; ++i) {
            System.out.print('-');
        }
        System.out.println();
    }

    private static List<Exercise> registeredExercises = new ArrayList<>();

    private static void addExercises() {
        ServiceLoader<Exercise> serviceLoader = ServiceLoader.load(Exercise.class);
        for (Exercise exercise : serviceLoader) {
            registeredExercises.add(exercise);
        }
    }
}
