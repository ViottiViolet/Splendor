package Nobles;

public class Noble {
    private String imageName;
    private int diamondCost, sapphireCost, emeraldCost, rubyCost, onyxCost;
    @SuppressWarnings("unused")
    private String nobleName;
    public Noble(String nobleName, int diamondCost, int sapphireCost, int emeraldCost, int rubyCost, int onyxCost, String imageName) {
        this.nobleName = nobleName;
        this.diamondCost = diamondCost;
        this.sapphireCost = sapphireCost;
        this.emeraldCost = emeraldCost;
        this.rubyCost = rubyCost;
        this.onyxCost = onyxCost;
        this.imageName = "src/Images/Nobles/" + imageName + ".png";
    }

    public int getPrestige(){return 3;}

    public int[] getCost(){
        int[] costs = new int[5];
        costs[0] = diamondCost;
        costs[1] = sapphireCost;
        costs[2] = emeraldCost;
        costs[3] = rubyCost;
        costs[4] = onyxCost;
        return costs;
    }

    public String getImage(){
        return imageName;
    }
}
