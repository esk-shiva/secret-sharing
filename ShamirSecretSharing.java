import java.io.FileReader;
import java.math.BigInteger;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ShamirSecretSharing {
    public static void main(String[] args) {
        try {
            // Read the first test case
            JSONObject testCase1 = (JSONObject) new JSONParser().parse(new FileReader("testcase1.json"));
            BigInteger secret1 = findConstantTerm(testCase1);
            
            // Read the second test case
            JSONObject testCase2 = (JSONObject) new JSONParser().parse(new FileReader("testcase2.json"));
            BigInteger secret2 = findConstantTerm(testCase2);
            
            // Output the secrets
            System.out.println("Secret for Test Case 1: " + secret1);
            System.out.println("Secret for Test Case 2: " + secret2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to calculate the constant term using Lagrange interpolation
    public static BigInteger findConstantTerm(JSONObject testCase) {
        // Extract keys and roots
        JSONObject keys = (JSONObject) testCase.get("keys");
        int n = ((Long) keys.get("n")).intValue();
        int k = ((Long) keys.get("k")).intValue();
        
        BigInteger[][] points = new BigInteger[k][2];
        int index = 0;
        
        for (int i = 1; i <= n; i++) {
            String key = String.valueOf(i);
            if (!testCase.containsKey(key)) continue;
            
            JSONObject root = (JSONObject) testCase.get(key);
            int x = i;
            int base = Integer.parseInt((String) root.get("base"));
            BigInteger y = new BigInteger((String) root.get("value"), base);
            
            points[index][0] = BigInteger.valueOf(x); // x value
            points[index][1] = y;                     // decoded y value
            index++;
            
            if (index == k) break;
        }
        
        // Apply Lagrange interpolation to find the constant term
        return lagrangeInterpolation(points, k);
    }

    // Lagrange interpolation formula to calculate the constant term
    public static BigInteger lagrangeInterpolation(BigInteger[][] points, int k) {
        BigInteger constant = BigInteger.ZERO;
        
        for (int i = 0; i < k; i++) {
            BigInteger xi = points[i][0];
            BigInteger yi = points[i][1];
            BigInteger term = yi;
            
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = points[j][0];
                    term = term.multiply(xj.negate()).divide(xi.subtract(xj));
                }
            }
            constant = constant.add(term);
        }
        
        return constant;
    }
}
