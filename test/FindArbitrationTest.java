import org.junit.Assert;
import org.junit.Test;

public class FindArbitrationTest {

    @Test
    public void arbitrationTestBigValue()
    {
        Graph graph;
        DataRead read = new DataRead("test/arb1.txt");
        graph = read.readFile();
        FindArbitration arbitration = new FindArbitration(graph);
        double result = 0;

        for(int i = 0; i < graph.getNumberOfVertexes(); i++) {
            result = arbitration.arbitration(i, 1000000);
            if(result > 0)
            {
                break;
            }
        }

        Assert.assertEquals(59999999.39, result, 0.01);
    }

    @Test
    public void arbitrationTestNormalValue()
    {
        Graph graph;
        DataRead read = new DataRead("test/arb1.txt");
        graph = read.readFile();
        FindArbitration arbitration = new FindArbitration(graph);
        double result = 0;

        for(int i = 0; i < graph.getNumberOfVertexes(); i++) {
            result = arbitration.arbitration(i, 1000);
            if(result > 0)
            {
                break;
            }
        }

        Assert.assertEquals(59999.39, result, 0.01);
    }

    @Test
    public void arbitrationTestSmallValue()
    {
        Graph graph;
        DataRead read = new DataRead("test/arb1.txt");
        graph = read.readFile();
        FindArbitration arbitration = new FindArbitration(graph);
        double result = 0;

        for(int i = 0; i < graph.getNumberOfVertexes(); i++) {
            result = arbitration.arbitration(i, 0.1);
            if(result > 0)
            {
                break;
            }
        }

        Assert.assertEquals(5.98, result, 0.01);
    }

    @Test
    public void arbitrationTestNoArbitrage()
    {
        Graph graph;
        DataRead read = new DataRead("test/arb1.txt");
        graph = read.readFile();
        FindArbitration arbitration = new FindArbitration(graph);
        double result = 0;

        for(int i = 0; i < graph.getNumberOfVertexes(); i++) {
            result = arbitration.arbitration(i, 0.000001);
            if(result > 0)
            {
                break;
            }
        }

        Assert.assertEquals(0, result, 0.01);
    }

    @Test
    public void arbitrationTestNegativeAmount()
    {
        Graph graph;
        DataRead read = new DataRead("test/arb1.txt");
        graph = read.readFile();
        FindArbitration arbitration = new FindArbitration(graph);
        double result = 0;

        for(int i = 0; i < graph.getNumberOfVertexes(); i++) {
            result = arbitration.arbitration(i, -1);
            if(result > 0)
            {
                break;
            }
        }

        Assert.assertEquals(0, result, 0.01);
    }

    @Test
    public void arbitrationTestZeroValue()
    {
        Graph graph;
        DataRead read = new DataRead("test/arb1.txt");
        graph = read.readFile();
        FindArbitration arbitration = new FindArbitration(graph);
        double result = 0;

        for(int i = 0; i < graph.getNumberOfVertexes(); i++) {
            result = arbitration.arbitration(i, 0);
            if(result > 0)
            {
                break;
            }
        }

        Assert.assertEquals(0, result, 0.01);
    }

    @Test
    public void arbitrationTestLongWay()
    {
        Graph graph;
        DataRead read = new DataRead("test/arb2.txt");
        graph = read.readFile();
        FindArbitration arbitration = new FindArbitration(graph);
        double result = 0;

        for(int i = 0; i < graph.getNumberOfVertexes(); i++) {
            result = arbitration.arbitration(i, 1000);
            if(result > 0)
            {
                break;
            }
        }

        Assert.assertEquals(57304.01, result, 0.01);
    }

    @Test
    public void arbitrationTestShortWay()
    {
        Graph graph;
        DataRead read = new DataRead("test/arb2.txt");
        graph = read.readFile();
        FindArbitration arbitration = new FindArbitration(graph);
        double result = 0;

        for(int i = 0; i < graph.getNumberOfVertexes(); i++) {
            result = arbitration.arbitration(i, 0.1);
            if(result > 0)
            {
                break;
            }
        }

        Assert.assertEquals(0.12, result, 0.01);
    }
}