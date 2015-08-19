package com.ouer.tools.generator.code;

import com.ouer.tools.generator.code.javamapper.OuerMapperGenerator;

import java.util.ArrayList;
import java.util.List;

import com.ouer.tools.generator.code.model.OuerModelGenerator;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;
import org.mybatis.generator.codegen.mybatis3.javamapper.AnnotatedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.MixedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.model.BaseRecordGenerator;
import org.mybatis.generator.codegen.mybatis3.model.ExampleGenerator;
import org.mybatis.generator.codegen.mybatis3.model.PrimaryKeyGenerator;
import org.mybatis.generator.codegen.mybatis3.model.RecordWithBLOBsGenerator;
import org.mybatis.generator.internal.ObjectFactory;

/**
 *
 * @author admin
 * @version V1.0
 * @since 2014-12-24 18:01
 */
public class OuerIntrospectedTable extends IntrospectedTableMyBatis3Impl {
    public OuerIntrospectedTable() {}

    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();

        if (xmlMapperGenerator != null) {
            Document document = xmlMapperGenerator.getDocument();
            GeneratedXmlFile gxf = new GeneratedXmlFile(document,
                    getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(),
                    context.getSqlMapGeneratorConfiguration().getTargetProject(),
                    false, context.getXmlFormatter());
            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }

        return answer;
    }

    protected void calculateXmlAttributes() {
        this.setIbatis2SqlMapPackage(this.calculateSqlMapPackage());
        this.setIbatis2SqlMapFileName(this.calculateIbatis2SqlMapFileName());
        this.setMyBatis3XmlMapperFileName(this.calculateMyBatis3XmlMapperFileName());
        this.setMyBatis3XmlMapperPackage(this.calculateSqlMapPackage());
        this.setIbatis2SqlMapNamespace(this.calculateIbatis2SqlMapNamespace());
        this.setMyBatis3FallbackSqlMapNamespace(this.calculateMyBatis3FallbackSqlMapNamespace());
        this.setSqlMapFullyQualifiedRuntimeTableName(this.calculateSqlMapFullyQualifiedRuntimeTableName());
        this.setSqlMapAliasedFullyQualifiedRuntimeTableName(this.calculateSqlMapAliasedFullyQualifiedRuntimeTableName());
        this.setCountByExampleStatementId("countByExample");
        this.setDeleteByExampleStatementId("deleteByExample");
        this.setDeleteByPrimaryKeyStatementId("delete");
        this.setInsertStatementId("insert");
        this.setInsertSelectiveStatementId("insertSelective");
        this.setSelectAllStatementId("selectAll");
        this.setSelectByExampleStatementId("selectByExample");
        this.setSelectByExampleWithBLOBsStatementId("selectByExampleWithBLOBs");
        this.setSelectByPrimaryKeyStatementId("get");
        this.setUpdateByExampleStatementId("updateByExample");
        this.setUpdateByExampleSelectiveStatementId("updateByExampleSelective");
        this.setUpdateByExampleWithBLOBsStatementId("updateByExampleWithBLOBs");
        this.setUpdateByPrimaryKeyStatementId("update");
        this.setUpdateByPrimaryKeySelectiveStatementId("update");
        this.setUpdateByPrimaryKeyWithBLOBsStatementId("updateByPrimaryKeyWithBLOBs");
        this.setBaseResultMapId(calculateMyBatis3MapName());
        this.setResultMapWithBLOBsId("ResultMapWithBLOBs");
        this.setExampleWhereClauseId("Example_Where_Clause");
        this.setBaseColumnListId("Base_Column_List");
        this.setBlobColumnListId("Blob_Column_List");
        this.setMyBatis3UpdateByExampleWhereClauseId("Update_By_Example_Where_Clause");
    }

    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if(this.context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        } else {
            String type = this.context.getJavaClientGeneratorConfiguration().getConfigurationType();
            Object javaGenerator;
            if("XMLMAPPER".equalsIgnoreCase(type)) {
                javaGenerator = new JavaMapperGenerator();
            } else if("MIXEDMAPPER".equalsIgnoreCase(type)) {
                javaGenerator = new MixedClientGenerator();
            } else if("ANNOTATEDMAPPER".equalsIgnoreCase(type)) {
                javaGenerator = new AnnotatedClientGenerator();
            } else if("MAPPER".equalsIgnoreCase(type)) {
                javaGenerator = new JavaMapperGenerator();
            } else if("OUERMAPPER".equalsIgnoreCase(type)){
                javaGenerator = new OuerMapperGenerator();
            } else {
                javaGenerator = (AbstractJavaClientGenerator) ObjectFactory.createInternalObject(type);
            }

            return (AbstractJavaClientGenerator)javaGenerator;
        }
    }

    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        AbstractJavaGenerator javaGenerator = new OuerModelGenerator();
        initializeAbstractGenerator(javaGenerator, warnings,
                progressCallback);
        javaModelGenerators.add(javaGenerator);
    }



    protected String calculateMyBatis3MapName() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
        sb.append("Map");
        return sb.toString();
    }

    protected void calculateModelAttributes() {
        String pakkage = this.calculateJavaModelPackage();
        StringBuilder sb = new StringBuilder();
        sb.append(pakkage);
        sb.append('.');
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
        sb.append("Key");
        this.setPrimaryKeyType(sb.toString());
        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
//        sb.append("PO");
        this.setBaseRecordType(sb.toString());
        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
        sb.append("WithBLOBs");
        this.setRecordWithBLOBsType(sb.toString());
        sb.setLength(0);
        sb.append(pakkage);
        sb.append('.');
        sb.append(this.fullyQualifiedTable.getDomainObjectName());
        sb.append("Example");
        this.setExampleType(sb.toString());
    }

//    protected void calculateJavaClientAttributes() {
//        if (context.getJavaClientGeneratorConfiguration() == null) {
//            return;
//        }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(calculateJavaClientImplementationPackage());
//        sb.append('.');
//        sb.append(fullyQualifiedTable.getDomainObjectName());
//        sb.append("DAOImpl"); //$NON-NLS-1$
//        setDAOImplementationType(sb.toString());
//
//        sb.setLength(0);
//        sb.append(calculateJavaClientInterfacePackage());
//        sb.append('.');
//        sb.append(fullyQualifiedTable.getDomainObjectName());
//        sb.append("DAO"); //$NON-NLS-1$
//        setDAOInterfaceType(sb.toString());
//
//        sb.setLength(0);
//        sb.append(calculateJavaClientInterfacePackage());
//        sb.append('.');
//        sb.append(fullyQualifiedTable.getDomainObjectName());
//        sb.append("DAO"); //$NON-NLS-1$
//        setMyBatis3JavaMapperType(sb.toString());
//
//        sb.setLength(0);
//        sb.append(calculateJavaClientInterfacePackage());
//        sb.append('.');
//        sb.append(fullyQualifiedTable.getDomainObjectName());
//        sb.append("SqlProvider"); //$NON-NLS-1$
//        setMyBatis3SqlProviderType(sb.toString());
//    }

}
