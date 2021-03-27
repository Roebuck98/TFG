package src;

import java.util.*;

public class Graph {
	private Set<Node> nodes = new HashSet<>();

	public void addNode(Node nodeA) {
		nodes.add(nodeA);
	}

	public Set<Node> getNodes() {
		return nodes;
	}

	public void setNodes(Set<Node> nodes) {
		this.nodes = nodes;
	}

	public void show() {
		Iterator<Node> i = nodes.iterator();
		while (i.hasNext()) {
			Node n = i.next();
			System.out.print(n.getName() + "-> ");

			Iterator<Node> t = n.adjacentNodes.keySet().iterator();
			
			while (t.hasNext()) {
				Node n2 = t.next();
				System.out.print(n2.getName() + " - " + n2.getDistance() + " ; ");

			}
		}

	}
}
