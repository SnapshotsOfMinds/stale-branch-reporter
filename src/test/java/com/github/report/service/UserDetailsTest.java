package com.github.report.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.report.json.parser.UserJSONParser;

@ExtendWith({ MockitoExtension.class })
public class UserDetailsTest
{
    @Spy
    private UserJSONParser spyUserDetails;

    @Test
    public void getUserInfo_succes()
    {
        String UserJson = "[\r\n" + "  {\r\n" + "    \"login\": \"cernzs\", \r\n" + "	},\r\n" + "	{\r\n"
                + "    \"login\": \"GA044433\",\r\n" + "	}\r\n" + "]";

        assertEquals("cernzs", spyUserDetails.getUserInfo(UserJson).get(0).getLogin());
    }
}
