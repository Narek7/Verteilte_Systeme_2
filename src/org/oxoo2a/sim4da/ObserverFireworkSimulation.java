package org.oxoo2a.sim4da;

import java.util.Random;
import java.util.Scanner;

/*
 * Diese Klasse ermöglicht die Simulation von Knoten in einem verteilten System.
 * Benutzer können wählen, ob sie Knoten mit einer globalen geteilten oder individuellen Wahrscheinlichkeit verwenden möchten.
 */

public class ObserverFireworkSimulation {
    public static void main(String[] args) {
        // Scanner für die Benutzereingabe initialisieren
        Scanner scanner = new Scanner(System.in);

        // Benutzer nach der Anzahl der Knoten fragen
        System.out.print("Bitte geben Sie die Anzahl der Knoten ein: ");
        int nodeCount = scanner.nextInt();

        // Benutzer fragen, welche Variante verwendet werden soll
        System.out.print("Verwenden Sie individuelle Wahrscheinlichkeit (1) oder globale Wahrscheinlichkeit (2)? ");
        int choice = scanner.nextInt();

        // Instanz des Simulators erhalten
        Simulator simulator = Simulator.getInstance();

        // Observer-Knoten erstellen
        ObserverNode observer = new ObserverNode("Observer", nodeCount);

        // Arrays zur Speicherung der Knoten und Knotennamen
        ObserverFireworkNode[] globalNodes = new ObserverFireworkNode[nodeCount];
        IndividualObserverFireworkNode[] individualNodes = new IndividualObserverFireworkNode[nodeCount];
        String[] nodeNames = new String[nodeCount];

        // Initialisieren der Knotennamen
        for (int i = 0; i < nodeCount; i++) {
            nodeNames[i] = "FireworkNode_" + i;
        }

        Random random = new Random();

        switch (choice) {
            case 1:
                System.out.println("Individuelle Wahrscheinlichkeit für jede Knoten");
                // Erstellen und Initialisieren der Knoten mit individueller Wahrscheinlichkeit
                for (int i = 0; i < nodeCount; i++) {
                    double initialProbability = 0.1 + (0.9 * random.nextDouble()); // Zufällige Wahrscheinlichkeit zwischen 0.1 und 1.0
                    individualNodes[i] = new IndividualObserverFireworkNode(nodeNames[i], nodeNames, observer, initialProbability);
                }
                break;

            case 2:
                System.out.println("Gemeinsam geteilte Wahrscheinlichkeit");
                // Erstellen und Initialisieren der Knoten mit globaler Wahrscheinlichkeit
                for (int i = 0; i < nodeCount; i++) {
                    globalNodes[i] = new ObserverFireworkNode(nodeNames[i], nodeNames, observer);
                }
                break;

            default:
                System.out.println("Ungültige Auswahl.");
                scanner.close();
                return;
        }

        // Starten der Simulation
        simulator.simulate();

        // Beenden der Simulation
        simulator.shutdown();

        // Scanner schließen
        scanner.close();
    }
}
