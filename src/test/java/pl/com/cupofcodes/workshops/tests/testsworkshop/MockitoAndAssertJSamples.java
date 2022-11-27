package pl.com.cupofcodes.workshops.tests.testsworkshop;

import org.mockito.ArgumentCaptor;
import pl.com.cupofcodes.workshops.tests.testsworkshop.loan.esception.LoanAlreadyStartedCantModifyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.calls;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MockitoAndAssertJSamples {
    //Mocking
    SomeClass someClass = mock(SomeClass.class);

    void methodWithSomeSamples() {

        //Assertion
        assertThat(1).isEqualTo(1);

        assertThat(1).isNotNull();

        //assertion checking Exception occurrence
        assertThatExceptionOfType(LoanAlreadyStartedCantModifyException.class)
                .isThrownBy(() -> sampleMethod());

        //assertion that no exception thrown
        assertThatNoException().isThrownBy(() -> sampleMethod());


        //Stubbing values
        when(someClass.sampleMethod()).thenReturn("Some expected text to be returned");

        //Stubbing and ignoring values of method
        when(someClass.sampleMethodWithArguments(any(), anyLong())).thenReturn("Some expected text to be returned");

        //Stubbing values of method invocation with concrete arguments
        when(someClass.sampleMethodWithArguments("expected argument", 2L)).thenReturn("Some expected text to be returned when argument matched");
        when(someClass.sampleMethodWithArguments("another expected argument", 5L)).thenReturn("Different text when argument matched");


        //Method invocation verification on Mock ( Mock assertions )
        verify(someClass, only()).sampleMethod(); // only once
        verify(someClass, calls(3)).sampleMethod(); // 3 times
        verify(someClass, never()).sampleMethod(); // never. there are few more atLeast atMost atMostOnce atLeastOnce...

        //Argument capture with invocation verification on Mock
        ArgumentCaptor<String> SomeClassArgumentCaptor = ArgumentCaptor
                .forClass(String.class);

        verify(someClass).sampleMethodWithArgument(SomeClassArgumentCaptor.capture());
        assertThat(SomeClassArgumentCaptor.getValue()).isEqualTo("expected value"); // we can capture also our own object


    }


    void sampleMethod() {

    }

    static class SomeClass {
        String sampleMethod() {
            return "test";
        }

        String sampleMethodWithArgument(String text) {
            return text;
        }

        String sampleMethodWithArguments(String text, Long aLong) {
            return "test";
        }
    }

}
