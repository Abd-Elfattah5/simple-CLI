
class Parser {
    String commandName;
    String[] args;
    //This method will divide the input into commandName and args
    //where "input" is the string command entered by the user
    public boolean parse(String input){return true;}
    public String getCommandName(){return "test";}
    public String[] getArgs(){return null;}
}

public class Terminal {
    Parser parser;
    //Implement each command in a method, for example:
    public String pwd(){return "test";}
    public void cd(String[] args){;}

    //This method will choose the suitable command method to be called
    public void chooseCommandAction(){;}
    public static void main(String[] args){
        System.out.printf("test haha\n");
    }
}
