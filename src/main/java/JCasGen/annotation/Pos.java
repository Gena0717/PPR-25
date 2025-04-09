

   
/* Apache UIMA v3 - First created by JCasGen Sun Mar 23 21:08:52 CET 2025 */

package annotation;
 

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;

import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.impl.TypeSystemImpl;
import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;


import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sun Mar 23 21:08:52 CET 2025
 * XML source: C:/Users/voron/Desktop/PPR/projects/multimodal_parliament_explorer_11_3/src/main/resources/typesystems/pos.xml
 * @generated */
public class Pos extends Annotation {
 
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static String _TypeName = "annotation.Pos";
  
  /** @generated
   * @ordered 
   */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = JCasRegistry.register(Pos.class);
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
  public final static String _FeatName_PosValue = "PosValue";
  public final static String _FeatName_CoarseValue = "CoarseValue";


  /* Feature Adjusted Offsets */
  private final static CallSite _FC_Id = TypeSystemImpl.createCallSite(Pos.class, "Id");
  private final static MethodHandle _FH_Id = _FC_Id.dynamicInvoker();
  private final static CallSite _FC_PosValue = TypeSystemImpl.createCallSite(Pos.class, "PosValue");
  private final static MethodHandle _FH_PosValue = _FC_PosValue.dynamicInvoker();
  private final static CallSite _FC_CoarseValue = TypeSystemImpl.createCallSite(Pos.class, "CoarseValue");
  private final static MethodHandle _FH_CoarseValue = _FC_CoarseValue.dynamicInvoker();

   
  /** Never called.  Disable default constructor
   * @generated */
  @Deprecated
  @SuppressWarnings ("deprecation")
  protected Pos() {/* intentionally empty block */}
    
  /** Internal - constructor used by generator 
   * @generated
   * @param casImpl the CAS this Feature Structure belongs to
   * @param type the type of this Feature Structure 
   */
  public Pos(TypeImpl type, CASImpl casImpl) {
    super(type, casImpl);
    readObject();
  }
  
  /** @generated
   * @param jcas JCas to which this Feature Structure belongs 
   */
  public Pos(JCas jcas) {
    super(jcas);
    readObject();   
  } 


  /** @generated
   * @param jcas JCas to which this Feature Structure belongs
   * @param begin offset to the begin spot in the SofA
   * @param end offset to the end spot in the SofA 
  */  
  public Pos(JCas jcas, int begin, int end) {
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
  //* Feature: PosValue

  /** getter for PosValue - gets 
   * @generated
   * @return value of the feature 
   */
  public String getPosValue() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_PosValue));
  }
    
  /** setter for PosValue - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setPosValue(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_PosValue), v);
  }    
    
   
    
  //*--------------*
  //* Feature: CoarseValue

  /** getter for CoarseValue - gets 
   * @generated
   * @return value of the feature 
   */
  public String getCoarseValue() { 
    return _getStringValueNc(wrapGetIntCatchException(_FH_CoarseValue));
  }
    
  /** setter for CoarseValue - sets  
   * @generated
   * @param v value to set into the feature 
   */
  public void setCoarseValue(String v) {
    _setStringValueNfc(wrapGetIntCatchException(_FH_CoarseValue), v);
  }    
    
  }

    