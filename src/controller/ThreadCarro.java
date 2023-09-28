package controller;

import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadCarro extends Thread {
    private int id;
    private Semaphore semaforo;
    private static AtomicInteger ordemChegada = new AtomicInteger(1);
    private static AtomicInteger ordemSaida = new AtomicInteger(1);

    public ThreadCarro(int id, Semaphore semaforo) {
        this.id = id;
        this.semaforo = semaforo;
    }

    @Override
    public void run() {
        indoAoEstacionamento();

        //Início seção crítica
        try {
            semaforo.acquire();
            chegouAoEstacionamento();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            semaforo.release(); //release() no finally para evitar Deadlock
            //Fim seção crítica

            saiuDoEstacionamento();
        }
    }

    public int gerarNumeroAleatorio(int minimo, int maximo) {
        return ThreadLocalRandom.current().nextInt(minimo, maximo);
    }

    private void indoAoEstacionamento() {
        final int DESLOCAMENTO = 100;
        final int TEMPO = 1000;
        int distanciaAteEstacionamento = gerarNumeroAleatorio(1000, 2001);

        int distanciaPercorrida = 0;
        while (distanciaPercorrida < distanciaAteEstacionamento) {
            distanciaPercorrida += DESLOCAMENTO;

            try {
                sleep(TEMPO);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mostrarProgresso(distanciaPercorrida);
        }

        mostrarResultado();
    }

    public void mostrarProgresso(int distanciaPercorrida) {
        System.out.println("#" + id + " >>> percorreu " + distanciaPercorrida + "m");
    }

    public void mostrarResultado() {
        System.out.println("#" + id + " >>>>> chegou ao estacionamento em " + ordemChegada.getAndIncrement());
    }

    private void chegouAoEstacionamento() {
        int tempoEstacionado = gerarNumeroAleatorio(1000, 3001);
        System.out.println("#" + id + " >>> ficara estacionado por " + tempoEstacionado + "ms");

        try {
            sleep(tempoEstacionado);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void saiuDoEstacionamento() {
        System.out.println("#" + id + " >>>>>  saiu do estacionamento em " + ordemSaida.getAndIncrement());
    }
}
