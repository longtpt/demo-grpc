package io.enotion.demo.mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class InjectMockDemoTest {
    @Mock
    Map<String, String> wordMap;
    @Mock
    Map<String, String> wordMap3;

    @InjectMocks
    MyDictionary dic = new MyDictionary();

    @Test
    public void whenUseInjectMocksAnnotation_thenCorrect() {
        // given
        Mockito.when(wordMap.get("aWord")).thenReturn("aMeaning");

        // when

        // then
        Assertions.assertEquals("aMeaning", dic.getMeaning("aWord"));
    }
}
