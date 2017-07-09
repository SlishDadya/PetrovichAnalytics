package entartaiment.goshan.petrovichanalytics;


import java.util.UUID;

public class Share {
    private UUID mUUID;
    private String mName;
    private double mPrice;
    private int mNum;

    public Share()
    {
        mUUID=UUID.randomUUID();
    }
    public Share(UUID id)
    {
        mUUID=id;
    }
    public int getNum() {
        return mNum;
    }

    public void setNum(int num) {
        mNum = num;
    }

    public double getPrice() {

        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public String getName() {

        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public UUID getUUID() {

        return mUUID;
    }

    public void setUUID(UUID UUID) {
        mUUID = UUID;
    }
}
