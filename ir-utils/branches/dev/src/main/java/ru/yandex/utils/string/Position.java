package ru.yandex.utils.string;

public class Position implements Comparable<Position> {

	private int start;
    private int end;

    public Position(int start, int end) {
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }


    public String toString() {
        return "Position{" +
        	   "start=" + start +
               ", end=" + end +
               '}';
    }


    public int compareTo(Position o) {
        return start - o.start;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (end != position.end) return false;
        if (start != position.start) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = start;
        result = 31 * result + end;
        return result;
    }

    public int getLength() {
        return end - start;
    }
}
