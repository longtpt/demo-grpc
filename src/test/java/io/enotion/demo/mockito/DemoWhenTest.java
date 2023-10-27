package io.enotion.demo.mockito;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DemoWhenTest {
    @Mock
    ArrayList<Integer> list = new ArrayList<>();

    @Test
    public void testCallRealMethod() {
        // given
        when(list.size())
                .thenCallRealMethod();
        // when

        //then
        assertEquals(list.size(), 2);
    }
}
