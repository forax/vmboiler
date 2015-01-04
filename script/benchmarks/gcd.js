function gcd (a, b) {
  while (a != b) {
    if (a > b) {
      a = a - b
    } else {
      b = b - a
    }
  }
  return a;
}

function main() {
  i = 0
  sum = 0
  while(i < 100000) {
    sum = sum + gcd(12, 9)
    i = i + 1
  }
  print(sum)
}
  
main()


