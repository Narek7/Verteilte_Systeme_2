package org.oxoo2a.sim4da;

import java.util.Scanner;

public class FireworkSimulation {
    public static void main(String[] args) {
        // Scanner für die Benutzereingabe initialisieren
        Scanner scanner = new Scanner(System.in);

        // Benutzer nach der Anzahl der Knoten fragen
        System.out.print("Bitte geben Sie die Anzahl der Knoten ein: ");
        int nodeCount = scanner.nextInt();

        // Initiale Wahrscheinlichkeit für das Senden weiterer Nachrichten
        final double initialProbability = 0.5;

        // Instanz des Simulators erhalten
        Simulator simulator = Simulator.getInstance();

        // Array zur Speicherung der Knoten
        FireworkNode[] nodes = new FireworkNode[nodeCount];

        // Array zur Speicherung der Knotennamen
        String[] nodeNames = new String[nodeCount];

        // Initialisieren der Knotennamen
        for (int i = 0; i < nodeCount; i++) {
            nodeNames[i] = "FireworkNode_" + i;
        }

        // Erstellen und Initialisieren der Knoten
        for (int i = 0; i < nodeCount; i++) {
            nodes[i] = new FireworkNode(nodeNames[i], nodeNames, initialProbability);
        }

        // Starten der Simulation für 10 Sekunden
        simulator.simulate(10);

        // Beenden der Simulation
        simulator.shutdown();

        // Scanner schließen
        scanner.close();
    }
}
