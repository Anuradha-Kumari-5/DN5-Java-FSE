# Exercise 7: Financial Forecasting

## 1. What is Recursion?

Recursion is a programming technique where a method calls itself to solve a problem. Each recursive call solves a smaller part of the original problem until it reaches a base case.

---

## 2. Recursive Algorithm

The future value is calculated using the formula:

Future Value = Current Value × (1 + Growth Rate)

The recursive function repeats this calculation until the required number of years becomes zero.

---

## 3. Time Complexity

The recursive method makes one recursive call for each year.

Time Complexity: **O(n)**

Space Complexity: **O(n)** because of the recursive call stack.

---

## 4. Optimization

The recursive solution can be optimized by:

- Using an iterative approach (loop) to avoid recursive call overhead.
- Using Dynamic Programming (Memoization) if repeated calculations are involved.
- Using the direct mathematical formula:

Future Value = Present Value × (1 + Growth Rate)^Years

This computes the result in constant time using built-in power functions.
