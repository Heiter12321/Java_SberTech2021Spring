package com.company;

public class Printer {
    /**
     * Метод Print принимает на вход массив фигур и выводит их позиции.
     * @param figures
     */
    public void Print(Figure[] figures) {
        for (Figure fig : figures) {
            if (fig.color == 0 && (int)fig.checker.charAt(0) != 0) {
                System.out.print(fig.column);
                System.out.print(fig.str + "_");
                for (int i = 0; i < fig.checker.length(); ++i) {
                    if (fig.checker.charAt(i) == '\0') {
                        break;
                    }
                    System.out.print(fig.checker.charAt(i));
                }
                System.out.print(" ");
            }
        }
        System.out.println();
        for (Figure fig : figures) {
            if (fig.color == 1 && (int)fig.checker.charAt(0) != 0) {
                System.out.print(fig.column);
                System.out.print(fig.str + "_");
                for (int i = 0; i < fig.checker.length(); ++i) {
                    if (fig.checker.charAt(i) == '\0') {
                        break;
                    }
                    System.out.print(fig.checker.charAt(i));
                }
                System.out.print(" ");                }
        }
    }
}
