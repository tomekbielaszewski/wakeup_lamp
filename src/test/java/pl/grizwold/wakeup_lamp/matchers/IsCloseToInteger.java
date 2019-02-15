package pl.grizwold.wakeup_lamp.matchers;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsCloseToInteger extends TypeSafeMatcher<Integer> {
    private final int delta;
    private final int value;

    @Factory
    public static Matcher<Integer> closeTo(int operand, int error) {
        return new IsCloseToInteger(operand, error);
    }

    IsCloseToInteger(int value, int error) {
        this.delta = error;
        this.value = value;
    }

    @Override
    protected boolean matchesSafely(Integer item) {
        return actualDelta(item) <= 0.0;
    }

    @Override
    public void describeMismatchSafely(Integer item, Description mismatchDescription) {
        mismatchDescription.appendValue(item)
                .appendText(" differed by ")
                .appendValue(actualDelta(item));
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a numeric value within ")
                .appendValue(delta)
                .appendText(" of ")
                .appendValue(value);
    }

    private double actualDelta(Integer item) {
        return (Math.abs((item - value)) - delta);
    }
}
