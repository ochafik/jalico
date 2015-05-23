package com.nativelibs4java.jalico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import com.nativelibs4java.jalico.CollectionEvent.EventType;

@RunWith(MockitoJUnitRunner.class)
public class AdaptedCollectionBiDiTest extends AbstractAdaptedCollectionTest {
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
	assertEquals(subject.getBackwardAdapter(),BACKWARD);
    }

    @Test
    public void testIterator() {
	assertNotNull(subject.iterator());
    }

    @Test
    public void testAddV() {
	when      (mockedDelegate.add(5)).thenReturn(true);
	assertTrue(subject.add("5"));
	verify    (mockedDelegate).add(5);
	final CollectionEvent<String> evt = expectEvent();
	 assertEquals (-1,evt.getFirstIndex());
	 assertEquals (-1,evt.getLastIndex());
	 assertEquals (EventType.ADDED,evt.getType());
	 assertEquals (subject,evt.getSource());
	 Collection<String> elements = evt.getElements();
	  assertEquals(1,elements.size());
	  assertTrue  (elements.contains("5"));
    }//end testAddV()

    @Test
    public void testSuccessfulRemoveObject() {
	when      (mockedDelegate.remove(5)).thenReturn(true);
	assertTrue(subject.remove("5"));
	verify    (mockedDelegate).remove(5);
	final CollectionEvent<String> evt = expectEvent();
	 assertEquals (-1,evt.getFirstIndex());
	 assertEquals (-1,evt.getLastIndex());
	 assertEquals (EventType.REMOVED,evt.getType());
	 assertEquals (subject,evt.getSource());
	 Collection<String> elements = evt.getElements();
	  assertEquals(1,elements.size());
	  assertTrue  (elements.contains("5"));
    }//end testSuccessfulRemoveObject()
    
    @Test
    public void testFailedRemoveObject() {
	when        (mockedDelegate.remove(5)).thenReturn(false);
	assertFalse (subject.remove("5"));
	verify      (mockedDelegate).remove(5);
	verifyZeroInteractions(mockedListener);
    }//end testFailedRemoveObject()

    @Test
    public void testRemoveWithoutBackWardAdapter() {
	when   (mockedDelegate .remove(0)).thenReturn(true);
	subject.removeWithBackwardAdapter("0");
	verify (mockedDelegate).remove(0);
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
	subject.add("9");
	verifyZeroInteractions(mockedListener);
    }//end testRemoveCollectionListener()

    @Override
    protected AdaptedCollection<Integer, String> constructSubject() {
	return new AdaptedCollection<Integer,String>(mockedDelegate, FORWARD, BACKWARD);
    }
}//end AdaptedCollectionTest
