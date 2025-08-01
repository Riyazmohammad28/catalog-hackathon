import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class SecretFinderBig {
    public static void main(String[] args) {
        String filePath = "input.json"; // Change path if needed

        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONObject json = new JSONObject(jsonContent);

            int k = json.getJSONObject("keys").getInt("k");

            List<Point> points = new ArrayList<>();
            for (String key : json.keySet()) {
                if (key.equals("keys")) continue;
                int x = Integer.parseInt(key);
                JSONObject pair = json.getJSONObject(key);
                int base = Integer.parseInt(pair.getString("base"));
                String value = pair.getString("value");

                // Use BigInteger to decode large y-values
                BigInteger yBig = new BigInteger(value, base);
                double y = yBig.doubleValue(); // Convert for solving

                points.add(new Point(x, y));
            }

            // Sort and take first k points
            points.sort(Comparator.comparingInt(p -> p.x));
            List<Point> selected = points.subList(0, k);

            // Build matrix A and vector B
            double[][] A = new double[k][k];
            double[] B = new double[k];

            for (int i = 0; i < k; i++) {
                Point p = selected.get(i);
                B[i] = p.y;
                for (int j = 0; j < k; j++) {
                    A[i][j] = Math.pow(p.x, k - j - 1);
                }
            }

            double[] coefficients = gaussianElimination(A, B);
            double c = coefficients[coefficients.length - 1];

            System.out.println("✅ Constant term (c) = " + c);

        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    static double[] gaussianElimination(double[][] A, double[] B) {
        int n = B.length;
        for (int p = 0; p < n; p++) {
            int max = p;
            for (int i = p + 1; i < n; i++) {
                if (Math.abs(A[i][p]) > Math.abs(A[max][p])) {
                    max = i;
                }
            }
            double[] temp = A[p]; A[p] = A[max]; A[max] = temp;
            double t = B[p]; B[p] = B[max]; B[max] = t;

            for (int i = p + 1; i < n; i++) {
                double alpha = A[i][p] / A[p][p];
                B[i] -= alpha * B[p];
                for (int j = p; j < n; j++) {
                    A[i][j] -= alpha * A[p][j];
                }
            }
        }

        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double sum = B[i];
            for (int j = i + 1; j < n; j++) {
                sum -= A[i][j] * x[j];
            }
            x[i] = sum / A[i][i];
        }
        return x;
    }

    static class Point {
        int x;
        double y;
        Point(int x, double y) {
            this.x = x;
            this.y = y;
        }
    }
}
