package com.github.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({ MockitoExtension.class })
public class SemiColonSplitterTest
{
    @Spy
    private SemiColonSplitter spySemiColonSplitter;

    @Test
    public void split_success()
    {

        String input = "abc@cerner.com;xyz@cerner.com";

        List<String> outputArray = spySemiColonSplitter.split(input);

        List<String> expectedArray = new ArrayList<>();
        expectedArray.add("abc@cerner.com");
        expectedArray.add("xyz@cerner.com");

        assertEquals(expectedArray, outputArray);
    }
}
