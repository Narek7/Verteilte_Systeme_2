package org.oxoo2a.sim4da;

import java.util.Random;

public class ObserverFireworkNode extends Node {
    private final String[] otherNodes; // Liste der Namen der anderen Knoten im Netzwerk
    private static double globalProbability = 0.5; // Gemeinsame Wahrscheinlichkeit für das Senden weiterer Nachrichten
    private final Random random; // Zufallszahlengenerator für verschiedene Zufallsentscheidungen
    private final ObserverNode observer; // Observer-Knoten zum Überwachen der Zustände

    // Konstruktor zur Initialisierung des Knotens
    public ObserverFireworkNode(String name, String[] otherNodes, ObserverNode observer) {
        super(name); // Aufruf des Konstruktors der Basisklasse Node
        this.otherNodes = otherNodes; // Initialisieren der anderen Knoten
        this.random = new Random(); // Initialisieren des Zufallszahlengenerators
        this.observer = observer; // Initialisieren des Observer-Knotens
    }

    // Hauptmethode, die das Verhalten des Knotens steuert
    @Override
    protected void engage() {

        // Initiale Verzögerung vor dem Senden der ersten Nachricht
        sleep(random.nextInt(1000));

        while (true) {
            observer.updateState(NodeName(), "active");
            // Senden der "Firework"-Nachricht an eine zufällige Teilmenge der anderen Knoten
            sendFireworkMessage();

            // Übergang in den passiven Zustand und Warten auf eingehende Nachrichten
            synchronized (ObserverFireworkNode.class) {
                System.out.println(NodeName() + " ist jetzt passiv. Aktuelle Wahrscheinlichkeit: " + globalProbability);
            }
            observer.updateState(NodeName(), "passive");
            while (true) {
                Message m = receive(); // Warten auf eine eingehende Nachricht
                if (m != null && m.query("type").equals("Firework")) {
                    // Behandeln der empfangenen "Firework"-Nachricht
                    observer.updateState(NodeName(), "active");
                    handleFireworkMessage(m);
                    break; // Schleife verlassen, um eine neue Iteration zu starten
                }
            }
        }
    }

    // Methode zum Senden einer "Firework"-Nachricht
    private void sendFireworkMessage() {
        Message m = new Message().add("type", "Firework").add("sender", NodeName()); // Erstellen der Nachricht
        for (String node : otherNodes) {
            if (!node.equals(NodeName()) && random.nextBoolean()) { // Sicherstellen, dass der Knoten keine Nachricht an sich selbst sendet
                sendBlindly(m, node); // Senden der Nachricht
                System.out.println(NodeName() + " hat eine Firework-Nachricht an " + node + " gesendet.");
            }
        }
    }

    // Methode zur Behandlung einer empfangenen "Firework"-Nachricht
    private void handleFireworkMessage(Message m) {
        String sender = m.queryHeader("sender");
        System.out.println(NodeName() + " hat eine Firework-Nachricht von " + sender + " erhalten.");
        synchronized (ObserverFireworkNode.class) {
            System.out.println(NodeName() + " ist jetzt aktiv. Aktuelle Wahrscheinlichkeit: " + globalProbability);
            if (random.nextDouble() < globalProbability) { // Entscheidung basierend auf der globalen Wahrscheinlichkeit
                sendFireworkMessage(); // Senden einer weiteren Nachricht
            }
            // Verringern der globalen Wahrscheinlichkeit für zukünftige Nachrichten
            globalProbability /= 2;
            System.out.println(NodeName() + " neue globale Wahrscheinlichkeit nach Reduktion: " + globalProbability);
        }
    }
}
