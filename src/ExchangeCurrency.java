import javafx.util.Pair;

import java.util.LinkedList;
import java.util.Stack;

public class ExchangeCurrency {

    private Graph graph;
    private double dist[];
    private int prevVertex[];

    public ExchangeCurrency(Graph graph) {
        this.graph = graph;
    }

    double exchange(int src, double amount, int dst) {
        int numberOfVertexes = graph.getNumberOfVertexes();
        dist = new double[numberOfVertexes];
        prevVertex = new int[numberOfVertexes];

        for (int i = 0; i < numberOfVertexes; i++) {
            prevVertex[i] = i;
            dist[i] = Double.MAX_VALUE;
        }
        dist[src] = (double) 1 / amount;

        for (int i = 1; i < numberOfVertexes; i++) {
            int currentVertex = 0;
            for (LinkedList<Pair<Integer, ChangeCost>> list : graph.getListOfNeighbor()) {
                for (Pair<Integer, ChangeCost> pair : list) {
                    if (currentVertex != dst) {
                        int v = pair.getKey();
                        double multipler = 1 / pair.getValue().getMultipler();
                        double cost;
                        if (pair.getValue().getIsPercent()) {
                            cost = 1 / (dist[currentVertex] * multipler) * pair.getValue().getCost() / 100;
                        } else {
                            cost = pair.getValue().getCost();
                        }
                        double afterExchange = 1 / (1 / (dist[currentVertex] * multipler) - cost);
                        if (dist[v] > afterExchange && afterExchange > 0) {
                            dist[v] = afterExchange;
                            prevVertex[v] = currentVertex;
                        }
                    }
                }
                currentVertex++;
            }
        }

        Stack<Integer> stackOfCurrency = new Stack<>();
        stackOfCurrency.push(dst);
        int u = dst;

        while (prevVertex[u] != u) {
            u = prevVertex[u];
            int posOfDuplicate;
            if (stackOfCurrency.search(u) > 0) {
                posOfDuplicate = stackOfCurrency.search(u);
                stackOfCurrency.add(u);
                for (int i = stackOfCurrency.size() - 1; i >= stackOfCurrency.size() - posOfDuplicate - 1; i--) {
                    System.out.print(graph.getCurrencyShortName(stackOfCurrency.get(i)));
                    if (i > stackOfCurrency.size() - posOfDuplicate - 1) {
                        System.out.print(" -> ");
                    }
                }
                System.out.println();
                return -1;
            }
            stackOfCurrency.push(u);
        }
        if (stackOfCurrency.peek() != src) {
            return 0;
        }
        double result = amount;
        while (stackOfCurrency.size() > 1) {
            u = stackOfCurrency.pop();
            System.out.print(graph.getCurrencyShortName(u) + " -> ");
            int v = stackOfCurrency.peek();
            LinkedList<Pair<Integer, ChangeCost>> lista = graph.getListOfNeighbor().get(u);
            for (Pair<Integer, ChangeCost> pair : lista) {
                if (pair.getKey() == v) {
                    ChangeCost change = pair.getValue();
                    double multipler = change.getMultipler();
                    double cost = change.getCost();
                    if (change.getIsPercent()) {
                        result = result * multipler;
                        result = result - (result * cost / 100);
                    } else {
                        result = result * multipler - cost;
                    }
                }
            }
        }
        System.out.println(graph.getCurrencyShortName(dst));
        return result;
    }
}
