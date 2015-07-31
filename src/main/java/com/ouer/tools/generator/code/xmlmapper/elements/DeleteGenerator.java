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
package com.ouer.tools.generator.code.xmlmapper.elements;

import com.ouer.tools.generator.code.OuerMyBatis3FormattingUtilities;

import java.util.Iterator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 *
 * @author admin
 * @version V1.0
 * @since 2014-12-24 13:10
 */
public class DeleteGenerator extends AbstractXmlElementGenerator {
    private boolean isSimple;

    public DeleteGenerator(boolean isSimple) {
        this.isSimple = isSimple;
    }

    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("delete");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getDeleteByPrimaryKeyStatementId()));
        String parameterClass;
        if(!this.isSimple && this.introspectedTable.getRules().generatePrimaryKeyClass()) {
            parameterClass = this.introspectedTable.getPrimaryKeyType();
        } else if(this.introspectedTable.getPrimaryKeyColumns().size() > 1) {
            parameterClass = "map";
        } else {
            parameterClass = ((IntrospectedColumn)this.introspectedTable.getPrimaryKeyColumns().get(0)).getFullyQualifiedJavaType().toString();
        }

        answer.addAttribute(new Attribute("parameterType", parameterClass));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        sb.append("delete from ");
        sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        boolean and = false;
        Iterator i$ = this.introspectedTable.getPrimaryKeyColumns().iterator();

        while(i$.hasNext()) {
            IntrospectedColumn introspectedColumn = (IntrospectedColumn)i$.next();
            sb.setLength(0);
            if(and) {
                sb.append("  and ");
            } else {
                sb.append("where ");
                and = true;
            }

            sb.append(OuerMyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(OuerMyBatis3FormattingUtilities.getParameterClause(introspectedColumn));
            answer.addElement(new TextElement(sb.toString()));
        }

        if(this.context.getPlugins().sqlMapDeleteByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}
