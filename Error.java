public class Error {

  public void error(String err) {
    System.out.printf(
      "\u001B[31m" +
      "==========[" +
      "\033[0;91m" +
      "%s" +
      "\u001B[31m" +
      "]==========" +
      "\u001B[0m\n",
      err
    );
  }
}
