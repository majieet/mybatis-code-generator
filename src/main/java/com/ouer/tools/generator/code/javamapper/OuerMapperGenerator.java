package com.ouer.tools.generator.code.javamapper;

import com.ouer.tools.generator.code.javamapper.elements.InsertMethodGenerator;
import com.ouer.tools.generator.code.javamapper.elements.ListMethodGenerator;
import com.ouer.tools.generator.code.javamapper.elements.UpdateMethodGenerator;
import com.ouer.tools.generator.code.xmlmapper.OuerXMLMapperGenerator;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.CountByExampleMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.DeleteByExampleMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.DeleteByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.InsertSelectiveMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.SelectByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.SelectByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.SelectByPrimaryKeyMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.UpdateByExampleSelectiveMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.UpdateByExampleWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.UpdateByExampleWithoutBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.UpdateByPrimaryKeyWithBLOBsMethodGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.UpdateByPrimaryKeyWithoutBLOBsMethodGenerator;
import org.mybatis.generator.internal.util.StringUtility;

/**
 *
 * @author admin
 * @version V1.0
 * @since 2014-12-24 09:36
 */
public class OuerMapperGenerator extends AbstractJavaClientGenerator {

    public OuerMapperGenerator() {
        super(true);
    }

    public OuerMapperGenerator(boolean requiresMatchedXMLGenerator) {
        super(requiresMatchedXMLGenerator);
    }

    public List<CompilationUnit> getCompilationUnits() {
        CommentGenerator commentGenerator = this.context.getCommentGenerator();
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(this.introspectedTable.getMyBatis3JavaMapperType());
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addJavaFileComment(interfaze);
        String rootInterface = this.introspectedTable.getTableConfigurationProperty("rootInterface");
        if(!StringUtility.stringHasValue(rootInterface)) {
            rootInterface = this.context.getJavaClientGeneratorConfiguration().getProperty("rootInterface");
        }

        if(StringUtility.stringHasValue(rootInterface)) {
            FullyQualifiedJavaType answer = new FullyQualifiedJavaType(rootInterface);
            FullyQualifiedJavaType returnType = introspectedTable.getRules()
                    .calculateAllFieldsClass();
            answer.addTypeArgument(returnType);
            answer.addTypeArgument(FullyQualifiedJavaType.getStringInstance());

            interfaze.addSuperInterface(answer);
            interfaze.addImportedType(answer);
        }

//        this.addSelectByPrimaryKeyMethod(interfaze);
//        this.addInsertMethod(interfaze);
//        this.addDeleteByPrimaryKeyMethod(interfaze);
//        this.addUpdateByPrimaryKeySelectiveMethod(interfaze);
//        this.addListMethod(interfaze);
        ArrayList answer1 = new ArrayList();
        if(this.context.getPlugins().clientGenerated(interfaze, (TopLevelClass)null, this.introspectedTable)) {
            answer1.add(interfaze);
        }

        List extraCompilationUnits = this.getExtraCompilationUnits();
        if(extraCompilationUnits != null) {
            answer1.addAll(extraCompilationUnits);
        }

        return answer1;
    }

    protected void addCountByExampleMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateCountByExample()) {
            CountByExampleMethodGenerator methodGenerator = new CountByExampleMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addDeleteByExampleMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateDeleteByExample()) {
            DeleteByExampleMethodGenerator methodGenerator = new DeleteByExampleMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addDeleteByPrimaryKeyMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateDeleteByPrimaryKey()) {
            DeleteByPrimaryKeyMethodGenerator methodGenerator = new DeleteByPrimaryKeyMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addInsertMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateInsert()) {
            InsertMethodGenerator methodGenerator = new InsertMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addInsertSelectiveMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateInsertSelective()) {
            InsertSelectiveMethodGenerator methodGenerator = new InsertSelectiveMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addSelectByExampleWithBLOBsMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateSelectByExampleWithBLOBs()) {
            SelectByExampleWithBLOBsMethodGenerator methodGenerator = new SelectByExampleWithBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addSelectByExampleWithoutBLOBsMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateSelectByExampleWithoutBLOBs()) {
            SelectByExampleWithoutBLOBsMethodGenerator methodGenerator = new SelectByExampleWithoutBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addSelectByPrimaryKeyMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateSelectByPrimaryKey()) {
            SelectByPrimaryKeyMethodGenerator methodGenerator = new SelectByPrimaryKeyMethodGenerator(false);
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addUpdateByExampleSelectiveMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateUpdateByExampleSelective()) {
            UpdateByExampleSelectiveMethodGenerator methodGenerator = new UpdateByExampleSelectiveMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addUpdateByExampleWithBLOBsMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateUpdateByExampleWithBLOBs()) {
            UpdateByExampleWithBLOBsMethodGenerator methodGenerator = new UpdateByExampleWithBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addUpdateByExampleWithoutBLOBsMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateUpdateByExampleWithoutBLOBs()) {
            UpdateByExampleWithoutBLOBsMethodGenerator methodGenerator = new UpdateByExampleWithoutBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addUpdateByPrimaryKeySelectiveMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
            UpdateMethodGenerator methodGenerator = new UpdateMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addUpdateByPrimaryKeyWithBLOBsMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithBLOBs()) {
            UpdateByPrimaryKeyWithBLOBsMethodGenerator methodGenerator = new UpdateByPrimaryKeyWithBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addUpdateByPrimaryKeyWithoutBLOBsMethod(Interface interfaze) {
        if(this.introspectedTable.getRules().generateUpdateByPrimaryKeyWithoutBLOBs()) {
            UpdateByPrimaryKeyWithoutBLOBsMethodGenerator methodGenerator = new UpdateByPrimaryKeyWithoutBLOBsMethodGenerator();
            this.initializeAndExecuteGenerator(methodGenerator, interfaze);
        }

    }

    protected void addListMethod(Interface interfaze) {
        ListMethodGenerator methodGenerator = new ListMethodGenerator(false);
        this.initializeAndExecuteGenerator(methodGenerator, interfaze);

    }

    protected void initializeAndExecuteGenerator(AbstractJavaMapperMethodGenerator methodGenerator, Interface interfaze) {
        methodGenerator.setContext(this.context);
        methodGenerator.setIntrospectedTable(this.introspectedTable);
        methodGenerator.setProgressCallback(this.progressCallback);
        methodGenerator.setWarnings(this.warnings);
        methodGenerator.addInterfaceElements(interfaze);
    }

    public List<CompilationUnit> getExtraCompilationUnits() {
        return null;
    }

    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new OuerXMLMapperGenerator();
    }

}
