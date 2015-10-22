package employeemaker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * A small application for building sample employee-based data files.
 *
 * @author Dr. Denise Case
 */
public class EmployeeMaker {

    private static ArrayList<String> first = new ArrayList<>();
    private static ArrayList<String> last = new ArrayList<>();
    private static ArrayList<String> hourly = new ArrayList<>();
    private static ArrayList<String> salary = new ArrayList<>();
    private static ArrayList<String> department = new ArrayList<>();
    private static final int NUM_SMALL = 3;
    private static final int NUM_LARGE = 800;
    private static final int MIN_EXTRA = 0;
    private static final int MAX_EXTRA = 20;
    
    private static enum EmpType {
        Hourly, 
        Salary
    };

    private static final String SMALL_FILE_NAME = "output1.txt";
    private static final String LARGE_FILE_NAME = "output2.txt";

    public static void main(String[] args) {

        final String DIR = System.getProperty("user.dir");
        final String PARTIAL = "/src/employeemaker/";
        final String PATH = DIR + PARTIAL;

        first = EmployeeMaker.read(new File(PATH + "first.txt"));
        last = EmployeeMaker.read(new File(PATH + "last.txt"));
        hourly = EmployeeMaker.read(new File(PATH + "hourly.txt"));
        salary = EmployeeMaker.read(new File(PATH + "salary.txt"));
        department = EmployeeMaker.read(new File(PATH + "department.txt"));
   
        EmployeeMaker.createFile(PATH + SMALL_FILE_NAME);
        EmployeeMaker.createFile(PATH + LARGE_FILE_NAME);

        EmployeeMaker.generate(NUM_SMALL, PATH + SMALL_FILE_NAME);
        EmployeeMaker.generate(NUM_LARGE, PATH + LARGE_FILE_NAME);
    }

    /**
     * Read the contents of the file into an array list. 
     * @param f - a File Object containing text data
     * @return the array list 
     */
    public static ArrayList<String> read(File f) {
        ArrayList<String> a = new ArrayList<>();

        try (Scanner sc = new Scanner(f)) {
            while (sc.hasNextLine()) {
                String s = sc.nextLine();
                a.add(s);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("READ FILE NOT FOUND: " + ex.getMessage());
        }
        System.out.println(a);

        return a;
    }

    /**
     * Create a file object from a string path and file name.
     * @param fname - the string path and file
     */
    public static void createFile(String fname) {
        File f = new File(fname);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
        } catch (IOException ex) {
            System.out.println("CREATE ERROR: " + ex.getMessage());
        }
    }
/**
 * Generate a new result text file with the random entries.
 * @param entryCount - number of entries in the new file
 * @param fname - the file name and extension of the output file
 */
    public static void generate(long entryCount, String fname) {
        for (long i = 0; i < entryCount; i++) {
            ArrayList<String> lines = new ArrayList<>();
            String name = EmployeeMaker.generateName();
            double rate = EmployeeMaker.generateRate();
            double extra = EmployeeMaker.generateExtra();
            String dept = EmployeeMaker.generateDepartment();
            lines.add(name);
            lines.add(String.valueOf(rate));
            lines.add(String.valueOf(extra));
            lines.add(dept);
            System.out.println(lines.toString());
            EmployeeMaker.append(fname, lines);
        }
    }

    /**
     * Append a new entry to the result file.
     * @param fname - the file name and extension of the output file to append to
     * @param lines - an array list of the entry lines
     */
    public static void append(String fname, ArrayList<String> lines) {
        FileWriter writer;
        try {
            writer = new FileWriter(fname, true);
            try (BufferedWriter bwriter = new BufferedWriter(writer)) {
                for (String line : lines) {
                    bwriter.append(line);
                    bwriter.newLine();

                }
            }
        } catch (IOException ex) {
            System.out.println("FILEWRITER ERROR: " + ex.getMessage());
        }

    }

    /**
     * Generate a random name.
     * @return string name
     */
    private static String generateName() {
        int randi = ThreadLocalRandom.current().nextInt(0, first.size());
        int randj = ThreadLocalRandom.current().nextInt(0, last.size());
        String givenName = EmployeeMaker.capitalize(first.get(randi).trim());
        return givenName + " " + last.get(randj).trim();
    }

    private static String capitalize(final String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /**
     * Generate a double random number between two values (inclusive).
     * @param min - the smallest number
     * @param max - the largest number
     * @return a random double in this range, inclusive
     */
    private static double randBetween(long min, long max) {
        return ((double) ThreadLocalRandom.current().nextLong(min, max + 1));
    }
    
    /**
     * Generate a double value for extra pay - 
     * either extra hours worked this week (if hourly) or 
     * extra pay earned this week (if salaried)
     * @return the value of the extra pay (either hours or $)
     */
      private static double generateExtra() {
        return ThreadLocalRandom.current().nextInt(MIN_EXTRA, MAX_EXTRA);
    }

    /**
     * Generate a department string
     *
     * @return the department name
     */
    private static String generateDepartment() {
        int j = ThreadLocalRandom.current().nextInt(0, department.size());
        return department.get(j).trim();
    }

    /**
     * Generate a double pay rate.
     * @return the double pay rate (either $/hr or $/year)
     */
    private static double generateRate() {
        String strRate;
        int iType = (int) EmployeeMaker.randBetween(0, 1);
        if (EmpType.values()[iType] == EmpType.Hourly) {
            int k = ThreadLocalRandom.current().nextInt(0, hourly.size());
            strRate = hourly.get(k).trim();
        } else {
            int k = ThreadLocalRandom.current().nextInt(0, salary.size());
            strRate = salary.get(k).trim();
        }
        return Double.parseDouble(strRate);
    }
}
