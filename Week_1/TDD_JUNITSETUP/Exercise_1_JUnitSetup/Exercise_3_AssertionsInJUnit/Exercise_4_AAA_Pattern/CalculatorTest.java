import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {

    private Calculator calculator;

    // Setup Method
    @Before
    public void setUp() {
        calculator = new Calculator();
        System.out.println("Setup: Calculator object created.");
    }

    // Test using AAA Pattern
    @Test
    public void testAddition() {

        // Arrange
        int a = 10;
        int b = 5;

        // Act
        int result = calculator.add(a, b);

        // Assert
        assertEquals(15, result);
    }

    @Test
    public void testSubtraction() {

        // Arrange
        int a = 10;
        int b = 5;

        // Act
        int result = calculator.subtract(a, b);

        // Assert
        assertEquals(5, result);
    }

    // Teardown Method
    @After
    public void tearDown() {
        calculator = null;
        System.out.println("Teardown: Calculator object destroyed.");
    }
}