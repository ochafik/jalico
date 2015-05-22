package com.nativelibs4java.jalico;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractAdaptedCollectionForwardTest extends
	AbstractAdaptedCollectionTest {

    @Before
    public void setUp() throws Exception {
	super.setUp();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Override
    protected AdaptedCollection<Integer, String> constructSubject() {
	return new AdaptedCollection<Integer,String>(mockedDelegate,FORWARD);
    }
}//end AbstractAdaptedCollectionForwardTest
