import java.util.Arrays;
import java.util.Comparator;

public class SearchTest {

    // Linear Search
    public static Product linearSearch(Product[] products, int id) {

        for (int i = 0; i < products.length; i++) {

            if (products[i].productId == id) {
                return products[i];
            }

        }

        return null;
    }

    // Binary Search
    public static Product binarySearch(Product[] products, int id) {

        int left = 0;
        int right = products.length - 1;

        while (left <= right) {

            int mid = (left + right) / 2;

            if (products[mid].productId == id) {
                return products[mid];
            }

            if (products[mid].productId < id) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }

        }

        return null;
    }

    public static void main(String[] args) {

        Product[] products = {
                new Product(103, "Laptop", "Electronics"),
                new Product(101, "Shoes", "Fashion"),
                new Product(104, "Watch", "Accessories"),
                new Product(102, "Phone", "Electronics")
        };

        System.out.println("Linear Search:");

        Product result1 = linearSearch(products, 102);

        if (result1 != null) {
            result1.display();
        } else {
            System.out.println("Product not found");
        }

        // Sorting the array before Binary Search
        Arrays.sort(products, new Comparator<Product>() {

            @Override
            public int compare(Product p1, Product p2) {
                return p1.productId - p2.productId;
            }

        });

        System.out.println("\nBinary Search:");

        Product result2 = binarySearch(products, 102);

        if (result2 != null) {
            result2.display();
        } else {
            System.out.println("Product not found");
        }
    }
}