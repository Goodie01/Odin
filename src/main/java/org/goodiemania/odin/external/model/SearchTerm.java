package org.goodiemania.odin.external.model;

public interface SearchTerm {
    static SearchTerm equals(final String field, final String value) {
        return new SearchTermImpl(field, value, SearchTermImpl.SearchType.EQUALS);
    }

    static SearchTerm notEquals(final String field, final String value) {
        return new SearchTermImpl(field, value, SearchTermImpl.SearchType.NOT_EQUALS);
    }

    static SearchTerm and(final SearchTerm firstTerm, final SearchTerm secondTerm) {
        return new SearchTermAndOr(firstTerm, secondTerm, SearchTermAndOr.SearchType.AND);
    }

    static SearchTerm or(final SearchTerm firstTerm, final SearchTerm secondTerm) {
        return new SearchTermAndOr(firstTerm, secondTerm, SearchTermAndOr.SearchType.OR);
    }
}
