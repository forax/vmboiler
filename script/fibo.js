function fibo(n) {
  return (n < 2)? 1: fibo(n - 1) + fibo(n - 2)
}

print(fibo(45))

