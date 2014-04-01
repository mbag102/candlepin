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

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

/**
 * CandlepinMessageInterpolator
 */
public class CandlepinMessageInterpolator extends
    ResourceBundleMessageInterpolator {

    public CandlepinMessageInterpolator() {
        super(new CandlepinResourceBundleLocator(), true);
    }

    @Override
    public String interpolate(String message, Context context) {
        HttpServletRequest request = ResteasyProviderFactory.getContextData(HttpServletRequest.class);
        Locale locale = request.getLocale();
        locale = (locale == null) ? Locale.US : locale;
        return super.interpolate(message, context, locale);
    }
}
