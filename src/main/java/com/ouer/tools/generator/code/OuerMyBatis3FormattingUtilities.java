/*
 * Project: test
 * 
 * File Created at 2014-12-24
 
 * Copyright 2012 Greenline.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Greenline Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Greenline.com.
 */
package com.ouer.tools.generator.code;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.internal.util.StringUtility;

/**
 *
 * @author admin
 * @version V1.0
 * @since 2014-12-24 10:45
 */
public class OuerMyBatis3FormattingUtilities {
    public static String getParameterClause(IntrospectedColumn introspectedColumn) {
        return getParameterClause(introspectedColumn, (String)null);
    }

    public static String getParameterClause(IntrospectedColumn introspectedColumn, String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append("#{");
        sb.append(introspectedColumn.getJavaProperty(prefix));
        if(StringUtility.stringHasValue(introspectedColumn.getTypeHandler())) {
            sb.append(",typeHandler=");
            sb.append(introspectedColumn.getTypeHandler());
        }

        sb.append('}');
        return sb.toString();
    }

    public static String getSelectListPhrase(IntrospectedColumn introspectedColumn) {
        if(StringUtility.stringHasValue(introspectedColumn.getTableAlias())) {
            StringBuilder sb = new StringBuilder();
            sb.append(getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" as ");
            if(introspectedColumn.isColumnNameDelimited()) {
                sb.append(introspectedColumn.getContext().getBeginningDelimiter());
            }

            sb.append(introspectedColumn.getTableAlias());
            sb.append('_');
            sb.append(escapeStringForMyBatis3(introspectedColumn.getActualColumnName()));
            if(introspectedColumn.isColumnNameDelimited()) {
                sb.append(introspectedColumn.getContext().getEndingDelimiter());
            }

            return sb.toString();
        } else {
            return getEscapedColumnName(introspectedColumn);
        }
    }

    public static String getEscapedColumnName(IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        sb.append(escapeStringForMyBatis3(introspectedColumn.getActualColumnName()));
        if(introspectedColumn.isColumnNameDelimited()) {
            sb.insert(0, introspectedColumn.getContext().getBeginningDelimiter());
            sb.append(introspectedColumn.getContext().getEndingDelimiter());
        }

        return sb.toString();
    }

    public static String getAliasedEscapedColumnName(IntrospectedColumn introspectedColumn) {
        if(StringUtility.stringHasValue(introspectedColumn.getTableAlias())) {
            StringBuilder sb = new StringBuilder();
            sb.append(introspectedColumn.getTableAlias());
            sb.append('.');
            sb.append(getEscapedColumnName(introspectedColumn));
            return sb.toString();
        } else {
            return getEscapedColumnName(introspectedColumn);
        }
    }

    public static String getAliasedActualColumnName(IntrospectedColumn introspectedColumn) {
        StringBuilder sb = new StringBuilder();
        if(StringUtility.stringHasValue(introspectedColumn.getTableAlias())) {
            sb.append(introspectedColumn.getTableAlias());
            sb.append('.');
        }

        if(introspectedColumn.isColumnNameDelimited()) {
            sb.append(StringUtility.escapeStringForJava(introspectedColumn.getContext().getBeginningDelimiter()));
        }

        sb.append(introspectedColumn.getActualColumnName());
        if(introspectedColumn.isColumnNameDelimited()) {
            sb.append(StringUtility.escapeStringForJava(introspectedColumn.getContext().getEndingDelimiter()));
        }

        return sb.toString();
    }

    public static String getRenamedColumnNameForResultMap(IntrospectedColumn introspectedColumn) {
        if(StringUtility.stringHasValue(introspectedColumn.getTableAlias())) {
            StringBuilder sb = new StringBuilder();
            sb.append(introspectedColumn.getTableAlias());
            sb.append('_');
            sb.append(introspectedColumn.getActualColumnName());
            return sb.toString();
        } else {
            return introspectedColumn.getActualColumnName();
        }
    }

    public static String escapeStringForMyBatis3(String s) {
        return s;
    }

    public static String getFirstLowerCaseString(String s){
        return s.substring(0,1).toLowerCase() + s.substring(1);
    }
}
