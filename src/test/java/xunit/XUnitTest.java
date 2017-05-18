package xunit;

import annotations.XTest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

public class XUnitTest {
    private XUnit xunit;

    @Before
    public void initXunit() {
        xunit = spy(new XUnit(mock(PrintWriter.class)));
    }
    @Test
    public void testSuccessful() throws Exception {
        xunit.test(SuccessfulClass.class.getName());
        verify(xunit, times(3)).printSuccess(AdditionalMatchers.geq(0d));
        verify(xunit, never()).printExpectedButNotThrown(any());
        verify(xunit, never()).printExpectedButOtherThrown(any(), any());
        verify(xunit, never()).printIgnored(any());
        verify(xunit, never()).printUnexpectedException(any());
    }
    @Test
    public void testUnexpected() throws Exception {
        xunit.test(UnexpectedExceptionClass.class.getName());
        verify(xunit, never()).printSuccess(anyDouble());
        verify(xunit, never()).printExpectedButNotThrown(any());
        verify(xunit, never()).printExpectedButOtherThrown(any(), any());
        verify(xunit, never()).printIgnored(any());
        verify(xunit, times(1)).printUnexpectedException(any());
    }
    @Test
    public void testNotThrown() throws Exception {
        xunit.test(NotThrownClass.class.getName());
        verify(xunit, never()).printSuccess(anyDouble());
        verify(xunit, times(1)).printExpectedButNotThrown(any());
        verify(xunit, never()).printExpectedButOtherThrown(any(), any());
        verify(xunit, never()).printIgnored(any());
        verify(xunit, never()).printUnexpectedException(any());
    }
    @Test
    public void testOtherThrown() throws Exception {
        xunit.test(OtherThrownClass.class.getName());
        verify(xunit, never()).printSuccess(anyDouble());
        verify(xunit, never()).printExpectedButNotThrown(any());
        verify(xunit, times(1)).printExpectedButOtherThrown(any(), any());
        verify(xunit, never()).printIgnored(any());
        verify(xunit, never()).printUnexpectedException(any());
    }
    @Test
    public void testIgnored() throws Exception {
        xunit.test(IgnoredClass.class.getName());
        verify(xunit, never()).printSuccess(anyDouble());
        verify(xunit, never()).printExpectedButNotThrown(any());
        verify(xunit, never()).printExpectedButOtherThrown(any(), any());
        verify(xunit, times(1)).printIgnored(any());
        verify(xunit, never()).printUnexpectedException(any());

    }


    public static class SuccessfulClass {
        @XTest
        public void test1(){}
        @XTest(expected = IOException.class)
        public void test2() throws IOException {
            throw new IOException();
        }
        @XTest(expected = IOException.class)
        public void test3() throws IOException {
            throw new FileNotFoundException();
        }
    }

    public static class UnexpectedExceptionClass {
        @XTest
        public void test1() throws Exception {
            throw new Exception();
        }
    }

    public static class NotThrownClass {
        @XTest(expected = Exception.class)
        public void test1() {}
    }

    public static class OtherThrownClass {
        @XTest(expected = IOException.class)
        public void test1() throws Exception {
            throw new Exception();
        }
    }

    public static class IgnoredClass {
        @XTest(ignore = "For testing purposes")
        public void test1() {}
    }
}