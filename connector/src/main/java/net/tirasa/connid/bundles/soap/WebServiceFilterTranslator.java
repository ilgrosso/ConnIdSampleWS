/**
 * Copyright (C) 2011 ConnId (connid-dev@googlegroups.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tirasa.connid.bundles.soap;

import java.util.HashSet;
import java.util.Set;
import net.tirasa.connid.bundles.soap.utilities.Operand;
import net.tirasa.connid.bundles.soap.utilities.Operator;
import org.identityconnectors.framework.common.objects.AttributeUtil;
import org.identityconnectors.common.StringUtil;
import org.identityconnectors.framework.common.objects.filter.AbstractFilterTranslator;
import org.identityconnectors.framework.common.objects.filter.ContainsFilter;
import org.identityconnectors.framework.common.objects.filter.EndsWithFilter;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.StartsWithFilter;

/**
 * This is an implementation of AbstractFilterTranslator that gives a concrete representation of which filters can be
 * applied at the connector level (natively). If the WebService doesn't support a certain expression type, that factory
 * method should return null. This level of filtering is present only to allow any native contructs that may be
 * available to help reduce the result set for the framework, which will (strictly) reapply all filters specified after
 * the connector does the initialfiltering.
 *
 * Note: The generic query type is most commonly a String, but does not have to be.
 */
public class WebServiceFilterTranslator extends AbstractFilterTranslator<Operand> {

    /**
     * {@inheritDoc}
     */
    @Override
    protected Operand createContainsExpression(final ContainsFilter filter, final boolean not) {
        if (filter == null) {
            return null;
        }

        final String name = filter.getAttribute().getName();
        final String value = AttributeUtil.getAsStringValue(filter.getAttribute());
        if (StringUtil.isBlank(value)) {
            return null;
        }

        return new Operand(Operator.CONTAINS, name, value, not);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Operand createEndsWithExpression(final EndsWithFilter filter, final boolean not) {
        if (filter == null) {
            return null;
        }

        final String name = filter.getAttribute().getName();
        final String value = AttributeUtil.getAsStringValue(filter.getAttribute());
        if (StringUtil.isBlank(value)) {
            return null;
        }

        return new Operand(Operator.ENDS, name, value, not);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Operand createStartsWithExpression(final StartsWithFilter filter, final boolean not) {
        if (filter == null) {
            return null;
        }

        final String name = filter.getAttribute().getName();
        final String value = AttributeUtil.getAsStringValue(filter.getAttribute());
        if (StringUtil.isBlank(value)) {
            return null;
        }

        return new Operand(Operator.STARTS, name, value, not);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Operand createEqualsExpression(final EqualsFilter filter, final boolean not) {
        if (filter == null) {
            return null;
        }

        final String name = filter.getAttribute().getName();
        final String value = AttributeUtil.getAsStringValue(filter.getAttribute());
        if (StringUtil.isBlank(value)) {
            return null;
        }

        return new Operand(Operator.EQ, name, value, not);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Operand createAndExpression(final Operand leftExpression, final Operand rightExpression) {
        if (leftExpression == null || rightExpression == null) {
            return null;
        }

        final Set<Operand> operands = new HashSet<Operand>();
        operands.add(leftExpression);
        operands.add(rightExpression);

        return new Operand(Operator.AND, operands);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Operand createOrExpression(final Operand leftExpression, final Operand rightExpression) {
        if (leftExpression == null || rightExpression == null) {
            return null;
        }

        final Set<Operand> operands = new HashSet<Operand>();
        operands.add(leftExpression);
        operands.add(rightExpression);

        return new Operand(Operator.OR, operands);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Operand createGreaterThanExpression(final GreaterThanFilter filter, final boolean not) {
        if (filter == null) {
            return null;
        }

        final String name = filter.getAttribute().getName();
        final String value = AttributeUtil.getAsStringValue(filter.getAttribute());
        if (StringUtil.isBlank(value)) {
            return null;
        }

        return new Operand(Operator.GT, name, value, not);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Operand createGreaterThanOrEqualExpression(final GreaterThanOrEqualFilter filter, final boolean not) {
        if (filter == null) {
            return null;
        }

        final String name = filter.getAttribute().getName();
        final String value = AttributeUtil.getAsStringValue(filter.getAttribute());
        if (StringUtil.isBlank(value)) {
            return null;
        }

        return new Operand(Operator.LT, name, value, !not);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Operand createLessThanExpression(final LessThanFilter filter, final boolean not) {
        if (filter == null) {
            return null;
        }

        final String name = filter.getAttribute().getName();
        final String value = AttributeUtil.getAsStringValue(filter.getAttribute());
        if (StringUtil.isBlank(value)) {
            return null;
        }

        return new Operand(Operator.LT, name, value, not);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Operand createLessThanOrEqualExpression(final LessThanOrEqualFilter filter, final boolean not) {
        if (filter == null) {
            return null;
        }

        final String name = filter.getAttribute().getName();
        final String value = AttributeUtil.getAsStringValue(filter.getAttribute());
        if (StringUtil.isBlank(value)) {
            return null;
        }

        return new Operand(Operator.GT, name, value, !not);
    }
}
