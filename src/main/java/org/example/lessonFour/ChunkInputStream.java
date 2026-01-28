package org.example.lessonFour;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

class ChunkInputStream extends InputStream {
    private final byte[] data;
    private final int chunkSize;
    private int pos = 0;

    public ChunkInputStream(String input, int chunkSize) {
        this.data = input.getBytes(StandardCharsets.UTF_8);
        this.chunkSize = chunkSize;
    }


    @Override
    public int read(byte[] buffer, int offset, int length) {
        if (pos >= data.length) return -1;

        int bytesToRead = Math.min(chunkSize, data.length - pos);
        System.arraycopy(data, pos, buffer, offset, bytesToRead);
        pos += bytesToRead;
        return bytesToRead;
    }

    @Override
    public int read() {
        if (pos >= data.length) return -1;
        return data[pos++];
    }
}
