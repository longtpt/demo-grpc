package io.enotion.demo.mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SpyDemo {
    @Spy
    List<String> spiedList = new ArrayList<>();

    @Test
    public void whenUseSpyAnnotation_thenSpyIsInjectedCorrectly() {
        // given

        // when
        spiedList.add("one");
        spiedList.add("two");

        // then
        verify(spiedList).add("one");
        verify(spiedList).add("two");
        Assertions.assertEquals(2, spiedList.size());
    }
}
