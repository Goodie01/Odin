package org.goodiemania.odin.internal.database;

import java.util.ArrayList;
import java.util.List;
import org.goodiemania.odin.external.exceptions.ShouldNeverHappenException;
import org.goodiemania.odin.external.model.SearchTerm;
import org.goodiemania.odin.external.model.SearchTermAndOr;
import org.goodiemania.odin.external.model.SearchTermImpl;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;

public class SearchTermProcessor {
    private final String queryString;
    private final List<ParameterPair> params = new ArrayList<>();
    private int count = 0;

    public SearchTermProcessor(final ClassInfo<?> classInfo, final SearchTerm searchTerm) {
        StringBuilder queryString = new StringBuilder("select jsonBlob from ")
                .append(classInfo.getTableName())
                .append(" where ");

        queryString.append(processSearchTerm(classInfo, searchTerm));

        this.queryString = queryString.toString();
    }

    private String processSearchTerm(final ClassInfo<?> classInfo, final SearchTerm searchTerm) {
        if (searchTerm instanceof SearchTermImpl) {
            final SearchTermImpl term = (SearchTermImpl) searchTerm;

            addToParameterStack(term.getField(), term.getValue());

            return "id in ("
                    + "select objectId "
                    + "from " + classInfo.getSearchTableName() + " "
                    + "where fieldName like :fieldName" + count + " and fieldValue " + term.getOperation().getOperand() + " :value" + count
                    + ")";
        } else if (searchTerm instanceof SearchTermAndOr) {
            final SearchTermAndOr term = (SearchTermAndOr) searchTerm;

            return processSearchTerm(classInfo, term.getFirstTerm())
                    + " " + term.getSearchType().getOperand() + " "
                    + processSearchTerm(classInfo, term.getSecondTerm());
        }

        throw new ShouldNeverHappenException(new IllegalStateException(""));
    }

    private void addToParameterStack(final String field, final String value) {
        params.add(new ParameterPair(field, value, ++count));
    }

    public String getQueryString() {
        return queryString;
    }

    public List<ParameterPair> getParams() {
        return params;
    }
}
