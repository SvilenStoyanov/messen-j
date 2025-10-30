package com.svistoyanov.mj;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import java.util.EnumSet;

public class SchemaProvider {

    public static void provideSchemaInDb(ServiceRegistry serviceRegistry) {
        provideSchemaInDb(serviceRegistry, TargetType.DATABASE, null);
    }

    public static void provideSchemaInFile(ServiceRegistry serviceRegistry) {
        provideSchemaInDb(serviceRegistry, TargetType.SCRIPT, "target/init.sql");
    }

    private static void provideSchemaInDb(ServiceRegistry serviceRegistry, TargetType targetType, String fileName) {
        MetadataSources metadataSources =
                new MetadataSources(serviceRegistry);
        Metadata metadata = metadataSources.buildMetadata();
        EnumSet<TargetType> enumSet = EnumSet.of(targetType);
        SchemaExport.Action action = SchemaExport.Action.BOTH;

        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setFormat(true);
        schemaExport.setDelimiter(";");
        schemaExport.setHaltOnError(true);
        schemaExport.setOutputFile(fileName);
        schemaExport.execute(enumSet, action, metadata);
    }
}
