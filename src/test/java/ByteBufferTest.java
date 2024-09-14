//javac --source 1.8 --target 1.8 ByteBufferTest.java

import java.nio.ByteBuffer;

public class ByteBufferTest {
    public static void main(String... args) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[] { 1, 2, 3 });
        buffer.position(1);
        System.out.println(buffer.get());
    }
}