package com.mscharhag.oleaster.runner;

import com.mscharhag.oleaster.runner.suite.Spec;
import com.mscharhag.oleaster.runner.suite.Suite;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.notification.RunNotifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.*;

public class HandlerTest {

    private List<String> calls;
    private Function<String, Invokable> block = (String name) -> () -> { calls.add(name); };
    private OleasterRunner runner;
    private Suite suite;

    public static class TestClass { }

    @Before
    public void before() throws Exception {
        calls = new ArrayList<>();
        runner = new OleasterRunner(TestClass.class);
        suite = new Suite(null, "suite");
    }

    @Test
    public void itExecutesBeforeEachHandlersInCorrectOrder() {
        suite.addBeforeEachHandler(block.apply("first"));
        suite.addBeforeEachHandler(block.apply("second"));
        runner.runChild(new Spec(suite, "spec", () -> {}), new RunNotifier());
        assertEquals(Arrays.asList("first", "second"), calls);
    }


    @Test
    public void itExecutesAfterEachHandlersInCorrectOrder() {
        suite.addAfterEachHandler(block.apply("first"));
        suite.addAfterEachHandler(block.apply("second"));
        runner.runChild(new Spec(suite, "spec", () -> {}), new RunNotifier());
        assertEquals(Arrays.asList("first", "second"), calls);
    }


    @Test
    public void itExecutesBeforeEachHandlersBeforeTheSpecIsExecuted() {
        suite.addBeforeEachHandler(block.apply("beforeEach"));
        runner.runChild(new Spec(suite, "spec", block.apply("spec")), new RunNotifier());
        assertEquals(Arrays.asList("beforeEach", "spec"), calls);
    }

    @Test
    public void itExecutesAfterEachHandlersAfterTheSpecIsExecuted() {
        suite.addAfterEachHandler(block.apply("afterEach"));
        runner.runChild(new Spec(suite, "spec", block.apply("spec")), new RunNotifier());
        assertEquals(Arrays.asList("spec", "afterEach"), calls);
    }

    @Test
    public void itExecutesOuterBeforeEachHandlersBeforeInnerOnes() {
        suite.addBeforeEachHandler(block.apply("outerBeforeEach"));
        Suite child = new Suite(suite, "child");
        child.addBeforeEachHandler(block.apply("innerBeforeEach"));
        runner.runChild(new Spec(child, "spec", () -> { }), new RunNotifier());
        assertEquals(Arrays.asList("outerBeforeEach", "innerBeforeEach"), calls);
    }

    @Test
    public void itExecutesInnerAfterEachHandlersBeforeOuterOnes() {
        suite.addAfterEachHandler(block.apply("outerBeforeEach"));
        Suite child = new Suite(suite, "child");
        child.addAfterEachHandler(block.apply("innerBeforeEach"));
        runner.runChild(new Spec(child, "spec", () -> {
        }), new RunNotifier());
        assertEquals(Arrays.asList("innerBeforeEach", "outerBeforeEach"), calls);
    }

}
