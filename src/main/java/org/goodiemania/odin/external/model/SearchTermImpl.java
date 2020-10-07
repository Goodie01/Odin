package org.goodiemania.odin.external.model;

public class SearchTermImpl implements SearchTerm {
    private final String field;
    private final String value;
    private final SearchType operation;

    protected SearchTermImpl(final String field, final String value, final SearchType operation) {
        this.field = field;
        this.value = value;
        this.operation = operation;
    }

    public enum SearchType {
        EQUALS("like"),
        NOT_EQUALS("not like");

        private final String operand;

        SearchType(final String operand) {
            this.operand = operand;
        }

        public String getOperand() {
            return operand;
        }
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        return value;
    }

    public SearchType getOperation() {
        return operation;
    }
}
