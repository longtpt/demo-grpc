package io.enotion.demo.mockito;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class InjectMockToSpyTest {
    @Mock
    Map<String, String> wordMap;

    @Spy
    @InjectMocks
    MyDictionary spyDic = new MyDictionary();

    @Test
    public void givenWordMap_whenAddToDictionary_thenGetMeaning() {
        // given
        Mockito.when(wordMap.get("aWord")).thenReturn("aMeaning");

        // when
        spyDic.add("cat", "meo");

        // then
        Mockito.verify(spyDic).add("cat", "meo");
        Assertions.assertEquals("aMeaning", spyDic.getMeaning("aWord"));
    }
}
