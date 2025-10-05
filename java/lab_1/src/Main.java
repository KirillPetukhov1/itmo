import java.lang.Math;

public class Main {
    public static void main(String[] args) {
        int[] s = new int[19];
        float[] x = new float[12];
        double[][] s1 = new double[10][12];

        for (int i = 0; i < 19; i++) {
            s[i] = 23 - i;
        }

        for (int i = 0; i < 12; i++) {
            x[i] = getRandomInRange(-14.0F, 13.0F);
        }

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 12; j++) {
                s1[i][j] = calcNewElement(s[i], x[j]);
            }
        }

        showMatrix(s1);
    }

    /**
     * Возвращает случайное число в промежутке [min; max)
     */
    public static float getRandomInRange(float min, float max) {
        return (float) (Math.random() * ((max - min) + 1)) + min;
    }


    /**
     * Вычисляет элемент матрицы по переданным элементам массивов s и x
     */
    public static double calcNewElement(int s, float x) {
        if (s == 17) {
            return Math.asin(Math.cos(Math.atan(Math.pow((x - 0.5) / 27, 2))));
        } else if (s == 5 || s == 7 || s == 9 || s == 21 || s == 23) {
            return 2 / (0.25 - Math.pow(Math.cos(x), 3));
        } else {
            return Math.sin(Math.sin(Math.pow(Math.cbrt(x) / (0.5 + Math.cos(x)), 2)));
        }
    }

    /**
     * Выводит на экран матрицу с 3 знаками после запятой
     */
    public static void showMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.printf("%7.3f ", matrix[i][j]);
            }

            {

            }
            System.out.print('\n');
        }
    }
}