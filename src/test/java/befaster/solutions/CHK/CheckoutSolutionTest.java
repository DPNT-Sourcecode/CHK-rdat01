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
    public void givenAValidBasketShouldReturnTotalCheckoutValue() {
        String basket = " ABCD";

        assertThat(checkoutSolution.checkout(basket), equalTo(115));
    }

    @Test
    public void givenAValidBasketWithASpecialOfferShouldReturnTotalCheckoutValue() {
        String basket = "AAA";

        assertThat(checkoutSolution.checkout(basket), equalTo(130));
    }

    @Test
    public void givenAnInvalidBasketShouldReturnNegativeOne() {
        String basket = "X";

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
    public void test1() {
        String basket = "AAAAAAAA";

        assertThat(checkoutSolution.checkout(basket), equalTo(330));
    }

    @Test
    public void test2() {
        String basket = "AAAAAAAAA";

        assertThat(checkoutSolution.checkout(basket), equalTo(380));
    }

    @Test
    public void test3() {
        String basket = "EEB";

        assertThat(checkoutSolution.checkout(basket), equalTo(80));
    }
}

