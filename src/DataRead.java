import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class DataRead {
    private File fileToRead;
    private Graph graph = null;

    public DataRead(String fileNameToRead) {
        this.fileToRead = new File(fileNameToRead);
    }

    private static boolean isAlpha(String stringToCheck) {
        char[] charArray = stringToCheck.toCharArray();
        for (char c : charArray) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean isDouble(String stringToCheck) {
        stringToCheck = stringToCheck.replace(",", ".");
        try {
            Double.parseDouble(stringToCheck);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Graph readFile() {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileToRead));
            String lineRead;
            int hashCount = 0;
            int lineNumber = 1;
            int duplicateCount = 0;
            ArrayList<String> listOfCurrency = new ArrayList<>();
            while ((lineRead = br.readLine()) != null) {
                lineRead = lineRead.trim();
                if (lineRead.length() > 0 && (lineRead.contains("#"))) {
                    hashCount++;
                    if (hashCount == 2) {
                        graph = new Graph(listOfCurrency);
                    }
                } else {
                    if (hashCount == 1) {
                        lineRead = checkData(lineRead, true, lineNumber);
                        if (lineRead != null && !lineRead.equals("-1")) {
                            String splittedLine[] = lineRead.split(" ");
                            if (Integer.parseInt(splittedLine[0]) != lineNumber - 2 - duplicateCount) {
                                System.out.println("Ostrzezenie 02: Nieprawidłowe ID waluty w linii " + lineNumber + ", ID zostało ustawione na wartość: " + (lineNumber - 2 - duplicateCount) + ".");
                                splittedLine[0] = String.valueOf(lineNumber - 2 - duplicateCount);
                            }
                            lineRead = Arrays.toString(splittedLine);
                            listOfCurrency.add(lineRead);

                            boolean isDuplicate = false;
                            for (int i = 0; i < listOfCurrency.size() - 1; i++) {
                                if (listOfCurrency.get(i).contains(splittedLine[1])) {
                                    isDuplicate = true;
                                    break;
                                }
                            }
                            if (isDuplicate) {
                                System.out.println("Blad 05: Wystapil duplikat podczas wczytywania waluty w linii: " + lineNumber + ". Wpisz \"E\", aby edytowac linie, wpisujac \"P\" pominiesz ja, natomiast \"W\" zakonczy dzialanie programu.");
                                while (true) {
                                    Scanner scannerError = new Scanner(System.in);
                                    String problemSolve = scannerError.nextLine();
                                    if (problemSolve.equals("W")) {
                                        return null;
                                    } else if (problemSolve.equals("E")) {
                                        System.out.println("Wprowadz linie ponownie: ");
                                        scannerError = new Scanner(System.in);
                                        String lineEdit = scannerError.nextLine();
                                        lineEdit = checkData(lineEdit, true, lineNumber - duplicateCount);
                                        listOfCurrency.remove(listOfCurrency.size() - 1);
                                        if (lineEdit != null) {
                                            lineEdit = lineEdit.replace(" ", ", ");
                                        }
                                        listOfCurrency.add(lineEdit);
                                        break;
                                    } else if (problemSolve.equals("P")) {
                                        listOfCurrency.remove(listOfCurrency.size() - 1);
                                        duplicateCount++;
                                        break;
                                    }
                                }
                            }
                        } else {
                            return null;
                        }
                    }
                    if (hashCount == 2) {
                        lineRead = checkData(lineRead, false, lineNumber);
                        if (lineRead != null && !lineRead.equals("-1")) {
                            String splittedLineRead[] = lineRead.split(" ");
                            int src = graph.getCurrencyID(splittedLineRead[1]);
                            int dst = graph.getCurrencyID(splittedLineRead[2]);
                            double multipler = Double.parseDouble(splittedLineRead[3]);
                            double cost = Double.parseDouble(splittedLineRead[5]);
                            boolean isPercent = true;
                            if (!splittedLineRead[4].equals("PROC")) {
                                isPercent = false;
                            }
                            graph.addEdge(src, dst, multipler, cost, isPercent);
                        }
                        if (lineRead != null && lineRead.equals("-1")) {
                            return null;
                        }
                    }
                }
                lineNumber++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Blad 02: Nie znaleziono zadanego pliku.");
            return null;
        } catch (IOException e) {
            System.out.println("Blad 03: Program nie moze uzyskac dostepu do pliku - brak uprawnien.");
            e.printStackTrace();
        }
        return graph;
    }

    private String checkData(String lineRead, boolean currencyNameRead, int lineNumber) {
        String[] splittedLine = lineRead.split(" ");
        if (currencyNameRead) {
            if (splittedLine.length == 3) {
                if (splittedLine[0].matches("[0-9]+") && isAlpha(splittedLine[1]) && isAlpha(splittedLine[2]) && splittedLine[1].length() == 3) {
                    return lineRead;
                }
            }
            if (splittedLine.length == 4) {
                if (splittedLine[0].matches("[0-9]+") && isAlpha(splittedLine[1]) && isAlpha(splittedLine[2]) && isAlpha(splittedLine[3]) && splittedLine[1].length() == 3) {
                    return lineRead;
                }
            }
            if (splittedLine.length > 4) {
                if (splittedLine[0].matches("[0-9]+") && isAlpha(splittedLine[1]) && isAlpha(splittedLine[2]) && splittedLine[1].length() == 3) {
                    if (!isAlpha(splittedLine[3])) {
                        System.out.println("Ostrzezenie 03: W linii " + lineNumber + " znajduja sie wiecej niz 3 argumenty. Zostana wczytane tylko 3 pierwsze.");
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 3; i++) {
                        sb.append(splittedLine[i]);
                    }
                    if (isAlpha(splittedLine[3])) {
                        sb.append(splittedLine[3]);
                    }
                    lineRead = sb.toString();
                    return lineRead;
                }
            }
            System.out.println("Blad 04: Blad przy odczytywaniu pliku w linii " + lineNumber + ". Wpisz \"E\", aby edytowac linie, namtomiast wpisanie \"W\" zakonczy dzialanie programu.");
            System.out.println("Prawidlowa linia wczytania walut zawiera 3 argumenty oddzielone bialymi znakami: ID waluty, skrot waluty (3 znaki), pelna nazwa waluty.");
            System.out.println("Zawartosc linii: " + Arrays.toString(splittedLine));
            while (true) {
                Scanner scannerError = new Scanner(System.in);
                String problemSolve = scannerError.nextLine();
                if (problemSolve.equals("W")) {
                    return "-1";
                } else if (problemSolve.equals("E")) {
                    System.out.println("Wprowadz linie ponownie:");
                    scannerError = new Scanner(System.in);
                    problemSolve = scannerError.nextLine();
                    return checkData(problemSolve, true, lineNumber);
                }
            }
        } else {
            if (splittedLine.length == 6) {
                lineRead = lineRead.replace(",", ".");
                if (splittedLine[0].matches("[0-9]+") && isAlpha(splittedLine[1]) && isAlpha(splittedLine[2]) && isDouble(splittedLine[3]) && (isAlpha(splittedLine[4]) || (splittedLine[4].contains("STA") && splittedLine[4].charAt(3) > 255 && splittedLine[4].charAt(4) > 255 && splittedLine[4].charAt(5) == 'A')) && isDouble(splittedLine[5]) && splittedLine[1].length() == 3 && splittedLine[2].length() == 3) {
                    if (graph.getCurrencyID(splittedLine[1]) == -1) {
                        System.out.println("Blad 07: Kurs zawiera walute wejsciowa, ktora nie istnieje w pliku.");
                    }
                    if (graph.getCurrencyID(splittedLine[2]) == -1) {
                        System.out.println("Blad 08: Kurs zawiera walute wyjsciowa, ktora nie istnieje w pliku.");
                    }
                    if (graph.getCurrencyID(splittedLine[1]) != -1 && graph.getCurrencyID(splittedLine[2]) != -1) {
                        return lineRead;
                    }
                }
            }
            if (splittedLine.length > 6) {
                if (splittedLine[0].matches("[0-9]+") && isAlpha(splittedLine[1]) && isAlpha(splittedLine[2]) && isDouble(splittedLine[3]) && isAlpha(splittedLine[4]) && isDouble(splittedLine[5])) {
                    System.out.println("Ostrzezenie 04: W linii " + lineNumber + "znajduje sie wiecej niz 6 argumentow. Zostanie wczytanych tylko 6 pierwszych.");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 5; i++) {
                        sb.append(splittedLine[i]);
                    }
                    lineRead = sb.toString();
                    lineRead = lineRead.replace(",", ".");
                    return lineRead;
                }
            }
            System.out.println("Blad 06: Blad przy odczytywaniu pliku w linii " + lineNumber + ". Wpisz \"E\", aby edytowac linie, wpisujac \"P\" pominiesz ja, natomiast \"W\" zakonczy dzialanie programu.");
            System.out.println("Prawidlowa linia wczytania kursow walut zawiera 6 argumentow oddzielonych bialymi znakami: ID kursu, skrot waluty wejsciowej (3 znaki), skrot waluty wyjsciowej (3 znaki), kurs między walutami, typ oplaty (procentowa - PROC, stala - STALA), oplata");
            System.out.println("Zawartosc linii: " + Arrays.toString(splittedLine));
            while (true) {
                Scanner scannerError = new Scanner(System.in);
                String problemSolve = scannerError.nextLine();

                if (problemSolve.equals("W")) {
                    return "-1";
                } else if (problemSolve.equals("P")) {
                    return null;
                } else if (problemSolve.equals("E")) {
                    System.out.println("Wprowadz linie ponownie: ");
                    scannerError = new Scanner(System.in);
                    problemSolve = scannerError.nextLine();
                    return checkData(problemSolve, false, lineNumber);
                }
            }
        }
    }
}
