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
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(115));
    }

    @Test
    public void givenAValidBasketWithASpecialOfferShouldReturnsTotalCheckoutValue() {
        String basket = "AAA";
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(130));
    }

    @Test
    public void givenAnInvalidBasketShouldReturnsNegativeOne() {
        String basket = "X";
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(-1));
    }

    @Test
    public void givenAValidBasketWithOneItemShouldReturnsTotalCheckoutValue() {
        String basket = "B";
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(30));
    }

    @Test
    public void givenAValidBasketWithSpecialOfferAndASingleRepeatedItemShouldReturnsLowestCheckoutValue() {
        String basket = "AAAAAA";
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(250));
    }

    @Test
    public void givenAValidBasketWithFreeItemShouldReturnsPriceOfBasket() {
        String basket = "EE";
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(80));
    }

    @Test
    public void givenAValidBasketWithEightItemsAReturnsPrice() {
        String basket = "AAAAAAAA";
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(330));
    }

    @Test
    public void givenAValidBasketWithNineItemsAReturnsPrice() {
        String basket = "AAAAAAAAA";
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(380));
    }

    @Test
    public void givenAValidBasketContainingAFreeItemReturnsPrice() {
        String basket = "EEB";
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(80));
    }

    @Test
    public void test1() {
        String basket = "EEEEBB";
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(160));
    }

    @Test
    public void test2() {
        String basket = "BEBEEE";
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(160));
    }

    @Test
    public void test3() {
        String basket = "ABCDEABCDE";
        checkoutSolution = new CheckoutSolution();
        assertThat(checkoutSolution.checkout(basket), equalTo(280));
    }
}

