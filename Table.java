import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Table {

  public static void table(String[][] table) {
    boolean leftJustifiedRows = false;

    Map<Integer, Integer> columnLengths = new HashMap<>();
    Arrays
      .stream(table)
      .forEach(
        a ->
          Stream
            .iterate(0, (i -> i < a.length), (i -> ++i))
            .forEach(
              i -> {
                if (columnLengths.get(i) == null) {
                  columnLengths.put(i, 0);
                }
                if (columnLengths.get(i) < a[i].length()) {
                  columnLengths.put(i, a[i].length());
                }
              }
            )
      );

    final StringBuilder formatString = new StringBuilder("");
    String flag = leftJustifiedRows ? "-" : "";
    columnLengths
      .entrySet()
      .stream()
      .forEach(e -> formatString.append("| %" + flag + e.getValue() + "s "));
    formatString.append("|\n");

    Stream
      .iterate(0, (i -> i < table.length), (i -> ++i))
      .forEach(a -> System.out.printf(formatString.toString(), table[a]));
  }
}
