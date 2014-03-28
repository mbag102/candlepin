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

import javax.validation.ConstraintValidatorFactory;
import javax.validation.MessageInterpolator;
import javax.validation.TraversableResolver;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorContext;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.hibernate.validator.spi.resourceloading.ResourceBundleLocator;

import com.google.inject.Inject;

/**
 * HibernateValidatorFactory
 */
public class CandlepinValidatorFactory  implements ValidatorFactory {
    private ValidatorFactory factory;

    @Inject
    public CandlepinValidatorFactory(ResourceBundleLocator rbl) {
        HibernateValidatorConfiguration configure = Validation.byProvider(HibernateValidator.class).configure();
        configure.messageInterpolator(new ResourceBundleMessageInterpolator(rbl, true));
        factory = configure.buildValidatorFactory();
    }

    /* (non-Javadoc)
     * @see javax.validation.ValidatorFactory#getConstraintValidatorFactory()
     */
    @Override
    public ConstraintValidatorFactory getConstraintValidatorFactory() {
        return factory.getConstraintValidatorFactory();
    }

    /* (non-Javadoc)
     * @see javax.validation.ValidatorFactory#getMessageInterpolator()
     */
    @Override
    public MessageInterpolator getMessageInterpolator() {
        return factory.getMessageInterpolator();
    }

    /* (non-Javadoc)
     * @see javax.validation.ValidatorFactory#getTraversableResolver()
     */
    @Override
    public TraversableResolver getTraversableResolver() {
        return factory.getTraversableResolver();
    }

    /* (non-Javadoc)
     * @see javax.validation.ValidatorFactory#getValidator()
     */
    @Override
    public Validator getValidator() {
        return factory.getValidator();
    }

    /* (non-Javadoc)
     * @see javax.validation.ValidatorFactory#unwrap(java.lang.Class)
     */
    @Override
    public <T> T unwrap(Class<T> arg0) {
        return factory.unwrap(arg0);
    }

    /* (non-Javadoc)
     * @see javax.validation.ValidatorFactory#usingContext()
     */
    @Override
    public ValidatorContext usingContext() {
        return factory.usingContext();
    }

}
