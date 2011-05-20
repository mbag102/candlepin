/**
 * Copyright (c) 2009 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package org.fedoraproject.candlepin.model.test;

import java.util.Date;

import org.fedoraproject.candlepin.model.ActivationKey;
import org.fedoraproject.candlepin.model.Owner;
import org.fedoraproject.candlepin.model.Pool;
import org.fedoraproject.candlepin.model.Product;
import org.fedoraproject.candlepin.test.DatabaseTestFixture;
import org.fedoraproject.candlepin.test.TestUtil;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * ActivationKeyTest
 */
public class ActivationKeyTest extends DatabaseTestFixture {
   
    private Owner owner;

    @Before
    public void setUp() {
        owner = createOwner();
        ownerCurator.create(owner);
    }
    
    @Test
    public void testCreate() {
        ActivationKey key = createActivationKey(owner);
        activationKeyCurator.create(key);
        assertNotNull(key.getId());
        assertNotNull(key.getName());
        assertEquals(owner, key.getOwner());
        assertEquals(false, key.getAutosubscribe());
    }
    
    @Test
    public void testOwnerRelationship() {
        ActivationKey key = createActivationKey(owner);
        activationKeyCurator.create(key);
        ownerCurator.refresh(owner);
        assertNotNull(owner.getActivationKeys());
        assertTrue("The count of keys should be 1", owner.getActivationKeys().size() == 1);
    }    
    
    @Test
    public void testPoolRelationship() {
        ActivationKey key = createActivationKey(owner);
        Product prod = TestUtil.createProduct();
        productCurator.create(prod);
        Pool pool = createPoolAndSub(owner, prod, 12L,
            new Date(), new Date(System.currentTimeMillis() + (365 * 24 * 60 * 60 * 1000)));
        key.getPools().add(pool);
        activationKeyCurator.create(key);
        activationKeyCurator.refresh(key);
        assertNotNull(key.getPools());
        assertTrue("The count of pools should be 1", key.getPools().size() == 1);        
    }
}
