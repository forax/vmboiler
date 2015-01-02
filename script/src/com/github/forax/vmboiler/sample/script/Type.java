package com.github.forax.vmboiler.sample.script;

import java.lang.invoke.MethodType;
import java.util.HashMap;

public enum Type implements com.github.forax.vmboiler.Type {
  VOID(VM_VOID),
  BOOL(VM_BOOLEAN),
  INT(VM_INT),
  MIXED_INT(VM_INT),
  NUM(VM_DOUBLE),
  MIXED_NUM(VM_DOUBLE),
  OBJECT(VM_OBJECT);
  
  private final String vmType;
  
  private Type(String vmType) {
    this.vmType = vmType;
  }
  
  @Override
  public boolean isMixed() {
    return this == MIXED_INT || this == MIXED_NUM;
  }
  
  @Override
  public String vmType() {
    return vmType;
  }
  
  public Type erase() {
    if (this == MIXED_INT) {
      return INT;
    }
    if (this == MIXED_NUM) {
      return NUM;
    }
    return this;
  }
  
  public Type mix(boolean mix) {
    if (mix == false) {
      return this;
    }
    if (this == INT) {
      return MIXED_INT;
    }
    if (this == NUM) {
      return MIXED_NUM;
    }
    return this;
  }

  public static Type merge(Type type1, Type type2) {
    if (type1 == type2) {
      return type1;
    }
    Type erased1 = type1.erase();
    Type erased2 = type2.erase();
    if (erased1 == erased2) {
      return erased1.mix(true);
    }
    if (erased1 == INT && erased2 == NUM) {
      return NUM.mix(type1.isMixed() || type2.isMixed());
    }
    if (erased1 == NUM && erased2 == INT) {
      return NUM.mix(type1.isMixed() || type2.isMixed());
    }
    return OBJECT;
  }
  
  public static Type getTypeFromValue(Object value) {
    if (value instanceof Integer) {
      return INT;
    }
    if (value instanceof Boolean) {
      return BOOL;
    }
    if (value instanceof Double) {
      return NUM;
    }
    return OBJECT;
  }
  
  public static Type getTypeFromClass(Class<?> clazz) {
    Type type = CLASS_TO_TYPE_MAP.get(clazz);
    if (type == null) {
      throw new IllegalArgumentException("no type for class " + clazz.getName());
    }
    return type;
  }
  
  public static Class<?> getClassFromType(Type type) {
    Class<?> clazz = TYPE_TO_CLASS_MAP.get(type);
    if (clazz == null) {
      throw new IllegalArgumentException("no class for type " + type);
    }
    return clazz;
  }
  
  private static Class<?> classFromDesc(String descriptor) { // ugly isn't it ?
    if (descriptor.equals("V")) {
      return void.class;
    }
    return MethodType.fromMethodDescriptorString('(' + descriptor + ")V", null).parameterType(0);
  }
  
  private static final HashMap<Class<?>, Type> CLASS_TO_TYPE_MAP;
  private static final HashMap<Type, Class<?>> TYPE_TO_CLASS_MAP;
  static {
    HashMap<Type, Class<?>> typeToClassMap = new HashMap<>();
    HashMap<Class<?>, Type> classToTypeMap = new HashMap<>();
    for(Type type: Type.values()) {
      if (type.isMixed()) {
        continue;  // skip mixed type
      }
      Class<?> clazz = classFromDesc(type.vmType());
      classToTypeMap.put(clazz, type);
      typeToClassMap.put(type, clazz);
    }
    CLASS_TO_TYPE_MAP = classToTypeMap;
    TYPE_TO_CLASS_MAP = typeToClassMap;
  }
}