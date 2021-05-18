package com.company;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    /**
     * Основной и единственный метод класса Main, который принимает расстановку шашек на шахматной доске, а также
     * их поочередные ходы. Далее выводится конечная расстановка шашек после введенных ходов или сообщение об ошибке
     * @param args
     */
    public static void main(String[] args) {
        try (Scanner in = new Scanner(System.in)) {
            Figure[] figures = new Figure[24];

            int[] whiteAndBlackFigureCount = new int[2];

            Reader reader = new Reader();
            reader.ReadStartPositions(figures, whiteAndBlackFigureCount, in);

            int whiteFiguresCount = whiteAndBlackFigureCount[0];
            int blackFiguresCount = whiteAndBlackFigureCount[1];

            Figure[] temp = new Figure[whiteFiguresCount + blackFiguresCount];
            System.arraycopy(figures, 0, temp, 0, whiteFiguresCount + blackFiguresCount);
            figures = temp;

            while (in.hasNextLine()) {
                String[] Motions = new String[2];
                reader.ReadNextMotion(in, Motions);

                for (int i = 0; i < figures.length; ++i) {
                    if (figures[i].column == Motions[0].charAt(0) &&
                        figures[i].str == Character.digit(Motions[0].charAt(1), 10) &&
                        figures[i].color == 0) {
                        try {
                            figures = figures[i].OneStepByChecker(Motions[0], figures);
                        } catch (InvalidMoveException | Error | BusyCellException | WhiteCellException err) {
                            System.out.println(err.getMessage());
                            System.exit(0);
                        }
                        break;
                    }
                }

                for (int i = 0; i < figures.length; ++i) {
                    if (figures[i].column == Motions[1].charAt(0) &&
                        figures[i].str == Character.digit(Motions[1].charAt(1), 10) &&
                        figures[i].color == 1) {
                        try {
                            figures = figures[i].OneStepByChecker(Motions[1], figures);
                        } catch (InvalidMoveException | Error | BusyCellException | WhiteCellException err) {
                            System.out.println(err.getMessage());
                            System.exit(0);
                        }
                        break;
                    }
                }
            }

            Arrays.sort(figures, Comparator.comparing(Figure::toString));

            Printer printer = new Printer();
            printer.Print(figures);
        }
    }
}
