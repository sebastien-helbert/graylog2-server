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
package org.graylog.plugins.views.search.searchfilters.db;

import org.graylog.plugins.views.search.Query;
import org.graylog.plugins.views.search.Search;
import org.graylog.plugins.views.search.searchfilters.model.ReferencedSearchFilter;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchFilterVisibilityChecker {

    public SearchFilterVisibilityCheckStatus checkSearchFilterVisibility(final Predicate<String> readPermissionPredicate, final Search search) {
        final List<String> hiddenSearchFiltersIDs = search.queries()
                .stream()
                .filter(Objects::nonNull)
                .map(Query::filters)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(usedSearchFilter -> usedSearchFilter instanceof ReferencedSearchFilter)
                .map(usedSearchFilter -> (ReferencedSearchFilter) usedSearchFilter)
                .map(ReferencedSearchFilter::id)
                .filter(id -> !readPermissionPredicate.test(id))
                .collect(Collectors.toList());

        return new SearchFilterVisibilityCheckStatus(hiddenSearchFiltersIDs);
    }
}
