package com.nativelibs4java.jalico;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import com.nativelibs4java.jalico.CollectionEvent.EventType;

@RunWith(MockitoJUnitRunner.class)
public class AdaptedCollectionForwardTest extends
	AbstractAdaptedCollectionForwardTest {

    @Before
    public void setUp() throws Exception {
	super.setUp();
    }

    @After
    public void tearDown() throws Exception {
	super.tearDown();
    }

    @Test
    public void testSize() {
	when        (mockedDelegate.size()).thenReturn(5);
	assertEquals(5,subject     .size());
	verifyZeroInteractions(mockedListener);
    }//end testSize()

    @Test
    public void testIsEmpty() {
	when       (mockedDelegate .isEmpty()).thenReturn(true);
	assertTrue (subject        .isEmpty());
	verify     (mockedDelegate).isEmpty();
	when       (mockedDelegate .isEmpty()).thenReturn(false);
	assertFalse(subject        .isEmpty());
	verify     (mockedDelegate, times(2)).isEmpty();
	verifyZeroInteractions(mockedListener);
    }//end testIsEmpty()
    
    private CollectionEvent<String> expectEvent(){
	ArgumentCaptor<CollectionEvent> arg 
	 = ArgumentCaptor.forClass(CollectionEvent.class);
	verify(mockedListener).collectionChanged(arg.capture());
	return arg.getValue();
    }

    @Test
    public void testClear() {
	subject                   .clear();
	verify    (mockedDelegate).clear();
	final CollectionEvent<String> evt = expectEvent();
	 assertEquals(0,evt.getFirstIndex());
	 assertEquals(2,evt.getLastIndex());
	 assertEquals(EventType.REMOVED,evt.getType());
	 assertEquals(subject,evt.getSource());
	 final Collection<String> elements = evt.getElements();
	  assertNotNull(elements);
	  assertEquals (3,elements.size());
	  assertTrue   (elements.contains("0"));
	  assertTrue   (elements.contains("1"));
	  assertTrue   (elements.contains("2"));
    }//end testClear()

    @Test
    public void testAdaptedCollectionCollectionOfUAdapterOfUVAdapterOfVU() {
	assertNotNull(subject);
    }

    @Test
    public void testGetForwardAdapter() {
	assertEquals(subject.getForwardAdapter(),FORWARD);
    }

    @Test
    public void testGetBackwardAdapter() {
	assertNull(subject.getBackwardAdapter());
    }

    @Test
    public void testIterator() {
	assertNotNull(subject.iterator());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void testAddV() {
	subject.add("0");
    }//end testAddV()

    @Test
    public void testSuccessfulRemoveObject() {
	when       (mockedDelegate.remove(0)).thenReturn(true);
	assertTrue (subject.remove("0"));
	final CollectionEvent<String> evt = expectEvent();
	 assertEquals(0,evt.getFirstIndex());
	 assertEquals(0,evt.getLastIndex());
	 assertEquals(EventType.REMOVED,evt.getType());
	 assertEquals(subject,evt.getSource());
	 Collection<String> elements = evt.getElements();
	  assertEquals(1,elements.size());
	  assertTrue  (elements.contains("0"));
    }//end testSuccessfulRemoveObject()
    
    @Test
    public void testFailedRemoveObject() {
	when        (mockedDelegate.remove(5)).thenReturn(false);
	assertFalse (subject.remove("5"));
	verifyZeroInteractions(mockedListener);
    }//end testFailedRemoveObject()

    @Test(expected=UnsupportedOperationException.class)
    public void testRemoveWithoutBackWardAdapter() {
	subject.removeWithBackwardAdapter("0");
    }//end testRemoveWithoutBackWardAdapter()

    @Test
    public void testContainsObject() {
	when       (mockedDelegate.contains(0)).thenReturn(true);
	when       (mockedDelegate.contains(7)).thenReturn(false);
	assertTrue (subject.contains("0"));
	assertFalse(subject.contains("7"));
	verifyZeroInteractions(mockedListener);
    }//end testContainsObject()

    @Test
    public void testRemoveCollectionListener() {
	subject.removeCollectionListener(mockedListener);
	//Cannot use add() due to lack of backward adapter but remove() has a nifty brute-force fallback.
	subject.remove("9");
	verifyZeroInteractions(mockedListener);
    }//end testRemoveCollectionListener()

}//end AdaptedCollectionForwardTest
