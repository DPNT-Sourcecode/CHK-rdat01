package befaster.entity;

public class SpecialOffer {
    private Integer quantity;
    private Integer fixedPrice;

    public SpecialOffer(Integer quantity, Integer fixedPrice) {
        this.quantity = quantity;
        this.fixedPrice = fixedPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getFixedPrice() {
        return fixedPrice;
    }
}


