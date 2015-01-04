class Gcd {
  static int gcd (int a, int b) {
    while (a != b) {
      if (a > b) {
        a = a - b;
      } else {
        b = b - a;
      }
    }
    return a;
  }

  public static void main(String[] args) {
    int i = 0;
    int sum = 0;
    while(i < 100000) {
      sum = sum + gcd(12, 9);
      i = i + 1;
    }
    System.out.println(sum);
  }
}

