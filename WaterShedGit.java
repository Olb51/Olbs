// Water Shed program for EnSoft
// Written by Sam Kruger

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;

public class WaterShed {
    
    private String[] avoidArray, pegStartArray, samStartArray, inputArray, segmentStart, segmentFinish;
    private int endOfRiverSegments;                    
    private List<String> buildingPath = new ArrayList<String>();
    private List<String> successfulPath = new ArrayList<String>();


    public void breakInputToArrays(String input){
        inputArray = input.split("%n");
        
        //Locate end of map coordinates in the array
        List<String> inputList = Arrays.asList(inputArray);
        endOfRiverSegments = inputList.indexOf("Avoid:");
        
        //Grab Peggy and Sam start locations and what river's to avoid
        //and make them arrays
        String avoid = inputArray[endOfRiverSegments+1];
        avoidArray = avoid.split("\\s");
        String pegStart = inputArray[endOfRiverSegments+3];
        pegStartArray = pegStart.split("\\s");
        String samStart = inputArray[endOfRiverSegments+5];
        samStartArray = samStart.split("\\s");
    }
    
    //Method to remove River segments that contain the junctions to avoid
    //and split remaining River segments into lists of start and finish locations
    public void removeSegments(){
        List<String> splitStart = new ArrayList<String>();
        List<String> splitFinish = new ArrayList<String>();
        for(int i = 1; i < endOfRiverSegments; i++){
            Boolean found = false;
            String preSplit = inputArray[i];
            String [] postSplit = preSplit.split("\\s");
            for(String address : avoidArray){
                if((postSplit[0].equals(address)) || (postSplit[1].equals(address))){
                found = true;
                break;
                }
            }
            if(!found){
                splitStart.add(postSplit[0]);
                splitFinish.add(postSplit[1]);
            }
        }
        segmentStart = splitStart.toArray(new String[splitStart.size()]);
        segmentFinish = splitFinish.toArray(new String[splitFinish.size()]);
    }
    
    //Method to check if there is a solution, if so sort, remove duplicates and print it
    public void printResult(List<String> solution){
        if (solution.equals("")){
            System.exit(1);
        } else {
            HashSet<String> hashSolution = new HashSet<String>();
            hashSolution.addAll(solution);
            solution.clear();
            solution.addAll(hashSolution);
            Collections.sort(solution);
            for(String temp: solution){
                System.out.println(temp);
            }
        }
    }

    //Method for checking if an element in the list is already there 2 times.
    public boolean onlyTwo(String start, List<String> path){
        int howMany = 0;
        for(int i=0;i<path.size();i++){
            if(start.equals(path.get(i))){
            howMany++;
            }
        }
        if(howMany<2){
            return true;
        }
        else return false;
    }

    
    //Method to find all successful paths between Peggy and Sam's Start Locations
    public List<String> findPath(String start, String finish){
        boolean multiple = onlyTwo(start, buildingPath);
        if(multiple){
            for (int i = 0; i < segmentStart.length;i++){
                if(start.equals(segmentStart[i]) && finish.equals(segmentFinish[i])){
                    buildingPath.add(start);
                    buildingPath.add(finish);
                    successfulPath.addAll(buildingPath);
                    buildingPath.remove(buildingPath.size()-1);
                    buildingPath.remove(buildingPath.size()-1);
                } else if(start.equals(segmentStart[i])) {
                    buildingPath.add(start);
                    findPath(segmentFinish[i], finish);
                }
            
                if(!buildingPath.isEmpty() && (i == (segmentStart.length - 1))){
                    buildingPath.remove(buildingPath.size()-1);
                }
            }
        } else{
            buildingPath.remove(buildingPath.size()-1);
        }
        return successfulPath;
    }

    public List<String> recurseArrays(){
        List<String> path = new ArrayList<String>();
        for (String i: pegStartArray){
            for (String j: samStartArray){
                path = findPath(i,j);
            }
        }
        return path;
    }  

    public static void main(String[] args) {
        //String input = "Map:%na1 b1%nb1 a1%na2 b1%nb1 a2%na2 b2%na2 b3%nb1 c1%nb2 c2%nb2 c3%nb3 c3%nAvoid:%nb2%nPeggy:%na2%nSam:%nc2 c3";
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        List<String> path = new ArrayList<String>();
                          
        WaterShed ws = new WaterShed();
        ws.breakInputToArrays(input);
        ws.removeSegments();
        path = ws.recurseArrays();                                      
                                                                                  
        ws.printResult(path);
    }
}
