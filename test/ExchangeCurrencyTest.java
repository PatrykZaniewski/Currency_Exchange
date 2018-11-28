import org.junit.Assert;
import org.junit.Test;

public class ExchangeCurrencyTest {

    @Test
    public void exchangeTest3CurrPath() {
        Graph graph;
        DataRead read = new DataRead("test/way1.txt");
        graph = read.readFile();
        ExchangeCurrency exchange = new ExchangeCurrency(graph);
        double result;

        result = exchange.exchange(graph.getCurrencyID("AUD"), 100, graph.getCurrencyID("CHF"));

        Assert.assertEquals(152.3, result, 0.01);
    }

    @Test
    public void exchangeTest2CurrPath() {
        Graph graph;
        DataRead read = new DataRead("test/way1.txt");
        graph = read.readFile();
        ExchangeCurrency exchange = new ExchangeCurrency(graph);
        double result;

        result = exchange.exchange(graph.getCurrencyID("EUR"), 1000, graph.getCurrencyID("AUD"));

        Assert.assertEquals(1560, result, 0.01);
    }

    @Test
    public void exchangeTest5CurrPath() {
        Graph graph;
        DataRead read = new DataRead("test/way1.txt");
        graph = read.readFile();
        ExchangeCurrency exchange = new ExchangeCurrency(graph);
        double result;

        result = exchange.exchange(graph.getCurrencyID("EUR"), 1000, graph.getCurrencyID("JPY"));

        Assert.assertEquals(2137.41, result, 0.01);
    }

    @Test
    public void exchangeTestCurrenctConnectionNotExists() {
        Graph graph;
        DataRead read = new DataRead("test/way1.txt");
        graph = read.readFile();
        ExchangeCurrency exchange = new ExchangeCurrency(graph);
        double result;

        result = exchange.exchange(graph.getCurrencyID("KON"), 1000, graph.getCurrencyID("JPY"));

        Assert.assertEquals(0, result, 0.01);
    }

    @Test
    public void exchangeTestZeroAmount() {
        Graph graph;
        DataRead read = new DataRead("test/way1.txt");
        graph = read.readFile();
        ExchangeCurrency exchange = new ExchangeCurrency(graph);
        double result;

        result = exchange.exchange(graph.getCurrencyID("EUR"), 0, graph.getCurrencyID("JPY"));

        Assert.assertEquals(0, result, 0.01);
    }
}