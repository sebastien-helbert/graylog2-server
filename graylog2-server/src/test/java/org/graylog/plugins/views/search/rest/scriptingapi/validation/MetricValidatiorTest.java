/*
 * Copyright (C) 2020 Graylog, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Server Side Public License, version 1,
 * as published by MongoDB, Inc.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Server Side Public License for more details.
 *
 * You should have received a copy of the Server Side Public License
 * along with this program. If not, see
 * <http://www.mongodb.com/licensing/server-side-public-license>.
 */
package org.graylog.plugins.views.search.rest.scriptingapi.validation;

import org.graylog.plugins.views.search.rest.SeriesDescription;
import org.graylog.plugins.views.search.rest.scriptingapi.request.Metric;
import org.graylog.plugins.views.search.searchtypes.pivot.SortSpec;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.ValidationException;
import java.util.Map;

import static org.junit.Assert.assertThrows;

class MetricValidatiorTest {

    private MetricValidator toTest;

    private final Map<String, SeriesDescription> AVAILABLE_FUNCTIONS = Map.of(
            "count", SeriesDescription.create("count", "nvmd"),
            "avg", SeriesDescription.create("avg", "nvmd")
    );

    @BeforeEach
    void setUp() {
        toTest = new MetricValidator(AVAILABLE_FUNCTIONS);
    }

    @Test
    void throwsExceptionOnNullMetric() {
        assertThrows(ValidationException.class, () -> toTest.validate(null));
    }

    @Test
    void throwsExceptionOnMetricWithNoFunctionName() {
        assertThrows(ValidationException.class, () -> toTest.validate(new Metric("field", null, SortSpec.Direction.Ascending)));
    }

    @Test
    void throwsExceptionOnMetricWithIllegalFunctionName() {
        assertThrows(ValidationException.class, () -> toTest.validate(new Metric("field", "bum", SortSpec.Direction.Ascending)));
    }

    @Test
    void throwsExceptionOnMetricMissingFieldName() {
        //check count metric is fine with no field
        toTest.validate(new Metric(null, "count", SortSpec.Direction.Ascending));
        //check other metrics throw exception on missing field
        assertThrows(ValidationException.class, () -> toTest.validate(new Metric("", "avg", SortSpec.Direction.Ascending)));
        assertThrows(ValidationException.class, () -> toTest.validate(new Metric(null, "avg", SortSpec.Direction.Ascending)));
    }

}


