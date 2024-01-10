package befaster.entity;

import java.util.ArrayList;
import java.util.List;

public class Item {
    private Character sku;
    private int price;
    private List<SpecialOffer> specialOffers;

    public Item(Character sku, int price) {
        this.sku = sku;
        this.price = price;
        this.specialOffers = new ArrayList<>();
    }

    public Character getSku() {
        return sku;
    }

    public int getPrice() { return price; }

    public void AddSpecialOffers(SpecialOffer... specialOffers) {
        for (var specialOffer : specialOffers) {
            if(specialOffer == null) continue;

            this.specialOffers.add(specialOffer);
        }
    }

    public List<SpecialOffer> filterApplicableSpecialOffers(int quantity) {
        return specialOffers.stream()
                .filter(specialOffer -> quantity >= specialOffer.getQuantity() || isInAGroupDiscountSpecialOffer())
                .toList();
    }

    public boolean isSpecialOfferApplicable(int quantity) {
        return specialOffers.stream()
                .anyMatch(specialOffer -> specialOffer.isSpecialOfferApplicable(quantity))
                || isInAGroupDiscountSpecialOffer();
    }

    public boolean hasFreeItemSpecialOffer(){
        return specialOffers.stream().anyMatch(specialOffer -> specialOffer.isDifferentItemFreeOffer());
    }

    public boolean isInAGroupDiscountSpecialOffer(){
        return specialOffers.stream().anyMatch(specialOffer -> specialOffer.isGroupDiscountOffer());
    }

    public SpecialOffer getGroupDiscountSpecialOffer(){
        return specialOffers.stream().filter(specialOffer -> specialOffer.isGroupDiscountOffer()).findFirst();
    }
}


