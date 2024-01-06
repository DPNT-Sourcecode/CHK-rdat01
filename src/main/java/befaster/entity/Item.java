package befaster.entity;

public class Item {
    private Character sku;
    private Integer price;
    private SpecialOffer specialOffer;

    public Item(Character sku, Integer price, SpecialOffer specialOffer) {
        this.sku = sku;
        this.price = price;
        this.specialOffer = specialOffer;
    }

    public Character getSku() {
        return sku;
    }

    public Integer getPrice() {
        return price;
    }

    public SpecialOffer getSpecialOffer() {
        return specialOffer;
    }
}

