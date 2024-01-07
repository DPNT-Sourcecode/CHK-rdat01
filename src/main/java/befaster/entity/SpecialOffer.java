package befaster.entity;

import befaster.entity.enums.SpecialOfferType;

public class SpecialOffer {
    private Integer quantity;
    private Integer fixedPrice;
    private SpecialOfferType specialOfferType;
    private char freeItem;

    public SpecialOffer(Integer quantity, Integer fixedPrice, SpecialOfferType specialOfferType) {
        this.quantity = quantity;
        this.fixedPrice = fixedPrice;
        this.specialOfferType = specialOfferType;
    }

    public SpecialOffer(Integer quantity, char freeItem, SpecialOfferType specialOfferType) {
        this.quantity = quantity;
        this.freeItem = freeItem;
        this.specialOfferType = specialOfferType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Integer getFixedPrice() {
        return fixedPrice;
    }

    public SpecialOfferType getSpecialOfferType() {
        return specialOfferType;
    }

    public char getFreeItem() {
        return freeItem;
    }
}


