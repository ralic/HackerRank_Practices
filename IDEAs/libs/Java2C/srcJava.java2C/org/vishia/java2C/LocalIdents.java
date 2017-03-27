/****************************************************************************
 * Copyright/Copyleft:
 *
 * For this source the LGPL Lesser General Public License,
 * published by the Free Software Foundation is valid.
 * It means:
 * 1) You can use this source without any restriction for any desired purpose.
 * 2) You can redistribute copies of this source to everybody.
 * 3) Every user of this source, also the user of redistribute copies
 *    with or without payment, must accept this license for further using.
 * 4) But the LPGL is not appropriate for a whole software product,
 *    if this source is only a part of them. It means, the user
 *    must publish this part of source,
 *    but don't need to publish the whole source of the own product.
 * 5) You can study and modify (improve) this source
 *    for own using or for redistribution, but you have to license the
 *    modified sources likewise under this LGPL Lesser General Public License.
 *    You mustn't delete this Copyright/Copyleft inscription in this source file.
 *
 * @author Hartmut Schorrig: hartmut.schorrig@vishia.de, www.vishia.org
 * @version 0.93 2011-01-05  (year-month-day)
 *******************************************************************************/ 
package org.vishia.java2C;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.vishia.java2C.ClassData.MethodWithZbnfItem;
import org.vishia.mainCmd.Report;


/**This class contains all identifiers visible at class level or visible at a local position of code.
 * It means, visible at class level by generating the class members,
 * visible at level of a block statement including stack variable etc.
 * @author JcHartmut
 * {@link FieldData#typeClazz}
 *
 */
public class LocalIdents
{
  
  /**List of known fields of this context. */
  final TreeMap<String, FieldData> fieldIdents;

  /**List of known types of this context. */
  //private final TreeMap<String, FieldData> typeIdents;
  private final TreeMap<String, JavaSources.ClassDataOrJavaSrcFile> typeIdents;

  /**The parent idents. They contained the visible types of super levels, 
   * for example the class types, if this is a LocalIdents of a statement block.*/
  private final LocalIdents parent;
  
  /**Only for toString in debugging/reporting. */
  private final String debugPath;
  
  /**Access to the package level idents. TODO: don't use for normal search from outside,
   * it should not stored here, but it should given in an extra parameter calling {@link #getTypeInfo(String, LocalIdents)}!
   * 
   */
  //private final LocalIdents fileLevelIdents;
  
  /**The ClassData for which is this member of, or null if it isn't classLevelIdents of {@link ClassData}. } */
  private final ClassData declaringClassData;
  
  /**constructs a new empty instance. 
  public LocalIdents()
  { this(Java2C_Main.singleton.userTypes);
  }
 */ 
  
  
  /**constructs a new instance copying all parent identifier. 
   * This method is called especially if the identifier infos of a class is built.
   * @param parent The identifier infos of the parent scope are copied,
   *               the parent scope is not touched (changed). This parameter should be <code>null</code>
   *               while the LocalIdents of a non-inner class is created because there aren't
   *               any identifiers to copy outside of a top-level class. 
   *               It should be given, if the LocalIdents of a method or statement blocks 
   *               or of an inner class is created.
   * @param declaringClassData All identifier infos of all super and outer classes
   *               of this class are copied. This parameter should be <code>null</code>
   *               while the LocalIdents of methods or statement blocks are created.
   * @param fileLevelIdents LocalIdents of the package.              
   */
  public LocalIdents(LocalIdents parent, ClassData declaringClassData, boolean hasFields, String sPkgName)
  { if(hasFields){
      fieldIdents = new TreeMap<String, FieldData>();
      if(parent != null && parent.fieldIdents != null) { fieldIdents.putAll(parent.fieldIdents); }
    } else {
      fieldIdents = null;
      //a given parent should not have fieldIdents too!
      assert(parent == null || parent.fieldIdents == null || parent.fieldIdents.size()==0);
    }
    typeIdents = new TreeMap<String, JavaSources.ClassDataOrJavaSrcFile>();
    if(parent!=null){
      /**copy types to locally localIdents or from outer to inner class.
       * don't copy the fileLevelIdents! They are given extra on search.
       */
    	typeIdents.putAll(parent.typeIdents);
    }
    this.parent = parent;
    //this.fileLevelIdents = fileLevelIdents != null ? fileLevelIdents 
    //                 : parent != null ? parent.fileLevelIdents
    //                   : null;
    this.declaringClassData = declaringClassData; //maybe null
    if(declaringClassData != null)
    {
    	assert(sPkgName == null || sPkgName.length() ==0);
  	  debugPath = declaringClassData.sClassNameJavaFullqualified;
      if(declaringClassData.sClassNameJava.equals("Object"))
        stop();
      ClassData superClass = declaringClassData.getSuperClassData();
      //int superLevel = 1;
      if( superClass != null){ //fields from the super-class, it contains super-super too.
        if(!superClass.sClassNameJava.equals("Object"))
          stop();
        Set<Entry<String,FieldData>> entrySet = superClass.classLevelIdents.fieldIdents.entrySet();
        for(Entry<String,FieldData> entry: entrySet)
        { FieldData field = entry.getValue();
          FieldData superField = new FieldData(field, 1, 0, '.', '.');
          String sName = superField.getName();
          if(sName.equals("x1") && field.nClassLevel == 2)
          	stop();
          fieldIdents.put(sName, superField);
        }
        //superClass = superClass.getSuperClassData();
        //superLevel +=1;
      }
    } else {
    	if(parent == null){
    		debugPath = sPkgName;   //the given name from outside.
    	} else {
    		assert(sPkgName == null || sPkgName.length() ==0);
    	  debugPath = parent.debugPath + "{";
    	}  
    }
  }

  
  /**constructs a new instance with copy all parent identifier as base. 
   * This constructor is called especially if the identifier infos
   * of a local scope (method, statement block) are built. 
   * @param parent The identifier infos of the parent scope is copied,
   *               but the parent scope should be not touched (changed).
   */
  public LocalIdents(LocalIdents parent, String sPkgName)
  { this(parent, null, true, sPkgName);
    
  }
  

  /**constructs a new Instance for primary types. Field Identifier are not planned. 
   */
  public LocalIdents(String sPkgName)
  { this(null, null, false, sPkgName);
  }
  

  /**constructs a new instance for a class level. 
   * All identifiers of outer and super classes of the declaring class are copied too. They are visible
   * as super identifiers or outer identifiers.
   * @param declaringClass The class where the LocalIdents are assigned to.
   * @param fileLevelIdents The LocalIdents of the package containing all package visible and commonly types.
   */
  public LocalIdents(ClassData declaringClass)
  { this(null, declaringClass, true, null);  //ignore the fileLevelIdents, given extra on search.
  }
  
  
  
  
  
  
  /**searches the infos to the given identifier.
   * 
   * @param sIdent The identifier.
   * @return null if not found. 
   */
  public FieldData get(String sIdent)
  { return fieldIdents.get(sIdent);
  }
  
  /**searches the ClassData to the given type identifier.
   * If the type is found as a {@link JavaSrcTreeFile}, which isn't translated, its first pass is run now.
   * The first pass translates the file either, or it reads its structure (stc-)file.
   * <br><br>
   * If the type isn't found, neither as {@link JavaSrcTreeFile} nor as {@link ClassData}, 
   * this method returns <code>null</code>.
   * It may be possible that the type should be exists as a <i>unknown<i> type, than it is created, outside of this routine
   * see {@link GenerateClass#getType(org.vishia.zbnf.ZbnfParseResultItem, LocalIdents)}. 
   * It may be possible too, that the identifier may be either a type
   * or an association. If it isn't found as type, it may be found as association. Therefore this routine
   * have to be returned <code>null</code> at unknown identifiers.
   * 
   * @param sIdent The identifier.
   * @return null if the identifier is not found, elsewhere the found instance.
   * @throws IllegalArgumentException if any problem while running first pass occurs. 
   */
  public ClassData getType(final String sIdent, LocalIdents fileLevelIdents)
  throws ParseException
  { //Search the type-info in the local idents, then in the fileLevelIdents, then in the stdTypes.
    //They may have ClassData or not.
  	final JavaSources.ClassDataOrJavaSrcFile infos = getTypeInfo(sIdent, fileLevelIdents);
    if(infos == null) return null;
    else
    { ClassData retClassData = infos.getClassData();
      if(retClassData == null){
        JavaSrcTreeFile javaFile = infos.getJavaSrc();
        if(infos.isToTranslate() && javaFile == null) 
          throw new IllegalArgumentException("no javaSrc available for. " + sIdent);
        else if(javaFile == null){
        	retClassData = null;  //admissible if it should be checked whether it is a reference.
        } 
        else {
	        try
	        { /**If a identifier is found, but it has not ClassData, it is a non-translated file.
	           * Than run the first pass to get the ClassData. It may be that a stc-file is read.
	           */
	          retClassData = Java2C_Main.singleton.runRequestedFirstPass(javaFile, sIdent);
	         	if(sIdent.equals("org/vishia/msgDispatch/LogMessage"))
	        		stop();
	        } 
	        catch (FileNotFoundException e){ throw new IllegalArgumentException("file not found: " + javaFile.toString()); }
	        catch (IOException e){ throw new IllegalArgumentException("file error: " + javaFile.toString()); }
	        //catch (ParseException e)
	        //{ //e.printStackTrace();
	          //throw new IllegalArgumentException("parse exception: " + e.getMessage() + " in "+ javaFile.toString()); 
	        //}
	        catch (InstantiationException e){ throw new IllegalArgumentException("instanciation exception: " + javaFile.toString()); }
	        catch (IllegalAccessException e){ throw new IllegalArgumentException("illegal access: " + javaFile.toString()); }
	        //catch (IllegalArgumentException e){ throw new IllegalArgumentException("file not found: " + javaFile.toString()); }
        }
      }
      return retClassData;
    }
  }
  
  /**searches the infos to the given type identifier. A translation process isn't activated from here,
   * see {@link #getType(String, LocalIdents)}.
   * <br><br>
   * The identifier respectively the first package is searched in this LocalIdents and all its parents
   * firstly. That are local visible idents. The parent of this are the outer blocks of statement,
   * the class and the outer classes. and super classes.
   * <br><br>
   * If the type isn't found there, it is searched in the given fileLevelIdents. The fileLevelIdents
   * are the idents of the package, where the to-translate-class is member of 
   * (not the declaring class of this!). The fileLevelIdents contains the types too, which are given 
   * with the import-statements of the to-translate-class.
   * <br><br>
   * If the type isn't found neither in the local context nor in the fileLevelIdents, 
   * they are searched as top-level-package. Consider, that this routine is called recursively
   * to dissolve a given package path. Therefore the top-level package should be found. If a local
   * package exists with the same name like a top level, it is found first and used therefore.
   * <br><br>
   * If the identifier isn't found, than it is searched in the global visible types. That are
   * the standard types like <code>int</code> etc. and the <code>java.lang</code>.Types. 
   * It are referred by {@link CRuntimeJavalikeClassData#stdTypes}.
   * <br><br>
   * At least the type is searched in {@link Java2C_Main#externalTypes}.  
   * <br><br> 
   * It may be possible in the users Java-code, that the identifier may be either a type
   * or an association. That isn't able to different by the syntax, because both cases
   * are written as "Type.element" or "association.element". The usual distinction using an upper case
   * letter to start for types can't be us for a decision really. Therefore this routine 
   * returns <code>null</code> at unknown identifiers.
   * An exception or error message mustn't create here.
   * 
   * 
   * 
   * @param sIdent The identifier. It is possible it is a package path, 
   *               at example <code>java.lang.Object</code>. The separator between package idents
   *               can be a slash or a dot.
   * @param fileLevelIdents Contains all types which are known as environment for translation
   *                        of this file. They are all package level types 
   *                        and all types, which are known because import-statements. 
   * @return null if not found. Than the type isn't known. If not null, than it may be an instance
   *   of {@link ClassData} for simple types, or it is an instance of {@link JavaSrcTreeFile}.
   *   If the file is translated already, calling {@link JavaSources.ClassDataOrJavaSrcFile#getClassData()}
   *   supplies the ClassData, if it isn't translated yet but only known as Java-File, getClassData() returns <code>null</code>.
   * @throws ParseException 
   */
  //public FieldData getType(String sIdent)
  public JavaSources.ClassDataOrJavaSrcFile getTypeInfo(final String sIdentP, LocalIdents fileLevelIdents)
  { 
    String sIdent = sIdentP.replace('/','.');  //NOTE: if '/' isn't contain, the method is optimized.
    int posDot;
    if(sIdentP.contains("FieldJc"))
    	stop();
    if(sIdentP.equals("org/vishia/msgDispatch/LogMessage"))
    	stop();
    LocalIdents envIdents = this;
    //JavaSources.ClassDataOrJavaSrcFile envInfosWithReplaceInfo = null;
    /**Infos of the last package. */
    ConfigSrcPathPkg_ifc.Set replaceCinfosPkg = null;
    JavaSrcTreePkg lastPackage = null;
    boolean hasPath = false;
    while( (posDot = sIdent.indexOf('.')) >=0){
    	hasPath = true;
    	String sEnvIdent = sIdent.substring(0, posDot);
      //recursively call of the same method!
      JavaSources.ClassDataOrJavaSrcFile envInfos = envIdents.getTypeInfo(sEnvIdent, envIdents == this ? fileLevelIdents : null);
      if(envInfos == null){
        throw new IllegalArgumentException("ReadStructure: package not found: " + sEnvIdent + " for " + sIdent);        
      }
      else {
        if(envInfos instanceof JavaSrcTreePkg){
         	//mostly it is a package, because it is written before dot.
          lastPackage = (JavaSrcTreePkg)envInfos;  
        }
      	envIdents = envInfos.getLocalIdents(sEnvIdent);  //if it is a JavaSrcTreePackage, get the pkgIdents 
        if(envIdents == null){
        	throw new IllegalArgumentException("Class has no classlevelIdents: " + sEnvIdent + " for " + sIdent);        
        }
      }
      sIdent = sIdent.substring(posDot+1);
      replaceCinfosPkg = envInfos.getReplaceCinfo();
    } //while envIdent.type
    //The envIdent is the ident of the given environment or this.    
    //Search from there.
    JavaSources.ClassDataOrJavaSrcFile infos;
    if(envIdents.declaringClassData !=null && envIdents.declaringClassData.sClassNameJava.equals(sIdent))
    { infos = envIdents.declaringClassData;  //the own class is requested.
    }
    else
    { infos = envIdents.typeIdents.get(sIdent);  //may be found at current level or not, then null.
    }
    //if infos == null, search in parent definitions, towards outer block statements, towards outer classes
    while(infos == null && (envIdents = envIdents.parent) != null){
    	infos = envIdents.typeIdents.get(sIdent);
    }
    //if infos == null, it aren't found in the environment.
    //Search in the fileLevelIdents. That are all idents of the package and imported classes and packages.
    if(infos == null && !hasPath && fileLevelIdents != null){ // && envIdents == this && fileLevelIdents != null){
      /**search in given fileLevelIdents only if no environment ident is given.
       * At example if pkgX.class is given, the file level idents are used already to search pkgX.
       */
      infos = fileLevelIdents.typeIdents.get(sIdent);  
    }
    //if infos == null, it aren't found in the file level idents too.
    //It may be the name of a root package like "java", "org" etc. 
    //Consider that this routine is called recursively to dissolve a package path like "org.etc" 
    if( infos == null && !hasPath){
      	infos = Java2C_Main.getRootPkg(sIdent); 
    }
    //if infos == null, it isn't a root package too.
    //Search in the global visible idents. It is for simple types, and standard types.
    if( infos == null && !hasPath){
      //at last search in the standard types. Threre are simple types.
      	infos = CRuntimeJavalikeClassData.singleton.stdTypes.typeIdents.get(sIdent); 
    }
    //if infos == null, it isn't a root package too.
    //Last not least search in external (unknwon) types.
    if(infos == null && !hasPath){ // &&  envIdents != Java2C_Main.externalTypes){
      infos = Java2C_Main.externalTypes.typeIdents.get(sIdent); 
      if(infos !=null)
      	stop();
    }
    if(infos == null && replaceCinfosPkg != null){
    	//The identifier isn't found. 
    	//But there is a package-globally replacement information for the given package
    	// the file is registered now as file of the package.
    	infos = new JavaSrcTreeFile(lastPackage, null, null, sIdent, replaceCinfosPkg, null, false);
    }
    return infos;  //null if not found.  
  }
  
  
  /**puts a new class element (field, attribute, reference) in the container.
   * 
   * @param sIdent The textual representation of the identifier in java code context.
   * @param sType The associated type string if it is a field or method. null if it is a type.
   * @param typeClazz The associated type class
   * @param sModifier Kind of the identifier. See table in the description of the class {@link ClassData}.
   * @param clazz The associated class data
   */
  public void putClassElement
  ( String sIdent, String sType, ClassData typeClazz, char staticMode
  , char modeAccess
  , String sModifier
  , int dimensionArray
  , String[] fixArraySizes
  , char modeArrayElement
  , ClassData clazz
  )
  { FieldData value = new FieldData(sIdent, typeClazz, null, null, null, staticMode, modeAccess, dimensionArray, fixArraySizes, modeArrayElement, clazz);
    putClassElement(sIdent, value);  
  }
  
  /**puts a new class element (field, attribute, reference) in the container.
   * @param sIdent name
   * @param identInfo all infos to the field
   */
  public void putClassElement(String sIdent, FieldData identInfo)
  { identInfo.nClassLevel = 1;
    identInfo.nOuterLevel = 1;
    declaringClassData.addField(sIdent, identInfo); //added to this.fieldIdents via putElement, but does somewhat else. 
    Java2C_Main.singleton.console.reportln(Report.debug, "Java2C-LocalIdents.putClassElement-1-1: class=" 
    	+ this.declaringClassData.toString() + ", field=" + identInfo.toString());
  }
  
  
  
  /**puts a new inner class as a Type of the class in the container.
   * 
   * @param sTypeIdent The textual representation of the identifier in java code context.
   * @param sTypeNameC The type name in C, with outerclass__sTypeIdent.
   * @param typeClazz The associated type class
   * @param sModifier Kind of the identifier. See table in the description of the class {@link ClassData}.
   * @param clazz The associated class data
   * @deprecated use {@link #putClassType(String, ClassData)}
   */
  public void putClassType
  ( String sTypeIdent
  , String sTypeNameC
  , ClassData typeClazz
  , String sModifier
  , ClassData clazz
  )
  { putClassType(sTypeIdent, typeClazz);
  }
  
  
  public void putClassType
  ( String sTypeIdent
  , JavaSources.ClassDataOrJavaSrcFile typeClazz
  )
  { //FieldData value = new FieldData(sTypeNameC, typeClazz, '.', sModifier, null, clazz);
    //value.nClassLevel = 1;
    //value.nOuterLevel = 1;
    //typeIdents.put(sTypeIdent, value);
    if(sTypeIdent.equals("SetValueGenerator"))
      stop();
    typeIdents.put(sTypeIdent, typeClazz);
  }
  
  public void putClassType
  ( ClassData typeClazz
  )
  { String sName = typeClazz.getClassNameJava();
  	JavaSources.ClassDataOrJavaSrcFile typeInfo = typeIdents.get(sName);
  	if(typeInfo !=null && typeInfo instanceof JavaSrcTreeFile){
  		JavaSrcTreeFile javaSrc = (JavaSrcTreeFile)typeInfo;
  		javaSrc.setClassData(typeClazz);
  	} else {
  	  
  	  typeIdents.put(sName, typeClazz);
  	}
  }
  
  /**Puts the class into the typeIdents, with key with and without package.
   * The class is able to found with its name only, but with its package too.
   * @param typeClazz The class.
   */
  public void putClassTypeStandard( ClassData typeClazz)
  { String sPkgClass = typeClazz.sPackage + typeClazz.sClassNameJava;
    typeIdents.put(sPkgClass, typeClazz);
    typeIdents.put(typeClazz.getClassNameJava(), typeClazz);
  }
  
  
  public void putClassTypesAll(LocalIdents parent)
  {
    for(Entry<String, JavaSources.ClassDataOrJavaSrcFile> entry: parent.typeIdents.entrySet()){
      String key = entry.getKey();
      JavaSources.ClassDataOrJavaSrcFile data = entry.getValue();
      typeIdents.put(key, data);
    }
  }
  
  
  /**puts a new stack-local element in the container.
   * @param sIdent
   * @param identInfo
   */
  public void putLocalElement(String sIdent, FieldData identInfo)
  { identInfo.nClassLevel = 0;
    identInfo.nOuterLevel = 0;
    fieldIdents.put(sIdent, identInfo);
  }
  
  /**puts a new stack-local element in the container.
   * @param sIdent
   * @param identInfo
   */
  void putElement(String sIdent, FieldData identInfo)
  { fieldIdents.put(sIdent, identInfo);
  }
  
  /**Test whether the field is known and returns it data.
   * @param name The name of the field. The field can be local or in super scopes.
   * @return null if the field isn't existing.
   */
  public FieldData getField(String name)
  { return fieldIdents.get(name); 
  }
  
	/**Adds all fields known here to the non-static inner classes
	 * and their methods.
	 * This routine is called for {@link ClassData#classLevelIdents}
	 * for all classes. 
	 * <br><br> 
	 * The fields were not be added before because the inner class was built first,
	 * after them the outer class was processed in first pass.
	 * But the non-static inner class should know all idents from the outer too.
	 */
  void xxxcompleteFieldIdentsForInnerClasses()
  {
    for(Map.Entry<String,JavaSources.ClassDataOrJavaSrcFile> innerClass: typeIdents.entrySet()){
    	ClassData innerClassData = innerClass.getValue().getClassData();
    	if(innerClassData.isNonStaticInner) {
    		copyFieldsTo(innerClassData);
    		
    	}	
    }
  }
  
  
  
  
  /**Copies all field idents from the current level (called for the outer class)
   * to the named inner class and all methods bodies (its main statement block).
   * That is necessary because all idents are known only at the end of the first pass of the
   * outer class, but the first pass of the inner class is finished already, without knowledge
   * of all outer idents. All idents should be known while running the second pass only.
   * <br><br>
   * The method {@link ClassData#completeFieldIdentsFromOuterClass(LocalIdents)} is called unlike
   * if a class is generated in the second pass inside a block statement. In this case the 
   * first pass of this class is running only in the second pass of the environment.
   * <br><br>
   * The situation of same identifier in the outer and inner class is detected in that
   * complete-methods. The inner identifier covers the outer one.
   * @param innerClassData
   */
  void copyFieldsTo(ClassData innerClassData){
		for(Map.Entry<String,FieldData> fieldEntry: fieldIdents.entrySet()){
			/**Get the field from the outer class. */
			String name = fieldEntry.getKey();
			if(innerClassData.getFieldIdent(name) == null){
				//do it only if the field is not known in the inner class.
				//It may be a local defined field in that scope which covers the outer!
				FieldData fieldOuter = fieldEntry.getValue();
				if(  innerClassData.isNonStaticInner 
				  || "sSd".indexOf(fieldOuter.modeStatic)>=0  //static variable are known anytime.
				  ){
					//Build a FieldData with the correct outerLevel.
			  	FieldData fieldInner = new FieldData(fieldOuter, 0, 1, '.', '.');
					/**Add the field to the non-static inner class. 
					 * It was not be added before because the inner class was built first,
					 * after them the outer class was processed in first pass.
					 * But the non-static inner class should know all idents from the outer too.
					 */
			  	innerClassData.addField(name, fieldInner);
					//All methods of the inner class should be completed with the field too. 
			  	// Note: The methods have only the head data until now, the body isn't translated.*/
					for(MethodWithZbnfItem method: innerClassData.methodsWithZbnf){
						method.method.putFieldIdent(fieldInner);
					}
				}	
			}
	  }
	}
 

  void copyTypesTo(ClassData innerClass)
  {
  	LocalIdents innerClassIdents = innerClass.classLevelIdents;
  	for(Map.Entry<String,JavaSources.ClassDataOrJavaSrcFile> typeEntry: typeIdents.entrySet()){
    	JavaSources.ClassDataOrJavaSrcFile type = typeEntry.getValue();
    	ClassData typeClass = type.getClassData();
    	assert(typeClass !=null);  //it is translated anytime.
    	if("PC".indexOf(typeClass.creationMode)>=0){ //regard knowledge of only non-anonymous  
    		String name = typeEntry.getKey();       //and class level classes
    		innerClassIdents.typeIdents.put(name, type);
    	}
    }
  	innerClassIdents.typeIdents.put(declaringClassData.getClassNameJavaFullqualified(), declaringClassData);
  }
  
  private final static String newLineIndent = "\n                                            ";
  
  String indent(int recursion){ return newLineIndent.substring(0, 2*recursion +1); } 
  
  
  public String xxxwriteStructOwnClassFields(int recursion)
  { String out = "";
    /*
    if(typeIdents != null)
    { out += indent(recursion) + "typeIdents {  ";
      Set<Entry<String,ClassData>> listEntries = typeIdents.entrySet();
      for(Entry<String,ClassData> field: listEntries)
      { //String sName = field.getKey();
        ClassData identInfos = field.getValue();
        out += indent(recursion+1) + "type;" + identInfos.getClassIdentName();
      }
      out += indent(recursion) + "}";
    }
    */  
    return out;
  }
  
  
  /**Returns a String with all available type idents comma separated. 
   * This String is usefull for help on error unknown type.
   */
  public String getAllTypeIdents()
  {
    String ret = "class-Level:";
    for(String sKey: typeIdents.keySet()){
      ret += " " + sKey;
    }
    //if(fileLevelIdents != null){
    //  ret += "\nfile-level:";
    //  for(String sKey: fileLevelIdents.typeIdents.keySet()){
    //  ret += " " + sKey;
    //} }
    if(Java2C_Main.singleton.standardClassData.stdTypes != this){
      ret += "\nlang-level:";
      for(String sKey: Java2C_Main.singleton.standardClassData.stdTypes.typeIdents.keySet()){
      ret += " " + sKey;
    } }
    return ret;
  }
  
  
  /**Returns an iterable list with all typeIdents.
   */
  public Set<Map.Entry<String,JavaSources.ClassDataOrJavaSrcFile>> getTypeSet()
  { return typeIdents.entrySet();
  }
  
  @Override public String toString(){ return debugPath; }
  
  /**It's a debug helper. The method is empty, but it is a mark to set a breakpoint. */
  void stop(){}
}



