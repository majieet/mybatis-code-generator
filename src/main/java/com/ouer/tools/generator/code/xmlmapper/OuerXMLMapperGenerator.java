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
package com.ouer.tools.generator.code.xmlmapper;

import com.ouer.tools.generator.code.xmlmapper.elements.DeleteGenerator;
import com.ouer.tools.generator.code.xmlmapper.elements.GetGenerator;
import com.ouer.tools.generator.code.xmlmapper.elements.InsertGenerator;
import com.ouer.tools.generator.code.xmlmapper.elements.ListGenerator;
import com.ouer.tools.generator.code.xmlmapper.elements.ResultMapWithoutBLOBsElementGenerator;
import com.ouer.tools.generator.code.xmlmapper.elements.UpdateGenerator;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.*;
import org.mybatis.generator.internal.util.messages.Messages;

/**
 *
 * @author admin
 * @version V1.0
 * @since 2014-12-24 09:50
 */
public class OuerXMLMapperGenerator extends AbstractXmlGenerator {
    public OuerXMLMapperGenerator(){}

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.12", table.toString()));
        XmlElement answer = new XmlElement("mapper");
        String namespace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));
        this.context.getCommentGenerator().addRootComment(answer);

        this.addResultMapWithoutBLOBsElement(answer);
        this.addResultMapWithBLOBsElement(answer);
        answer.addElement(new TextElement(""));

        this.addBaseColumnListElement(answer);
        this.addBlobColumnListElement(answer);
        answer.addElement(new TextElement(""));

        this.addSelectByPrimaryKeyElement(answer);
        answer.addElement(new TextElement(""));

        this.addInsertElement(answer);
        answer.addElement(new TextElement(""));

        this.addDeleteByPrimaryKeyElement(answer);
        answer.addElement(new TextElement(""));

        this.addUpdateByPrimaryKeySelectiveElement(answer);
        answer.addElement(new TextElement(""));

        this.addListElement(answer);
        return answer;
    }

    protected void addResultMapWithoutBLOBsElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateBaseResultMap()) {
            ResultMapWithoutBLOBsElementGenerator elementGenerator = new ResultMapWithoutBLOBsElementGenerator(false);
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addResultMapWithBLOBsElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateResultMapWithBLOBs()) {
            ResultMapWithBLOBsElementGenerator elementGenerator = new ResultMapWithBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }
    }

    protected void addExampleWhereClauseElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateSQLExampleWhereClause()) {
            ExampleWhereClauseElementGenerator elementGenerator = new ExampleWhereClauseElementGenerator(false);
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addMyBatis3UpdateByExampleWhereClauseElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateMyBatis3UpdateByExampleWhereClause()) {
            ExampleWhereClauseElementGenerator elementGenerator = new ExampleWhereClauseElementGenerator(true);
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addBaseColumnListElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateBaseColumnList()) {
            BaseColumnListElementGenerator elementGenerator = new BaseColumnListElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addBlobColumnListElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateBlobColumnList()) {
            BlobColumnListElementGenerator elementGenerator = new BlobColumnListElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addSelectByExampleWithoutBLOBsElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            SelectByExampleWithoutBLOBsElementGenerator elementGenerator = new SelectByExampleWithoutBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addSelectByExampleWithBLOBsElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            SelectByExampleWithBLOBsElementGenerator elementGenerator = new SelectByExampleWithBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addSelectByPrimaryKeyElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            GetGenerator elementGenerator = new GetGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addDeleteByExampleElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateDeleteByExample()) {
            DeleteByExampleElementGenerator elementGenerator = new DeleteByExampleElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addDeleteByPrimaryKeyElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            DeleteGenerator elementGenerator = new DeleteGenerator(false);
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addInsertElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateInsert()) {
            InsertGenerator elementGenerator = new InsertGenerator(false);
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addInsertSelectiveElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateInsertSelective()) {
            InsertSelectiveElementGenerator elementGenerator = new InsertSelectiveElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addCountByExampleElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateCountByExample()) {
            CountByExampleElementGenerator elementGenerator = new CountByExampleElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addUpdateByExampleSelectiveElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateUpdateByExampleSelective()) {
            UpdateByExampleSelectiveElementGenerator elementGenerator = new UpdateByExampleSelectiveElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addUpdateByExampleWithBLOBsElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            UpdateByExampleWithBLOBsElementGenerator elementGenerator = new UpdateByExampleWithBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addUpdateByExampleWithoutBLOBsElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            UpdateByExampleWithoutBLOBsElementGenerator elementGenerator = new UpdateByExampleWithoutBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addUpdateByPrimaryKeySelectiveElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            UpdateGenerator elementGenerator = new UpdateGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addUpdateByPrimaryKeyWithBLOBsElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            UpdateByPrimaryKeyWithBLOBsElementGenerator elementGenerator = new UpdateByPrimaryKeyWithBLOBsElementGenerator();
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addUpdateByPrimaryKeyWithoutBLOBsElement(XmlElement parentElement) {
        if(this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            UpdateByPrimaryKeyWithoutBLOBsElementGenerator elementGenerator = new UpdateByPrimaryKeyWithoutBLOBsElementGenerator(false);
            this.initializeAndExecuteGenerator(elementGenerator, parentElement);
        }

    }

    protected void addListElement(XmlElement parentElement) {
        ListGenerator elementGenerator = new ListGenerator();
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);

    }

    protected void initializeAndExecuteGenerator(AbstractXmlElementGenerator elementGenerator, XmlElement parentElement) {
        elementGenerator.setContext(this.context);
        elementGenerator.setIntrospectedTable(this.introspectedTable);
        elementGenerator.setProgressCallback(this.progressCallback);
        elementGenerator.setWarnings(this.warnings);
        elementGenerator.addElements(parentElement);
    }

    public Document getDocument() {
        Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN", "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
        document.setRootElement(this.getSqlMapElement());
        if(!this.context.getPlugins().sqlMapDocumentGenerated(document, this.introspectedTable)) {
            document = null;
        }

        return document;
    }
}
