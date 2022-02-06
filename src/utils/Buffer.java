package utils;

public class Buffer {
    private final int maxBuffer;
    private int buffer;

    public Buffer(int maxBuffer) {
        this.maxBuffer = maxBuffer;
        buffer = maxBuffer/2;
    }

    public boolean toLittleSpace() {
        return buffer + 1 > maxBuffer;
    }

    public boolean toLittleResources() {
        return buffer - 1 < 0;
    }

    public void increment() {
        if (toLittleSpace()) {
            throw new IllegalStateException("Too much elements in buffer:" + (buffer + 1));
        }
        else {
            buffer = buffer + 1;
        }
    }

    public void decrement() {
        if (toLittleResources()) {
            throw new IllegalStateException("Not enough elements in buffer:" + (buffer - 1));
        }
        else {
            buffer = buffer - 1;
        }
    }

    @Override
    public String toString() {
        return Integer.toString(buffer);
    }
}
