package org.vishia.Inspc;

import java.lang.reflect.Field;

public class InspcClassData
{

  public final String[] names;
  
  public final Field[] fields;
  
  InspcClassData(Class<?> clazz)
  {
    fields = clazz.getDeclaredFields();
    names = new String[fields.length];
    int ixName = 0;
    for(Field field: fields){
      names[ixName++] = field.getName();
    }
  }
  
}
