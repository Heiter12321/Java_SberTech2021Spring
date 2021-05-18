package com.company;

/**
 * Класс "Фигура" с полями соответствующими номеру строки, колонке, "башни", цвета шашки и соответствие
 * обычной шашке или дамке
 */
public class Figure {
    public char column;
    public int str;
    public String checker;
    public int color;
    public Boolean isQueen;

    /**
     * Пустой конструктор класса
     */
    public Figure() {
    }

    /**
     * Конструктор фигуры
     * @param column
     * @param str
     * @param checker
     */
    public Figure(char column, int str, String checker) {
        this.column = column;
        this.str = str;
        this.checker = checker;
        IsFigureQueen();
    }

    /**
     * Проверка на то, является ли фигура дамкой по верхней шашке в башне
     */
    public void IsFigureQueen() {
        if (this.checker.charAt(0) == 'b' || this.checker.charAt(0) == 'B') {
            this.color = 1;
            this.isQueen = this.checker.charAt(0) == 'B';
        } else {
            this.color = 0;
            this.isQueen = this.checker.charAt(0) == 'W';
        }
    }

    /**
     * Проверка на то, является ли позиция, в которой находится шашка, необходимой для становления дамкой
     */
    public void IsQueenPosition() {
        if (this.color == 0 && this.str == 8) {
            this.isQueen = true;
        } else {
            if (this.color == 1 && this.str == 1) {
                this.isQueen = true;
            }
        }
    }

    /**
     * Проверяет все ли фигуры, которые являются дамками, имеют верхнюю шашку в виде дамки
     * @param figures
     * @return
     */
    public Figure[] IAmQueen(Figure[] figures) {
        for (Figure fig : figures) {
            if (fig.isQueen && fig.color == 0) {
                char[] newChecker = new char[fig.checker.length()];
                newChecker[0] = 'W';
                System.arraycopy(fig.checker.toCharArray(), 1, newChecker, 1, fig.checker.length() - 1);
                fig.checker = new String(newChecker);
            } else {
                if (fig.isQueen && fig.color == 1) {
                    char[] newChecker = new char[fig.checker.length()];
                    newChecker[0] = 'B';
                    System.arraycopy(fig.checker.toCharArray(), 1, newChecker, 1, fig.checker.length() - 1);
                    fig.checker = new String(newChecker);
                }
            }
        }
        return figures;
    }

    /**
     * Проверка на то, что указанная клетка является белой
     * @param str
     * @throws WhiteCellException
     */
    public void IsCellWhite(String str) throws WhiteCellException {
        if (((int)str.charAt(0) + Character.digit(str.charAt(1), 10)) % 2 == 1) {
            throw new WhiteCellException("white cell");
        }
    }

    /**
     * Проверка корректности хода для разных типов шашек
     * @param isStepUsual
     * @param str
     * @throws Error
     * @throws WhiteCellException
     */
    public void IsCellCorrect(boolean isStepUsual, String str) throws Error, WhiteCellException{
        IsCellWhite(str);

        if (isStepUsual) {
            if (!(Math.abs((int) this.column - (int) str.charAt(0)) == 1 &&
                    1 == Math.abs(this.str - Character.digit(str.charAt(1), 10)))) {
                throw new Error("error");
            }
        } else {
            if (!this.isQueen) {
                if (!(Math.abs((int) this.column - (int) str.charAt(0)) == 2 &&
                        2 == Math.abs(this.str - Character.digit(str.charAt(1), 10)))) {
                    throw new Error("error");
                }
            } else {
                if (!(Math.abs((int) this.column - (int) str.charAt(0)) ==
                        Math.abs(this.str - Character.digit(str.charAt(1), 10)))) {
                    throw new Error("error");
                }
            }
        }
    }

    /**
     * Выполнения одного хода шашки
     * @param str
     * @param figures
     * @return
     * @throws BusyCellException
     * @throws Error
     * @throws InvalidMoveException
     * @throws WhiteCellException
     */
    public Figure[] OneStepByChecker(String str, Figure[] figures) throws BusyCellException, Error, InvalidMoveException, WhiteCellException{
        if (str.indexOf('-') != -1) {
            if (this.CanIDoNextStep(figures, this.color)) {
                throw new InvalidMoveException("invalid move");
            }

            char[] temp = new char[str.indexOf('-') - 3];
            System.arraycopy(str.toCharArray(), 3, temp, 0, str.indexOf('-') - 3);

            for (int i = 0; i < this.checker.length(); ++i) {
                if (this.checker.charAt(i) != temp[i]) {
                    throw new Error("error");
                }
            }

            String nextPosition = String.copyValueOf(str.toCharArray(), str.indexOf('-') + 1,
                    str.length() - str.indexOf('-') - 1);

            this.IsCellCorrect(true, nextPosition);

            for (Figure figure : figures) {
                if (nextPosition.charAt(0) == figure.column &&
                        Character.digit(nextPosition.charAt(1), 10) == figure.str) {
                    throw new BusyCellException("busy cell");
                }
            }

            for (int i = 0; i < this.checker.length(); ++i) {
                if (this.checker.charAt(i) != nextPosition.charAt(i + 3)) {
                    throw new Error("error");
                }
            }

            if (this.isQueen) {
                char startPositionColumn;
                char endPositionColumn;
                if (nextPosition.charAt(0) > this.column) {
                    startPositionColumn = (char) (this.column + 1);
                    endPositionColumn = (char) (nextPosition.charAt(0) - 1);
                } else {
                    endPositionColumn = (char) (this.column + 1);
                    startPositionColumn = (char) (nextPosition.charAt(0) - 1);
                }

                int startPositionStr;
                int endPositionStr;
                if (Character.digit(nextPosition.charAt(1), 10) > this.str) {
                    startPositionStr = this.str + 1;
                    endPositionStr = Character.digit(nextPosition.charAt(1), 10) - 1;
                } else {
                    endPositionStr = this.str + 1;
                    startPositionStr = Character.digit(nextPosition.charAt(1), 10) - 1;
                }

                while (startPositionColumn <= endPositionColumn && startPositionStr <= endPositionStr) {
                    for (Figure fig : figures) {
                        if (fig.column == startPositionColumn &&
                                fig.str == startPositionStr) {
                            throw new Error("error");
                        }
                    }
                    startPositionColumn = (char) (startPositionColumn + 1);
                    ++startPositionStr;
                }
            }

            this.column = nextPosition.charAt(0);
            this.str = Character.digit(nextPosition.charAt(1), 10);
            IsFigureQueen();
        } else {
            String[] steps = new String[12];
            int k = 0;
            while (str.indexOf(':') != -1) {
                char[] tmp = new char[str.indexOf(':')];
                System.arraycopy(str.toCharArray(), 0, tmp, 0, str.indexOf(':'));
                steps[k] = new String(tmp);

                char[] temp = new char[str.length() - str.indexOf(':') - 1];
                System.arraycopy(str.toCharArray(), str.indexOf(':') + 1, temp, 0, temp.length);
                str = new String(temp);
                ++k;
            }
            steps[k] = str;
            ++k;

            figures = Eating(steps, figures, k, this.color);
        }

        return figures;
    }

    /**
     * Поедание шашек противника
     * @param steps
     * @param figures
     * @param k
     * @param color
     * @return
     * @throws WhiteCellException
     * @throws BusyCellException
     * @throws Error
     * @throws InvalidMoveException
     */
    public Figure[] Eating(String[] steps, Figure[] figures, int k, int color) throws WhiteCellException, BusyCellException, Error, InvalidMoveException {
        int currentStep = 0;
        while (currentStep < k - 1) {
            for (Figure figure : figures) {
                if (figure.column == steps[currentStep].charAt(0) &&
                        figure.str == Character.digit(steps[currentStep].charAt(1), 10) &&
                        figure.color == color) {
                    for (int i = 0; i < steps[currentStep].length() - 3; ++i) {
                        char c = steps[currentStep].charAt(i + 3);

                        for (Figure fig : figures) {
                            if (fig.column == steps[currentStep + 1].charAt(0) &&
                            fig.str == Character.digit(steps[currentStep + 1].charAt(1), 10)) {
                                throw new BusyCellException("busy cell");
                            }
                        }

                        if (figure.checker.charAt(i) != c) {
                            throw new Error("error");
                        }
                    }
                    figure.IsCellCorrect(false, steps[currentStep + 1]);

                    Figure fig2 = new Figure();
                    boolean flag = false;

                    if (!figure.isQueen) {

                        char ch = (char) ((steps[currentStep].charAt(0) + steps[currentStep + 1].charAt(0)) / 2);
                        int index = (Character.digit(steps[currentStep].charAt(1), 10) +
                                Character.digit(steps[currentStep + 1].charAt(1), 10)) / 2;

                        for (Figure fig : figures) {
                            if (fig.column == ch &&
                                    fig.str == index) {
                                if (fig.color == color) {
                                    throw new Error("error");
                                }
                                fig2 = fig;
                                flag = true;
                            }
                        }

                        if (!flag) {
                            throw new Error("error");
                        }

                    } else {
                        char startPositionColumn;
                        char endPositionColumn;
                        if (steps[currentStep + 1].charAt(0) > steps[currentStep].charAt(0)) {
                            startPositionColumn = (char) (steps[currentStep].charAt(0) + 1);
                            endPositionColumn = (char) (steps[currentStep + 1].charAt(0) - 1);
                        } else {
                            endPositionColumn = (char) (steps[currentStep].charAt(0) - 1);
                            startPositionColumn = (char) (steps[currentStep + 1].charAt(0) + 1);
                        }

                        int startPositionStr;
                        int endPositionStr;
                        if (Character.digit(steps[currentStep + 1].charAt(1), 10) >
                                Character.digit(steps[currentStep].charAt(1), 10)) {
                            startPositionStr = Character.digit(steps[currentStep].charAt(1), 10) + 1;
                            endPositionStr = Character.digit(steps[currentStep + 1].charAt(1), 10) - 1;
                        } else {
                            endPositionStr = Character.digit(steps[currentStep].charAt(1), 10) - 1;
                            startPositionStr = Character.digit(steps[currentStep + 1].charAt(1), 10) + 1;
                        }

                        while (startPositionColumn <= endPositionColumn && startPositionStr <= endPositionStr) {
                            for (Figure fig : figures) {
                                if (fig.column == startPositionColumn &&
                                        fig.str == startPositionStr) {
                                    if (fig.color == figure.color) {
                                        throw new Error("error");
                                    }
                                    fig2 = fig;
                                    if (!flag) {
                                        flag = true;
                                    } else {
                                        throw new Error("error");
                                    }
                                }
                            }
                            startPositionColumn = (char) (startPositionColumn + 1);
                            ++startPositionStr;
                        }

                        if (!flag) {
                            throw new Error("error");
                        }
                    }
                    figure.LevelUp(fig2.isQueen);
                    fig2.LevelDown();

                    if ((int)fig2.checker.charAt(0) == 0) {
                        Figure[] newFigures = new Figure[figures.length - 1];
                        int t = 0;
                        for (Figure fig : figures) {
                            if ((int)fig.checker.charAt(0) != 0) {
                                newFigures[t] = fig;
                                ++t;
                            }
                        }
                        figures = new Figure[newFigures.length];
                        System.arraycopy(newFigures, 0, figures, 0, figures.length);
                    }

                    figure.column = steps[currentStep + 1].charAt(0);
                    figure.str = Character.digit(steps[currentStep + 1].charAt(1), 10);
                    figure.IsQueenPosition();

                    break;
                }
            }

            ++currentStep;
        }
        if (currentStep == k - 2) {
            if (this.CanIDoNextStep(figures, color)) {
                throw new InvalidMoveException("invalid move");
            }
        }

        return IAmQueen(figures);
    }

    /**
     * Эта шашка съела шашку противника и ставит ее в низ башни
     * @param isPreviousCheckerQueen
     */
    public void LevelUp(boolean isPreviousCheckerQueen) {
        char[] newChecker = new char[this.checker.length() + 1];
        System.arraycopy(this.checker.toCharArray(), 0, newChecker, 0, newChecker.length - 1);
        int ind = this.checker.length();

        if (isPreviousCheckerQueen) {
            if (this.color == 0) {
                newChecker[ind] = 'B';
            } else {
                newChecker[ind] = 'W';
            }
        } else {
            if (this.color == 0) {
                newChecker[ind] = 'b';
            } else {
                newChecker[ind] = 'w';
            }
        }

        this.checker = new String(newChecker);
    }

    /**
     * Эту шашку съели, и она теряет свою верхнюю шашку в башне
     */
    public void LevelDown() {
        if (this.checker.length() != 1) {
            char[] newChecker = new char[this.checker.length() - 1];
            System.arraycopy(this.checker.toCharArray(), 1, newChecker, 0, newChecker.length);
            this.checker = new String(newChecker);
            if (newChecker[0] == 'w' || newChecker[0] == 'W') {
                this.color = 0;
            } else {
                this.color = 1;
            }
        } else {
            this.checker = "\0";
        }
    }

    /**
     * Проверка на то, что шашки определенного цвета могут съесть шашку противника
     * @param figures
     * @param color
     * @return
     */
    public boolean CanIDoNextStep(Figure[] figures, int color) {
        for (Figure figure : figures) {
            if (figure.color == color) {
                boolean isQueen = figure.isQueen;
                if (isQueen) {
                    for (Figure fig : figures) {
                        int k = fig.str - figure.str;
                        int j = fig.column - figure.column;
                        int i = Math.abs(k) > Math.abs(k + 1) ? k - 1 : k + 1;
                        int i1 = Math.abs(j) > Math.abs(j + 1) ? j - 1 : j + 1;

                        if (Math.abs(k) == Math.abs(j) && fig.color != figure.color &&
                                figure.str + i >= 1 && figure.str + i <= 8 &&
                                figure.column + i1 >= 'a' && figure.column + i1 <= 'h' &&
                                Math.abs(k) > 0) {
                            boolean flag = false;
                            for (Figure fig2 : figures) {

                                if (fig2.str == figure.str + i && fig2.column == figure.column + i1) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                return true;
                            }
                        }
                    }
                } else {
                    for (Figure fig : figures) {
                        int k = fig.str - figure.str;
                        int j = fig.column - figure.column;

                        if (Math.abs(k) == 1 && Math.abs(j) == 1 && fig.color != figure.color &&
                                figure.str + k * 2 >= 1 && figure.str + k * 2 <= 8 &&
                                figure.column + j * 2 >= 'a' && figure.column + j * 2 <= 'h') {
                            boolean flag = false;
                            for (Figure fig2 : figures) {
                                if (fig2.str == figure.str + k * 2 && fig2.column == figure.column + j * 2) {
                                    flag = true;
                                    break;
                                }
                            }
                            if (!flag) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Переопределения метода, чтобы можно было применить сортировку массива из "Фигур"
     * @return
     */
    public String toString() {
        return "[column = " + this.column + " str = " + this.str + "]";
    }
}





/*
a1_w c1_w e1_w g1_w
b8_b d8_b f8_b h8_b
a1_w-b2_w b8_b-a7_b
c1_w-d2_w d8_b-c7_b
e1_w-f2_w f8_b-e7_b
g1_w-h2_w h8_b-g7_b


a7_wbb b2_ww c1_w e1_w f2_w g1_w
b4_bwww b8_b c3_b c7_b e5_bww e7_b f8_b g5_b g7_b h8_b
b2_ww:d4_wwb:f6_wwbb:d8_wwbbb:b6_wwbbbb b4_bwww-a3_bwww

a1_w c1_w e1_w f2_ww g5_wbb h2_w
a3_b a5_bww b8_b c5_bwww d8_b e3_b e7_b f8_b g7_b h8_b
f2_ww:d4_wwb:b6_wwbb a5_bww:c7_bwww
b6_wbb-a7_wbb c7_bwww-b6_bwww
a1_w-b2_w b6_bwww:d4_bwwww

a1_w c1_w c3_w e1_w f2_ww g3_ww g5_wbbb
b4_b b6_bw b8_b d8_b e5_bww e7_b f8_b g7_b h8_b
c3_w:a5_wb:c7_wbb b8_b:d6_bw
a1_w-a2_w b8_b-a7_b

*/

