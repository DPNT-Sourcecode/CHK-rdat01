package befaster.entity;

public class Item {
    private Character sku;
    private Integer price;
    private SpecialOffer specialOffer;

    public Item(Character sku, Integer price) {
        this.sku = sku;
        this.price = price;
    }

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

    public void setSku(Character sku) {
        this.sku = sku;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setSpecialOffer(SpecialOffer specialOffer) {
        this.specialOffer = specialOffer;
    }

    public SpecialOffer getSpecialOffer() {
        return specialOffer;
    }

    public boolean hasSpecialOffer() {
        return getSpecialOffer() == null ? false : true;
    }

    public Integer getFinalPrice(int quantity) {
        if (hasSpecialOffer()) {
            Integer remainder = quantity % specialOffer.getQuantity();
            Integer divisionResult = quantity / specialOffer.getQuantity();

            return remainder * quantity + divisionResult * specialOffer.getFixedPrice();
        }

        return quantity * price;
    }
}



