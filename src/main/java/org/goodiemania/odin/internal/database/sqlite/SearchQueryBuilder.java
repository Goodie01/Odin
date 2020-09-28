package org.goodiemania.odin.internal.database.sqlite;

import java.util.List;
import org.goodiemania.odin.external.model.SearchTerm;
import org.goodiemania.odin.internal.manager.classinfo.ClassInfo;

public class SearchQueryBuilder {
    public String build(final ClassInfo<?> classInfo, final List<SearchTerm> searchTerms) {
        StringBuilder queryString = new StringBuilder("select objectId from ").append(classInfo.getSearchTableName()).append(" where ");
        for (int i = 0; i < searchTerms.size(); i++) {
            if (i != 0) {
                queryString.append(" or ");
            }

            queryString.append("(fieldName like :fieldName").append(i).append(" and fieldValue like :value").append(i).append(")");
        }
        return queryString.toString();
    }
}
