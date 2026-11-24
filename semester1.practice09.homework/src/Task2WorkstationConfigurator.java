package semester1.practice09.homework;

import java.util.Arrays;

class Motherboard {
    private final String model;
    private final String socketType;
    private final int maxExpansionSlots;

    public Motherboard(String model, String socketType, int maxExpansionSlots) {
        this.model = model;
        this.socketType = socketType;
        this.maxExpansionSlots = (maxExpansionSlots > 0 && maxExpansionSlots <= 8) ? maxExpansionSlots : 4;
    }

    public String getModel() { return model; }
    public String getSocketType() { return socketType; }
    public int getMaxExpansionSlots() { return maxExpansionSlots; }

    public boolean isSocketCompatible(String cpuSocket) {
        if (cpuSocket == null) return false;
        return this.socketType.equalsIgnoreCase(cpuSocket);
    }
}

class PowerSupplyUnit {
    private final String model;
    private final int maxOutputWatts;
    private final double efficiencyCoefficient;

    public PowerSupplyUnit(String model, int maxOutputWatts, double efficiencyCoefficient) {
        this.model = model;
        this.maxOutputWatts = maxOutputWatts;
        if (efficiencyCoefficient < 0.80) this.efficiencyCoefficient = 0.80;
        else if (efficiencyCoefficient > 0.96) this.efficiencyCoefficient = 0.96;
        else this.efficiencyCoefficient = efficiencyCoefficient;
    }

    public int getSafeContinuousWatts() {
        return (int) (maxOutputWatts * 0.85);
    }

    public double calculateWallPowerDraw(int internalLoadWatts) {
        return internalLoadWatts / efficiencyCoefficient;
    }

    public String getModel() { return model; }
    public double getEfficiencyCoefficient() { return efficiencyCoefficient; }
}

abstract class AbstractHardwareComponent {
    private final String modelName;
    private final double priceUah;

    public AbstractHardwareComponent(String modelName, double priceUah) {
        this.modelName = modelName;
        this.priceUah = (priceUah >= 0) ? priceUah : 0.0;
    }

    public String getModelName() { return modelName; }
    public double getPriceUah() { return priceUah; }

    public abstract int getPowerWatts();
    public abstract double getBenchmarkScore();
    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format("[%s] %s (%d Вт | Score: %.1f)",
                getCategory(), modelName, getPowerWatts(), getBenchmarkScore());
    }
}

class ProcessorUnit extends AbstractHardwareComponent {
    private final String socket;
    private final int cores;
    private final double boostClockGhz;
    private final int tdpWatts;

    public ProcessorUnit(String modelName, double priceUah, String socket, int cores, double boostClockGhz, int tdpWatts) {
        super(modelName, priceUah);
        this.socket = socket;
        this.cores = (cores > 0) ? cores : 1;
        this.boostClockGhz = (boostClockGhz > 0) ? boostClockGhz : 1.0;
        this.tdpWatts = (tdpWatts > 0) ? tdpWatts : 10;
    }

    public String getSocket() { return socket; }

    @Override
    public int getPowerWatts() { return tdpWatts; }

    @Override
    public double getBenchmarkScore() {
        return cores * boostClockGhz * 120.0;
    }

    @Override
    public String getCategory() { return "CPU"; }
}

class GraphicsAccelerator extends AbstractHardwareComponent {
    private final int vramGb;
    private final int tensorCores;
    private final int boardPowerWatts;

    public GraphicsAccelerator(String modelName, double priceUah, int vramGb, int tensorCores, int boardPowerWatts) {
        super(modelName, priceUah);
        this.vramGb = (vramGb > 0) ? vramGb : 1;
        this.tensorCores = (tensorCores >= 0) ? tensorCores : 0;
        this.boardPowerWatts = (boardPowerWatts > 0) ? boardPowerWatts : 10;
    }

    @Override
    public int getPowerWatts() { return boardPowerWatts; }

    @Override
    public double getBenchmarkScore() {
        return vramGb * 250.0 + tensorCores * 1.5;
    }

    @Override
    public String getCategory() { return "GPU"; }
}

class NvmeSsdDrive extends AbstractHardwareComponent {
    private final int capacityGb;
    private final int readSpeedMbSec;
    private final int[] speedSamples;

    public NvmeSsdDrive(String modelName, double priceUah, int capacityGb, int readSpeedMbSec, int[] speedSamples) {
        super(modelName, priceUah);
        this.capacityGb = (capacityGb > 0) ? capacityGb : 120;
        this.readSpeedMbSec = (readSpeedMbSec > 0) ? readSpeedMbSec : 1000;
        if (speedSamples != null) {
            this.speedSamples = Arrays.copyOf(speedSamples, speedSamples.length);
        } else {
            this.speedSamples = new int[0];
        }
    }

    public int[] getSpeedSamples() {
        return Arrays.copyOf(speedSamples, speedSamples.length);
    }

    @Override
    public int getPowerWatts() { return 12; }

    @Override
    public double getBenchmarkScore() {
        return readSpeedMbSec * 0.25;
    }

    @Override
    public String getCategory() { return "NVMe SSD"; }
}

class WorkstationPC {
    private final String buildName;
    private final Motherboard motherboard;
    private final PowerSupplyUnit psu;
    private final AbstractHardwareComponent[] components;
    private int componentCount;

    public WorkstationPC(String buildName, Motherboard motherboard, PowerSupplyUnit psu) {
        this.buildName = buildName;
        this.motherboard = motherboard;
        this.psu = psu;
        this.components = new AbstractHardwareComponent[motherboard.getMaxExpansionSlots()];
        this.componentCount = 0;
    }

    public boolean installComponent(AbstractHardwareComponent comp) {
        if (comp == null) return false;

        if (componentCount >= components.length) {
            System.out.println("❌ Відмова: Немає вільних слотів розширення на материнській платі!");
            return false;
        }

        if (comp instanceof ProcessorUnit) {
            ProcessorUnit cpu = (ProcessorUnit) comp;
            if (!motherboard.isSocketCompatible(cpu.getSocket())) {
                System.out.printf("❌ Відмова: Процесор %s (%s) несумісний із платою %s (Сокет %s)!\n",
                        cpu.getModelName(), cpu.getSocket(), motherboard.getModel(), motherboard.getSocketType());
                return false;
            }
        }

        if (getTotalPowerWatts() + comp.getPowerWatts() > psu.getSafeContinuousWatts()) {
            System.out.printf("❌ Відмова: Встановлення %s заблоковано через ризик перевантаження БЖ!\n", comp.getModelName());
            return false;
        }

        components[componentCount++] = comp;
        System.out.println("✅ Встановлено: " + comp);
        return true;
    }

    public int getTotalPowerWatts() {
        int total = 0;
        for (int i = 0; i < componentCount; i++) {
            total += components[i].getPowerWatts();
        }
        return total;
    }

    public double getTotalSystemScore() {
        double total = 0;
        for (int i = 0; i < componentCount; i++) {
            total += components[i].getBenchmarkScore();
        }
        return total;
    }

    public double getTotalBuildPrice() {
        double total = 0;
        for (int i = 0; i < componentCount; i++) {
            total += components[i].getPriceUah();
        }
        return total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("[ПАСПОРТ ЗБІРКИ '%s']:\n", buildName));
        sb.append(String.format("  Материнська плата: %s (Socket: %s, Слотів: %d/%d)\n",
                motherboard.getModel(), motherboard.getSocketType(), componentCount, motherboard.getMaxExpansionSlots()));
        sb.append(String.format("  Блок живлення:     %s (Безпечний ліміт 85%%: %d Вт, ККД: %.0f%%)\n",
                psu.getModel(), psu.getSafeContinuousWatts(), psu.getEfficiencyCoefficient() * 100));

        int internalWatts = getTotalPowerWatts();
        double wallWatts = psu.calculateWallPowerDraw(internalWatts);
        sb.append(String.format("  Внутрішнє споживання: %d Вт (З розетки з урахуванням ККД: %.2f Вт) -> СТАБІЛЬНО!\n",
                internalWatts, wallWatts));
        sb.append(String.format("  Сумарний індекс продуктивності: %.2f балів\n", getTotalSystemScore()));
        sb.append(String.format("  Загальна вартість конфігурації: %.2f грн", getTotalBuildPrice()));
        return sb.toString();
    }
}

public class Task2WorkstationConfigurator {
    public static void main(String[] args) {
        System.out.println("================ КОНФІГУРАТОР РОБОЧОЇ СТАНЦІЇ (КОМПОЗИЦІЯ) ================");

        Motherboard mb = new Motherboard("ASUS ROG B650", "AM5", 4);
        PowerSupplyUnit psu = new PowerSupplyUnit("Seasonics 750W Gold", 750, 0.90);
        WorkstationPC pc = new WorkstationPC("DeepLearning-Rig", mb, psu);

        System.out.println("[ПЕРЕВІРКА СУМІСНОСТІ]:");

        ProcessorUnit intelCpu = new ProcessorUnit("Intel Core i9", 24000, "LGA1700", 24, 5.8, 150);
        pc.installComponent(intelCpu);

        ProcessorUnit amdCpu = new ProcessorUnit("AMD Ryzen 9 7950X", 22500, "AM5", 16, 5.7, 170);
        pc.installComponent(amdCpu);

        GraphicsAccelerator gpu = new GraphicsAccelerator("NVIDIA RTX 4080 Super", 48000, 16, 320, 320);
        pc.installComponent(gpu);

        int[] samples = {7000, 7100, 6950};
        NvmeSsdDrive ssd = new NvmeSsdDrive("Samsung 990 Pro 2TB", 8400, 2000, 7450, samples);
        samples[0] = 99999;
        pc.installComponent(ssd);

        GraphicsAccelerator secondaryGpu = new GraphicsAccelerator("NVIDIA RTX 4080 Super (Втора)", 48000, 16, 320, 320);
        pc.installComponent(secondaryGpu);

        System.out.println();
        System.out.println(pc);
        System.out.println("===========================================================================");
    }
}
