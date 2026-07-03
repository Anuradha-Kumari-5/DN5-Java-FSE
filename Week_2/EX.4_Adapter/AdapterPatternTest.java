public class AdapterPatternTest {

    public static void main(String[] args) {

        PaymentProcessor paypal = new PayPalAdapter();
        paypal.processPayment(1000);

        PaymentProcessor stripe = new StripeAdapter();
        stripe.processPayment(2500);
    }
}
