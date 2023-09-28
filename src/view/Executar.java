package view;

import controller.ThreadCarro;

import java.util.concurrent.Semaphore;

public class Executar {
    public static void main(String[] args) {
        int vagasNoEstacionamento = 3;
        Semaphore semaforo = new Semaphore(vagasNoEstacionamento);

        ThreadCarro[] carros = new ThreadCarro[10];
        for (int i = 0; i < 10; i++) {
            carros[i] = new ThreadCarro(i, semaforo);
            carros[i].start();
        }
    }
}
