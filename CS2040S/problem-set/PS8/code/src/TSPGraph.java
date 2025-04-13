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
        int vertax = map.getCount();
        parent = new int[vertax];
        boolean[] visited = new boolean[vertax];

        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(new NodeComparator());

        Node[] nodes = new Node[vertax];
        for (int i = 0; i < vertax; i++) {
            nodes[i] = new Node(i, (i == 0) ? 0 : Double.POSITIVE_INFINITY);
            priorityQueue.add(nodes[i]);
        }

        PriorityQueue<Node> dupPriorityQueue = new PriorityQueue<>(new NodeComparator());

        while (!priorityQueue.isEmpty()) {
            Node curr = priorityQueue.poll();
            int u = curr.getId();
            visited[u] = true;

            for (Node neighbor : priorityQueue) {
                int v = neighbor.getId();
                double dist = map.pointDistance(u, v);
                if (!visited[v] && dist < neighbor.getWeight()) {
                    neighbor.setWeight(dist);
                    parent[v] = u;



                }
                dupPriorityQueue.add(neighbor);
            }

            priorityQueue.clear();
            int containerSize = dupPriorityQueue.size();
            for (int i = 0; i < containerSize; i++) {
                priorityQueue.add(dupPriorityQueue.poll());
            }
        }

        for (int i = 1; i < vertax; i++) {
            map.setLink(i, parent[i], false);
        }

        map.setLink(0, 0, false);
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
        TSPMap map = new TSPMap(args.length > 0 ? args[0] : "fiftypoints.txt");
        TSPGraph graph = new TSPGraph();

        graph.MST(map);
        // graph.TSP(map);
        // System.out.println(graph.isValidTour(map));
        // System.out.println(graph.tourDistance(map));
    }
}
