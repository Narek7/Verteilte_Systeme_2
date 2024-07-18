package org.oxoo2a.sim4da;

import java.util.HashMap;
import java.util.Map;

public class ObserverNode extends Node {
    private final int totalNodes; // Gesamtanzahl der Knoten
    private final Map<String, String> nodeStates; // Zustände der Knoten

    // Konstruktor zur Initialisierung des Observer-Knotens
    public ObserverNode(String name, int totalNodes) {
        super(name);
        this.totalNodes = totalNodes;
        this.nodeStates = new HashMap<>();
    }

    // Methode zur Aktualisierung des Zustands eines Knotens
    public synchronized void updateState(String nodeName, String state) {
        nodeStates.put(nodeName, state);
        checkTermination();
    }

    // Methode zur Überprüfung, ob alle Knoten passiv sind und keine Nachrichten mehr gesendet werden
    private synchronized void checkTermination() {
        boolean allPassive = nodeStates.values().stream().allMatch(state -> state.equals("passive"));

        if (allPassive && nodeStates.size() == totalNodes) {
            System.out.println("Alle Knoten sind passiv und haben ihre Operationen beendet. Die Simulation wird beendet.");
            System.exit(0); // Beenden der Simulation
        }
    }
}
