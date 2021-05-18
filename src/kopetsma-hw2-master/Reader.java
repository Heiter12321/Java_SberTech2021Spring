package com.company;

import java.util.Scanner;

public class Reader {
    /**
     * Считывание первоначальной расстановки шашек
     * @param figures
     * @param whiteAndBlackFigureCounts
     * @param in
     */
    public void ReadStartPositions(Figure[] figures, int[] whiteAndBlackFigureCounts, Scanner in) {
        String whiteCheckers;
        whiteCheckers = in.nextLine();
        char[] whCheckers = whiteCheckers.toCharArray();
        int spaceCount = 0;

        for (int i = 0; i < whiteCheckers.length(); ++i) {
            if (whCheckers[i] == ' ') {
                ++spaceCount;
            }
        }

        int whiteFiguresCount = spaceCount + 1;
        int i = 0;
        int stage = 1;
        StringBuilder checker = new StringBuilder();
        char column = ' ';
        int str = 0;
        while (i < whiteCheckers.length()) {
            if (stage == 1) {
                column = whCheckers[i];
                ++stage;
            } else {
                if (stage == 2) {
                    str = Character.getNumericValue(whCheckers[i]);
                    ++stage;
                    ++i;
                } else {
                    while (i < whiteCheckers.length() && whCheckers[i] != ' ') {
                        checker.append(whCheckers[i]);
                        ++i;
                    }
                    stage = 1;
                    figures[whiteFiguresCount - spaceCount - 1] = new Figure(column, str, new String(checker));
                    --spaceCount;
                    checker = new StringBuilder();
                }
            }

            ++i;
        }

        String blackCheckers;
        blackCheckers = in.nextLine();
        char[] blCheckers = blackCheckers.toCharArray();
        spaceCount = 0;
        for (i = 0; i < blackCheckers.length(); ++i) {
            if (blCheckers[i] == ' ') {
                ++spaceCount;
            }
        }
        int blackFiguresCount = spaceCount + 1;
        checker = new StringBuilder();
        column = ' ';
        str = 0;
        i = 0;
        stage = 1;
        while (i < blackCheckers.length()) {
            if (stage == 1) {
                column = blCheckers[i];
                ++stage;
            } else {
                if (stage == 2) {
                    str = Character.getNumericValue(blCheckers[i]);
                    ++stage;
                    ++i;
                } else {
                    while (i < blackCheckers.length() && blCheckers[i] != ' ') {
                        checker.append(blCheckers[i]);
                        ++i;
                    }
                    stage = 1;
                    figures[blackFiguresCount + whiteFiguresCount - spaceCount - 1] = new Figure(column, str, new String(checker));
                    --spaceCount;
                    checker = new StringBuilder();
                }
            }

            ++i;
        }

        whiteAndBlackFigureCounts[0] = whiteFiguresCount;
        whiteAndBlackFigureCounts[1] = blackFiguresCount;
    }

    /**
     * Считывание и парсинг строки с ходами белой и черной шашки
     * @param in
     * @param Motions
     */
    public void ReadNextMotion(Scanner in, String[] Motions) {
        String str = in.nextLine();
        char[] strChar = str.toCharArray();

        char[] blMotion = new char[str.length()];
        char[] whMotion = new char[str.length()];

        boolean flag = true;
        int k = 0;
        for (int i = 0; i < str.length(); ++i) {
            if (strChar[i] == ' ') {
                flag = false;
                k = 0;
                continue;
            }
            if (flag) {
                blMotion[k] = strChar[i];
            } else {
                whMotion[k] = strChar[i];
            }
            ++k;
        }

        Motions[0] = new String(blMotion);
        Motions[1] = new String(whMotion);
    }
}
