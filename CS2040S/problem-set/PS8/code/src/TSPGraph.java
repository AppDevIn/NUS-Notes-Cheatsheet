import java.util.Comparator;
import java.util.PriorityQueue;

class Node {
    int id;
    double weight;

    public int getId() {
        return id;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    Node(int id, double weight) {
        this.id = id;
        this.weight = weight;
    }
}

class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node a, Node b) {
        return Double.compare(a.weight, b.weight);
    }
}

public class TSPGraph implements IApproximateTSP {
    TSPMap map = null;
    int[] parent = null;

    @Override
    public void MST(TSPMap map) {
        this.map = map;
        int n = map.getCount(); // number of points
        parent = new int[n];    // store MST parent links
        boolean[] visited = new boolean[n];

        // Create a priority queue (min heap) ordered by node weight
        PriorityQueue<Node> pq = new PriorityQueue<>(new NodeComparator());

        // Add all nodes to the queue with default weights
        Node[] nodes = new Node[n];
        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i, (i == 0) ? 0 : Double.POSITIVE_INFINITY); // start with node 0
            pq.add(nodes[i]);
        }

        while (!pq.isEmpty()) {
            Node curr = pq.poll();       // pick node with smallest weight
            int u = curr.getId();
            visited[u] = true;           // mark it visited

            for (Node neighbor : pq) {  // loop through all remaining nodes in PQ
                int v = neighbor.getId();
                double dist = map.pointDistance(u, v);
                if (!visited[v] && dist < neighbor.getWeight()) {
                    neighbor.setWeight(dist); // update the weight
                    parent[v] = u;            // set parent link

                    // Since Java’s PQ doesn’t auto-update weights, reinsert manually:
                    pq.remove(neighbor);
                    pq.add(neighbor);
                }
            }
        }

        // Link the nodes in the map to form MST
        for (int i = 1; i < n; i++) {
            map.setLink(i, parent[i], false);
        }

        map.setLink(0, 0, false); // just for completeness
        map.redraw();
    }

    @Override
    public void TSP(TSPMap map) {
        MST(map);
        // TODO: implement the rest of this method.
    }

    @Override
    public boolean isValidTour(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        return false;
    }

    @Override
    public double tourDistance(TSPMap map) {
        // Note: this function should with with *any* map, and not just results from TSP().
        // TODO: implement this method
        return 0;
    }

    public static void main(String[] args) {
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "../hundredpoints.txt");
        TSPGraph graph = new TSPGraph();

        graph.MST(map);
        // graph.TSP(map);
        // System.out.println(graph.isValidTour(map));
        // System.out.println(graph.tourDistance(map));
    }
}
