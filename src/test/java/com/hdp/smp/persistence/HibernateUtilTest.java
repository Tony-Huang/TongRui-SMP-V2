package com.hdp.smp.persistence;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Created by admin on 2017/12/10.
 */
public class HibernateUtilTest {

    @Test
    public void testGetConnection() {

        boolean result = HibernateUtil.getSession().isConnected();
        assertTrue(result);
    }
}
