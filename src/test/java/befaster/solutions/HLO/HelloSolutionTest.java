package befaster.solutions.HLO;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class HelloSolutionTest {
    private HelloSolution helloSolution;

    @BeforeEach
    public void setUp() {
        helloSolution = new HelloSolution();
    }

    @Test
    public void helloMethodShouldReturnHelloWithTheSentString() {
        final String friendName = "John";

        assertThat(helloSolution.hello(friendName), equalTo("Hello, " + friendName + "!"));
    }

    @Test
    public void helloMethodShouldReturnHelloWithTheSentStringTrimmed() {
        final String friendName = " John ";

        assertThat(helloSolution.hello(friendName), equalTo("Hello, " + friendName.trim() + "!"));
    }
}
