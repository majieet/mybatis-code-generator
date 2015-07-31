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

import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.StringUtility;

/**
 *
 * @author admin
 * @version V1.0
 * @since 2014-12-24 09:54
 */
public class ResultMapWithoutBLOBsElementGenerator extends AbstractXmlElementGenerator {
    private boolean isSimple;

    public ResultMapWithoutBLOBsElementGenerator(boolean isSimple) {
        this.isSimple = isSimple;
    }

    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("resultMap");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getBaseResultMapId()));
        String returnType;
        returnType = this.introspectedTable.getBaseRecordType();

        answer.addAttribute(new Attribute("type", returnType));
        this.context.getCommentGenerator().addComment(answer);
        this.addResultMapElements(answer);

        parentElement.addElement(answer);

    }

    private void addResultMapElements(XmlElement answer) {
        XmlElement introspectedColumn;
        for(Iterator columns = this.introspectedTable.getPrimaryKeyColumns().iterator(); columns.hasNext(); answer.addElement(introspectedColumn)) {
            IntrospectedColumn i$ = (IntrospectedColumn)columns.next();
            introspectedColumn = new XmlElement("id");
            introspectedColumn.addAttribute(new Attribute("column", MyBatis3FormattingUtilities
                    .getRenamedColumnNameForResultMap(i$)));
            introspectedColumn.addAttribute(new Attribute("property", i$.getJavaProperty()));
            if(StringUtility.stringHasValue(i$.getTypeHandler())) {
                introspectedColumn.addAttribute(new Attribute("typeHandler", i$.getTypeHandler()));
            }
        }

        List columns1;
        if(this.isSimple) {
            columns1 = this.introspectedTable.getNonPrimaryKeyColumns();
        } else {
            columns1 = this.introspectedTable.getBaseColumns();
        }

        XmlElement resultElement;
        for(Iterator i$1 = columns1.iterator(); i$1.hasNext(); answer.addElement(resultElement)) {
            IntrospectedColumn introspectedColumn1 = (IntrospectedColumn)i$1.next();
            resultElement = new XmlElement("result");
            resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn1)));
            resultElement.addAttribute(new Attribute("property", introspectedColumn1.getJavaProperty()));
            if(StringUtility.stringHasValue(introspectedColumn1.getTypeHandler())) {
                resultElement.addAttribute(new Attribute("typeHandler", introspectedColumn1.getTypeHandler()));
            }
        }

    }

}
