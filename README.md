# Catalog Hackathon – Secret Finder

This Java program solves a polynomial decoding problem by:
- Reading x, y point pairs from a JSON file
- Decoding base-encoded y-values
- Solving a system of equations to extract the **constant term c** in a polynomial

## Input Format
A `input.json` file with the following structure:
```json
{
  "keys": { "n": 10, "k": 7 },
  ...
}
