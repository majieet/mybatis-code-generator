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
 * @since 2014-12-24 12:32
 */
public class UpdateGenerator extends AbstractXmlElementGenerator {
    public UpdateGenerator() {
    }

    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getUpdateByPrimaryKeySelectiveStatementId()));
        String parameterType;
        if(this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = this.introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = this.introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("parameterType", parameterType));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        sb.append("update ");
        sb.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));
        XmlElement dynamicElement = new XmlElement("set");
        answer.addElement(dynamicElement);
        Iterator and = this.introspectedTable.getNonPrimaryKeyColumns().iterator();

        while(and.hasNext()) {
            IntrospectedColumn i$ = (IntrospectedColumn)and.next();
            XmlElement introspectedColumn = new XmlElement("if");
            sb.setLength(0);
            sb.append(i$.getJavaProperty());
            sb.append(" != null");
            introspectedColumn.addAttribute(new Attribute("test", sb.toString()));
            dynamicElement.addElement(introspectedColumn);
            sb.setLength(0);
            sb.append(OuerMyBatis3FormattingUtilities.getEscapedColumnName(i$));
            sb.append(" = ");
            sb.append(OuerMyBatis3FormattingUtilities.getParameterClause(i$));
            sb.append(',');
            introspectedColumn.addElement(new TextElement(sb.toString()));
        }

        boolean and1 = false;
        Iterator i$1 = this.introspectedTable.getPrimaryKeyColumns().iterator();

        while(i$1.hasNext()) {
            IntrospectedColumn introspectedColumn1 = (IntrospectedColumn)i$1.next();
            sb.setLength(0);
            if(and1) {
                sb.append("  and ");
            } else {
                sb.append("where ");
                and1 = true;
            }

            sb.append(OuerMyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn1));
            sb.append(" = ");
            sb.append(OuerMyBatis3FormattingUtilities.getParameterClause(introspectedColumn1));
            answer.addElement(new TextElement(sb.toString()));
        }

        if(this.context.getPlugins().sqlMapUpdateByPrimaryKeySelectiveElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}
