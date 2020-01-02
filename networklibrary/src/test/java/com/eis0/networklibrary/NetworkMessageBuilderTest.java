package com.eis0.networklibrary;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link NetworkMessageBuilder}.
 *
 * @author Matteo Carnelos
 */
public class NetworkMessageBuilderTest {

    private static final String VALID_CMD = "Command";
    private static final String VALID_ARG_1 = "Arg1";
    private static final String VALID_ARG_2 = "Arg2";
    private static final String VALID_ARG_3 = "Arg3";
    private static final String SEP = NetworkMessageBuilder.FIELD_SEPARATOR;
    private static final String VALID_MESSAGE =
            VALID_CMD + SEP + VALID_ARG_1 + SEP + VALID_ARG_2 + SEP + VALID_ARG_3;

    private static List<String> testArguments = new ArrayList<>();

    @BeforeClass
    public static void setup() {
        testArguments.add(VALID_ARG_1);
        testArguments.add(VALID_ARG_2);
        testArguments.add(VALID_ARG_3);
    }

    @Test
    public void validBuilder_isCreated1() {
        NetworkMessageBuilder testBuilder = new NetworkMessageBuilder(VALID_CMD)
                .addArgument(VALID_ARG_1)
                .addArgument(VALID_ARG_2)
                .addArgument(VALID_ARG_3);
        assertEquals(VALID_CMD, testBuilder.getCommand());
        assertEquals(testArguments, testBuilder.getArguments());
    }

    @Test
    public void validBuilder_isCreated2() {
        NetworkMessageBuilder testBuilder = new NetworkMessageBuilder(VALID_CMD, testArguments);
        assertEquals(VALID_CMD, testBuilder.getCommand());
        assertEquals(testArguments, testBuilder.getArguments());
    }

    @Test
    public void validBuilder_isCreated3() {
        NetworkMessageBuilder testBuilder = new NetworkMessageBuilder(VALID_CMD)
                .addArguments(testArguments);
        assertEquals(VALID_CMD, testBuilder.getCommand());
        assertEquals(testArguments, testBuilder.getArguments());
    }

    @Test
    public void validMessage_isBuilt() {
        NetworkMessageBuilder testBuilder = new NetworkMessageBuilder(VALID_CMD, testArguments);
        assertEquals(VALID_MESSAGE, testBuilder.buildMessage());
    }

    @Test
    public void validMessage_isParsed() {
        NetworkMessageBuilder testBuilder = NetworkMessageBuilder.parseNetworkMessage(VALID_MESSAGE);
        assertEquals(VALID_CMD, testBuilder.getCommand());
        assertEquals(testArguments, testBuilder.getArguments());
    }

}
