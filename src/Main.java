import java.text.DecimalFormat;
import java.util.Scanner;

public class Main {

    private static Graph graph;

    private static boolean isAlpha(String name) {
        char[] charsArray = name.toCharArray();

        for (char c : charsArray) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        //TODO wczytywanie pliku jako argument
        DataRead read = new DataRead("src/arbsciezki.txt");
        graph = read.readFile();
        if (graph == null) {
            System.out.println("Program zostal przerwany w czasie wczytywania danych.");
        } else {
            System.out.println("Aby uzyskac pomoc dotyczaca dzialania programu wpisz 'POMOC'.");
            while (true) {
                ExchangeCurrency exchange = new ExchangeCurrency(graph);
                FindArbitration arbitration = new FindArbitration(graph);
                Scanner scannerRead = new Scanner(System.in);
                String textConsolRead = scannerRead.nextLine();
                String[] splitedConsolRead = textConsolRead.split(" ");

                if (splitedConsolRead.length == 1 && textConsolRead.length() > 0) {
                    if (splitedConsolRead[0].equals("wyjscie") || splitedConsolRead[0].equals("Wyjscie") || splitedConsolRead[0].equals("WYJSCIE")) {
                        break;
                    }
                    if (splitedConsolRead[0].equals("POMOC")) {
                        System.out.println("Aby wyznaczyc arbitraz podaj jeden argument - ilosc waluty.");
                        System.out.println("Aby wyznaczyc najkorzystniejsza sciezke wymiany waluty podaj 3 argumenty oddzielone bialym znakiem - skrot waluty wejsciowej, ilosc waluty, skrot waluty wyjsciowej.");
                        System.out.println("Aby wyznaczyc dzialanie programu wpisz WYJSCIE.");
                    } else {
                        try {
                            double stringToDouble = Double.parseDouble(splitedConsolRead[0]);
                            double result = 0;
                            for (int i = 0; i < graph.getNumberOfVertexes(); i++) {
                                result = arbitration.BellmanFord(i, stringToDouble);
                                if (result > 0) {
                                    DecimalFormat df = new DecimalFormat("###.##");
                                    System.out.println(df.format(result));
                                    break;
                                }
                            }
                            if (result <= 0) {
                                System.out.println("Nie znaleziono abitrazu z podana kwota lub arbitraz nie jest mozliwy.");
                            }

                        } catch (NumberFormatException e) {
                            System.out.println("Blad 09: Podany argument arbitrazu zawiera znaki inne niż cyfry.");
                        }
                    }
                } else if (splitedConsolRead.length == 2) {
                    System.out.println("Blad 10: Brak jednego z argumentow potrzebnych do wyszukania korzystnej wymiany waluty.");
                } else if (splitedConsolRead.length > 2) {
                    boolean firstArgErr = false;
                    boolean secondArgErr = false;
                    double amount;
                    if (splitedConsolRead.length > 3) {
                        System.out.println("Ostrzezenie 02: Podano wiecej argumentow niz wymaga tego algorytm wyliczania najkorzystniejszej sciezki wymiany walut. Zostaly wczytane tylko pierwsze 3 argumenty.");
                    }
                    if (!isAlpha(splitedConsolRead[0])) {
                        firstArgErr = true;
                        System.out.println("Blad 07: Pierwszy z argumentow zawiera znaki inne niz litery.");
                    }
                    if (splitedConsolRead[0].length() != 3) {
                        firstArgErr = true;
                        System.out.println("Blad XX: Argument waluty wejsciowej jest dluzszy niz 3 znaki.");
                    }
                    if (!isAlpha(splitedConsolRead[2])) {
                        secondArgErr = true;
                        System.out.println("Blad 08: Trzeci z argumentow zawiera znaki inne niz litery.");
                    }
                    if (splitedConsolRead[2].length() != 3) {
                        secondArgErr = true;
                        System.out.println("Blad XX: Argument waluty wyjsciowej jest dluzszy niz 3 znaki.");
                    }
                    try {
                        amount = Double.parseDouble(splitedConsolRead[1]);
                        if (amount < 0) {
                            System.out.println("Blad 06: Kwota wymiany mniejsza od 0.");
                        } else {
                            if (!firstArgErr && !secondArgErr) {
                                boolean firstArgNotExist = false;
                                boolean secondArgNotExist = false;
                                int src = graph.getCurrencyID(splitedConsolRead[0]);
                                int dst = graph.getCurrencyID(splitedConsolRead[2]);
                                if (src == -1) {
                                    firstArgNotExist = true;
                                    System.out.println("Blad 04: Waluta wejsciowa nie istnieje w pliku.");
                                }
                                if (dst == -1) {
                                    secondArgNotExist = true;
                                    System.out.println("Blad 05: Waluta wyjsciowa nie istnieje w pliku.");
                                }
                                if (!firstArgNotExist && !secondArgNotExist) {
                                    double result = exchange.exchange(src, amount, dst);
                                    if (result == 0) {
                                        System.out.println("Nie mozna wymienic walut gdyz nie istnieje miedzy nimi bezposrednie albo posrednie polaczenie.");
                                    } else if (result == -1) {
                                        System.out.println("Na sciezce wymiany istnieje arbitraz wymiany walut co prowadzi do osiagniecia nieskonczonego wyniku.");
                                    } else {
                                        DecimalFormat df = new DecimalFormat("###.##");
                                        System.out.println("Wynik koncowy wymiany: " + df.format(result));
                                    }
                                }
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Blad 12: Drugi z argumentow zawiera znaki inne niz cyfry.");
                    }
                }
            }
        }
    }
}


