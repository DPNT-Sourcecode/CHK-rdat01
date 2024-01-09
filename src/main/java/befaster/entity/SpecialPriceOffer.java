package befaster.entity;

import befaster.entity.enums.SpecialOfferType;

public class SpecialPriceOffer extends SpecialOffer{
    private int specialPrice;

    public SpecialPriceOffer(int specialPrice, int quantity) {
        super(quantity);
        this.specialPrice = specialPrice;
    }
}

