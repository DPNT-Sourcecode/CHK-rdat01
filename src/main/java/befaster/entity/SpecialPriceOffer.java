package befaster.entity;

import befaster.entity.enums.SpecialOfferType;

public class SpecialPriceOffer extends SpecialOffer{
    public SpecialPriceOffer(int fixedPrice, int quantity, SpecialOfferType specialOfferType) {
        super(quantity, fixedPrice, specialOfferType);
    }
}
