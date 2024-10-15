import java.awt.*;
import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

//TO DO:
//Fix files read so that is takes the variable to read it

public class BabyNames {

    //class constants for open area in graphics (Do NOT change)
    private static final Integer OPEN_AREA_WIDTH = 780;
    private static final Integer OPEN_AREA_HEIGHT = 500;

    //prompt msg class constants (Do NOT change)
    private static final String MESSAGE_PREFIX = "This program allows you to search through the\n" +
            "data from the Social Security Administration\n" +
            "to see how popular a particular name has been\n" +
            "since ";

    // class constant for meaning file (Do NOT change)
    private static final String MEANING_FILENAME = "meanings.txt";

    // class constant for name file (Change the value only. Do NOT change the names of the constant)
    // Test with both "names.txt" and "names2.txt" (Before submission, change back to "names.txt")
    private static String nameFilename = "names.txt";

    // Other class constants (Change the value only. Do NOT change the names of the constants)
    private static Integer startingYear = 1890; // change the value according to spec
    private static Integer decadeWidth = 60; // change the value according to spec
    private static Integer legendHeight = 30; // change the value according to spec

    // YOU ARE NOT ALLOWED TO ADD ANY OTHER CONSTANTS THAN ABOVE
    //Our global class varibles created to hold our first name and gender
    public static String firstName; //Stores our first name input
    public static String gender; //Stores our gender input
    public static boolean nameDataFound = false; //Allows us to check if name data is found in different scopes

    public static String nameFound;  // The data that is stored if a name is found
    public static String meaningFound;  //The data that is stored if a meaning is found
    public static void main(String[] args) throws FileNotFoundException {
        //1) takes user input of gender
        getUserInput();
        //2) Based on the user input, output to console meaning and data for popularity
        outputDataToConsole();
        //3) If found, output to graphics
        outputToGraph();
    }
    //Get User Input and store it in two variables
    public static void getUserInput(){
        //1) prompt the user
        System.out.println(MESSAGE_PREFIX + startingYear);
        System.out.println();
        //2) ask for name and store it
        //Scanner to read the input
        Scanner consumeUserInput = new Scanner(System.in);
        System.out.print("Name: ");
        firstName = consumeUserInput.next();
        //3) ask for gender and store it
        System.out.print("Gender (M or F): ");
        gender = consumeUserInput.next().charAt(0) + "";
    }
    //Outputs data if found to the console
    public static void outputDataToConsole() throws FileNotFoundException {
        //1)Check to see if our name and gender are in our file
        nameFound = dataCheck(nameFilename);
        meaningFound = dataCheck(MEANING_FILENAME);
        //2)If found output that data to our graph, if not found output "name" not found.
        if (nameDataFound){
            System.out.println(nameFound);
            System.out.println(meaningFound);
        }else{
            System.out.println("\"" + firstName +"\" not found.");
        }
    }
    //Will search the two files to see if our name and gender exist in it
    private static String dataCheck(String givenFile)throws FileNotFoundException{
        //1)Search for our name
        String returnData = null;
        nameDataFound = false;
        Scanner fileData = new Scanner(new File(givenFile));
        //While name is not found repeat searching
        while(!nameDataFound && fileData.hasNextLine()){
            String oneLineOfData = fileData.nextLine();
            Scanner oneToken = new Scanner(oneLineOfData);
            String nameToken = oneToken.next();
            String genderToken = oneToken.next().charAt(0) + "";
            //Performing the actual check to see if the data is found
            if (nameToken.equalsIgnoreCase(firstName) && genderToken.equalsIgnoreCase(gender)){
                //2)If found store data
                returnData = oneLineOfData;
                nameDataFound = true;
            }
        }
        return  returnData; //Returns the data found for a given name or returns null if not found
    }

    public static void outputToGraph(){
        //1)Create Drawing Panel and Graphics if data found is true
        if (nameDataFound){ //If our name is found execute  a panel otherwise stop
            int panelWidth = OPEN_AREA_WIDTH;
            int panelHeight = OPEN_AREA_HEIGHT + legendHeight*2;
            //Initialize pane (Change the parameters in below line according to spec!)
            DrawingPanel panel = initializePanel(panelWidth, panelHeight, Color.WHITE);
            Graphics g = panel.getGraphics();
            //2)Create Static Drawings
            outputStaticGraphicsToGraph(g, panelWidth, panelHeight);
            //3) Create Variable Drawings
            outputVaryingGraphicsToGraph(g, panelHeight);
        }
    }
    //Outputs our gray rectangles and black lines
    private static void outputStaticGraphicsToGraph(Graphics g, int panelWidth, int panelHeight) {
        //1) Gray Rectangles
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0,0,panelWidth,legendHeight);
        g.fillRect(0,panelHeight-legendHeight,panelWidth,legendHeight);
        //2) Black Lines
        //2)Black Lines
        g.setColor(Color.BLACK);
        g.drawLine(0,legendHeight,panelWidth, legendHeight);
        g.drawLine(0,panelHeight-legendHeight,panelWidth, panelHeight-legendHeight);

    }
    //Outputs our changing graphics from iteration to iteration
    private static void outputVaryingGraphicsToGraph(Graphics g, int panelHeight){
        int distanceToBottom = panelHeight - legendHeight; //The distance to subtract by in later equations
        //1) Output Meaning in top gray bar
        g.drawString(meaningFound,0,16);
        //2) Determine amount of decades
        //3)Draw decades strings and green bars
        Scanner valueScanner = new Scanner(nameFound);
        String throwAway = valueScanner.next();
        throwAway = valueScanner.next();
        //For while to create varying graphics
        int decadeIncrement = 0; //Sets the increment of our decade to 0
        while(valueScanner.hasNextInt()){
            g.setColor(Color.BLACK);
            g.drawString(startingYear + decadeIncrement*10+"",(decadeIncrement * decadeWidth), panelHeight-8); //Bottom year strings
            int dataNum = valueScanner.nextInt();
            int yValue = getTopY(dataNum, distanceToBottom);
            g.setColor(Color.GREEN);
            //Green bars
            g.fillRect((decadeIncrement * decadeWidth), yValue, decadeWidth/2, distanceToBottom-yValue);
            g.setColor(Color.BLACK);
            //Black numbers on top of bars
            g.drawString(""+dataNum,(decadeIncrement * decadeWidth), yValue);

            decadeIncrement++;
        }
    }
    //Converts the rank to a Y value
    private static int getTopY(int dataNum, int panelHeight) {
        boolean isCalculated = false;
        int calculatedInt = dataNum;
        while (!isCalculated){ //continues calculation till is calculated returns true
            if (dataNum == 1){ //If the rank is 1 execute
                calculatedInt = legendHeight;
                isCalculated = true;
            }else if(dataNum == 0){ // if the rank is 0 execute
                calculatedInt = panelHeight;
                isCalculated = true;
            }
            else{ //If the equation is niether 1 or 0 execute
                //Equation to find top of Y value
                calculatedInt = dataNum/2 + legendHeight;
                isCalculated = true;
            }
        }
        return calculatedInt;
    }

    //Calls an instance of the drawing panel to make graphics easier
    private static DrawingPanel initializePanel(int width, int height, Color bgColor) {
        //Create the drawing panel object
        DrawingPanel panel = new DrawingPanel(width, height);

        //Set background color
        panel.setBackground(bgColor);

        //Return the panel object
        return panel;
    }
}