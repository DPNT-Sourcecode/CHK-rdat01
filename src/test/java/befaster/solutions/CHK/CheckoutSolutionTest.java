package befaster.solutions.CHK;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CheckoutSolutionTest {
    private CheckoutSolution checkoutSolution;

    @BeforeEach
    public void setUp() {
        checkoutSolution = new CheckoutSolution();
    }

    @Test
    public void givenAValidBasketShouldReturnsTotalCheckoutValue() {
        String basket = " ABCD";

        assertThat(checkoutSolution.checkout(basket), equalTo(115));
    }

    @Test
    public void givenAValidBasketWithASpecialOfferShouldReturnsTotalCheckoutValue() {
        String basket = "AAA";

        assertThat(checkoutSolution.checkout(basket), equalTo(130));
    }

    @Test
    public void givenAnInvalidBasketShouldReturnNegativeOne() {
        String basket = "999";

        assertThat(checkoutSolution.checkout(basket), equalTo(-1));
    }

    @Test
    public void givenAValidBasketWithOneItemShouldReturnTotalCheckoutValue() {
        String basket = "B";

        assertThat(checkoutSolution.checkout(basket), equalTo(30));
    }

    @Test
    public void givenAValidBasketWithSpecialOfferAndASingleRepeatedItemShouldReturnLowestCheckoutValue() {
        String basket = "AAAAAA";

        assertThat(checkoutSolution.checkout(basket), equalTo(250));
    }

    @Test
    public void givenAValidBasketWithFreeItemShouldReturnPriceOfBasket() {
        String basket = "EE";

        assertThat(checkoutSolution.checkout(basket), equalTo(80));
    }

    @Test
    public void givenAValidBasketWithEightItemsAReturnsPrice() {
        String basket = "AAAAAAAA";

        assertThat(checkoutSolution.checkout(basket), equalTo(330));
    }

    @Test
    public void givenAValidBasketWithNineItemsAReturnsPrice() {
        String basket = "AAAAAAAAA";

        assertThat(checkoutSolution.checkout(basket), equalTo(380));
    }

    @Test
    public void givenAValidBasketContainingAFreeItemReturnsPrice() {
        String basket = "EEB";

        assertThat(checkoutSolution.checkout(basket), equalTo(80));
    }

    @Test
    public void givenAValidBasketWithEEEEBBShouldReturn160() {
        String basket = "EEEEBB";

        assertThat(checkoutSolution.checkout(basket), equalTo(160));
    }

    @Test
    public void givenAValidBasketWithBEBEEEShouldReturn160() {
        String basket = "BEBEEE";

        assertThat(checkoutSolution.checkout(basket), equalTo(160));
    }

    @Test
    public void givenAValidBasketWithABCDEABCDEShouldReturn280() {
        String basket = "ABCDEABCDE";

        assertThat(checkoutSolution.checkout(basket), equalTo(280));
    }

    @Test
    public void givenAValidBasketWithCCADDEEBBAShouldReturn280() {
        String basket = "CCADDEEBBA";

        assertThat(checkoutSolution.checkout(basket), equalTo(280));
    }

    @Test
    public void givenAValidBasketWithFFShouldReturn20() {
        String basket = "FF";

        assertThat(checkoutSolution.checkout(basket), equalTo(20));
    }

    @Test
    public void givenAValidBasketWithFFFShouldReturn20() {
        String basket = "FFF";

        assertThat(checkoutSolution.checkout(basket), equalTo(20));
    }

    @Test
    public void givenAValidBasketWithFFFFShouldReturn30() {
        String basket = "FFFF";

        assertThat(checkoutSolution.checkout(basket), equalTo(30));
    }

    @Test
    public void givenAValidBasketWithFFFFFFShouldReturn40() {
        String basket = "FFFFFF";

        assertThat(checkoutSolution.checkout(basket), equalTo(40));
    }

    @Test
    public void givenAValidBasketWithUUUShouldReturn120() {
        String basket = "UUU";

        assertThat(checkoutSolution.checkout(basket), equalTo(120));
    }

    @Test
    public void givenAValidBasketWithUUUUShouldReturn120() {
        String basket = "UUUU";

        assertThat(checkoutSolution.checkout(basket), equalTo(120));
    }

    @Test
    public void givenAValidBasketWithUUUUUShouldReturn160() {
        String basket = "UUUUU";

        assertThat(checkoutSolution.checkout(basket), equalTo(160));
    }

    @Test
    public void givenAValidBasketWithSTXShouldReturn45() {
        String basket = "STX";

        assertThat(checkoutSolution.checkout(basket), equalTo(45));
    }

    @Test
    public void givenAValidBasketWithSTXSTXShouldReturn90() {
        String basket = "STXSTX";

        assertThat(checkoutSolution.checkout(basket), equalTo(90));
    }

    @Test
    public void givenAValidBasketWithSSSZShouldReturn65() {
        String basket = "SSSZ";

        assertThat(checkoutSolution.checkout(basket), equalTo(65));
    }

    @Test
    public void givenAValidBasketWithSShouldReturn20() {
        String basket = "S";

        assertThat(checkoutSolution.checkout(basket), equalTo(20));
    }

    @Test
    public void givenAValidBasketWithXShouldReturn17() {
        String basket = "X";

        assertThat(checkoutSolution.checkout(basket), equalTo(17));
    }

    @Test
    public void givenAValidBasketWithSTXSShouldReturn62() {
        String basket = "STXS";

        assertThat(checkoutSolution.checkout(basket), equalTo(62));
    }

    @Test
    public void givenAValidBasketWithSTXZShouldReturn62() {
        String basket = "STXZ";

        assertThat(checkoutSolution.checkout(basket), equalTo(62));
    }

    @Test
    public void givenAValidBasketWithABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZShouldReturn1602() {
        String basket = "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ";

        assertThat(checkoutSolution.checkout(basket), equalTo(1602));
    }
}

