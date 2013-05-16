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
package org.candlepin.resteasy.interceptor;

import org.candlepin.exceptions.BadRequestException;
import org.candlepin.paging.DataPresentation;
import org.candlepin.paging.Paginate;
import org.candlepin.paging.DataPresentation.Order;

import com.google.inject.Inject;

import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;
import org.xnap.commons.i18n.I18n;

import java.lang.reflect.Method;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

/**
 * DataPresentationInterceptor
 */
@Provider
@ServerInterceptor
public class DataPresentationInterceptor implements PreProcessInterceptor,
    AcceptedByMethod {

    private I18n i18n;

    @Inject
    public DataPresentationInterceptor(I18n i18n) {
        super();
        this.i18n = i18n;
    }

    @Override
    public boolean accept(Class declaring, Method method) {
        return method.isAnnotationPresent(Paginate.class);
    }

    @Override
    public ServerResponse preProcess(HttpRequest request, ResourceMethod method)
        throws Failure, WebApplicationException {
        DataPresentation p = null;

        MultivaluedMap<String, String> params = request.getUri().getQueryParameters();

        String offset = params.getFirst("offset");
        String limit = params.getFirst("limit");
        String order = params.getFirst("order");
        String sortBy = params.getFirst("sort_by");

        if (offset != null || limit != null || order != null || sortBy != null) {
            p = new DataPresentation();

            if (order == null) {
                p.setOrder(DataPresentation.DEFAULT_ORDER);
            }
            else {
                p.setOrder(readOrder(order));
            }

            /* We'll leave it to the curator layer to figure out what to sort by if
             * sortBy is null. */
            p.setSortBy(sortBy);

            try {
                if (offset == null && limit != null) {
                    p.setOffset(DataPresentation.DEFAULT_OFFSET);
                    p.setLimit(readInteger(limit));
                }
                else if (offset != null && limit == null) {
                    p.setOffset(readInteger(offset));
                    p.setLimit(DataPresentation.DEFAULT_LIMIT);
                }
                else {
                    p.setOffset(readInteger(offset));
                    p.setLimit(readInteger(limit));
                }
            }
            catch (NumberFormatException nfe) {
                throw new BadRequestException(i18n.tr("offset and limit parameters" +
                    " must be positive integers"), nfe);
            }
        }

        ResteasyProviderFactory.pushContext(DataPresentation.class, p);

        return null;
    }

    private Order readOrder(String order) {
        if ("ascending".equalsIgnoreCase(order) || "asc".equalsIgnoreCase(order)) {
            return Order.ASCENDING;
        }
        else if ("descending".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order)) {
            return Order.DESCENDING;
        }

        throw new BadRequestException(i18n.tr("the order parameter must be either" +
                " 'ascending' or 'descending'"));
    }

    private Integer readInteger(String value) {
        if (value != null) {
            int i = Integer.parseInt(value);

            if (i < 0) {
                throw new NumberFormatException(i18n.tr("Expected a positive integer."));
            }
            return i;
        }

        return null;
    }
}
