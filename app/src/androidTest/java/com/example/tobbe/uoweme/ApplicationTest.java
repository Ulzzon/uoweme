package com.example.tobbe.uoweme;

import android.app.Application;
import android.test.AndroidTestCase;
import android.test.ApplicationTestCase;
import android.test.RenamingDelegatingContext;
import android.test.suitebuilder.annotation.SmallTest;

import helper.DatabaseHelper;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public class DatabaseTest extends AndroidTestCase{
        private DatabaseHelper db;

        @Override
        public void setUp() throws  Exception{
            super.setUp();
            RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(),"test_");
            db = new DatabaseHelper(context);
        }
        @SmallTest
        public void testSplitString(){
            String testId = "1, 2, 15,";
            long[] testArray = db.splitStringToId(testId);
            long[] expected = {1,2,15};
            assertEquals(testArray,expected);
        }
        @SmallTest
        public void testCreateStringOfId(){
            long[] idArray = {1,2,15};
            String resultString = db.createStringOfId(idArray);
            String expected = "1, 2, 15, ";
            assertEquals(resultString,expected);
        }

        @Override
        public void tearDown() throws Exception{
            db.close();
            super.tearDown();
        }



    }
}