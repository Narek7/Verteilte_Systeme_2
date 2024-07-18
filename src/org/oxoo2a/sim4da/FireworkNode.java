package org.oxoo2a.sim4da;

import java.util.Random;

public class FireworkNode extends Node {
    private final String[] otherNodes; // Liste der Namen der anderen Knoten im Netzwerk
    private double probability; // Wahrscheinlichkeit für das Senden weiterer Nachrichten
    private final Random random; // Zufallszahlengenerator für verschiedene Zufallsentscheidungen
    private static final int MAX_ITERATIONS = 3; // Maximale Anzahl von Iterationen, um die Simulation zu beenden

    // Konstruktor zur Initialisierung des Knotens
    public FireworkNode(String name, String[] otherNodes, double probability) {
        super(name); // Aufruf des Konstruktors der Basisklasse Node
        this.otherNodes = otherNodes; // Initialisieren der anderen Knoten
        this.probability = probability; // Setzen der initialen Wahrscheinlichkeit
        this.random = new Random(); // Initialisieren des Zufallszahlengenerators
    }

    // Hauptmethode, die das Verhalten des Knotens steuert
    @Override
    protected void engage() {
        // Initiale Verzögerung vor dem Senden der ersten Nachricht
        sleep(random.nextInt(1000));
        int iterations = 0; // Zähler für die Anzahl der Iterationen

        // Schleife zur Ausführung der Knotenoperationen, begrenzt durch MAX_ITERATIONS
        while (iterations < MAX_ITERATIONS) {
            // Senden der "Firework"-Nachricht an eine zufällige Teilmenge der anderen Knoten
            sendFireworkMessage();

            // Übergang in den passiven Zustand und Warten auf eingehende Nachrichten
            System.out.println(NodeName() + " ist jetzt passiv. Aktuelle Wahrscheinlichkeit: " + probability);
            while (true) {
                Message m = receive(); // Warten auf eine eingehende Nachricht
                if (m != null && m.query("type").equals("Firework")) {
                    // Behandeln der empfangenen "Firework"-Nachricht
                    handleFireworkMessage(m);
                    break; // Schleife verlassen, um eine neue Iteration zu starten
                }
            }
            iterations++; // Erhöhen des Iterationszählers
        }
        System.out.println(NodeName() + " hat seine Operationen beendet. Letzte Wahrscheinlichkeit: " + probability);
    }

    // Methode zum Senden einer "Firework"-Nachricht
    private void sendFireworkMessage() {
        Message m = new Message().add("type", "Firework").add("sender", NodeName()); // Erstellen der Nachricht
        for (String node : otherNodes) {
            if (!node.equals(NodeName()) && random.nextBoolean()) { // Sicherstellen, dass der Knoten keine Nachricht an sich selbst sendet
                sendBlindly(m, node); // Senden der Nachricht
            }
        }
        System.out.println(NodeName() + " hat eine Firework-Nachricht gesendet.");
    }

    // Methode zur Behandlung einer empfangenen "Firework"-Nachricht
    private void handleFireworkMessage(Message m) {
        System.out.println(NodeName() + " hat eine Firework-Nachricht von " + m.queryHeader("sender") + " erhalten.");
        System.out.println(NodeName() + " ist jetzt aktiv. Aktuelle Wahrscheinlichkeit: " + probability);
        if (random.nextDouble() < probability) { // Entscheidung basierend auf der Wahrscheinlichkeit
            sendFireworkMessage(); // Senden einer weiteren Nachricht
        }
        // Verringern der Wahrscheinlichkeit für zukünftige Nachrichten
        probability /= 2;
    }
}
