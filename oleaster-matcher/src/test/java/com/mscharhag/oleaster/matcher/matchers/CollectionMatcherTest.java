package com.mscharhag.oleaster.matcher.matchers;

import com.mscharhag.oleaster.runner.OleasterRunner;
import org.junit.runner.RunWith;

import java.util.*;

import static com.mscharhag.oleaster.matcher.TestUtil.expectAssertionError;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;

@RunWith(OleasterRunner.class)
public class CollectionMatcherTest {{
    describe("CollectionMatcher test", () -> {
        final List<String> list = new LinkedList<>();
        list.add("one");
        list.add("two");

        final Set<String> set = new HashSet<>();
        set.add("one");
        set.add("two");

        describe("when toContain() is called", () -> {
            it("fails if the stored value does not contain the expected value", () -> {
                expectAssertionError(() -> new CollectionMatcher(list).toContain("three"),
                        "Expected '[one, two]' to contain 'three'");
                expectAssertionError(() -> new CollectionMatcher(set).toContain("four"),
                        "Expected '[one, two]' to contain 'four'");
            });

            it("is ok if the stored value contains the expected value", () -> {
                new CollectionMatcher(list).toContain("one");
                new CollectionMatcher(set).toContain("two");
            });
        });

        describe("when toNotContain() is called", () -> {
            it("fails if hte stored value does contain the expected value", () -> {
                expectAssertionError(() -> new CollectionMatcher(list).toNotContain("one"),
                        "Expected '[one, two]' to not contain 'one'");
                expectAssertionError(() -> new CollectionMatcher(set).toNotContain("one"),
                        "Expected '[one, two]' to not contain 'one'");
            });

            it("is ok if the stored value not contains the expected value", () -> {
               new CollectionMatcher(list).toNotContain("three");
               new CollectionMatcher(set).toNotContain("three");
            });
        });

        describe("when toBeEmpty() is called", () -> {
            it("fails if the stored value is not empty", () -> {
                expectAssertionError(() -> new CollectionMatcher(list).toBeEmpty(),
                        "Expected '[one, two]' to be empty");
                expectAssertionError(() -> new CollectionMatcher(set).toBeEmpty(),
                        "Expected '[one, two]' to be empty");
            });

            it("is ok if the stored value is empty", () -> {
                new CollectionMatcher(new LinkedList()).toBeEmpty();
                new CollectionMatcher(new HashSet()).toBeEmpty();
            });
        });

        describe("when toNotBeEmpty() is called", () -> {
            it("fails if the stored value is empty", () -> {
                expectAssertionError(() -> new CollectionMatcher(new LinkedList()).toNotBeEmpty(),
                        "Expected '[]' to not be empty");
                expectAssertionError(() -> new CollectionMatcher(new HashSet()).toNotBeEmpty(),
                        "Expected '[]' to not be empty");
            });

            it("is ok if the stored value is not empty", () -> {
                new CollectionMatcher(list).toNotBeEmpty();
                new CollectionMatcher(set).toNotBeEmpty();
            });
        });

        describe("when toHaveLength() is called", () -> {
            it("fails if the stored value does not have the provided length", () -> {
                expectAssertionError(() -> new CollectionMatcher(list).toHaveLength(1),
                        "Expected '[one, two]' to have a length of 1, instead has a length of 2");
                expectAssertionError(() -> new CollectionMatcher(set).toHaveLength(-1),
                        "Expected '[one, two]' to have a length of -1, instead has a length of 2");
            });

            it("is ok if hte stored value has the provided length", () -> {
               new CollectionMatcher(list).toHaveLength(2);
               new CollectionMatcher(set).toHaveLength(2);
            });
        });
    });
}}
