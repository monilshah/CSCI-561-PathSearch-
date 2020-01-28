
import java.io.*;
import java.util.*;
import java.util.Queue;

class coordinatePointStore {
 int row_coordinate, column_coordinate; 
         long distance_cost, hueristic_cost;

    coordinatePointStore(int row_coordinate, int column_coordinate) {
        this.column_coordinate = column_coordinate;
        this.row_coordinate = row_coordinate;
    }
    coordinatePointStore parent;
    @Override
    public boolean equals(Object test) {
        if (this == test)
            return true;

        if (test == null || test.getClass() != this.getClass())
            return false;
        coordinatePointStore coordinatePointVariable = (coordinatePointStore) test;
        return (coordinatePointVariable.row_coordinate == this.row_coordinate && coordinatePointVariable.column_coordinate == this.column_coordinate);
    }
    
    @Override
    public int hashCode() {
        return (19 * row_coordinate + 29 * column_coordinate);
    }
}

class coordinatePointComparatorForAStar implements Comparator<coordinatePointStore> {
    @Override
    public int compare(coordinatePointStore coordinate1, coordinatePointStore coordinate2) {
        if ((coordinate1.distance_cost + coordinate1.hueristic_cost) < (coordinate2.distance_cost + coordinate2.hueristic_cost)) {
            return -1;
        } else if ((coordinate1.distance_cost + coordinate1.hueristic_cost) > (coordinate2.distance_cost + coordinate2.hueristic_cost)) {
            return 1;
        }
        return 0;
    }
}

class coordinatePointComparator implements Comparator<coordinatePointStore> {
    @Override
    public int compare(coordinatePointStore coordinate1, coordinatePointStore coordinate2) {
        if (coordinate1.distance_cost < coordinate2.distance_cost) {
            return -1;
        } else if (coordinate1.distance_cost > coordinate2.distance_cost) {
            return 1;
        }
        return 0;
    }
}

public class homework {
    static boolean coordinate_is_in_boundary(int row, int column, int no_columns, int no_rows) {
        return column >= 0 && column < no_columns && row >= 0 && row < no_rows;
    }

    public static void main(String args[]) throws IOException {
        
        int no_rows; int no_columns;
        String algorithm_to_run = "";
        int startRow = 0, startColumn = 0;
        long max_diff = 0;
        int number_of_targets = 0;
        String write_output_to_file = "";

        // taking input from the file
        File file = new File("input.txt");
        Scanner sc = new Scanner(file);

        algorithm_to_run = sc.nextLine();

        String line = sc.nextLine();
        String[] rowColumns = line.split("\\s+");
        no_rows = Integer.parseInt(rowColumns[1]);
        no_columns = Integer.parseInt(rowColumns[0]);

        line = sc.nextLine();
        String[] landingPosition = line.split("\\s+");
        startRow = Integer.parseInt(landingPosition[1]);
        startColumn = Integer.parseInt(landingPosition[0]);

        line = sc.nextLine();
        String ZElevation = line;
        max_diff = Long.parseLong(ZElevation);

        line = sc.nextLine();
        String no_of_target = line;
        number_of_targets = Integer.parseInt(no_of_target);

        List<coordinatePointStore> targets_list = new ArrayList<>();
        for (int i = 0; i < number_of_targets; i++) {
            line = sc.nextLine();
            String[] x = line.split("\\s+");
            int column_element = Integer.parseInt(x[0]);
            int row_element = Integer.parseInt(x[1]);
            targets_list.add(new coordinatePointStore(row_element, column_element));
        }

        int grid[][];
        grid = new int[no_rows][no_columns];

        for (int i = 0; i < no_rows; i++) {
            line = sc.nextLine();

            String[] x = line.split("\\s+");
            
            for (int j = 0; j < no_columns; j++) {
                grid[i][j] = Integer.parseInt(x[j]);
            }

        }
        
        // BFS algorithm
            if( algorithm_to_run.equalsIgnoreCase("BFS") == true)     
    {   
            boolean visited_sites[][] = new boolean[no_rows][no_columns];

            coordinatePointStore start = new coordinatePointStore(startRow, startColumn);
            start.distance_cost = 0;
            int number_of_targets_copy = number_of_targets;
            coordinatePointStore[][] intermediate_point = new coordinatePointStore[no_rows][no_columns];
           
            Queue<coordinatePointStore> coordinatePointQueue = new ArrayDeque<>();

            HashMap<coordinatePointStore, coordinatePointStore> solution_hash_map = new HashMap<>();
            coordinatePointQueue.add(start);
            intermediate_point[start.row_coordinate][start.column_coordinate] = start;
            visited_sites[start.row_coordinate][start.column_coordinate] = true;
            while (!coordinatePointQueue.isEmpty() && number_of_targets_copy > 0) {
                coordinatePointStore current_point = coordinatePointQueue.poll();
 
                if (targets_list.contains(current_point)) {
                    number_of_targets_copy--;
                    for (coordinatePointStore target : targets_list) {
                        if (target.equals(current_point)) {
                            solution_hash_map.put(target, current_point);
                            break;
                        }
                    }
                }
               
                for (int diagonal_row_chng = -1; diagonal_row_chng <= 1; diagonal_row_chng++) {
                    for (int diagonal_column_chng = -1; diagonal_column_chng <= 1; diagonal_column_chng++) {
                        if (diagonal_row_chng == 0 && diagonal_column_chng == 0) {
                            continue;
                        }
                        if (coordinate_is_in_boundary(current_point.row_coordinate + diagonal_row_chng, current_point.column_coordinate + diagonal_column_chng, no_columns, no_rows)
                                && Math.abs(grid[current_point.row_coordinate][current_point.column_coordinate] - grid[current_point.row_coordinate
                                        + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]) <= max_diff) {
                            if (visited_sites[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng] == false) {
                                intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]
                                        = new coordinatePointStore(current_point.row_coordinate + diagonal_row_chng, current_point.column_coordinate + diagonal_column_chng);
                                
                                intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng].parent
                                        = current_point;

                                visited_sites[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng] = true;

                                coordinatePointQueue.add(intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]);
                            }
                        }
                        
                    }
                }

            }
            for (int i = 0 ; i < targets_list.size(); i++) {
                    
                    coordinatePointStore targetPoint = targets_list.get(i);
                    
                    if (solution_hash_map.containsKey(targetPoint)) {
                        coordinatePointStore pathCoordinate = solution_hash_map.get(targetPoint);
                        String path = "";
                      
                                
                        while (pathCoordinate != null) {
                            path = pathCoordinate.column_coordinate + "," + pathCoordinate.row_coordinate + " " + path;
                            pathCoordinate = pathCoordinate.parent;
                        }
                        
                        path = path.substring(0, path.length() - 1);
                        
                        if( i == targets_list.size()-1){
                            write_output_to_file = write_output_to_file + path;
                        }
                            
                        else{    
                        write_output_to_file = write_output_to_file + path + "\r\n";
                        }
                        
                    } else if(i == targets_list.size()-1){
                        write_output_to_file = write_output_to_file + "FAIL";
                    }
                    else {
                        write_output_to_file = write_output_to_file + "FAIL\r\n";
                    }
                    
                }

    }

        // UCS algorithm

       else if (algorithm_to_run.equalsIgnoreCase("UCS") == true) {
            
             boolean visited_sites[][] = new boolean[no_rows][no_columns];

            coordinatePointStore start = new coordinatePointStore(startRow, startColumn);
            start.distance_cost = 0;
            int number_of_targets_copy = number_of_targets;
            coordinatePointStore[][] intermediate_point = new coordinatePointStore[no_rows][no_columns];
           
            PriorityQueue<coordinatePointStore> coordinatePointQueue = new PriorityQueue<>(new coordinatePointComparator());

            HashMap<coordinatePointStore, coordinatePointStore> solution_hash_map = new HashMap<>();
            coordinatePointQueue.add(start);
            intermediate_point[start.row_coordinate][start.column_coordinate] = start;
            visited_sites[start.row_coordinate][start.column_coordinate] = true;
            while (!coordinatePointQueue.isEmpty() && number_of_targets_copy > 0) {
                coordinatePointStore current_point = coordinatePointQueue.poll();
 
                if (targets_list.contains(current_point)) {
                    number_of_targets_copy--;
                    for (coordinatePointStore target : targets_list) {
                        if (target.equals(current_point)) {
                            solution_hash_map.put(target, current_point);
                            break;
                        }
                    }
                }
               
                for (int diagonal_row_chng = -1; diagonal_row_chng <= 1; diagonal_row_chng++) {
                    for (int diagonal_column_chng = -1; diagonal_column_chng <= 1; diagonal_column_chng++) {
                        if (diagonal_row_chng == 0 && diagonal_column_chng == 0) {
                            continue;
                        }
                        if (coordinate_is_in_boundary(current_point.row_coordinate + diagonal_row_chng, current_point.column_coordinate + diagonal_column_chng, no_columns, no_rows)
                                && Math.abs(grid[current_point.row_coordinate][current_point.column_coordinate] - grid[current_point.row_coordinate
                                        + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]) <= max_diff) {
                            if (visited_sites[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng] == false) {
                                intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]
                                        = new coordinatePointStore(current_point.row_coordinate + diagonal_row_chng, current_point.column_coordinate + diagonal_column_chng);
                                
                                intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng].parent
                                        = current_point;

                                visited_sites[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng] = true;

                                if ((diagonal_row_chng == -1 && diagonal_column_chng == -1) || (diagonal_row_chng == -1 && diagonal_column_chng == 1) || (diagonal_row_chng == 1 && diagonal_column_chng == -1)
                                        || (diagonal_row_chng == 1 && diagonal_column_chng == 1)) {
                                    intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng].distance_cost
                                            = current_point.distance_cost + 14;
                                } else {
                                    intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng].distance_cost
                                            = current_point.distance_cost + 10;    
                                }

                                coordinatePointQueue.add(intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]);

                                

                            } else {
                                coordinatePointStore sameCoordinateDifferentPath = new coordinatePointStore(current_point.row_coordinate + diagonal_row_chng, current_point.column_coordinate + diagonal_column_chng);
                                sameCoordinateDifferentPath.parent = current_point;

                                if ((diagonal_row_chng == -1 && diagonal_column_chng == -1) || (diagonal_row_chng == -1 && diagonal_column_chng == 1) || (diagonal_row_chng == 1 && diagonal_column_chng == -1)
                                        || (diagonal_row_chng == 1 && diagonal_column_chng == 1)) {
                                    sameCoordinateDifferentPath.distance_cost = current_point.distance_cost + 14;
                                } else {
                                    sameCoordinateDifferentPath.distance_cost = current_point.distance_cost + 10;    
                                }

                                if (intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng].distance_cost > sameCoordinateDifferentPath.distance_cost) {
                                    coordinatePointQueue.remove(intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]);
                                    intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng] = sameCoordinateDifferentPath;
                                    coordinatePointQueue.add(sameCoordinateDifferentPath);
                                }
                            }
                        }
                        
                    }
                }

            }
            for (int i = 0 ; i < targets_list.size(); i++) {
                    
                    coordinatePointStore targetPoint = targets_list.get(i);
                    
                    if (solution_hash_map.containsKey(targetPoint)) {
                        coordinatePointStore pathCoordinate = solution_hash_map.get(targetPoint);
                        String path = "";
                     
                                
                        while (pathCoordinate != null) {
                            path = pathCoordinate.column_coordinate + "," + pathCoordinate.row_coordinate + " " + path;
                            pathCoordinate = pathCoordinate.parent;
                        }
                        
                        path = path.substring(0, path.length() - 1);
                        
                        if( i == targets_list.size()-1){
                            write_output_to_file = write_output_to_file + path;
                        }
                            
                        else{    
                        write_output_to_file = write_output_to_file + path + "\r\n";
                        }
                        
                    } else if(i == targets_list.size()-1){
                        write_output_to_file = write_output_to_file + "FAIL";
                    }
                    else {
                        write_output_to_file = write_output_to_file + "FAIL\r\n";
                    }
                    
                }
        }
            // A* Algorithm
        else    if (algorithm_to_run.equalsIgnoreCase("A*") == true) {
                PriorityQueue<coordinatePointStore> coordinatePointQueue = new PriorityQueue<>(new coordinatePointComparatorForAStar());
                HashMap<coordinatePointStore, coordinatePointStore> solution_hash_map = new HashMap<>();

                for (int k = 0; k < targets_list.size(); k++) {
                    coordinatePointStore cTarget = targets_list.get(k);
                    coordinatePointStore start = new coordinatePointStore(startRow, startColumn);
                    start.distance_cost = 0;

                    coordinatePointStore[][] intermediate_point = new coordinatePointStore[no_rows][no_columns];
                    boolean visited_sites[][] = new boolean[no_rows][no_columns];
                    coordinatePointQueue.clear();
                    coordinatePointQueue.add(start);
                    intermediate_point[start.row_coordinate][start.column_coordinate] = start;
                    visited_sites[start.row_coordinate][start.column_coordinate] = true;
                    while (!coordinatePointQueue.isEmpty() && number_of_targets > 0) {
                        coordinatePointStore current_point = coordinatePointQueue.poll();
                       
                        if (cTarget.equals(current_point)) {
                            solution_hash_map.put(cTarget, current_point);
                            break;
                        }
                       
                        for (int diagonal_row_chng = -1; diagonal_row_chng <= 1; diagonal_row_chng++) {
                            for (int diagonal_column_chng = -1; diagonal_column_chng <= 1; diagonal_column_chng++) {
                                if (diagonal_row_chng == 0 && diagonal_column_chng == 0) {
                                    continue;
                                }
                                if (coordinate_is_in_boundary(current_point.row_coordinate + diagonal_row_chng, current_point.column_coordinate + diagonal_column_chng, no_columns, no_rows)
                                        && Math.abs(grid[current_point.row_coordinate][current_point.column_coordinate] - grid[current_point.row_coordinate
                                                + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]) <= max_diff) {
                                    if (visited_sites[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng] == false) {
                                        intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]
                                                = new coordinatePointStore(current_point.row_coordinate + diagonal_row_chng, current_point.column_coordinate + diagonal_column_chng);
                         
                                        intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng].parent
                                                = current_point;

                                        visited_sites[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng] = true;

                                        if ((diagonal_row_chng == -1 && diagonal_column_chng == -1) || (diagonal_row_chng == -1 && diagonal_column_chng == 1) || (diagonal_row_chng == 1 && diagonal_column_chng == -1)
                                                || (diagonal_row_chng == 1 && diagonal_column_chng == 1)) {
                                            intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng].distance_cost
                                                    = current_point.distance_cost + Math.abs(grid[current_point.row_coordinate][current_point.column_coordinate]
                                                            - grid[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]) + 14;
                                           
                                        } else {
                                            intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng].distance_cost
                                                    = current_point.distance_cost + Math.abs(grid[current_point.row_coordinate][current_point.column_coordinate]
                                                            - grid[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]) + 10;

                                                
                                        }

                                        intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng].hueristic_cost
                                                = Math.abs(cTarget.row_coordinate - (current_point.row_coordinate + diagonal_row_chng))
                                                + Math.abs(cTarget.column_coordinate - (current_point.column_coordinate + diagonal_column_chng)) + 
                                                Math.abs(grid[cTarget.row_coordinate][cTarget.column_coordinate] - grid[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]);

                                        coordinatePointQueue.add(intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]);

                                    } else if (visited_sites[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng] == true) {
                                        coordinatePointStore sameCoordinateDifferentPath = new coordinatePointStore(current_point.row_coordinate + diagonal_row_chng, current_point.column_coordinate + diagonal_column_chng);
                                        sameCoordinateDifferentPath.parent = current_point;

                                        if ((diagonal_row_chng == -1 && diagonal_column_chng == -1) || (diagonal_row_chng == -1 && diagonal_column_chng == 1) || (diagonal_row_chng == 1 && diagonal_column_chng == -1)
                                                || (diagonal_row_chng == 1 && diagonal_column_chng == 1)) {
                                            sameCoordinateDifferentPath.distance_cost = current_point.distance_cost + Math.abs(grid[current_point.row_coordinate][current_point.column_coordinate]
                                                    - grid[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]) + 14;

                                           
                                        } else {
                                            sameCoordinateDifferentPath.distance_cost = current_point.distance_cost + Math.abs(grid[current_point.row_coordinate][current_point.column_coordinate]
                                                    - grid[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]) + 10;

                                              
                                        }

                                        if (intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng].distance_cost > sameCoordinateDifferentPath.distance_cost) {
                                            if (coordinatePointQueue.contains(sameCoordinateDifferentPath)) {
                                                coordinatePointQueue.remove(intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng]);
                                            }
                                            
                                            sameCoordinateDifferentPath.hueristic_cost = intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng].hueristic_cost;
                                            intermediate_point[current_point.row_coordinate + diagonal_row_chng][current_point.column_coordinate + diagonal_column_chng] = sameCoordinateDifferentPath;
                                            coordinatePointQueue.add(sameCoordinateDifferentPath);

                                        }
                                    }
                                    
                                }
                            }
                        }
                    }
                }
                for (int i = 0 ; i < targets_list.size(); i++) {
                    
                    coordinatePointStore targetPoint = targets_list.get(i);
                    
                    if (solution_hash_map.containsKey(targetPoint)) {
                        coordinatePointStore pathCoordinate = solution_hash_map.get(targetPoint);
                        String path = "";
                      
                                
                        while (pathCoordinate != null) {
                            path = pathCoordinate.column_coordinate + "," + pathCoordinate.row_coordinate + " " + path;
                            pathCoordinate = pathCoordinate.parent;
                        }
                        
                        path = path.substring(0, path.length() - 1);
                        
                        if( i == targets_list.size()-1){
                            write_output_to_file = write_output_to_file + path;
                        }
                            
                        else{    
                        write_output_to_file = write_output_to_file + path + "\r\n";
                        }
                      
                        
                    } else if(i == targets_list.size()-1){
                        write_output_to_file = write_output_to_file + "FAIL";
                    }
                    else {
                        write_output_to_file = write_output_to_file + "FAIL\r\n";
                    }
                    
                }
                
            }
            
        else {
        write_output_to_file = write_output_to_file + "FAIL";
        }

        try ( // writing the output in back in file
                FileWriter new_file_output = new FileWriter("output.txt")) {
            for (int char_i = 0; char_i < write_output_to_file.length(); char_i++) {
                new_file_output.write(write_output_to_file.charAt(char_i));
            }
        }
        }
    }
