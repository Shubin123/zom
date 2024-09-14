package com.shub;

import java.io.IOException;

public class Main {
    private static long window;

    public Main() {
    }

    public static void main(String[] args) throws Exception {
        System.out.print("app start\n");
        Game g = new Game(window);
        g.start();
    }
}