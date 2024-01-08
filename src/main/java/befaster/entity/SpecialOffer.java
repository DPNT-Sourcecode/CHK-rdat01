package befaster.entity;

import befaster.entity.enums.SpecialOfferType;

public class SpecialOffer {
    private int quantity;
    private int price;
    private SpecialOfferType specialOfferType;
    private char freeItemSKU;

    public SpecialOffer(int quantity, int fixedPrice, SpecialOfferType specialOfferType) {
        this.quantity = quantity;
        this.price = fixedPrice;
        this.specialOfferType = specialOfferType;
    }

    public SpecialOffer(
            int quantity,
            char freeItemSKU,
            int freeItemPrice,
            SpecialOfferType specialOfferType) {
        this.quantity = quantity;
        this.freeItemSKU = freeItemSKU;
        this.price = freeItemPrice;
        this.specialOfferType = specialOfferType;
    }

    public int getQuantity() { return quantity; }

    public int getPrice() { return price; }

    public boolean isSpecialOfferApplicable(int quantity){
        return quantity >= getQuantity();
    }

    public boolean isSpecialPriceOffer() { return specialOfferType.equals(SpecialOfferType.SPECIAL_PRICE); }

    public boolean isFreeItemOffer() { return specialOfferType.equals(SpecialOfferType.FREE_ITEM); }
}
