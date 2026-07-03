public class Computer {

    private String CPU;
    private int RAM;
    private int Storage;
    private String GraphicsCard;

    // Private constructor
    private Computer(Builder builder) {
        this.CPU = builder.CPU;
        this.RAM = builder.RAM;
        this.Storage = builder.Storage;
        this.GraphicsCard = builder.GraphicsCard;
    }

    public void display() {
        System.out.println("Computer Configuration:");
        System.out.println("CPU: " + CPU);
        System.out.println("RAM: " + RAM + " GB");
        System.out.println("Storage: " + Storage + " GB");
        System.out.println("Graphics Card: " + GraphicsCard);
        System.out.println();
    }

    // Static Nested Builder Class
    public static class Builder {

        private String CPU;
        private int RAM;
        private int Storage;
        private String GraphicsCard;

        public Builder setCPU(String CPU) {
            this.CPU = CPU;
            return this;
        }

        public Builder setRAM(int RAM) {
            this.RAM = RAM;
            return this;
        }

        public Builder setStorage(int Storage) {
            this.Storage = Storage;
            return this;
        }

        public Builder setGraphicsCard(String GraphicsCard) {
            this.GraphicsCard = GraphicsCard;
            return this;
        }

        public Computer build() {
            return new Computer(this);
        }
    }
}