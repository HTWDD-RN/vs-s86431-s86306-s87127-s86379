public class MandelbrotModel
{
    private int iter(double a, double b) {
    int MAX_ITERATIONS = 1000;

       
        double x = 0.0;
        double y = 0.0;
        int iterations = 0;
        
        do {
            double xnew = x * x - y * y + a;
            double ynew = 2 * x * y + b;
            x = xnew;
            y = ynew;
            iterations++;
            if (iterations == MAX_ITERATIONS)
                return MAX_ITERATIONS;
            } while (x <= 2 && y <= 2);
                return iterations;
    }

}
