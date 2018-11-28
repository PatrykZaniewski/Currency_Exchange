import javafx.util.Pair;
import java.util.ArrayList;
import java.util.LinkedList;

public class Graph {
    private ArrayList<Pair<String, String>> nameOfCurrency = new ArrayList<>();
    private ArrayList<LinkedList<Pair<Integer, ChangeCost>>> listOfNeighbor;
    private int numberOfVertexes;
    private int numberOfEdges = 0;

    public Graph(ArrayList<String> listOfCurrency) {
        numberOfVertexes = listOfCurrency.size();
        listOfNeighbor = new ArrayList<>();
        for (int i = 0; i < numberOfVertexes; i++) {
            listOfNeighbor.add(new LinkedList<>());
            String splittedCurrency[] = listOfCurrency.get(i).split("[, ][ ]");
            if (splittedCurrency.length == 4) {
                splittedCurrency[2] = splittedCurrency[2] + " " + splittedCurrency[3];
            }
            nameOfCurrency.add(new Pair<>(splittedCurrency[2], splittedCurrency[1]));
        }
    }

    String getCurrencyShortName(int id) {
        return nameOfCurrency.get(id).getValue();
    }

    ArrayList<LinkedList<Pair<Integer, ChangeCost>>> getListOfNeighbor() {
        return listOfNeighbor;
    }

    int getNumberOfVertexes() {
        return numberOfVertexes;
    }

    int getNumberOfEdges() {
        return numberOfEdges;
    }

    void addEdge(int src, int dst, double multipler, double cost, boolean isPercent) {
        ChangeCost costs = new ChangeCost(multipler, cost, isPercent);
        listOfNeighbor.get(src).add(new Pair<>(dst, costs));
        numberOfEdges++;
    }

    int getCurrencyID(String shortName) {
        int i = 0;
        for (Pair currencyPair : nameOfCurrency) {
            if (currencyPair.getValue().equals(shortName)) {
                return i;
            }
            i++;
        }
        return -1;
    }
}
