import java.util.Hashtable;
import java.util.LinkedList;

public class DjikstrasAlgorithm<E> {

    private class Vertex {
        public E data;
        public LinkedList<Edge> edgesLeaving;

        public Vertex(E data) {
            this.data = data;
            this.edgesLeaving = new LinkedList<>();
        }
    }

    private class Edge {
        public Vertex target;
        public int weight;

        public Edge(Vertex target, int weight) {
            this.target = target;
            this.weight = weight;
        }
    }

    public Hashtable<E, Vertex> vertices;

    public DjikstrasAlgorithm() {
        vertices = new Hashtable<>();
    }

    public boolean insertVertex(E data) {
        if(data == null) {
            throw new NullPointerException("your data value is null");
        }

        if(vertices.containsKey(data)) {
            return false; // no duplicate values
        }

        vertices.put(data, new Vertex(data));
        return true;
    }

    public boolean removeVertex(E data) {

    }

    public boolean insertEdge(E start, E end, int weight) {

    }

    public boolean removeEdge(E start, E end) {

    }

    public boolean containsVertex(E data) {

    }

    public boolean containsEdge(E data) {

    }
}