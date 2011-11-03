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
package org.candlepin.servlet.filter;

import org.candlepin.util.VersionUtil;

import com.google.inject.Singleton;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * VersionFilter
 */
@Singleton // For use with Guice
public class VersionFilter implements Filter {
    public static final String VERSION_HEADER = "X-Candlepin-Version";

    @Override
    public void destroy() {
        // Nothing to do here
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
        HttpServletResponse rsp = (HttpServletResponse) response;

        Map<String, String> map = VersionUtil.getVersionMap();
        rsp.addHeader(VERSION_HEADER, map.get("version") + "-" +
            map.get("release"));
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Nothing to do here
    }
}