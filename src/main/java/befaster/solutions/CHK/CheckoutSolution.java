package befaster.solutions.CHK;

import befaster.entity.Checkout;
import befaster.entity.Item;
import befaster.entity.SpecialOffer;
import befaster.entity.enums.SpecialOfferType;

import java.util.HashMap;

public class CheckoutSolution {
    public Integer checkout(String skus) {
        var checkout = new Checkout();
        var itemsList = new HashMap<Character, Item>();
        var skusAndQuantities = new HashMap<Character, Integer>();
        var basketList = skus.trim().replaceAll("\\p{C}", "").toCharArray();

        createItemsList(itemsList);

        for (char sku : basketList) {
            if(!itemsList.containsKey(sku)){
                return -1;
            }

            int currentQuantity = skusAndQuantities.getOrDefault(sku, 0);
            skusAndQuantities.put(sku, currentQuantity + 1);
        }

        for (var entry : skusAndQuantities.entrySet()) {
            checkout.addItem(itemsList.get(entry.getKey()), entry.getValue());
        }

        var checkoutValue = checkout.calculateCheckoutValue();

        return checkoutValue;
    }

    private void createItemsList(HashMap<Character, Item> itemsList){
        var itemA = new Item('A', 50);
        itemA.AddSpecialOffers(
                new SpecialOffer(3, 130, SpecialOfferType.SPECIAL_PRICE),
                new SpecialOffer(5, 200, SpecialOfferType.SPECIAL_PRICE)
        );

        var itemB = new Item('B', 30);
        itemB.AddSpecialOffers(
                new SpecialOffer(2, 45, SpecialOfferType.SPECIAL_PRICE)
        );

        var itemC = new Item('C', 20);
        var itemD = new Item('D', 15);
        var itemE = new Item('E', 40);
        itemE.AddSpecialOffers(
                new SpecialOffer(2, itemB.getSku(), itemB.getPrice(), SpecialOfferType.DIFFERENT_ITEM_FREE)
        );
        var itemF = new Item('F', 10);
        itemF.AddSpecialOffers(
                new SpecialOffer(2, itemF.getSku(), itemF.getPrice(), SpecialOfferType.SAME_ITEM_FREE)
        );
        var itemG = new Item('G', 20);
        var itemH = new Item('H', 10);
        itemH.AddSpecialOffers(
                new SpecialOffer(5, 45, SpecialOfferType.SPECIAL_PRICE),
                new SpecialOffer(10, 80, SpecialOfferType.SPECIAL_PRICE)
        );
        var itemI = new Item('I', 35);
        var itemJ = new Item('J', 60);
        var itemK = new Item('K', 80);
        itemK.AddSpecialOffers(
                new SpecialOffer(2, 150, SpecialOfferType.SPECIAL_PRICE)
        );
        var itemL = new Item('L', 90);
        var itemM = new Item('M', 15);
        var itemN = new Item('N', 40);
        itemN.AddSpecialOffers(
                new SpecialOffer(3, itemM.getSku(), itemM.getPrice(), SpecialOfferType.DIFFERENT_ITEM_FREE)
        );
        var itemO = new Item('O', 10);
        var itemP = new Item('P', 50);
        itemP.AddSpecialOffers(
                new SpecialOffer(5, 200, SpecialOfferType.SPECIAL_PRICE)
        );
        var itemQ = new Item('Q', 30);
        itemQ.AddSpecialOffers(
                new SpecialOffer(3, 80, SpecialOfferType.SPECIAL_PRICE)
        );
        var itemR = new Item('R', 50);
        itemR.AddSpecialOffers(
                new SpecialOffer(3, itemQ.getSku(), itemQ.getPrice(), SpecialOfferType.DIFFERENT_ITEM_FREE)
        );
        var itemS = new Item('S', 30);
        var itemT = new Item('T', 20);
        var itemU = new Item('U', 40);
        itemU.AddSpecialOffers(
                new SpecialOffer(3, itemU.getSku(), itemU.getPrice(), SpecialOfferType.SAME_ITEM_FREE)
        );
        var itemV = new Item('V', 50);
        itemV.AddSpecialOffers(
                new SpecialOffer(2, 90, SpecialOfferType.SPECIAL_PRICE),
                new SpecialOffer(3, 130, SpecialOfferType.SPECIAL_PRICE)
        );
        var itemW = new Item('W', 20);
        var itemX = new Item('X', 90);
        var itemY = new Item('Y', 10);
        var itemZ = new Item('Z', 50);

        itemsList.put(itemA.getSku(), itemA);
        itemsList.put(itemB.getSku(), itemB);
        itemsList.put(itemC.getSku(), itemC);
        itemsList.put(itemD.getSku(), itemD);
        itemsList.put(itemE.getSku(), itemE);
        itemsList.put(itemF.getSku(), itemF);
        itemsList.put(itemG.getSku(), itemG);
        itemsList.put(itemH.getSku(), itemH);
        itemsList.put(itemI.getSku(), itemI);
        itemsList.put(itemJ.getSku(), itemJ);
        itemsList.put(itemK.getSku(), itemK);
        itemsList.put(itemL.getSku(), itemL);
        itemsList.put(itemM.getSku(), itemM);
        itemsList.put(itemN.getSku(), itemN);
        itemsList.put(itemO.getSku(), itemO);
        itemsList.put(itemP.getSku(), itemP);
        itemsList.put(itemQ.getSku(), itemQ);
        itemsList.put(itemR.getSku(), itemR);
        itemsList.put(itemS.getSku(), itemS);
        itemsList.put(itemT.getSku(), itemT);
        itemsList.put(itemU.getSku(), itemU);
        itemsList.put(itemV.getSku(), itemV);
        itemsList.put(itemW.getSku(), itemW);
        itemsList.put(itemX.getSku(), itemX);
        itemsList.put(itemY.getSku(), itemY);
        itemsList.put(itemZ.getSku(), itemZ);
    }
}
