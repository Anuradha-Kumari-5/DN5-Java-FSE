public class BuilderPatternTest {

    public static void main(String[] args) {

        Computer gamingPC = new Computer.Builder()
                .setCPU("Intel i9")
                .setRAM(32)
                .setStorage(1000)
                .setGraphicsCard("NVIDIA RTX 4080")
                .build();

        Computer officePC = new Computer.Builder()
                .setCPU("Intel i5")
                .setRAM(16)
                .setStorage(512)
                .setGraphicsCard("Integrated Graphics")
                .build();

        gamingPC.display();
        officePC.display();
    }
}