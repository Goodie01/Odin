package org.goodiemania.odin.external.model;

public class SearchTermAndOr implements SearchTerm {
    private final SearchTerm firstTerm;
    private final SearchTerm secondTerm;
    private final SearchType searchType;

    protected SearchTermAndOr(final SearchTerm firstTerm, final SearchTerm secondTerm, final SearchType searchType) {
        this.firstTerm = firstTerm;
        this.secondTerm = secondTerm;
        this.searchType = searchType;
    }

    public enum SearchType {
        AND("and"),
        OR("or");

        private final String operand;

        SearchType(final String operand) {

            this.operand = operand;
        }

        public String getOperand() {
            return operand;
        }
    }

    public SearchTerm getFirstTerm() {
        return firstTerm;
    }

    public SearchTerm getSecondTerm() {
        return secondTerm;
    }

    public SearchType getSearchType() {
        return searchType;
    }
}
