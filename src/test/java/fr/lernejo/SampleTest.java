package fr.lernejo;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static  org.assertj.core.api.Assertions.assertThat;

class SampleTest {

    private final Sample sample = new Sample();
    @ParameterizedTest
    @CsvSource({
        "3,7,10",
        "0,3,3",
        "4,5,9",
    })
    void add_behaves_as_expected(int a, int b, int expectedResult){
        int result = sample.op(Sample.Operation.ADD,a,b);
        assertThat(result).isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @CsvSource({
        "2,3,6",
        "0,3,0",
        "4,5,20",
    })
    void mult_behaves_as_expected(int a, int b, int expectedResult){
        int result = sample.op(Sample.Operation.MULT,a,b);
        assertThat(result).isEqualTo(expectedResult);
    }


    @ParameterizedTest
    @CsvSource({
        "0,1",
        "5,120",
        "7,5040",
    })
    void fact_behaves_as_expected (int n, int expectedResult) {
        int facto = sample.fact(n);
        assertThat(facto).isEqualTo(expectedResult);
    }



}
