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

import com.google.inject.Inject;
import com.google.inject.Provider;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;
import org.xnap.commons.i18n.I18n;

/**
 * CandlepinMessageInterpolator
 */
public class CandlepinMessageInterpolator extends
    ResourceBundleMessageInterpolator {

    // You must use the Provider here otherwise you will end up with a stale I18n object!
    private Provider<I18n> i18nProvider;

    @Inject
    public CandlepinMessageInterpolator(ResourceBundleLocator rbi, Provider<I18n> i18nProvider) {
        super(rbi);
        this.i18nProvider = i18nProvider;
    }

    @Override
    public String interpolate(String message, Context context) {
        return super.interpolate(message, context, i18nProvider.get().getLocale());
    }
}
