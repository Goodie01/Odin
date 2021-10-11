package org.goodiemania.odin.internal.manager.classinfo;

import java.lang.reflect.Method;

public record ClassPropertyInfo(String name, Method readMethod, boolean isIdField, boolean isIndexField) {
}
