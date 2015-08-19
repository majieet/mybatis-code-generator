package com.ouer.tools.generator.code.xmlmapper.elements;

import com.ouer.tools.generator.code.OuerMyBatis3FormattingUtilities;

import java.util.Iterator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.StringUtility;

/**
 *
 * @author admin
 * @version V1.0
 * @since 2014-12-24 13:14
 */
public class ListGenerator extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "list"));
        String parameterType;
        if(this.introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            parameterType = this.introspectedTable.getRecordWithBLOBsType();
        } else {
            parameterType = this.introspectedTable.getBaseRecordType();
        }

        answer.addAttribute(new Attribute("resultMap", this.introspectedTable.getBaseResultMapId()));
        answer.addAttribute(new Attribute("parameterType", parameterType));
        this.context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("select ");
        if(StringUtility.stringHasValue(this.introspectedTable.getSelectByPrimaryKeyQueryId())) {
            sb.append('\'');
            sb.append(this.introspectedTable.getSelectByPrimaryKeyQueryId());
            sb.append("\' as QUERYID,");
        }

        answer.addElement(new TextElement(sb.toString()));
        answer.addElement(this.getBaseColumnListElement());
        if(this.introspectedTable.hasBLOBColumns()) {
            answer.addElement(new TextElement(","));
            answer.addElement(this.getBlobColumnListElement());
        }

        sb.setLength(0);
        sb.append("from ");
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        XmlElement dynamicElement = new XmlElement("trim");
        dynamicElement.addAttribute(new Attribute("prefix", "where"));
        dynamicElement.addAttribute(new Attribute("prefixOverrides", "AND|OR"));
        answer.addElement(dynamicElement);
        Iterator and = this.introspectedTable.getNonPrimaryKeyColumns().iterator();

        while(and.hasNext()) {
            IntrospectedColumn i$ = (IntrospectedColumn)and.next();
            XmlElement introspectedColumn = new XmlElement("if");
            sb.setLength(0);
            sb.append(i$.getJavaProperty());
            sb.append(" != null");
            if(i$.getFullyQualifiedJavaType().getFullyQualifiedName().equals("java.lang.String")){
                sb.append(" and ");
                sb.append(i$.getJavaProperty());
                sb.append(" != ''");
            }
            introspectedColumn.addAttribute(new Attribute("test", sb.toString()));
            dynamicElement.addElement(introspectedColumn);
            sb.setLength(0);
            sb.append("and ");
            sb.append(OuerMyBatis3FormattingUtilities.getEscapedColumnName(i$));
            sb.append(" = ");
            sb.append(OuerMyBatis3FormattingUtilities.getParameterClause(i$));
            introspectedColumn.addElement(new TextElement(sb.toString()));
        }


        if(this.context.getPlugins().sqlMapUpdateByPrimaryKeySelectiveElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}
