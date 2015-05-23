package com.nativelibs4java.jalico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import com.nativelibs4java.jalico.CollectionEvent.EventType;

@RunWith(MockitoJUnitRunner.class)
public class AdaptedCollectionBiDiIteratorTest extends
	AbstractAdaptedCollectionBiDiTest {
    protected Iterator<String> subjectIterator;

    @Before
    public void setUp() throws Exception {
	super.setUp();
	subjectIterator = subject.iterator();
    }

    @Test
    public void testHasNextTrue() {
	assertTrue(subjectIterator.hasNext());
	verifyZeroInteractions(mockedListener);
	subjectIterator.next();
	subjectIterator.next();
	assertTrue(subjectIterator.hasNext());
    }
    
    @Test
    public void testHasNextFalse() {
	subjectIterator.next();
	subjectIterator.next();
	subjectIterator.next();
	assertFalse(subjectIterator.hasNext());
	verifyZeroInteractions(mockedListener);
    }
    
    @Test
    public void testNext(){
	assertEquals("0",subjectIterator.next());
	assertEquals("1",subjectIterator.next());
	assertEquals("2",subjectIterator.next());
	verifyZeroInteractions(mockedListener);
    }
    
    @Test
    public void testNextFail(){
	assertEquals("0",subjectIterator.next());
	assertEquals("1",subjectIterator.next());
	assertEquals("2",subjectIterator.next());
    }
    
    @Test
    public void testRemove(){
	when(mockedDelegate.remove(0)).thenReturn(true);
	subjectIterator    .next();
	subjectIterator    .remove();
	assertEquals(2,iteratorSource.size());
	ArgumentCaptor<CollectionEvent> arg 
	 = ArgumentCaptor.forClass(CollectionEvent.class);
	verify(mockedListener).collectionChanged(arg.capture());
	CollectionEvent evt = arg.getValue();
	assertEquals(-1,evt.getFirstIndex());
	assertEquals(-1,evt.getLastIndex());
	assertTrue  (evt.getElements().contains("0"));
	assertEquals(1,evt.getElements().size());
	assertEquals(subject,evt.getSource());
	assertEquals(EventType.REMOVED,evt.getType());
    }
    
    @Test(expected=IllegalStateException.class)
    public void testRemoveIllegalState(){
	subjectIterator.remove();
    }
}//end AdaptedCollectionIteratorTest
