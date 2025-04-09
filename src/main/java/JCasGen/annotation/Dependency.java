

   
/* Apache UIMA v3 - First created by JCasGen Sun Mar 23 21:03:06 CET 2025 */

package JCasGen.annotation;
 

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;


import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun Mar 23 21:03:06 CET 2025
 * XML source: C:/Users/voron/Desktop/PPR/projects/multimodal_parliament_explorer_11_3/src/main/resources/typesystems/dependency.xml
 * @generated */
public class Dependency extends Annotation {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "annotation.Dependency";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Dependency.class);
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int type = typeIndexID;
  /** @generated
   * @return index of the type  
   */
  @Override
  public              int getTypeIndexID() {return typeIndexID;}
 
 
  /* *******************
   *   Feature Offsets *
   * *******************/ 
   
  public final static String _FeatName_Id = "Id";
  public final static String _FeatName_Governor = "Governor";
  public final static String _FeatName_Dependent = "Dependent";
  public final static String _FeatName_DependencyType = "DependencyType";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_Id = TypeSystemImpl.createCallSite(Dependency.class, "Id");
  private final static MethodHandle _FH_Id = _FC_Id.dynamicInvoker();
  private final static CallSite _FC_Governor = TypeSystemImpl.createCallSite(Dependency.class, "Governor");
  private final static MethodHandle _FH_Governor = _FC_Governor.dynamicInvoker();
  private final static CallSite _FC_Dependent = TypeSystemImpl.createCallSite(Dependency.class, "Dependent");
  private final static MethodHandle _FH_Dependent = _FC_Dependent.dynamicInvoker();
  private final static CallSite _FC_DependencyType = TypeSystemImpl.createCallSite(Dependency.class, "DependencyType");
  private final static MethodHandle _FH_DependencyType = _FC_DependencyType.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  @Deprecated
  @SuppressWarnings ("deprecation")
  protected Dependency() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public Dependency(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Dependency(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Dependency(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** 
   * <!-- begin-user-doc -->
   * Write your own initialization here
   * <!-- end-user-doc -->
   *
   * @generated modifiable 
   */
  private void readObject() {/*default - does nothing empty block */}
     
 
    
  //*--------------*
  //* Feature: Id

  /** getter for Id - gets 
   * @generated
   * @return value of the feature 
   */
  public String getId() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_Id));
  }
    
  /** setter for Id - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setId(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_Id), v);
  }    
    
   
    
  //*--------------*
  //* Feature: Governor

  /** getter for Governor - gets 
   * @generated
   * @return value of the feature 
   */
  public String getGovernor() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_Governor));
  }
    
  /** setter for Governor - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setGovernor(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_Governor), v);
  }    
    
   
    
  //*--------------*
  //* Feature: Dependent

  /** getter for Dependent - gets 
   * @generated
   * @return value of the feature 
   */
  public String getDependent() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_Dependent));
  }
    
  /** setter for Dependent - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setDependent(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_Dependent), v);
  }    
    
   
    
  //*--------------*
  //* Feature: DependencyType

  /** getter for DependencyType - gets 
   * @generated
   * @return value of the feature 
   */
  public String getDependencyType() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_DependencyType));
  }
    
  /** setter for DependencyType - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setDependencyType(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_DependencyType), v);
  }    
    
  }

    