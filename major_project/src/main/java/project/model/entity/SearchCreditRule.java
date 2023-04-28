package project.model.entity;
/**
 * The Search credit rule with the value range of credit limits
 * Serve as a record with attributes minimum, maximum and consumption per search
 */
public class SearchCreditRule implements Entity{
    private int min;
    private int max;
    private int consumptionPerTime;

    public SearchCreditRule(int min, int max, int consumptionPerTime) {
        this.min = min;
        this.max = max;
        this.consumptionPerTime = consumptionPerTime;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getConsumptionPerTime() {
        return consumptionPerTime;
    }

    @Override
    public String getEntityInfo() {
        String info = "The setting credit should be between " + min + " and " + max
                + "\nEach tag search would consume " + consumptionPerTime + " credit.";
        return info;
    }
}
