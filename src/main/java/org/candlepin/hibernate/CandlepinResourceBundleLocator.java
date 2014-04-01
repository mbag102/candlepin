/**
 * Copyright (c) 2009 - 2012 Red Hat, Inc.
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
package org.candlepin.hibernate;

import org.candlepin.guice.I18nProvider;

import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * The CandlepinResourceBundleLocator simply delegates Validation message lookups
 * to the I18n class.
 */
public class CandlepinResourceBundleLocator implements ResourceBundleLocator {
    @Override
    public ResourceBundle getResourceBundle(Locale locale) {
        return new I18nProvider(locale).get().getResources();
    }
}
