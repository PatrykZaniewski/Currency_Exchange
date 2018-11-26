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

    //TODO Automatyczne przerabianie pliku?
    //TODO Wyszukiwanie danych przy mniejszej liczbie argumentow?
    Graph readFile() {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(fileToRead));
            String lineRead;
            int hashCount = 0;
            int lineNumber = 1;
            ArrayList<String> listOfCurrency = new ArrayList<>();
            int duplicateCount = 0;
            while ((lineRead = br.readLine()) != null) {
                System.out.println(lineRead);
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
                            String[] splitedLine = lineRead.split(" ");
                            if (Integer.parseInt(splitedLine[0]) != lineNumber - 2 - duplicateCount) {
                                System.out.println("Nieprawidłowe ID waluty w linii " + lineNumber + ", ID zostało ustawione na wartość: " + (lineNumber - 2 - duplicateCount));
                                splitedLine[0] = String.valueOf(lineNumber - 2 - duplicateCount);
                            }
                            lineRead = Arrays.toString(splitedLine);
                            listOfCurrency.add(lineRead);

                            boolean isDuplicate = false;
                            for (int i = 0; i < listOfCurrency.size() - 1; i++) {
                                if (listOfCurrency.get(i).contains(splitedLine[1])) {
                                    isDuplicate = true;
                                    break;
                                }
                            }
                            if (isDuplicate) {
                                System.out.println("Błąd 11: Wystąpił duplikat podczas wczytywania waluty w linii: " + lineNumber + ". Wpisz \"E\", aby edytować linię, wpisując \"P\" pominiesz ją, natomiast \"W\" zakończy działanie programu.");
                                while (true) {
                                    Scanner scannerError = new Scanner(System.in);
                                    String problemSolve = scannerError.nextLine();
                                    if (problemSolve.equals("W")) {
                                        return null;
                                    } else if (problemSolve.equals("E")) {
                                        System.out.println("Wprowadź linię ponownie:");
                                        scannerError = new Scanner(System.in);
                                        String lineEdit = scannerError.nextLine();
                                        lineEdit = checkData(lineEdit, true, lineNumber - duplicateCount);
                                        listOfCurrency.remove(listOfCurrency.size() - 1);
                                        lineEdit = lineEdit.replace(" ", ", ");
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
                            String[] splited = lineRead.split(" ");
                            int src = graph.getCurrencyID(splited[1]);
                            int dst = graph.getCurrencyID(splited[2]);
                            double multipler = Double.parseDouble(splited[3]);
                            double cost = Double.parseDouble(splited[5]);
                            boolean isPercent = true;
                            if (!splited[4].equals("PROC")) {
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
            System.out.println("Błąd 13: Nie znaleziono pliku o podanej nazwie");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return graph;
    }

    private String checkData(String lineRead, boolean currencyNameRead, int lineNumber) {
        String[] splitedLine = lineRead.split(" ");
        if (currencyNameRead) {
            if (splitedLine.length == 3) {
                if (splitedLine[0].matches("[0-9]+") && isAlpha(splitedLine[1]) && isAlpha(splitedLine[2])) {
                    return lineRead;
                }
            }
            if (splitedLine.length == 4) {
                if (splitedLine[0].matches("[0-9]+") && isAlpha(splitedLine[1]) && isAlpha(splitedLine[2]) && isAlpha(splitedLine[3])) {
                    return lineRead;
                }
            }
            if (splitedLine.length > 4) {
                if (splitedLine[0].matches("[0-9]+") && isAlpha(splitedLine[1]) && isAlpha(splitedLine[2])) {
                    if(!isAlpha(splitedLine[3]))
                    {
                        System.out.println("W linii " + lineNumber + " znajduja sie wiecej niz 3 argumenty. Zostana wczytane tylko pierwsze 3.");
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 3; i++) {
                        sb.append(splitedLine[i]);
                    }
                    if(isAlpha(splitedLine[3]))
                    {
                        sb.append(splitedLine[3]);
                    }
                    lineRead = sb.toString();
                    return lineRead;
                }
            }
            System.out.println("Ostrzeżenie 01: Błąd przy odczytywaniu pliku w linii " + lineNumber + ". Wpisz \"E\", aby edytować linię, natomiast wpisanie \"W\" zakończy działanie programu.");
            System.out.println("Prawidłowa linia wczytania walut zawiera 3 argumenty oddzielone białymi znakami: ID waluty, skrót waluty, pełna nazwa waluty.");
            System.out.println("Zawartość linii: " + Arrays.toString(splitedLine));
            while (true) {
                Scanner scannerError = new Scanner(System.in);
                String problemSolve = scannerError.nextLine();
                if (problemSolve.equals("W")) {
                    return "-1";
                } else if (problemSolve.equals("E")) {
                    System.out.println("Wprowadź linię ponownie:");
                    scannerError = new Scanner(System.in);
                    problemSolve = scannerError.nextLine();
                    return checkData(problemSolve, true, lineNumber);
                }
            }
        } else {
            if (splitedLine.length == 6) {
                lineRead = lineRead.replace(",", ".");
                if (splitedLine[0].matches("[0-9]+") && isAlpha(splitedLine[1]) && isAlpha(splitedLine[2]) && isDouble(splitedLine[3]) && (isAlpha(splitedLine[4]) || (splitedLine[4].contains("STA") && splitedLine[4].charAt(3) == 9532)) && isDouble(splitedLine[5])) {
                    return lineRead;
                }
            }
            if (splitedLine.length > 6) {
                if (splitedLine[0].matches("[0-9]+") && isAlpha(splitedLine[1]) && isAlpha(splitedLine[2]) && isDouble(splitedLine[3]) && isAlpha(splitedLine[4]) && isDouble(splitedLine[5])) {
                    System.out.println("Zostanie wczytane 6 pierwszych argumentow. Pozostale zostano pominiete");
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < 5; i++) {
                        sb.append(splitedLine[i]);
                    }
                    lineRead = sb.toString();
                    lineRead = lineRead.replace(",", ".");
                    return lineRead;
                }
            }
            System.out.println("Ostrzeżenie 01: Błąd przy odczytywaniu pliku w linii " + lineNumber +". Wpisz \"E\", aby edytować linię, wpisując \"P\" pominiesz ją, natomiast \"W\" zakończy działanie programu.");
            System.out.println("Prawidłowa linia wczytania kursów walut zawiera 6 argumentów oddzielonych białymi znakami: ID kursu, skrót waluty wejściowej, skrót waluty wyjściowej, kurs między walutami, typ opłaty, opłata");
            System.out.println("Zawartość linii: " + Arrays.toString(splitedLine));
            while (true) {
                Scanner scannerError = new Scanner(System.in);
                String problemSolve = scannerError.nextLine();

                if(problemSolve.equals("W"))
                {
                    return "-1";
                }
                else if(problemSolve.equals("P"))
                {
                    return null;
                }
                else if(problemSolve.equals("E"))
                {
                    System.out.println("Wprowadź linię ponownie:");
                    scannerError = new Scanner(System.in);
                    problemSolve = scannerError.nextLine();
                    return checkData(problemSolve, false, lineNumber);
                }
            }
        }
    }
}
