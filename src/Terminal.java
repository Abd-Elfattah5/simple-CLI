import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.File;
/**
 * this is class which is responsible for dealing with strings
 * taking input strings and parsing it in a proper way*/
class Parser {
    /**
     * a command name that is the first argument of the command line
     * */
    private String commandName;
    /**
     * this is the arguments array which contains all the arguments
     * starting with the commandName and followed with the rest of the flags
     * */
    private String[] args;
    /**
     * this is a Scanner which is responsible for reading from the input stream
     * */
    private Scanner reader;
    /**
     * this is the read line in each iteration
     * */
    private String line;
    /**
     * this is the constructor function
     * */
    public Parser() {
        reader = new Scanner(System.in);
    }

    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    /**
     * this is the function which is responsible for parsing the input line
     *
     * @param input this is the input line parameter
     * @return  True if the input text parsed successfully and false otherwise
     * */
    public boolean parse(String input){
        if (input.equals("\n") || input.isEmpty())
        {
            return false;
        }
        args = input.split("\\s+");
        if (args.length == 0)
            return false;
        commandName = args[0];
        return true;
    }
    /**
     * a function responsible for reading a command from the input stream
     *
     * @return a line that's read from the input stream
     * */
    public String getCommandName(){
        line = reader.nextLine();
        return line;
    }
    /**
     * this is a getter function to get all the parsed line of command
     *
     * @return an array of string which contains the command and its arguments
     * */
    public String[] getArgs(){return args;}

    /**
     * this function is just to close the Scanner reader
     * */
    public void end() {
        reader.close();
    }
}

/**
 * this is the main class Terminal which is responsible for operating
 * the whole CLI
 * */
public class Terminal {
    /**
     * this is Parser object for the terminal
     * */
    Parser parser;
    /**
     * a map where I can save all the commands used during the life
     * of the terminal
     * */
    Map<Integer, String> dictionary = new HashMap<>();
    /**
     * an integer where I keep track of how many commands used
     * */
    int numCommand = 0;
    //Implement each command in a method, for example:
    /**
     * this is the terminal constructor
     * */
    public Terminal(){
        parser = new Parser();
    }


    /**
     * function to print strings
     * @param S the string to be printed
     * */
    public void print_string(String S)
    {
        if (S != null)
            System.out.println(S);
    }

    /**
     * function to check if the file path is absolute
     * @param Args the string to be checked
     * @return true if absolute and false otherwise
     * */
    public boolean is_absolute(String Args)
    {
        int c = 0;
        for (char l: Args.toCharArray()
        ) {
            if (l == '\\')
                c++;
            if (c > 0)
                return true;
        }
        return false;
    }

    /**
     * this function is command implementation for
     * printing the current working directory
     *
     * @return the current working directory
     * */
    public String pwd(){
        return System.getProperty("user.dir");
    }
    /**
     * this function is command implementation for
     * changing the directory
     * 
     * @param Args this is the array of command line arguments parsed
     * */
    public void cd(String[] Args){
        if (Args.length == 1){
            String home = System.getProperty("user.home");
            System.setProperty("user.dir", home);
        }
        else if (Args[1].equals(".."))
        {
            File me = new File(pwd());
            File my_parent = me.getParentFile();
            if (my_parent == null)
            {
                System.out.println("there is no parent directory to this directory.");
            }
            else
            {
                System.setProperty("user.dir", my_parent.getAbsolutePath());
            }
        }
        else
        {
            File directory;
            if (!is_absolute(Args[1]))
                directory = new File(pwd() + "\\" + Args[1]);
            else
                directory = new File(Args[1]);
            if (directory.exists() && directory.isDirectory())
                System.setProperty("user.dir", directory.getAbsolutePath());
            else
                System.out.println("there is no such directory with this name.");
        }
    }
    /**
     * this function is command implementation for
     * counting lines, words and chars in a file
     *
     * @param Args this is the array of command line arguments parsed
     * @return the string to be printed with full information.
     * */
    public String wc(String[] Args) {
        File target;
        int ch;
        int lineCount = 0;
        int charCount = 0;
        int wordCount = 0;
        boolean newLine = false;

        if (Args.length != 2) {
            System.out.println("invalid arguments, Usage: wc <file name>");
            return null;
        }
        if (!is_absolute(Args[1]))
            target = new File(pwd() + "\\" + Args[1]);
        else
            target = new File(Args[1]);
        try (FileReader fileReader = new FileReader(target))
        {
            while ((ch = fileReader.read()) != -1)
            {
                charCount++;
                if (ch == ' ')
                    wordCount++;
                else if (ch == '\n') {
                    wordCount++;
                    lineCount++;
                    newLine = true;
                }
                else
                    newLine = false;
            }

            if (!newLine)
                lineCount++;

            return String.format("%d %d %d %s", lineCount, wordCount, charCount, target.getName());

        } catch (IOException e) {
            System.out.println("there is no such file with this name.");
        }
        return null;
    }
    /**
     * this function is commandline implementation
     * of showing the history command
     * */
    public void history()
    {
        dictionary.forEach((key, value) -> System.out.printf("%d %s%n", key, value));
    }

    //This method will choose the suitable command method to be called
    /**
     * this function is to choose the proper implementation
     * for the command chosen
     *
     * @param Args is the parsed command line
     * */
    public void chooseCommandAction(String[] Args){
        switch (Args[0])
        {
            case "cd":
                cd(Args);
                numCommand += 1;
                dictionary.put(numCommand, "cd");
                break;
            case "pwd":
                print_string(pwd());
                numCommand += 1;
                dictionary.put(numCommand, "pwd");
                break;
            case "wc":
                print_string(wc(Args));
                numCommand += 1;
                dictionary.put(numCommand, "wc");
                break;
            case "history":
                history();
                numCommand += 1;
                dictionary.put(numCommand, "history");
            case "exit":
                break;
            default:
                System.out.println("Invalid command, please try to write a proper command.");
        }
    }
    /**
     * this function is the starting point of the program
     *
     * @param args is the args given to the program through the Terminal
     * */
    public static void main(String[] args){
        Terminal T1 = new Terminal();
        while (true){
            System.out.print("^_^$ ");
            if (!T1.parser.parse(T1.parser.getCommandName()))
                continue;
            T1.chooseCommandAction(T1.parser.getArgs());
            if (T1.parser.getArgs()[0].equals("exit"))
            {
                T1.parser.end();
                break;
            }
        }
    }
}
