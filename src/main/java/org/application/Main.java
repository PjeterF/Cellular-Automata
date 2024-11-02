package org.application;

public class Main {
    public static void main(String[] args) {
        try {
            Application application = new Application(1000, 1000, "Cellular-Automata");
            application.initialize();
            application.run();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}