package com.ouer.tools.generator.code.xmlmapper.elements;

import com.ouer.tools.generator.code.OuerMyBatis3FormattingUtilities;

import java.util.ArrayList;
import java.util.Iterator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.config.GeneratedKey;

/**
 *
 * @author admin
 * @version V1.0
 * @since 2014-12-24 11:18
 */
public class InsertGenerator extends AbstractXmlElementGenerator {
    private boolean isSimple;

    public InsertGenerator(boolean isSimple) {
        this.isSimple = isSimple;
    }

    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", this.introspectedTable.getInsertStatementId()));
        FullyQualifiedJavaType parameterType;
        if(this.isSimple) {
            parameterType = new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType());
        } else {
            parameterType = this.introspectedTable.getRules().calculateAllFieldsClass();
        }

        answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName()));
        this.context.getCommentGenerator().addComment(answer);
        GeneratedKey gk = this.introspectedTable.getGeneratedKey();
        if(gk != null) {
            IntrospectedColumn insertClause = this.introspectedTable.getColumn(gk.getColumn());
            if(insertClause != null) {
                if(gk.isJdbcStandard()) {
                    answer.addAttribute(new Attribute("useGeneratedKeys", "true"));
                    answer.addAttribute(new Attribute("keyProperty", insertClause.getJavaProperty()));
                } else {
                    answer.addElement(this.getSelectKey(insertClause, gk));
                }
            }
        }
        answer.addAttribute(new Attribute("useGeneratedKeys", "true"));
        answer.addAttribute(new Attribute("keyColumn", this.introspectedTable.getPrimaryKeyColumns().get(0).getActualColumnName()));
        answer.addAttribute(new Attribute("keyProperty",  "idRaw"));

        StringBuilder insertClause1 = new StringBuilder();
        StringBuilder valuesClause = new StringBuilder();
        insertClause1.append("insert into ");
        insertClause1.append(this.introspectedTable.getFullyQualifiedTableNameAtRuntime());
        insertClause1.append(" (");
        valuesClause.append("values (");
        ArrayList valuesClauses = new ArrayList();
        Iterator iter = this.introspectedTable.getNonPrimaryKeyColumns().iterator();

        while(iter.hasNext()) {
            IntrospectedColumn i$ = (IntrospectedColumn)iter.next();
            if(!i$.isIdentity()) {
                String columnName= OuerMyBatis3FormattingUtilities.getEscapedColumnName(i$);
                insertClause1.append(OuerMyBatis3FormattingUtilities.getEscapedColumnName(i$));
                if(columnName.equals("created_at") || columnName.equals("updated_at")){
                    valuesClause.append("now()");
                }else{
                    valuesClause.append(OuerMyBatis3FormattingUtilities.getParameterClause(i$));
                }
                if(iter.hasNext()) {
                    insertClause1.append(", ");
                    valuesClause.append(", ");
                }

                if(valuesClause.length() > 80) {
                    answer.addElement(new TextElement(insertClause1.toString()));
                    insertClause1.setLength(0);
                    OutputUtilities.xmlIndent(insertClause1, 1);
                    valuesClauses.add(valuesClause.toString());
                    valuesClause.setLength(0);
                    OutputUtilities.xmlIndent(valuesClause, 1);
                }
            }
        }

        insertClause1.append(')');
        answer.addElement(new TextElement(insertClause1.toString()));
        valuesClause.append(')');
        valuesClauses.add(valuesClause.toString());
        Iterator i$1 = valuesClauses.iterator();

        while(i$1.hasNext()) {
            String clause = (String)i$1.next();
            answer.addElement(new TextElement(clause));
        }

        if(this.context.getPlugins().sqlMapInsertElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}
