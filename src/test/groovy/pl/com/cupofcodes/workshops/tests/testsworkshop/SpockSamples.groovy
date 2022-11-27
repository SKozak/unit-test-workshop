package pl.com.cupofcodes.workshops.tests.testsworkshop


import spock.lang.Ignore
import spock.lang.Specification

@Ignore
class SpockSamples extends Specification {

    SomeClass someClass = Mock(SomeClass) // or Stub(SomeClass) if we only wont to return values, not verify occurrence

    /*
    def "examples"() {

          //assertion checking Exception occurrence
          thrown(LoanAlreadyStartedCantModifyException)

          //assertion that no exception thrown
          noExceptionThrown()

          //Stubbing values
          someClass.sampleMethod() >> "Some expected text to be returned"

          //Stubbing and ignoring values of method
          someClass.sampleMethodWithArguments(_ as String, _ as Long) >> "Some expected text to be returned"

          //Stubbing values of method invocation with concrete arguments
          someClass.sampleMethodWithArguments("expected argument", 2L) >> "Some expected text to be returned when argument matched"
          someClass.sampleMethodWithArguments("another expected argument", 5L) >> "Different text when argument matched"


          //Method invocation verification on Mock ( Mock assertions )
          1 * someClass.sampleMethod() // invoke only once
          3 * someClass.sampleMethod() // 3 times
          0 * someClass.sampleMethod() // never. there are few more atLeast atMost atMostOnce atLeastOnce...
          _ * someClass.sampleMethod() // more than 0 times

          //Argument capture with invocation verification on Mock
          String capturedEvent;
          1 * someClass.sampleMethodWithArgument(_) >> { String event ->
              capturedEvent = event
          }
          capturedEvent == "expected value"
      }*/

    def sampleMethod() {

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
