package com.nativelibs4java.jalico;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.mockito.Mock;


public abstract class AbstractAdaptedCollectionTest {
   ////FIELDS
   protected AdaptedCollection<Integer,String> subject;
   protected @Mock CollectionListener<String>  mockedListener;
   protected @Mock Collection<Integer>         mockedDelegate;
   protected Collection<Integer>	       iteratorSource;
   
   //// ADAPTERS
   protected static final Adapter<Integer,String> FORWARD = new Adapter<Integer,String>(){
	public String adapt(Integer value) {
	    return value.toString();
	}};
   protected static final Adapter<String,Integer> BACKWARD = new Adapter<String,Integer>(){
	public Integer adapt(String value) {
	    return Integer.parseInt(value);
	}};

   //// PREP
   @Before
   public void setUp() throws Exception {
        subject = constructSubject();
	subject . addCollectionListener                (mockedListener);
	//Going with traditional way instead of over-engineering with mocks.
	iteratorSource = new ArrayList<Integer>(Arrays.asList(0,1,2));
	when(mockedDelegate.iterator()).thenReturn(iteratorSource.iterator());
   }//end setUp()
   
   protected abstract AdaptedCollection<Integer,String> constructSubject();

}//end AbstractAdaptedCollectionTest
