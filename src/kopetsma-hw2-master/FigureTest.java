package com.company;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FigureTest {
    private Figure figure = new Figure();
    @Test
    void isFigureQueen() {
        figure = new Figure('a', 5, "WbbbW");
        assertEquals(true, figure.isQueen);
    }

    @Test
    void isQueenPosition() {
        figure = new Figure('a', 1, "bwbwbwb");
        figure.IsQueenPosition();
        assertEquals(true, figure.isQueen);
    }

    @Test
    void IAmQueen() {
        Figure[] figures = new Figure[8];
        char column = 'a';
        for (int i = 1; i < 8; i += 2, column += 2) {
            figures[i / 2] = new Figure(column, 1, "bbbb");
            figures[i / 2].isQueen = true;
        }
        column = 'b';
        for (int i = 2; i <= 8; i += 2, column += 2) {
            figures[i / 2 + 3] = new Figure(column, 8, "wwww");
            figures[i / 2 + 3].isQueen = true;
        }

        figures[0].IAmQueen(figures);

        for (int i = 0; i < 4; ++i) {
            assertEquals('B', figures[i].checker.charAt(0));
        }
        for (int i = 4; i < 8; ++i) {
            assertEquals('W', figures[i].checker.charAt(0));

        }
    }

    @Test
    void isCellWhite() {
        for (int i = 2; i < 8; i += 2) {
            try {
                figure.IsCellWhite(new String("a" + i));
            } catch (WhiteCellException err) {
                assertEquals(err.getMessage(), "white cell");
            }
        }
    }

    @Test
    void levelUp() {
        figure = new Figure('a', 1, "wWwWwBb");
        figure.LevelUp(true);
        assertEquals(figure.checker, "wWwWwBbB");
    }

    @Test
    void levelDown() {
        figure = new Figure('a', 1, "wbBb");
        figure.LevelDown();
        assertEquals(figure.checker, "bBb");
        assertEquals(figure.color, 1);
    }
}