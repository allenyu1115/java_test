public class TestMain{

/**
   * Split a list into consecutive days
       by date ranges. 
   * 
   * 
   * for example: day1: obj1, obj2, obj3
   *              day2: obj4,
   *              day3: obj5,obj6
   * The objects are split into three groups:
   * day1-day3:obj1,obj4,obj5
   * day1:obj2,obj3
   * day3:obj6
   * 
   * another example:
   * day1:obj1
   * day2:obj2,obj3,obj4
   * day3:obj5,obj6
   * day4:obj7,obj8
   * The objects are split into three groups:
   * day1-day4:obj1,obj2,obj5,obj7
   * day2-day4:obj3,obj6,obj8
   * day2:obj4
   * 
   * @param lst
   * @param dateFunc
   * @return
   */
  
  import java.time.LocalDate;
import java.util.*;

public static List<Map<LocalDate, String>> splitByDays(Map<LocalDate, List<String>> dateToObjects) {
        // Sort the dates chronologically
        List<LocalDate> sortedDates = new ArrayList<>(dateToObjects.keySet());
        Collections.sort(sortedDates);

        // Convert each list to a queue so we can consume them one by one
        Map<LocalDate, Queue<String>> remaining = new LinkedHashMap<>();
        for (LocalDate date : sortedDates) {
            remaining.put(date, new LinkedList<>(dateToObjects.get(date)));
        }

        List<Map<LocalDate, String>> columns = new ArrayList<>();

        while (remaining.values().stream().anyMatch(q -> !q.isEmpty())) {
            Map<LocalDate, String> column = new LinkedHashMap<>();
            for (LocalDate date : sortedDates) {
                Queue<String> queue = remaining.get(date);
                if (!queue.isEmpty()) {
                    column.put(date, queue.poll());
                }
            }
            columns.add(column);
        }

        return columns;
    }

    public static void main(String[] args) {
        // Example 1
        Map<LocalDate, List<String>> data1 = new LinkedHashMap<>();
        data1.put(LocalDate.of(2024, 5, 1), Arrays.asList("obj1", "obj2", "obj3")); // day1
        data1.put(LocalDate.of(2024, 5, 2), Arrays.asList("obj4"));                // day2
        data1.put(LocalDate.of(2024, 5, 3), Arrays.asList("obj5", "obj6"));        // day3

        System.out.println("Example 1:");
        printResult(splitByDays(data1));

        // Example 2
        Map<LocalDate, List<String>> data2 = new LinkedHashMap<>();
        data2.put(LocalDate.of(2024, 5, 1), Arrays.asList("obj1"));                      // day1
        data2.put(LocalDate.of(2024, 5, 2), Arrays.asList("obj2", "obj3", "obj4"));      // day2
        data2.put(LocalDate.of(2024, 5, 3), Arrays.asList("obj5", "obj6"));              // day3
        data2.put(LocalDate.of(2024, 5, 4), Arrays.asList("obj7", "obj8"));              // day4

        System.out.println("\nExample 2:");
        printResult(splitByDays(data2));
    }

    private static void printResult(List<Map<LocalDate, String>> columns) {
        int group = 1;
        for (Map<LocalDate, String> col : columns) {
            StringBuilder sb = new StringBuilder();
            sb.append("Group ").append(group++).append(": ");
            sb.append("{");
            boolean first = true;
            for (Map.Entry<LocalDate, String> entry : col.entrySet()) {
                if (!first) sb.append(", ");
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }
            sb.append("}");
            System.out.println(sb);
        }
    }
}
  

