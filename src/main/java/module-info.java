module org.goodiemania.odin {
    exports org.goodiemania.odin.external;
    exports org.goodiemania.odin.external.annotations;
    exports org.goodiemania.odin.external.model;
    exports org.goodiemania.odin.external.exceptions;

    //for tests
    exports org.goodiemania.odin.entities;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jdk8;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires org.apache.commons.lang3;
    requires org.jdbi.v3.core;
    requires java.desktop;
    requires org.reflections;

    opens org.goodiemania.odin.external;
    opens org.goodiemania.odin.external.annotations;
    opens org.goodiemania.odin.external.model;
    opens org.goodiemania.odin.external.exceptions;

    opens org.goodiemania.odin.internal;
    opens org.goodiemania.odin.internal.database;
    opens org.goodiemania.odin.internal.database.impl;
    opens org.goodiemania.odin.internal.manager;
    opens org.goodiemania.odin.internal.manager.search;
    opens org.goodiemania.odin.internal.manager.classinfo;

}