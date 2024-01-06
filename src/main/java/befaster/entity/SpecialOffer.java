package befaster.entity;

public class SpecialOffer {
    private Integer quantity;
    private Integer fixedPrice;

    public SpecialOffer(Character sku, Integer quantity, Integer fixedPrice) {
        this.sku = sku;
        this.quantity = quantity;
        this.fixedPrice = fixedPrice;
    }

    public Character getSku() {
        return sku;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getFixedPrice() {
        return fixedPrice;
    }
}

