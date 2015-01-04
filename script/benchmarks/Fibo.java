public class Fibo {
  private static int fibo(int n) {
    return (n < 2)? 1: fibo(n - 1) + fibo(n - 2);
  }
  public static void main(String[] args) {
    System.out.println(fibo(/*46*/ 45));
  }
}

