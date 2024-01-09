package befaster.entity;

import befaster.entity.enums.SpecialOfferType;

public class SpecialPriceOffer extends SpecialOffer{
    private int specialPrice;
    private SpecialOfferType specialOfferType;

    public SpecialPriceOffer(int specialPrice, int quantity) {
        super(quantity);
        this.specialPrice = specialPrice;
        this.specialOfferType = SpecialOfferType.SPECIAL_PRICE;
    }
}


